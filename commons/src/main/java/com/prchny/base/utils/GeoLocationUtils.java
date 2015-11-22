
package com.prchny.base.utils;

/**
 * @author singla
 */
public class GeoLocationUtils {
  
  static final double EARTH_RADIUS = 6378137.0;
  
  public enum DistanceUnit {
    KM(
        1000.0),
    MILES(
        1609.344);
    
    private final double conversionFactor;
    
    private DistanceUnit(final double conversionFactor) {
    
      this.conversionFactor = conversionFactor;
    }
    
    public double conversionFactor() {
    
      return conversionFactor;
    }
    
  }
  
  public static double radians(final double x) {
  
    return (x * Math.PI) / 180;
  }
  
  /**
   * Calculate the distance between two places in Km
   * 
   * @param lat1
   * @param lng1
   * @param lat2
   * @param lng2
   * @return
   */
  public static double distanceBetweenPlacesInKM(final double lat1,
      final double lng1, final double lat2, final double lng2) {
  
    return distanceBetweenPlaces(lat1, lng1, lat2, lng2, DistanceUnit.KM);
  }
  
  /**
   * Calculate the distance between two places in Km
   * 
   * @param lat1
   * @param lng1
   * @param lat2
   * @param lng2
   * @return
   */
  public static double distanceBetweenPlacesInMiles(final double lat1,
      final double lng1, final double lat2, final double lng2) {
  
    return distanceBetweenPlaces(lat1, lng1, lat2, lng2, DistanceUnit.MILES);
  }
  
  /**
   * Calculate the distance between two places.
   * 
   * @param lat1
   * @param lng1
   * @param lat2
   * @param lng2
   * @return
   */
  public static double distanceBetweenPlaces(final double lat1,
      final double lng1, final double lat2, final double lng2,
      final DistanceUnit distanceUnit) {
  
    final double dLat = Math.toRadians(lat2 - lat1);
    final double dLon = Math.toRadians(lng2 - lng1);
    final double a =
        (Math.sin(dLat / 2) * Math.sin(dLat / 2))
            + (Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2));
    final double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return (EARTH_RADIUS * c) / distanceUnit.conversionFactor;
  }
  
  /**
   * @param latitude
   * @param longitude
   * @param distance
   * @param distanceUnit
   * @return double[] with four elements double[0] = min latitude, double[1] =
   *         max latitude, double[2] = min longitude, double[3] = max longitude
   *         in degrees
   */
  public static double[] getEnclosingBox(final double latitude,
      final double longitude, double distance, final DistanceUnit distanceUnit) {
  
    distance = distance * distanceUnit.conversionFactor();
    final double[] northWest =
        getLatLongDirect(Math.toRadians(latitude), Math.toRadians(longitude),
            (3 * Math.PI) / 4, distance);
    final double[] southEast =
        getLatLongDirect(Math.toRadians(latitude), Math.toRadians(longitude),
            -Math.PI / 4, distance);
    
    final double[] box = new double[] {
        southEast[0], northWest[0], southEast[1], northWest[1]
    };
    return box;
  }
  
  private static double[] getLatLongDirect(final double latitude,
      final double longitude, final double bearing, final double distance) {
  
    final double lat2 =
        Math.asin((Math.sin(latitude) * Math.cos(distance / EARTH_RADIUS))
            + (Math.cos(latitude) * Math.sin(distance / EARTH_RADIUS) * Math
                .cos(bearing)));
    final double lon2 =
        longitude
            + Math.atan2(
                Math.sin(bearing) * Math.sin(distance / EARTH_RADIUS)
                    * Math.cos(latitude),
                Math.cos(distance / EARTH_RADIUS)
                    - (Math.sin(latitude) * Math.sin(lat2)));
    return new double[] {
        Math.toDegrees(lat2), Math.toDegrees(lon2)
    };
  }
}
