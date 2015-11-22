/*
 *  Copyright 2011 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 19-Oct-2011
 *  @author rahul
 */

package com.prchny.base.utils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ImagingUtils {
  
  private static Executor executor = new ThreadPoolExecutor(2, 2, 120,
      TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(5));
  
  public static void convert(final String srcFilePath,
      final String destfilePath, final int width, final int height)
      throws IOException {
  
    final int slashIndex = destfilePath.lastIndexOf('/');
    String dirnameWithoutExtension;
    dirnameWithoutExtension = destfilePath.substring(0, slashIndex);
    
    new File(dirnameWithoutExtension).mkdirs();
    
    final StringBuilder sb = new StringBuilder("convert ");
    sb.append(srcFilePath).append(" -resize ").append(width).append('x')
        .append(height).append(' ');
    sb.append(destfilePath);
    final String args[] = {};
    Runtime.getRuntime().exec(sb.toString(), args);
    // @TODO uncomment it. Commented for testing
    /*
     * final String command = sb.toString(); Runnable task = new Runnable () {
     * public void run() { String args[] = {}; try { Process p =
     * Runtime.getRuntime().exec(command, args); p.waitFor(); } catch (Exception
     * e) { e.printStackTrace(); } } } ; executor.execute( task);
     */
  }
  
  public static void main(final String[] args) throws IOException {
  
    // convert("/home/rahul/Desktop/fwd2xpics/jewellery_2x.jpg",
    // "/home/rahul/Desktop/fwd2xpics/jewellery_2x_310x363.jpg", 310, 363);
    convert("/home/rahul/Desktop/fwd2xpics/top_2x.jpg",
        "/home/rahul/Desktop/fwd2xpics/xyz/top_2x_50x58.jpg", 50, 58);
    // convert("/home/rahul/Desktop/fwd2xpics/tshirt_2x.jpg",
    // "/home/rahul/Desktop/fwd2xpics/tshirt_2x_310x363.jpg", 310, 363);
    
    // convert("/home/rahul/Desktop/fwd2xpics/jewellery_2x.jpg",
    // "/home/rahul/Desktop/fwd2xpics/jewellery_2x_154x100.jpg", 154, 100);
    // convert("/home/rahul/Desktop/fwd2xpics/top_2x.jpg",
    // "/home/rahul/Desktop/fwd2xpics/top_2x_154x100.jpg", 154, 100);
    // convert("/home/rahul/Desktop/fwd2xpics/tshirt_2x.jpg",
    // "/home/rahul/Desktop/fwd2xpics/tshirt_2x_154x100.jpg", 154, 100);
  }
  
}
