package org.yastral.exoticchallenges.lobbydata;

import java.io.*;
import java.nio.channels.*;

public class copyFilesFaster {
    public static void copyFile(File in, File out) throws IOException
    {
        FileChannel inChannel = new
                FileInputStream(in).getChannel();
        FileChannel outChannel = new
                FileOutputStream(out).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(),
                    outChannel);
        }
        catch (IOException e) {
            throw e;
        }
        finally {
            if (inChannel != null) inChannel.close();
            if (outChannel != null) outChannel.close();
        }
    }


    public static void copyFile2(File sourceFile, File destinationPath) {
        try (FileInputStream fis = new FileInputStream(sourceFile);
             FileOutputStream fos = new FileOutputStream(destinationPath + "\\" + sourceFile.getName())) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            System.err.println("Errore durante la copia del file: " + e.getMessage());
        }
    }
}
