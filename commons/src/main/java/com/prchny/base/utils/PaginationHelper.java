/*
 *  Copyright 2011 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, Apr 22, 2011
 *  @author singla
 */

package com.prchny.base.utils;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class to paginate the results for processing (can be used for database
 * queries which return large datasets
 * 
 * @author singla
 * @param <T>
 */
public abstract class PaginationHelper<T> {
  
  private static final Logger LOG = LoggerFactory
      .getLogger(PaginationHelper.class);
  
  private final int pageSize;
  
  private int pageIndex;
  
  private int totalResults;
  
  public PaginationHelper(final int pageSize) {
  
    this.pageSize = pageSize;
  }
  
  /**
   * @param start
   * @param end
   * @return the results for next page
   */
  protected abstract List<T> moreResults(int start, int pageSize);
  
  /**
   * processes the pageItems for a particular page
   * 
   * @param pageItems
   * @param pageIndex
   */
  protected abstract void process(List<T> pageItems, int pageIndex);
  
  /**
   * starts the process pagination
   */
  public void paginate() {
  
    while (true) {
      // fetch next results
      List<T> pageItems = null;
      
      try {
        pageItems = moreResults(pageIndex * pageSize, pageSize);
      } catch (final Exception e) {
        paginateSingleObjects(pageIndex * pageSize, pageSize);
        pageIndex++;
        continue;
      }
      
      process(pageItems, pageIndex);
      final int pageItemsSize = pageItems == null ? 0 : pageItems.size();
      totalResults += pageItemsSize;
      // exits if no more results
      if (pageItemsSize < pageSize) {
        break;
      }
      pageIndex++;
    }
  }
  
  public void paginateSingleObjects(final int startingPoint,
      final int numberOfResults) {
  
    for (int i = 0; i < numberOfResults; i++) {
      try {
        final List<T> pageItems = moreResults(startingPoint + i, 1);
        process(pageItems, pageIndex);
        totalResults += pageItems.size();
      } catch (final Exception e) {
        LOG.error("Error fetching record {}", startingPoint + i);
        LOG.error("Stack trace: ", e);
      }
    }
    
  }
  
  public int getPageSize() {
  
    return pageSize;
  }
  
  public int getTotalResults() {
  
    return totalResults;
  }
}
