/*
 *  Copyright 2010 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, Aug 13, 2010
 *  @author singla
 */

package com.prchny.base.fileutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.springframework.util.StringUtils;

public class DelimitedFileParser implements FileParser {
  
  private final File file;
  
  private final char delim;
  
  private final boolean containsHeader;
  
  private final char enclosedBy;
  
  private final String encoding;
  
  private final boolean isMultiline = true;
  
  private static final char DEFAULT_DELIMITER = '\t';
  
  private static final char DEFAULT_ENCLOSEDBY = '"';
  
  private static final boolean DEFAULT_CONTAINS_HEADER = true;
  
  private static final String DEFAULT_ENCODING = "UTF-8";
  
  /**
   * full constructor for delimited file parser
   * 
   * @param filePath
   *          absolute file path
   * @param delim
   *          delim token delimiter default value is '\t'
   * @param enclosedBy
   *          token optional enclosed, default value is '"'
   * @param containsHeader
   *          true if file contains header line, default values is true
   * @param encoding
   *          character encoding to be used for the file
   */
  public DelimitedFileParser(final String filePath, final char delim,
      final char enclosedBy, final boolean containsHeader, final String encoding) {
  
    this.delim = delim;
    this.enclosedBy = enclosedBy;
    this.containsHeader = containsHeader;
    this.encoding = encoding;
    file = new File(filePath);
    if (!file.exists()) {
      throw new IllegalArgumentException("No such File Exists.File Path :"
          + filePath);
    }
  }
  
  /**
   * with encoding defaulted to 'UTF=8'
   * 
   * @param filePath
   *          absolute file path
   * @param delim
   *          delim token delimiter default value is '\t'
   * @param enclosedBy
   *          token optional enclosed, default value is '"'
   * @param containsHeader
   *          true if file contains header line, default values is true
   */
  public DelimitedFileParser(final String filePath, final char delim,
      final char enclosedBy, final boolean containsHeader) {
  
    this(filePath, delim, enclosedBy, containsHeader, DEFAULT_ENCODING);
  }
  
  /**
   * with enclosedBy defaulted to '"' and containsHeader defaulted to true
   * 
   * @param filePath
   *          absolute file path
   * @param delim
   *          token delimiter, default value is '\t'
   */
  public DelimitedFileParser(final String filePath, final char delim) {
  
    this(filePath, delim, DEFAULT_ENCLOSEDBY, DEFAULT_CONTAINS_HEADER,
        DEFAULT_ENCODING);
  }
  
  /**
   * with containsHeader defaulted to true
   * 
   * @param filePath
   *          absolute file path
   * @param delim
   *          token delimiter, default value is '\t'
   * @param enclosedBy
   *          token optional enclosed, default value is '"'
   */
  public DelimitedFileParser(final String filePath, final char delim,
      final char enclosedBy) {
  
    this(filePath, delim, enclosedBy, DEFAULT_CONTAINS_HEADER, DEFAULT_ENCODING);
  }
  
