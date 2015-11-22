/*
 *  Copyright 2010 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, Aug 13, 2010
 *  @author singla
 */

package com.prchny.base.fileutils;

import java.util.Map;

public class Row {
  
  private final int lineNo;
  
  private Map<String, Integer> headerPositions;
  
  private final String[] columnValues;
  
  public Row(final int lineNo, final String[] columnValues) {
  
    this.lineNo = lineNo;
    this.columnValues = columnValues;
  }
  
  public Row(final int lineNo, final String[] columnValues,
      final Map<String, Integer> headerPositions) {
  
    this(lineNo, columnValues);
    this.headerPositions = headerPositions;
  }
  
  public int getLineNo() {
  
    return lineNo;
  }
  
  /**
   * Index starts from 0
   * 
   * @param index
   * @return
   */
  public String getColumnValue(final int index) {
  
    if (columnValues != null) {
      if ((index < 0) || (index >= columnValues.length)) {
        return null;
      }
      return columnValues[index];
    }
    return null;
  }
  
  public String getColumnValue(final String columnName) {
  
    if (headerPositions == null) {
      throw new UnsupportedOperationException(
          "Access by Column Name is not allowed for files without header.");
    }
    final Integer columnIndex = headerPositions.get(columnName);
    if (columnIndex == null) {
      return null;
    }
    return getColumnValue(columnIndex);
  }
  
  @Override
  public String toString() {
  
    final StringBuilder builder = new StringBuilder();
    builder.append('[');
    for (final String s : columnValues) {
      builder.append('"').append(s).append('"').append(',');
    }
    return builder.deleteCharAt(builder.length() - 1).append(']').toString();
  }
}
