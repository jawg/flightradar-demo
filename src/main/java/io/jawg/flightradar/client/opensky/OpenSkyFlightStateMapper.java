package io.jawg.flightradar.client.opensky;

import io.jawg.flightradar.entity.FlightRadarEnvelope;
import io.jawg.flightradar.entity.FlightState;
import io.jawg.flightradar.client.FlightStateMapper;
import org.opensky.model.OpenSkyStates;
import org.opensky.model.StateVector;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper for OpenSky Radar
 *
 * @author Loïc Ortola on 09/07/2017
 */
public class OpenSkyFlightStateMapper implements FlightStateMapper<OpenSkyStates> {
  @Override
  public FlightRadarEnvelope map(OpenSkyStates o) {
    if (o == null) {
      return null;
    }
    List<FlightState> flightStates = null;
    if (o.getStates() != null && !o.getStates().isEmpty()) {
      flightStates = new ArrayList<>(o.getStates().size());
      for (StateVector s : o.getStates()) {
        if (s != null && s.getLatitude() != null && s.getLongitude() != null && isInBbox(s.getLatitude(), s.getLongitude())) {
          flightStates.add(FlightState.builder()
              .callsign(s.getCallsign().trim())
              .icao24(s.getIcao24())
              .latitude(s.getLatitude())
              .longitude(s.getLongitude())
              .heading(s.getHeading())
              .altitude(s.getAltitude())
              .originCountry(s.getOriginCountry())
              .onGround(s.isOnGround())
              .velocity(s.getVelocity())
              .verticalRate(s.getVerticalRate())
              .lastPositionUpdate(s.getLastPositionUpdate() == null ? null : s.getLastPositionUpdate().longValue())
              .lastVelocityUpdate(s.getLastVelocityUpdate() == null ? null : s.getLastVelocityUpdate().longValue())
              .build());
        }
      }
    }
    return FlightRadarEnvelope.builder()
        .lastUpdate(o.getTime())
        .states(flightStates)
        .build();
  }

}
