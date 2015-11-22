/*
 *  Copyright 2010 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, Oct 18, 2010
 *  @author singla
 */

package com.prchny.base.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class FileUtils {
  
  public static String getFileAsString(final String filePath)
      throws IOException {
  
    final BufferedReader reader =
        new BufferedReader(new InputStreamReader(new FileInputStream(filePath),
            "UTF-8"));
    String line = null;
    final StringBuilder builder = new StringBuilder();
    while ((line = reader.readLine()) != null) {
      builder.append(line);
      builder.append(System.getProperty("line.separator"));
    }
    return builder.toString();
  }
  
  /**
   * @param zipFile
   *          : Path to zip file Recursively unzip a .zip file. Unzipped files
   *          remain at the location of the .zip file.
   * 
   */
  public static void unzipFile(final String zipFile) throws ZipException,
      IOException {
  
    final int BUFFER = 2048;
    final File file = new File(zipFile);
    
    final ZipFile zip = new ZipFile(file);
    // String newPath = zipFile.substring(0, zipFile.length() - 4);
    final String newPath = file.getParent();
    
    new File(newPath).mkdir();
    final Enumeration zipFileEntries = zip.entries();
    
    // Process each entry
    while (zipFileEntries.hasMoreElements()) {
      // grab a zip file entry
      final ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
      
      final String currentEntry = entry.getName();
      
      File destFile = new File(newPath, currentEntry);
      destFile = new File(newPath, destFile.getName());
      final File destinationParent = destFile.getParentFile();
      
      // create the parent directory structure if needed
      destinationParent.mkdirs();
      if (!entry.isDirectory()) {
        final BufferedInputStream is =
            new BufferedInputStream(zip.getInputStream(entry));
        int currentByte;
        // establish buffer for writing file
        final byte data[] = new byte[BUFFER];
        
        // write the current file to disk
        final FileOutputStream fos = new FileOutputStream(destFile);
        final BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);
        
        // read and write until last byte is encountered
        while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
          dest.write(data, 0, currentByte);
        }
        dest.flush();
        dest.close();
        is.close();
      }
      if (currentEntry.endsWith(".zip")) {
        // found a zip file, try to open
        unzipFile(destFile.getAbsolutePath());
      }
    }
  }
}
