package io.jawg.flightradar.server.handler;

import io.jawg.flightradar.server.worker.BufferWorker;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StatesHandler implements Handler<RoutingContext> {
  private static final Logger LOGGER = LoggerFactory.getLogger(StatesHandler.class);
  private Buffer bufferGzip;

  @Autowired
  public StatesHandler(Vertx vertx) {
    vertx.eventBus().consumer(BufferWorker.MSG_STATES_BUFFERED, event -> {
      LOGGER.debug("Updated states data buffer");
      this.bufferGzip = (Buffer) event.body();
    });
  }

  @Override
  public void handle(RoutingContext ctx) {
    if (bufferGzip == null) {
      ctx.response()
          .setStatusCode(204) // no content
          .end();
      return;
    }
    if (!supportsGzip(ctx)) {
      ctx.response()
          .setStatusCode(406) // no content
          .setStatusMessage("To get this content, you need to have a browser that supports GZip encoding")
          .putHeader("Accept-Encoding", "gzip")
          .end();
      return;
    }
    // Manual Gzip content
    ctx.response()
        .setChunked(true)
        .putHeader("Content-Type", "application/json charset=utf-8")
        .putHeader("Content-Encoding", "gzip")
        .putHeader("Content-Length", String.valueOf(bufferGzip.length()))
        .write(bufferGzip)
        .end();
  }

  private boolean supportsGzip(RoutingContext ctx) {
    String header = ctx.request().getHeader("Accept-Encoding");
    return header != null && header.contains("gzip");
  }
}
