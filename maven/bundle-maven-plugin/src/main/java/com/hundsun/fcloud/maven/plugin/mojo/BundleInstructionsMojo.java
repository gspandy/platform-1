package com.hundsun.fcloud.maven.plugin.mojo;

import aQute.bnd.header.Parameters;
import aQute.bnd.osgi.Analyzer;
import aQute.bnd.osgi.Jar;
import aQute.bnd.osgi.Processor;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * Created by Gavin Hu on 2015/2/16.
 */
@Mojo(name="bundle-instructions",requiresDependencyResolution = ResolutionScope.COMPILE, threadSafe = true)
public class BundleInstructionsMojo extends AbstractMojo {

    @Parameter(defaultValue = "${session}", readonly = true, required = true )
    private MavenSession session;

    @Parameter(defaultValue = "${project.build.directory}")
    private File buildDirectory;

    /**
     * Comma separated list of artifactIds to exclude from the dependency classpath passed to BND (use "true" to exclude everything)
     *
     * @parameter expression="${excludeDependencies}"
     */
    protected String excludeDependencies;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        //
        try {
            calculateImportPackage();
            //
        } catch (Exception e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }

    }

    protected void calculateImportPackage() throws IOException, MojoExecutionException {
        //
        Parameters parameters = new Parameters();
        for(Artifact dependency : session.getCurrentProject().getDependencyArtifacts()) {
            //
            JarFile jarFile = new JarFile(dependency.getFile());
            Manifest manifest = jarFile.getManifest();
            //
            String exportPackageHeaderValue = manifest.getMainAttributes().getValue("Export-Package");
            String importPackageHeaderValue = manifest.getMainAttributes().getValue("Import-Package");
            //
            Set optionalPackages = getOptionalPackages(session.getCurrentProject());
            //
            if(exportPackageHeaderValue!=null) {
                parsePackageHeader(parameters, optionalPackages, exportPackageHeaderValue);
            }
            if(importPackageHeaderValue!=null) {
                parsePackageHeader(parameters, optionalPackages, importPackageHeaderValue);
            }
        }
        //
        String importedPackages = Processor.printClauses( parameters );
        //
        writeInstruction("Import-Package", importedPackages);
    }

    private void parsePackageHeader(Parameters parameters, Set<String> optionalPackages, String packageHeaderValue) {
        Parameters values = new Analyzer().parseHeader( packageHeaderValue );
        for ( Map.Entry<String, ? extends Map<String, String>> entry : values.entrySet() )
        {
            String pkg = entry.getKey();
            Map<String, String> options = entry.getValue();
            if ( !options.containsKey( "resolution:" ) && optionalPackages.contains( pkg ) )
            {
                options.put( "resolution:", "optional" );
            }
            //
            getLog().info(pkg);
        }
        //
        parameters.putAll(values);
    }

    private Collection getSelectedDependencies( Collection artifacts ) throws MojoExecutionException
    {
        if ( null == excludeDependencies || excludeDependencies.length() == 0 )
        {
            return artifacts;
        }
        else if ( "true".equalsIgnoreCase( excludeDependencies ) )
        {
            return Collections.EMPTY_LIST;
        }

        Collection selectedDependencies = new LinkedHashSet( artifacts );
        DependencyExcluder excluder = new DependencyExcluder( artifacts );
        excluder.processHeaders( excludeDependencies );
        selectedDependencies.removeAll( excluder.getExcludedArtifacts() );

        return selectedDependencies;
    }


    protected Set getOptionalPackages( MavenProject currentProject ) throws IOException, MojoExecutionException
    {
        ArrayList inscope = new ArrayList();
        final Collection artifacts = getSelectedDependencies( currentProject.getArtifacts() );
        for ( Iterator it = artifacts.iterator(); it.hasNext(); )
        {
            Artifact artifact = ( Artifact ) it.next();
            if ( artifact.getArtifactHandler().isAddedToClasspath() )
            {
                if ( !Artifact.SCOPE_TEST.equals( artifact.getScope() ) )
                {
                    inscope.add( artifact );
                }
            }
        }

        HashSet optionalArtifactIds = new HashSet();
        for ( Iterator it = inscope.iterator(); it.hasNext(); )
        {
            Artifact artifact = ( Artifact ) it.next();
            if ( artifact.isOptional() )
            {
                String id = artifact.toString();
                if ( artifact.getScope() != null )
                {
                    // strip the scope...
                    id = id.replaceFirst( ":[^:]*$", "" );
                }
                optionalArtifactIds.add( id );
            }

        }

        HashSet required = new HashSet();
        HashSet optional = new HashSet();
        for ( Iterator it = inscope.iterator(); it.hasNext(); )
        {
            Artifact artifact = ( Artifact ) it.next();
            File file = artifact.getFile();
            if ( file == null )
            {
                continue;
            }

            Jar jar = new Jar( artifact.getArtifactId(), file );
            if ( isTransitivelyOptional( optionalArtifactIds, artifact ) )
            {
                optional.addAll( jar.getPackages() );
            }
            else
            {
                required.addAll( jar.getPackages() );
            }
            jar.close();
        }

        optional.removeAll( required );
        return optional;
    }

    /**
     * Check to see if any dependency along the dependency trail of
     * the artifact is optional.
     *
     * @param artifact
     */
    protected boolean isTransitivelyOptional( HashSet optionalArtifactIds, Artifact artifact )
    {
        List trail = artifact.getDependencyTrail();
        for ( Iterator iterator = trail.iterator(); iterator.hasNext(); )
        {
            String next = ( String ) iterator.next();
            if ( optionalArtifactIds.contains( next ) )
            {
                return true;
            }
        }
        return false;
    }

    protected void writeInstruction(String name, Object value) throws IOException {
        PrintWriter printWriter = new PrintWriter(new File(buildDirectory, name));
        if(value instanceof String) {
            printWriter.println(value);
        } else if (value instanceof Collection) {
            for(String line : (Collection<String>)value) {
                printWriter.println(line);
            }
        }
        //
        printWriter.close();
    }

}
