package io.jawg.flightradar.client;

import io.jawg.flightradar.entity.FlightRadarEnvelope;

/**
 * TODO class details.
 *
 * @author Loïc Ortola on 09/07/2017
 */
public interface FlightStateMapper<T> {
  
  double[] EUROPE_BBOX = new double[]{-5,41,8,51};
  
  FlightRadarEnvelope map(T o);
  
  default boolean isInBbox(double lat, double lng) {
    if (lng < EUROPE_BBOX[0] || lng > EUROPE_BBOX[2]) {
      return false;
    }
    if (lat < EUROPE_BBOX[1] || lat > EUROPE_BBOX[3]) {
      return false;
    }
    return true;
  }
}
