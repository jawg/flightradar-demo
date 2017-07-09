package io.jawg.flightradar.server.worker;

import io.jawg.flightradar.entity.FlightRadarEnvelopeDto;
import io.jawg.flightradar.entity.PlaneHistory;
import io.jawg.flightradar.entity.PlaneHistoryWrapper;
import io.jawg.flightradar.entity.UpdateWrapper;
import io.jawg.flightradar.server.codec.PlaneHistoryCodec;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import static io.jawg.flightradar.server.worker.AsyncClientRefreshWorker.MSG_DATA_UPDATED;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * The main FlightRadar Worker which sends the relevant data to verticles.
 *
 * @author Loïc Ortola on 09/07/2017
 */
@Component
public class BufferWorker extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(BufferWorker.class);
  
  public static final String MSG_STATES_BUFFERED = "flightradar.states.buffered";
  public static final String MSG_HISTORY_BUFFERED = "flightradar.history.buffered";
  private final PlaneHistoryCodec codec;

  @Autowired
  public BufferWorker(PlaneHistoryCodec codec) {
    this.codec = codec;
  }
  
  @Override
  public void start() throws Exception {
    LOGGER.debug("[Worker] Starting in " + Thread.currentThread().getName());
    
    vertx.eventBus().registerDefaultCodec(PlaneHistoryWrapper.class, codec);
    
    vertx.eventBus().consumer(MSG_DATA_UPDATED, event -> {
      UpdateWrapper wrapper = (UpdateWrapper) event.body();
      update(wrapper);
    });
    

  }

  private void update(UpdateWrapper wrapper) {
    vertx.eventBus().send(MSG_STATES_BUFFERED, getStatesBuffer(wrapper.envelope));
    vertx.eventBus().send(MSG_HISTORY_BUFFERED, new PlaneHistoryWrapper(getPlaneHistoryBuffers(wrapper.planeHistory)));
  }

  private Map<String, Buffer> getPlaneHistoryBuffers(Map<String, PlaneHistory> planeHistory) {
    Map<String, Buffer> newHistory = new HashMap<>();
    planeHistory.forEach((s, ph) -> newHistory.put(s, Buffer.buffer(getGzipContent(Json.encode(ph)))));
    return newHistory;
  }

  private Buffer getStatesBuffer(FlightRadarEnvelopeDto envelope) {
    String s = Json.encode(envelope);
    return Buffer.buffer(getGzipContent(s));
  }

  private byte[] getGzipContent(String s) {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    try (GZIPOutputStream gzip = new GZIPOutputStream(out)) {
      gzip.write(s.getBytes(UTF_8));
      gzip.finish();
      return out.toByteArray();
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }
}