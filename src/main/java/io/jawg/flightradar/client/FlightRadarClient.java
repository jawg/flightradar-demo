package io.jawg.flightradar.client;

import io.jawg.flightradar.entity.FlightRadarEnvelope;

/**
 * TODO class details.
 *
 * @author Loïc Ortola on 09/07/2017
 */
public interface FlightRadarClient {
  FlightRadarEnvelope getFlightStates();
}
