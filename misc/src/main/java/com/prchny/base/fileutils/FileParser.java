/*
 *  Copyright 2010 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, Aug 13, 2010
 *  @author singla
 */

package com.prchny.base.fileutils;

import java.util.Iterator;

public interface FileParser {
  
  /**
   * Parses the file and provides an Iterator over rows in the file
   * 
   * @return Iterator<Row> iterate over the lines of the file
   */
  public Iterator<Row> parse();
}
