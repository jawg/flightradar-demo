package io.jawg.flightradar.server.codec;

/**
 * TODO class details.
 *
 * @author Loïc Ortola on 09/07/2017
 */

import io.jawg.flightradar.entity.UpdateWrapper;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import org.springframework.stereotype.Component;

/**
 * Vert.x core example for {@link io.vertx.core.eventbus.EventBus} and {@link MessageCodec}
 * @author Junbong
 */
@Component
public class UpdateWrapperCodec implements MessageCodec<UpdateWrapper, UpdateWrapper> {
  @Override
  public void encodeToWire(Buffer buffer, UpdateWrapper customMessage) {
    throw new UnsupportedOperationException("");
  }

  @Override
  public UpdateWrapper decodeFromWire(int position, Buffer buffer) {
    throw new UnsupportedOperationException("");
  }

  @Override
  public UpdateWrapper transform(UpdateWrapper envelope) {
    return envelope;
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