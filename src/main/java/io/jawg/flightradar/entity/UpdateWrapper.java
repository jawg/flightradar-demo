package io.jawg.flightradar.entity;

import java.util.Map;

/**
 * TODO class details.
 *
 * @author Loïc Ortola on 09/07/2017
 */
public class UpdateWrapper {
  public final FlightRadarEnvelopeDto envelope;
  public final Map<String, PlaneHistory> planeHistory;

  public UpdateWrapper(FlightRadarEnvelopeDto envelope, Map<String, PlaneHistory> planeHistory) {
    this.envelope = envelope;
    this.planeHistory = planeHistory;
  }
}
