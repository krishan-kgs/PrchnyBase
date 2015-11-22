/*
 *  Copyright 2010 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, Aug 13, 2010
 *  @author singla
 */

package com.prchny.base.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class DateUtils {
  
  private static final Logger LOG = LoggerFactory.getLogger(DateUtils.class);
  
  private final static TimeZone IST = TimeZone.getTimeZone("IST");
  
  public static TimeZone DEFAULT_TZ = IST;
  
  public static Date stringToDate(final String date, final String pattern) {
  
    final DateFormat format = new SimpleDateFormat(pattern);
    try {
      return format.parse(date);
    } catch (final ParseException e) {
      LOG.debug(e.getMessage());
    }
    return null;
  }
  
  public static String dateToString(final Date date, final String pattern) {
  
    if ((date != null) && StringUtils.hasText(pattern)) {
      final DateFormat format = new SimpleDateFormat(pattern);
      return format.format(date);
    } else {
      return null;
    }
  }
  
  public static boolean isPastTime(final Date input) {
  
    final Calendar currentTime = Calendar.getInstance();
    currentTime.setTime(getCurrentTime());
    final Calendar inputTime = Calendar.getInstance();
    inputTime.setTime(input);
    return inputTime.before(currentTime);
  }
  
  public static boolean after(final Date checkDate, final Date givenDate) {
  
    final Calendar currentTime = Calendar.getInstance();
    currentTime.setTime(givenDate);
    final Calendar inputTime = Calendar.getInstance();
    inputTime.setTime(checkDate);
    return inputTime.after(currentTime);
  }
  
  public static boolean isFutureTime(final Date input) {
  
    final Calendar currentTime = Calendar.getInstance();
    currentTime.setTime(getCurrentTime());
    final Calendar inputTime = Calendar.getInstance();
    inputTime.setTime(input);
    return inputTime.after(currentTime);
  }
  
  public static Date getCurrentTime() {
  
    return new Date();
  }
  
  public static Date getCurrentDate() {
  
    final Calendar now = Calendar.getInstance();
    now.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH),
        now.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
    now.set(Calendar.MILLISECOND, 0);
    return now.getTime();
  }
  
  public static boolean isSameDay(final Date d1, final Date d2) {
  
    return org.apache.commons.lang3.time.DateUtils.isSameDay(d1, d2);
  }
  
  /**
   * Usage Example : For date 5 Feb 2011 enter year = 2011, month = 2, and day =
   * 5
   * 
   * @param year
   * @param month
   * @param day
   * @return
   */
  public static Date createDate(final int year, final int month, final int day) {
  
    final Calendar date = Calendar.getInstance();
    date.set(year, month - 1, day, 0, 0, 0);
    return date.getTime();
  }
  
  public static boolean isDNDHour() {
  
    final Calendar now = Calendar.getInstance();
    return (now.get(Calendar.HOUR_OF_DAY) >= 21)
        || (now.get(Calendar.HOUR_OF_DAY) < 9);
  }
  
  public static Date addToDate(final Date date, final int type,
      final int noOfUnits) {
  
    final Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.add(type, noOfUnits);
    return calendar.getTime();
  }
  
  /**
   * Limit a date's resolution. For example, the date
   * <code>2004-09-21 13:50:11</code> will be changed to
   * <code>2004-09-01 00:00:00</code> when using <code>Resolution.MONTH</code>.
   * 
   * @param resolution
   *          The desired resolution of the date to be returned
   * @return the date with all values more precise than <code>resolution</code>
   *         set to 0 or 1
   */
  public static Date round(final Date date, final Resolution resolution) {
  
    return new Date(round(date.getTime(), resolution));
  }
  
  public static long round(final long time, final Resolution resolution) {
  
    return round(time, resolution, DEFAULT_TZ);
  }
  
  public static long round(final long time, final Resolution resolution,
      final TimeZone tz) {
  
    final Calendar cal = Calendar.getInstance(tz == null ? DEFAULT_TZ : tz);
    
    // protected in JDK's prior to 1.4
    // cal.setTimeInMillis(time);
    
    cal.setTime(new Date(time));
    
    if (resolution == Resolution.YEAR) {
      cal.set(Calendar.MONTH, 0);
      cal.set(Calendar.DAY_OF_MONTH, 1);
      cal.set(Calendar.HOUR_OF_DAY, 0);
      cal.set(Calendar.MINUTE, 0);
      cal.set(Calendar.SECOND, 0);
      cal.set(Calendar.MILLISECOND, 0);
    } else if (resolution == Resolution.MONTH) {
      cal.set(Calendar.DAY_OF_MONTH, 1);
      cal.set(Calendar.HOUR_OF_DAY, 0);
      cal.set(Calendar.MINUTE, 0);
      cal.set(Calendar.SECOND, 0);
      cal.set(Calendar.MILLISECOND, 0);
    } else if (resolution == Resolution.DAY) {
      cal.set(Calendar.HOUR_OF_DAY, 0);
      cal.set(Calendar.MINUTE, 0);
      cal.set(Calendar.SECOND, 0);
      cal.set(Calendar.MILLISECOND, 0);
    } else if (resolution == Resolution.HOUR) {
      cal.set(Calendar.MINUTE, 0);
      cal.set(Calendar.SECOND, 0);
      cal.set(Calendar.MILLISECOND, 0);
    } else if (resolution == Resolution.MINUTE) {
      cal.set(Calendar.SECOND, 0);
      cal.set(Calendar.MILLISECOND, 0);
    } else if (resolution == Resolution.SECOND) {
      cal.set(Calendar.MILLISECOND, 0);
    } else if (resolution == Resolution.MILLISECOND) {
      // don't cut off anything
    } else {
      throw new IllegalArgumentException("unknown resolution " + resolution);
    }
    return cal.getTime().getTime();
  }
  
  /**
   * Specifies the time granularity.
   */
  public static class Resolution {
    
    public static final Resolution YEAR = new Resolution("year");
    
    public static final Resolution MONTH = new Resolution("month");
    
    public static final Resolution DAY = new Resolution("day");
    
    public static final Resolution HOUR = new Resolution("hour");
    
    public static final Resolution MINUTE = new Resolution("minute");
    
    public static final Resolution SECOND = new Resolution("second");
    
    public static final Resolution MILLISECOND = new Resolution("millisecond");
    
    private String resolution;
    
    private Resolution() {
    
    }
    
    private Resolution(final String resolution) {
    
      this.resolution = resolution;
    }
    
    @Override
    public String toString() {
    
      return resolution;
    }
  }
  
  public static final class Interval {
    
    private final TimeUnit timeUnit;
    
    private final long period;
    
    public Interval(final TimeUnit timeUnit, final long period) {
    
      this.timeUnit = timeUnit;
      this.period = period;
    }
    
    public TimeUnit getTimeUnit() {
    
      return timeUnit;
    }
    
    public long getPeriod() {
    
      return period;
    }
    
    public int toMinutes() {
    
      return (int) timeUnit.toMinutes(period);
    }
    
  }
  
  public static DateRange getFutureInterval(final Date startTime,
      final Interval interval) {
  
    final Date endTime =
        addToDate(startTime, Calendar.MINUTE, interval.toMinutes());
    return new DateRange(startTime, endTime);
  }
  
  public static DateRange getPastInterval(final Date endTime,
      final Interval interval) {
  
    final Date startTime =
        addToDate(endTime, Calendar.MINUTE, -interval.toMinutes());
    return new DateRange(startTime, endTime);
  }
  
  public static DateRange getDayRange(final Date anytime) {
  
    final Date startTime = round(anytime, Resolution.DAY);
    return new DateRange(startTime, DateUtils.addToDate(startTime,
        Calendar.DATE, 1));
  }
  
  public static DateRange getLastDayRange() {
  
    return getDayRange(DateUtils.addToDate(DateUtils.getCurrentTime(),
        Calendar.DATE, -1));
  }
  
  public static class DateRange {
    
    @NotNull
    private Date start;
    
    @NotNull
    private Date end;
    
    public DateRange() {
    
    }
    
    public DateRange(final Date start, final Date end) {
    
      this.start = start;
      this.end = end;
    }
    
    public Date getStart() {
    
      return start;
    }
    
    public Date getEnd() {
    
      return end;
    }
    
    public void setStart(final Date start) {
    
      this.start = start;
    }
    
    public void setEnd(final Date end) {
    
      this.end = end;
    }
    
    @Override
    public String toString() {
    
      return start + " - " + end;
    }
    
  }
  
  /**
   * This method can find a time embedded in a string in the following formats :
   * hhmm, hh:mm, h:m, h:mm, hh:m (1 space after/before : is also accepted) This
   * method runs faster than the parse() method of Java
   * 
   * @param token
   * @return the date object
   */
  public static Date parseTime(final String token) {
  
    if ((token == null) || "".equals(token)) {
      return null;
    }
    
    final Calendar cal = new GregorianCalendar();
    cal.clear();
    
    final char[] ctoken = token.toCharArray();
    final StringBuilder hours = new StringBuilder(2);
    final StringBuilder mins = new StringBuilder(2);
    
    if (token.indexOf(":") < 0) {
      for (int i = 0; i < ctoken.length; i++) {
        if (Character.isDigit(ctoken[i])
            && (((i + 1) < ctoken.length) && Character.isDigit(ctoken[i + 1]))) {
          if ((((i + 2) < ctoken.length) && Character.isDigit(ctoken[i + 2]))
              && (((i + 3) < ctoken.length) && Character.isDigit(ctoken[i + 3]))) {
            hours.append(ctoken[i]).append(ctoken[i + 1]);
            mins.append(ctoken[i + 2]).append(ctoken[i + 3]);
          }
        }
      }
    } else {
      for (int i = 0; i < ctoken.length; i++) {
        if (ctoken[i] == ':') {
          if (((i - 1) >= 0) && Character.isDigit(ctoken[i - 1])) {
            if (((i - 2) >= 0) && Character.isDigit(ctoken[i - 2])) {
              hours.append(ctoken[i - 2]).append(ctoken[i - 1]);
            } else {
              hours.append(ctoken[i - 1]);
            }
          } else {
            if (((i - 2) >= 0) && Character.isDigit(ctoken[i - 2])) {
              if (((i - 3) >= 0) && Character.isDigit(ctoken[i - 3])) {
                hours.append(ctoken[i - 3]).append(ctoken[i - 2]);
              } else {
                hours.append(ctoken[i - 2]);
              }
            }
          }
          
          if (((i + 1) < ctoken.length) && Character.isDigit(ctoken[i + 1])) {
            if (((i + 2) < ctoken.length) && Character.isDigit(ctoken[i + 2])) {
              mins.append(ctoken[i + 1]).append(ctoken[i + 2]);
            } else {
              mins.append(ctoken[i + 1]);
            }
          } else {
            if (((i + 2) < ctoken.length) && Character.isDigit(ctoken[i + 2])) {
              if (((i + 3) < ctoken.length) && Character.isDigit(ctoken[i + 3])) {
                mins.append(ctoken[i + 2]).append(ctoken[i + 3]);
              } else {
                mins.append(ctoken[i + 2]);
              }
            }
          }
          break;
        }
      }
    }
    try {
      int hrs = Integer.parseInt(hours.toString());
      final int minutes = Integer.parseInt(mins.toString());
      if ((token.contains("pm") || token.contains("PM")) && (hrs != 12)) {
        hrs += 12;
        hrs %= 24;
      }
      if ((token.contains("am") || token.contains("AM")) && (hrs == 12)) {
        hrs = 0;
      }
      
      cal.set(Calendar.HOUR_OF_DAY, hrs);
      cal.set(Calendar.MINUTE, minutes);
    } catch (final NumberFormatException e) {
      LOG.debug("Could not parse " + hours + ":" + mins
          + " as time. Initial token was " + token);
      return null;
    }
    
    return cal.getTime();
  }
  
  public static long getDateDiff(final Date current, final Date dealtime) {
  
    long diff;
    diff = current.getTime() - dealtime.getTime();
    diff = diff / (1000 * 60 * 60);
    return diff;
  }
  
  public static long getNextInterval(final Date current, final Date reference,
      final long interval) {
  
    return current.getTime()
        + (interval - ((current.getTime() - reference.getTime()) % interval));
  }
  
  public static Date getDateTimeAfterGivenWorkingDays(final Date fromDate,
      final int noOfDays) {
  
    int count = 0;
    final Calendar c1 = Calendar.getInstance();
    if (fromDate != null) {
      c1.setTime(fromDate);
      
      while (count < noOfDays) {
        c1.add(Calendar.DATE, 1);
        if (!((c1.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) || (c1
            .get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY))) {
          count++;
        }
      }
      return c1.getTime();
    }
    return null;
  }
  
  public static Date getDateAfterGivenWorkingDays(final Date fromDate,
      final int noOfDays) {
  
    int count = 0;
    final Calendar c1 = Calendar.getInstance();
    if (fromDate != null) {
      c1.setTime(fromDate);
      
      while (count < noOfDays) {
        c1.add(Calendar.DATE, 1);
        /*
         * The below lines are commented due to sudden change in business
         * reqment from Ankit. Previously business logic was to exclude non
         * working days logic like Sat, Sun. (on 2012-02-21)
         */
        
        // if (!(c1.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ||
        // c1.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)) {
        count++;
        // }
      }
      return c1.getTime();
    }
    return null;
  }
  
  public static Date getDateBeforeGivenWorkingDays(final Date fromDate,
      final int noOfDays) {
  
    int count = noOfDays;
    final Calendar c1 = Calendar.getInstance();
    if (fromDate != null) {
      c1.setTime(fromDate);
      
      while (count > 0) {
        c1.add(Calendar.DATE, -1);
        /*
         * The below lines are commented due to sudden change in business
         * reqment from Ankit. Previously business logic was to exclude non
         * working days logic like Sat, Sun. (on 2012-02-21)
         */
        
        // if (!(c1.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ||
        // c1.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)) {
        count--;
        // }
      }
      return c1.getTime();
    }
    return null;
  }
  
  public static boolean isExpiringIn24Hrs(final Date endTime) {
  
    return endTime.after(DateUtils.getCurrentTime())
        && endTime.before(DateUtils.addToDate(DateUtils.getCurrentTime(),
            Calendar.MINUTE, 24 * 60));
  }
  
  public static Date getStartOfMonth(final int numOfMonths) {
  
    final Calendar c = Calendar.getInstance();
    c.add(Calendar.MONTH, -numOfMonths);
    c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);
    c.set(Calendar.MILLISECOND, 0);
    return c.getTime();
  }
  
  public static Date getEndOfMonth(final int numOfMonths) {
  
    final Calendar c = Calendar.getInstance();
    c.add(Calendar.MONTH, -numOfMonths);
    c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
    c.set(Calendar.HOUR_OF_DAY, 23);
    c.set(Calendar.MINUTE, 59);
    c.set(Calendar.SECOND, 59);
    c.set(Calendar.MILLISECOND, 999);
    return c.getTime();
  }
  
  public static Date getStartOfDay(final Date date) {
  
    final Calendar startTime = Calendar.getInstance();
    startTime.setTime(date);
    startTime.set(Calendar.HOUR_OF_DAY, 0);
    startTime.set(Calendar.MINUTE, 0);
    startTime.set(Calendar.SECOND, 0);
    startTime.set(Calendar.MILLISECOND, 0);
    return startTime.getTime();
  }
  
  public static Date getEndOfDay(final Date date) {
  
    final Calendar endTime = Calendar.getInstance();
    endTime.setTime(date);
    endTime.set(Calendar.HOUR_OF_DAY, 23);
    endTime.set(Calendar.MINUTE, 59);
    endTime.set(Calendar.SECOND, 59);
    endTime.set(Calendar.MILLISECOND, 999);
    return endTime.getTime();
  }
  
  public static void main(final String args[]) {
  
    final Date date = getCurrentTime();
    System.out.println(date);
    System.out.println(getWeekFormatForDate(date));
    System.out.println(getFornightFormatForDate(DateUtils.getCurrentTime()));
    // System.out.println(DateUtils.getCurrentTime());
    System.out.println(date);
  }
  
  public static Date getDateAfterGivenWorkingDays(final Date fromDate,
      final int noOfDays, final Set<Integer> weekends) {
  
    int count = 0;
    final Calendar c1 = Calendar.getInstance();
    if (fromDate != null) {
      c1.setTime(fromDate);
      
      while (count < noOfDays) {
        c1.add(Calendar.DATE, 1);
        /*
         * The below lines are commented due to sudden change in business
         * reqment from Ankit. Previously business logic was to exclude non
         * working days logic like Sat, Sun. (on 2012-02-21)
         */
        boolean skipDate = false;
        for (final Integer weekend : weekends) {
          if ((c1.get(Calendar.DAY_OF_WEEK) == weekend.intValue())) {
            skipDate = true;
            break;
          }
        }
        
        if (!(skipDate)) {
          count++;
        }
      }
      return c1.getTime();
    }
    return null;
  }
  
  public static String getFornightFormatForDate(final Date date) {
  
    final Calendar endTime = Calendar.getInstance();
    endTime.setTime(date);
    return (endTime.get(Calendar.DAY_OF_MONTH) > 15 ? "2nd" : "1st")
        + " Half of " + new SimpleDateFormat("MMMM").format(date);
  }
  
  public static String getWeekFormatForDate(final Date date) {
  
    final Calendar endTime = Calendar.getInstance();
    endTime.setTime(date);
    return endTime.get(Calendar.WEEK_OF_MONTH)
        + getDayOfMonthSuffix(endTime.get(Calendar.WEEK_OF_MONTH))
        + " Week Of " + new SimpleDateFormat("MMMM").format(date);
  }
  
  private static String getDayOfMonthSuffix(final int n) {
  
    String suffix = "th";
    if ((n == 1) || (n == 21) || (n == 31)) {
      suffix = "st";
    } else if ((n == 2) || (n == 22)) {
      suffix = "nd";
    } else if ((n == 3) || (n == 23)) {
      suffix = "rd";
    }
    return suffix;
  }
}
