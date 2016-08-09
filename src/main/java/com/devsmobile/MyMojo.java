package com.devsmobile;


import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;

import java.io.*;

/**
 * Launch embedded scalafmt.jar in the directory with the options provided
 */
@Mojo( name = "format")
public class MyMojo extends AbstractMojo {

    public void execute() throws MojoExecutionException {

        getLog().info("ALLA VAMOOOOOSS");

        try{
            getLog().info("1");

            if(!existsScalaFmtInPath()){
                getLog().info("Creating jar");
                copyJarToHome();
            }

            getLog().info("2");

            Process ps = Runtime.getRuntime().exec(new String[]{"java", "-jar", getFinalPathToScalaFmt(), "-h"});
            ps.waitFor();
            java.io.InputStream is=ps.getInputStream();
            byte b[]=new byte[is.available()];
            is.read(b,0,b.length);
            getLog().info(new String(b));
            getLog().info("3");
        } catch(Exception e) {
            throw new MojoExecutionException("Error formatting Scala files", e);
        }

    }

    /**
     * Copy scalafmt.jar to HOME based on Operating System if not exists
     * @throws IOException
     */
    private void copyJarToHome() throws IOException {

        //Check Folder exists
        getLog().info("Checking parent folder");
        File basePath = new File(getBasePath());
        if(!basePath.exists()){
            basePath.mkdir();
        }

        getLog().info("Parent folder assured");

        InputStream is = MyMojo.class.getResourceAsStream("/scalafmt.jar");

        getLog().info("Found");

        getLog().info("outputfolder: " + getBasePath());

        getLog().info("outputfile: " + getFinalPathToScalaFmt());

        File outputFile = new File(getFinalPathToScalaFmt());
        OutputStream os = new FileOutputStream(outputFile);

        getLog().info("Writting");

        byte[] buffer = new byte[1024];

        getLog().info(is.toString());

        int length;
        //copy the file content in bytes
        while ((length = is.read(buffer)) > 0){
            os.write(buffer, 0, length);
        }

        getLog().info("Written");
        is.close();
        os.close();
        getLog().info("Closed");
    }

    private boolean existsScalaFmtInPath() {
        File expected = new File(getFinalPathToScalaFmt());
        return expected.exists();
    }

    private String getBasePath() {
        if(OSValidator.isWindows()){
            throw new RuntimeException("Not supported");
        } else if(OSValidator.isUnix()){
            String homePath = System.getProperty("user.home");
            return homePath + File.separator + ".scalafmt";
        } else if(OSValidator.isMac()){
            throw new RuntimeException("Not supported");
        } else if(OSValidator.isSolaris()){
            throw new RuntimeException("Not supported");
        } else {
            throw new RuntimeException("Can't find running Operating System");
        }
    }

    private String getFinalPathToScalaFmt() {
        return getBasePath() + File.separator + "scalafmt.jar";
    }
}