  /**
   * with delimiter defaulted to '\t', enclosedBy defaulted to '"' and
   * containsHeader defaulted to true
   * 
   * @param filePath
   *          absolute file path
   */
  public DelimitedFileParser(final String filePath) {
  
    this(filePath, DEFAULT_DELIMITER, DEFAULT_ENCLOSEDBY,
        DEFAULT_CONTAINS_HEADER, DEFAULT_ENCODING);
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see com.ixigo.io.FileParser#parse()
   */
  @Override
  public Iterator<Row> parse() {
  
    try {
      return new RowIterator(IOUtils.lineIterator(new FileInputStream(file),
          encoding), delim, enclosedBy, containsHeader, isMultiline);
    } catch (final FileNotFoundException e) {
      throw new IllegalArgumentException("No such File Exists.File Path :"
          + file.getAbsolutePath());
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
    
  }
  
  /**
   * RowIterator is a standard iterator which provides hasNext and next method.
   * If there are no more rows hasNext will return false.next will return the
   * next row and will throw NoSuchElementException if no next row is found
   * Internally it uses LineIterator over the file to iterate over the file
   */
  public static class RowIterator implements Iterator<Row> {
    
    // iterator over the lines of File
    private final LineIterator lineIterator;
    
    // Next row to be returned in next method
    private Row next;
    
    // current line number
    private int lineNo;
    
    // delim
    private final char delim;
    
    private final char enclosedBy;
    
    // column names , it will be null if containsHeader is false
    private Map<String, Integer> columnNamesToIndex;
    
    // it indicates whether we have iterated over the underlying line iterator
    // and already processed all the rows.
    private boolean completed;
    
    private final boolean isMultiline = true;
    
    /**
     * Constructs a Row Iterator
     * 
     * @param lineIterator
     *          iterator over the File
     * @param delim
     *          for identifying columns
     * @param containsHeader
     *          whether first line in the file is column header
     */
    public RowIterator(final LineIterator lineIterator, final char delim,
        final char enclosedBy, final boolean containsHeader,
        final boolean isMultiline) {
    
      this.lineIterator = lineIterator;
      this.delim = delim;
      this.enclosedBy = enclosedBy;
      if (containsHeader) {
        // reads till we get the first line
        final String[] columnNames = getNextRowValues(lineIterator);
        if (columnNames != null) {
          columnNamesToIndex = new HashMap<String, Integer>(columnNames.length);
          for (int i = 0; i < columnNames.length; i++) {
            columnNamesToIndex.put(columnNames[i], i);
          }
        }
      }
    }
    
    private String[] getNextRowValues(final LineIterator lineIterator) {
    
      while (lineIterator.hasNext()) {
        String line = lineIterator.nextLine();
        lineNo++;
        if (StringUtils.hasText(line) && !line.startsWith("#")) {
          final LineParser parser = new LineParser(delim, enclosedBy);
          while (parser.parseLine(line) && isMultiline
              && lineIterator.hasNext()) {
            line = lineIterator.nextLine();
            lineNo++;
          }
          return parser.getTokens();
        }
      }
      return null;
    }
    
    /**
     * Computes the Next element
     * 
     * @return true it computed next element or false if it is done and there
     *         are no rows left
     */
    private boolean computeNext() {
    
      final String[] nextRowValues = getNextRowValues(lineIterator);
      
      if (nextRowValues != null) {
        if (columnNamesToIndex != null) {
          // if contains header is set and column names has been read, lets read
          // data now
          // we need to make sure column names are matching with the columns
          // found in the row, otherwise
          // we cannot find which column is missing
          if (columnNamesToIndex.size() == nextRowValues.length) {
            // constructs the row and set it to next so that it can be returned
            // in next method call
            next = new Row(lineNo, nextRowValues, columnNamesToIndex);
            // yes it should return true as we have a next element which can be
            // returned in next method
            // call
            return true;
          } else {
            // politely log that we got a row which had columns which doesn't
            // match the column
            // headers
            System.out.println("Line No:" + lineNo
                + " has column mismatch with the column headers");
            return computeNext();
          }
        } else {
          // this case is when containsHeader is false
          // directly create a Row with column values
          next = new Row(lineNo, nextRowValues);
          // yes it should return true as we have a next element which can be
          // returned in next method
          // call
          return true;
        }
      }
      return false;
    }
    
    @Override
    /*
     * Determines whether there are more rows to be returned
     */
    public boolean hasNext() {
    
      // if we completed iterating and there are no more elements
      if (completed) {
        return false;
      }
      // otherwise computeNext and check completed again
      if (next == null) {
        completed = !computeNext();
      }
      return !completed;
    }
    
    @Override
    public Row next() {
    
      // if we are not complete and next is null then lets compute next
      if (!completed && (next == null)) {
        completed = !computeNext();
      }
      // check if we have next, if yes then return it and set next to null
      if (next != null) {
        try {
          return next;
        } finally {
          next = null;
        }
      }
      // we dont have any more elements throw the NoSuchElementException
      throw new NoSuchElementException();
    }
    
    /*
     * We are not supporting remove operation
     */
    @Override
    public void remove() {
    
      throw new UnsupportedOperationException();
    }
    
  }
  
  public static void main(final String[] args) {
  
    final FileParser parser =
        new DelimitedFileParser(
            "/home/kamal/Documents/SDMobile-Cancelled-Verified Orders.txt",
            ',', '"', true);
    final Iterator<Row> iterator = parser.parse();
    while (iterator.hasNext()) {
      System.out.println(iterator.next());
    }
  }
  
}
