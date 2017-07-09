package io.jawg.flightradar.server.worker;

import io.jawg.flightradar.client.FlightRadarClient;
import io.jawg.flightradar.entity.FlightRadarEnvelope;
import io.jawg.flightradar.entity.FlightRadarEnvelopeDto;
import io.jawg.flightradar.entity.FlightState;
import io.jawg.flightradar.entity.PlaneHistory;
import io.jawg.flightradar.entity.UpdateWrapper;
import io.jawg.flightradar.server.codec.UpdateWrapperCodec;
import io.vertx.core.AbstractVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * The main FlightRadar Worker which sends the relevant data to verticles.
 *
 * @author Loïc Ortola on 09/07/2017
 */
@Component
public class AsyncClientRefreshWorker extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(AsyncClientRefreshWorker.class);
  
  public static final String MSG_DATA_UPDATED = "flightradar.data.updated";
  
  public static final long REFRESH_TIME = 10000;
  public static final long MAX_HISTORY = 10000;
  
  private final FlightRadarClient flightRadarClient;
  private final UpdateWrapperCodec codec;
  private LinkedList<FlightRadarEnvelope> database = new LinkedList<>();
  private long lastUpdate;
  
  @Autowired
  public AsyncClientRefreshWorker(FlightRadarClient flightRadarClient, UpdateWrapperCodec codec) {
    this.flightRadarClient = flightRadarClient;
    this.codec = codec;
  }
  
  @Override
  public void start() throws Exception {
    LOGGER.debug("[Worker] Starting in " + Thread.currentThread().getName());
    
    vertx.eventBus().registerDefaultCodec(UpdateWrapper.class, codec);
    
    vertx.setPeriodic(REFRESH_TIME, event -> {
      LOGGER.debug("[Worker] Attempting to refresh flight states in " + Thread.currentThread().getName());
      FlightRadarEnvelope flightStates = flightRadarClient.getFlightStates();
      LOGGER.debug("[Worker] Refresh successful. Envelope has {}", flightStates.getStates() == null ? -1 : flightStates.getStates().size());
      addLatest(flightStates);
      Map<String, PlaneHistory> planeHistoryMap = computeHistoryByPlane();
      vertx.eventBus().send(MSG_DATA_UPDATED, new UpdateWrapper(FlightRadarEnvelopeDto.from(flightStates), planeHistoryMap));
    });

  }

  private Map<String, PlaneHistory> computeHistoryByPlane() {
    Map<String, PlaneHistory> planeHistory = new HashMap<>();
    for (FlightRadarEnvelope envelope : database) {
      for (FlightState state : envelope.getStates()) {
        planeHistory.putIfAbsent(state.getIcao24(), new PlaneHistory(state));
        PlaneHistory ph = planeHistory.get(state.getIcao24());
        ph.addHistory(envelope.getLastUpdate(), state.getHeading(), state.getLatitude(), state.getLongitude(), state.getAltitude(), state.getVelocity(), state.getVerticalRate(), state.getOnGround());
        if (ph.getLastPositionUpdate() < state.getLastPositionUpdate()) {
          ph.setLastPositionUpdate(state.getLastPositionUpdate());
        }
        if (ph.getLastVelocityUpdate() < state.getLastVelocityUpdate()) {
          ph.setLastVelocityUpdate(state.getLastVelocityUpdate());
        }
      }
    }
    return planeHistory;
  }

  private void addLatest(FlightRadarEnvelope flightStates) {
    if (flightStates.getLastUpdate() <= lastUpdate) {
      // Data is not fresher than our current data. useless
      return;
    }
    // Add latest at first position
    database.add(0, flightStates);
    // Remove oldest if cache is full
    if (database.size() == MAX_HISTORY) {
      database.removeLast();
    }
    // Set lastUpdate
    lastUpdate = flightStates.getLastUpdate();
  }
}