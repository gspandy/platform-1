package com.hundsun.fcloud.maven.plugin.mojo;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * Created by Gavin Hu on 2015/1/10.
 */
@Mojo(name="export-packages-print", requiresDependencyResolution = ResolutionScope.COMPILE, threadSafe = true )
public class ExportPackagePrintMojo extends AbstractMojo {

    @Parameter( defaultValue = "${session}", readonly = true, required = true )
    private MavenSession session;

    @Parameter( defaultValue = "${project.artifacts}", required = true, readonly = true )
    private Set<Artifact> dependencies;

    @Parameter(property = "includes")
    private List<String> includes = new ArrayList<String>();

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        //
        Set<String> exportPackageSet = new TreeSet<String>();
        //
        for(Artifact artifact : dependencies) {
            //
            try {
                JarFile jarFile = new JarFile(artifact.getFile());
                Manifest manifest = jarFile.getManifest();
                String exportPackageHeaderValue = manifest.getMainAttributes().getValue("Export-Package");
                if(exportPackageHeaderValue!=null) {
                    String[] exportPackages = exportPackageHeaderValue.split(",");
                    for(String exportPackage : exportPackages) {
                        String[] tokens = exportPackage.split(";");
                        String exportedPackage = tokens[0];
                        exportedPackage = exportedPackage.replaceAll("\\u0022", "");
                        exportPackageSet.add(exportedPackage);
                    }
                }
                //
            } catch (IOException e) {
                getLog().error(e);
            }
        }
        //
        File exportPackageList = new File(session.getCurrentProject().getBasedir(), "target/exportPackageList.txt");
        try {
            PrintWriter writer = new PrintWriter(exportPackageList);
            //
            for(String include : includes) {
                for(String exportPackage : exportPackageSet) {
                    if(exportPackage.startsWith(include)) {
                        //
                        writer.println(exportPackage + ";resolution:=optional,");
                    }
                }
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
