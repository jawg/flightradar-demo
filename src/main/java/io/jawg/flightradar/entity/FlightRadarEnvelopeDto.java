package io.jawg.flightradar.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * The main envelope for flight radar states
 *
 * @author Loïc Ortola on 09/07/2017
 */
public class FlightRadarEnvelopeDto {
  private long lastUpdate;
  private List<Object[]> states;

  public long getLastUpdate() {
    return lastUpdate;
  }

  public List<Object[]> getStates() {
    return states;
  }

  @Override
  public String toString() {
    return "FlightRadarEnvelope{" +
        "lastUpdate=" + lastUpdate +
        ", states=" + states +
        '}';
  }

  public static FlightRadarEnvelopeDto from(FlightRadarEnvelope envelope) {
    FlightRadarEnvelopeDto flightRadarEnvelopeDto = new FlightRadarEnvelopeDto();
    if (envelope.getStates() != null && !envelope.getStates().isEmpty()) {
      flightRadarEnvelopeDto.states = new ArrayList<>(envelope.getStates().size());
      for (FlightState s : envelope.getStates()) {
        flightRadarEnvelopeDto.states.add(
            new Object[]{s.getCallsign(), s.getIcao24(), s.getOriginCountry(), s.getLastPositionUpdate(), s.getLastVelocityUpdate(), s.getHeading(), s.getLatitude(), s.getLongitude(), s.getAltitude(), s.getVelocity(), s.getVerticalRate(), s.getOnGround()}
        );
      }
    }
    flightRadarEnvelopeDto.lastUpdate = envelope.getLastUpdate();
    return flightRadarEnvelopeDto;
  }

}
