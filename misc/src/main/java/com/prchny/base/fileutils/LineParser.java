/*
 *  Copyright 2010 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, Aug 13, 2010
 *  @author singla
 */

package com.prchny.base.fileutils;

import java.util.ArrayList;
import java.util.List;

public class LineParser {
  
  private final char delim;
  
  private final char enclosedBy;
  
  private StringBuilder tokenBuilder;
  
  private boolean inField;
  
  private final List<String> tokensOnThisLine = new ArrayList<String>();
  
  private static final char ESCAPE_CHARACTER = '\\';
  
  public LineParser(final char delim, final char enclosedBy) {
  
    this.delim = delim;
    this.enclosedBy = enclosedBy;
  }
  
  public boolean parseLine(final String nextLine) {
  
    boolean inQuotes = false;
    if (tokenBuilder != null) {
      inQuotes = true;
      inField = true;
    } else {
      tokenBuilder = new StringBuilder();
    }
    for (int i = 0; i < nextLine.length(); i++) {
      final char c = nextLine.charAt(i);
      if (c == ESCAPE_CHARACTER) {
        if (nextLine.length() > (i + 1)) {
          final Character eChar = getEscapedChar(nextLine.charAt(i + 1));
          if (eChar != null) {
            tokenBuilder.append(eChar);
            i++;
          } else {
            tokenBuilder.append(c);
          }
        } else {
          tokenBuilder.append(c);
        }
      } else if (c == enclosedBy) {
        if (!inField) {
          inQuotes = true;
          inField = true;
        } else if (inQuotes) {
          i = nextLine.indexOf(delim, i);
          inQuotes = false;
          tokensOnThisLine.add(tokenBuilder.toString().trim());
          tokenBuilder.setLength(0);
          inField = false;
          if (i == -1) {
            return false;
          }
        } else {
          tokenBuilder.append(c);
        }
      } else if ((c == delim) && !inQuotes) {
        tokensOnThisLine.add(tokenBuilder.toString().trim());
        tokenBuilder.setLength(0);
        inField = false;
      } else if (inField || !Character.isWhitespace(c)) {
        tokenBuilder.append(c);
        inField = true;
      }
    }
    // line is done - check status
    if (inQuotes) {
      // continuing a quoted section, re-append newline
      tokenBuilder.append("\n");
      return true;
    } else {
      tokensOnThisLine.add(tokenBuilder.toString().trim());
      return false;
    }
  }
  
  private Character getEscapedChar(final char ch) {
  
    switch (ch) {
      case '\\':
        return '\\';
      case '\'':
        return '\'';
      case '\"':
        return '"';
      case 'r':
        return '\r';
      case 'f':
        return '\f';
      case 't':
        return '\t';
      case 'n':
        return '\n';
      case 'b':
        return '\b';
      default:
        return null;
    }
  }
  
  protected boolean isAllWhiteSpace(final CharSequence sb) {
  
    final boolean result = true;
    for (int i = 0; i < sb.length(); i++) {
      final char c = sb.charAt(i);
      
      if (!Character.isWhitespace(c)) {
        return false;
      }
    }
    return result;
  }
  
  public String[] getTokens() {
  
    return tokensOnThisLine.toArray(new String[tokensOnThisLine.size()]);
  }
  
}
