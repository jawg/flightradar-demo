package opensky;

import io.jawg.flightradar.entity.FlightRadarEnvelope;
import io.jawg.flightradar.client.FlightRadarClient;
import io.jawg.flightradar.client.opensky.OpenSkyFlightRadarClient;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * Created by loicortola on 04/10/2016.
 */
public class OpenSkyApiTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(OpenSkyApiTest.class);
  private FlightRadarClient client;
  
  @Before
  public void initializeCache() {
    client = new OpenSkyFlightRadarClient();
  }

  @Test
  public void testRetrieveData() {
    FlightRadarEnvelope flightStates = client.getFlightStates();
    Assert.notNull(flightStates, "Flight states cannot be null");
  }
}
