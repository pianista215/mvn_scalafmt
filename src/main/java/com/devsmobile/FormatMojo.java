package com.devsmobile;


import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Launch embedded scalafmt.jar in the directory with the options provided to format
 */
@Mojo(name = "format")
public class FormatMojo extends MyAbstractMojo {

    @Parameter(property = "format.formatParameters", defaultValue = "-i -f .")
    private String formatParameters;

    public void execute() throws MojoExecutionException {

        getLog().info("Formatting with options: " + formatParameters);

        try {
            if (!existsScalaFmtInPath()) {
                copyJarToHome();
            }

            List<String> parameters = new ArrayList<String>(Arrays.asList("java", "-jar", getFinalPathToScalaFmt()));
            parameters.addAll(Arrays.asList(formatParameters.split(" ")));

            Process ps = Runtime.getRuntime().exec(parameters.toArray(new String[parameters.size()]));
            ps.waitFor();

            if (ps.exitValue() == 0) {
                java.io.InputStream is = ps.getInputStream();
                byte b[] = new byte[is.available()];
                is.read(b, 0, b.length);
                getLog().debug(new String(b));
                getLog().info("Format finished");
            } else {
                InputStream is = ps.getErrorStream();
                byte b[] = new byte[is.available()];
                is.read(b, 0, b.length);
                String errorMsg = new String(b);
                throw new MojoExecutionException("Error formatting scala code:" + errorMsg);
            }


        } catch (Exception e) {
            throw new MojoExecutionException("Error formatting Scala files", e);
        }

    }
}
