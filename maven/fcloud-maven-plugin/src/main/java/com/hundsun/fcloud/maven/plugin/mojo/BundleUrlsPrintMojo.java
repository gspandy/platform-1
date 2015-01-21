package com.hundsun.fcloud.maven.plugin.mojo;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import java.io.IOException;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * Created by Gavin Hu on 2015/1/10.
 */
@Mojo(name="bundle-urls-print", requiresDependencyResolution = ResolutionScope.COMPILE, threadSafe = true )
public class BundleUrlsPrintMojo extends AbstractMojo {

    @Parameter( defaultValue = "${session}", readonly = true, required = true )
    private MavenSession session;

    @Parameter( defaultValue = "${project.artifacts}", required = true, readonly = true )
    private Set<Artifact> dependencies;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        //
        System.out.println(String.format("========================== %s (begin) ===========================", session.getCurrentProject().getName()));
        for(Artifact artifact : dependencies) {
            System.out.println(String.format("mvn:%s/%s/%s", artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion()));
        }
        System.out.println(String.format("mvn:%s/%s/%s", session.getCurrentProject().getGroupId(), session.getCurrentProject().getArtifactId(), session.getCurrentProject().getVersion()));
        System.out.println(String.format("========================== %s (end) ===========================", session.getCurrentProject().getName()));
    }

}
