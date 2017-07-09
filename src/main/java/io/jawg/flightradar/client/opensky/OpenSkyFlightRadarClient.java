package io.jawg.flightradar.client.opensky;

import io.jawg.flightradar.client.FlightRadarClient;
import io.jawg.flightradar.entity.FlightRadarEnvelope;
import org.opensky.api.OpenSkyApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * This client gets its data from opensky-network.
 *
 * @author Loïc Ortola on 09/07/2017
 */
@Component
public class OpenSkyFlightRadarClient implements FlightRadarClient {

  private static final Logger LOGGER = LoggerFactory.getLogger(OpenSkyFlightRadarClient.class);
  private OpenSkyApi openSkyApi;
  private OpenSkyFlightStateMapper mapper;

  public OpenSkyFlightRadarClient() {
    openSkyApi = new OpenSkyApi();
    mapper = new OpenSkyFlightStateMapper();
  }

  @Override
  public FlightRadarEnvelope getFlightStates() {
    try {
      return mapper.map(openSkyApi.getStates(0, null));
    } catch (IOException e) {
      LOGGER.warn("An error occured while calling remote API: ", e);
      throw new IllegalStateException(e);
    }
  }
}
