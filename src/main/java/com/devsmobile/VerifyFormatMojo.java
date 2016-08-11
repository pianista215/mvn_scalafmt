package com.devsmobile;


import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Launch embedded scalafmt.jar in the directory with the options provided to verify
 */
@Mojo(name = "verify")
public class VerifyFormatMojo extends MyAbstractMojo {

    @Parameter(property = "verify.scalaFmtParameters", defaultValue = "--test -f .")
    private String scalafmtParameters;


    private String folder;

    public void execute() throws MojoExecutionException {

        getLog().info("Checking format with: " + scalafmtParameters);

        try {

            if (!existsScalaFmtInPath()) {
                getLog().info("Creating jar");
                copyJarToHome();
            }

            List<String> parameters = new ArrayList<String>(Arrays.asList("java", "-jar", getFinalPathToScalaFmt()));
            parameters.addAll(Arrays.asList(scalafmtParameters.split(" ")));

            Process ps = Runtime.getRuntime().exec(parameters.toArray(new String[parameters.size()]));
            ps.waitFor();

            if (ps.exitValue() == 0) {
                InputStream is = ps.getInputStream();
                byte b[] = new byte[is.available()];
                is.read(b, 0, b.length);
                getLog().debug(new String(b));
                getLog().info("Format checked");
            } else {
                InputStream is = ps.getErrorStream();
                byte b[] = new byte[is.available()];
                is.read(b, 0, b.length);
                String errorMsg = new String(b);

                throw new MojoExecutionException("The code is not well formatted. Error from scalafmt:" + errorMsg);
            }

        } catch (Exception e) {
            throw new MojoExecutionException("Error checking format in Scala files", e);
        }

    }
}
