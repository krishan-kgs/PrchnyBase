/*
 *  Copyright 2010 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, Aug 13, 2010
 *  @author singla
 */

package com.prchny.base.fileutils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

import org.springframework.util.StringUtils;

public class ExcelSheetParser implements FileParser {
  
  private final File file;
  
  private boolean containsHeader = true;
  
  private String sheetName;
  
  public ExcelSheetParser(final String filePath, final String sheetName,
      final boolean containsHeader) {
  
    this(filePath, sheetName);
    this.containsHeader = containsHeader;
  }
  
  public ExcelSheetParser(final String filePath, final boolean containsHeader) {
  
    this(filePath);
    this.containsHeader = containsHeader;
  }
  
  public ExcelSheetParser(final String filePath, final String sheetName) {
  
    this(filePath);
    this.sheetName = sheetName;
  }
  
  public ExcelSheetParser(final String filePath) {
  
    file = new File(filePath);
    if (!file.exists()) {
      throw new IllegalArgumentException("No such file exists at path :"
          + filePath);
    }
  }
  
  @Override
  public Iterator<Row> parse() {
  
    try {
      final WorkbookSettings ws = new WorkbookSettings();
      ws.setEncoding("Cp1252");
      final Workbook wk = Workbook.getWorkbook(file, ws);
      Sheet s;
      if (!StringUtils.hasText(sheetName)) {
        s = wk.getSheet(0);
      } else {
        s = wk.getSheet(sheetName);
      }
      return new SheetIterator(s, containsHeader);
    } catch (final BiffException e) {
      throw new RuntimeException(e);
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  public static class SheetIterator implements Iterator<Row> {
    
    private final Sheet sheet;
    
    private int lineNo;
    
    private Map<String, Integer> columnNamesToIndex;
    
    public SheetIterator(final Sheet sheet, final boolean containsHeader) {
    
      this.sheet = sheet;
      if (containsHeader) {
        // read the column names and store the position for headers
        final String[] columnNames = getColumnValues(sheet.getRow(lineNo++));
        columnNamesToIndex = new HashMap<String, Integer>(columnNames.length);
        for (int i = 0; i < columnNames.length; i++) {
          columnNamesToIndex.put(columnNames[i], i);
        }
      }
    }
    
    private String[] getColumnValues(final Cell[] row) {
    
      final String[] columnValues = new String[row.length];
      for (int i = 0; i < row.length; i++) {
        columnValues[i] = row[i].getContents().trim();
      }
      return columnValues;
    }
    
    @Override
    public boolean hasNext() {
    
      return lineNo < sheet.getRows();
    }
    
    @Override
    public Row next() {
    
      if (hasNext()) {
        final String[] columnValues = getColumnValues(sheet.getRow(lineNo++));
        if (columnNamesToIndex != null) {
          return new Row(lineNo, columnValues, columnNamesToIndex);
        } else {
          return new Row(lineNo, columnValues);
        }
      }
      throw new NoSuchElementException();
    }
    
    @Override
    public void remove() {
    
      throw new UnsupportedOperationException();
    }
    
  }
}
