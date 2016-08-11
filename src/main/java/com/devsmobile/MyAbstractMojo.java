package com.devsmobile;


import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.*;

/**
 * provide installation operations for scalafmt.jar
 */
public abstract class MyAbstractMojo extends AbstractMojo {

    /**
     * Copy scalafmt.jar to HOME based on Operating System if not exists
     * @throws IOException
     */
    protected void copyJarToHome() throws IOException {

        //Check Folder exists
        File basePath = new File(getBasePath());
        if(!basePath.exists()){
            basePath.mkdir();
        }

        InputStream is = MyAbstractMojo.class.getResourceAsStream("/scalafmt.jar");

        File outputFile = new File(getFinalPathToScalaFmt());
        OutputStream os = new FileOutputStream(outputFile);

        byte[] buffer = new byte[1024];

        int length;
        //copy the file content in bytes
        while ((length = is.read(buffer)) > 0){
            os.write(buffer, 0, length);
        }

        is.close();
        os.close();
    }

    protected boolean existsScalaFmtInPath() {
        File expected = new File(getFinalPathToScalaFmt());
        return expected.exists();
    }

    protected String getBasePath() {
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

    protected String getFinalPathToScalaFmt() {
        return getBasePath() + File.separator + "scalafmt.jar";
    }
}
