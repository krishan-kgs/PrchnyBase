/*
 *  Copyright 2011 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, Mar 18, 2011
 *  @author rahul
 */

package com.prchny.base.fileutils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.CRC32;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SDFileUploadUtils {
  
  private static final Logger LOG = LoggerFactory
      .getLogger(SDFileUploadUtils.class);
  
  public static void upload(final InputStream uploadStream,
      final String contentType, final String filePath) {
  
    // Upload file to Disk
    final File file = new File(filePath);
    file.getParentFile().mkdirs();
    
    BufferedInputStream bufferedInputStream = null;
    FileOutputStream fileOutputStream = null;
    
    try {
      bufferedInputStream = new BufferedInputStream(uploadStream);
      fileOutputStream = new FileOutputStream(filePath);
      final byte[] by = new byte[32768]; // 32K buffer buf, 0, buf.length
      int index = bufferedInputStream.read(by, 0, 32768);
      while (index != -1) {
        fileOutputStream.write(by, 0, index);
        index = bufferedInputStream.read(by, 0, 32768);
      }
    } catch (final IOException e) {
      LOG.error("IOException: " + e.toString());
    } finally {
      try {
        if (fileOutputStream != null) {
          fileOutputStream.flush();
        }
        if (bufferedInputStream != null) {
          bufferedInputStream.close();
        }
        if (fileOutputStream != null) {
          fileOutputStream.close();
        }
      } catch (final IOException e) {
        LOG.error("IOException while cleaning up upload temp file");
      }
    }
  }
  
  public static Long copyFile(final File srcFile, final File destFile,
      final boolean verify) throws IOException {
  
    if (srcFile.getAbsolutePath().equals(destFile.getAbsolutePath())) {
      return null;
    }
    final File destDir = destFile.getParentFile();
    destDir.mkdirs();
    final InputStream in = new FileInputStream(srcFile);
    final OutputStream out = new FileOutputStream(destFile);
    CRC32 checksum = null;
    if (verify) {
      checksum = new CRC32();
      checksum.reset();
    }
    final byte[] buffer = new byte[32768];
    int bytesRead;
    while ((bytesRead = in.read(buffer)) >= 0) {
      if (verify) {
        checksum.update(buffer, 0, bytesRead);
      }
      out.write(buffer, 0, bytesRead);
    }
    out.close();
    in.close();
    if (verify) {
      return checksum.getValue();
    } else {
      return null;
    }
  }
  
}
