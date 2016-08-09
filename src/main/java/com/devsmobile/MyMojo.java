package com.devsmobile;


import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * Launch embedded scalafmt.jar in the directory with the options provided
 */
@Mojo( name = "format")
public class MyMojo extends AbstractMojo {

    public void execute() throws MojoExecutionException {

        try{
            Process ps = Runtime.getRuntime().exec(new String[]{"java", "-jar", "/resources/scalafmt.jar"});
            ps.waitFor();
            java.io.InputStream is=ps.getInputStream();
            byte b[]=new byte[is.available()];
            is.read(b,0,b.length);
            System.out.println(new String(b));
        } catch(Exception e) {
            throw new MojoExecutionException("Error formatting Scala files", e);
        }


    }
}
