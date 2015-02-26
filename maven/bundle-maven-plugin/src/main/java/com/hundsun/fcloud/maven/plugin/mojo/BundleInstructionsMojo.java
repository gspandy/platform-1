package com.hundsun.fcloud.maven.plugin.mojo;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.versioning.VersionRange;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

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

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        //
        try {

            calculateImportPackage();
        } catch (Exception e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }

    }

    protected void calculateImportPackage() throws IOException {
        //
        Map<String, String> exportedPackageMap = new TreeMap<String, String>();
        Map<String, Boolean> exportedPackageOptionalMap = new HashMap<String, Boolean>();
        //
        for(Artifact dependency : session.getCurrentProject().getDependencyArtifacts()) {
            //
            String version = null;
            VersionRange versionRange = dependency.getVersionRange();
            if(versionRange.getRecommendedVersion()!=null) {
                version = versionRange.getRecommendedVersion().toString();
            } else {
                version = versionRange.getRestrictions().get(0).toString();
            }
            //
            JarFile jarFile = new JarFile(dependency.getFile());
            Manifest manifest = jarFile.getManifest();
            //
            String exportPackageHeaderValue = manifest.getMainAttributes().getValue("Export-Package");
            if(exportPackageHeaderValue!=null) {
                String[] exportPackages = exportPackageHeaderValue.split(",");
                for(String exportPackage : exportPackages) {
                    String[] tokens = exportPackage.split(";");
                    String exportedPackage = tokens[0];
                    exportedPackage = exportedPackage.replaceAll("\\u0022", "");
                    //
                    exportedPackageMap.put(exportedPackage, version);
                    exportedPackageOptionalMap.put(exportedPackage, dependency.isOptional());
                }
            }
        }
        //
        List<String> importedPackages = new ArrayList<String>();
        //
        for(String exportedPackage : exportedPackageMap.keySet()) {
            StringBuffer sb = new StringBuffer();
            String version = exportedPackageMap.get(exportedPackage);
            sb.append(exportedPackage).append(";");
            sb.append("resolution:=optional;");
            sb.append("version=\"").append(version).append("\"");
            //
            importedPackages.add(sb.toString());
        }
        //
        writeInstruction("Import-Package", importedPackages);
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
