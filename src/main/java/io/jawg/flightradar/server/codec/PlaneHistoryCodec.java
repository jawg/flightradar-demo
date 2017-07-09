package io.jawg.flightradar.server.codec;

/**
 * TODO class details.
 *
 * @author Loïc Ortola on 09/07/2017
 */

import io.jawg.flightradar.entity.PlaneHistoryWrapper;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import org.springframework.stereotype.Component;

/**
 * Vert.x core example for {@link io.vertx.core.eventbus.EventBus} and {@link MessageCodec}
 * @author Junbong
 */
@Component
public class PlaneHistoryCodec implements MessageCodec<PlaneHistoryWrapper, PlaneHistoryWrapper> {
  @Override
  public void encodeToWire(Buffer buffer, PlaneHistoryWrapper customMessage) {
    throw new UnsupportedOperationException("");
  }

  @Override
  public PlaneHistoryWrapper decodeFromWire(int position, Buffer buffer) {
    throw new UnsupportedOperationException("");
  }

  @Override
  public PlaneHistoryWrapper transform(PlaneHistoryWrapper history) {
    return history;
  }

  @Override
  public String name() {
    // Each codec must have a unique name.
    // This is used to identify a codec when sending a message and for unregistering codecs.
    return this.getClass().getSimpleName();
  }

  @Override
  public byte systemCodecID() {
    // Always -1
    return -1;
  }
}