/*
 *  Copyright 2013 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 18-Jan-2013
 *  @author amd
 */

package com.prchny.base.fileutils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class SDFileDownloadUtils {
  
  private static final Logger LOG = LoggerFactory
      .getLogger(SDFileDownloadUtils.class);
  
  public static byte[] download(final String url) throws Exception {
  
    ByteArrayOutputStream out = null;
    try {
      final URL u = new URL(url);
      final InputStream in = new BufferedInputStream(u.openStream());
      out = new ByteArrayOutputStream();
      final byte[] buf = new byte[1024];
      int n = 0;
      while (-1 != (n = in.read(buf))) {
        out.write(buf, 0, n);
      }
      out.close();
      in.close();
    } catch (final Exception e) {
      LOG.error("Error occoured while downloading images", e);
      throw e;
    }
    return out == null ? null : out.toByteArray();
  }
  
  public static void downloadAndCopy(final String url, final String destination)
      throws Exception {
  
    final byte[] downloadResult = download(url);
    if (downloadResult != null) {
      try {
        final FileOutputStream fos =
            new FileOutputStream(destination
                + url.substring(url.lastIndexOf("/") + 1));
        fos.write(downloadResult);
        fos.close();
      } catch (final IOException ex) {
        LOG.error("Failed to copy images to destination dir", ex);
        throw ex;
      }
    }
  }
}
