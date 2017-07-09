package io.jawg.flightradar.server.handler;

import io.jawg.flightradar.entity.PlaneHistoryWrapper;
import io.jawg.flightradar.server.worker.BufferWorker;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class HistoryHandler implements Handler<RoutingContext> {
  private static final Logger LOGGER = LoggerFactory.getLogger(HistoryHandler.class);
  private Map<String, Buffer> planeHistoryGzip;

  @Autowired
  public HistoryHandler(Vertx vertx) {
    vertx.eventBus().consumer(BufferWorker.MSG_HISTORY_BUFFERED, event -> {
      LOGGER.debug("Updated history data buffer");
      PlaneHistoryWrapper body = (PlaneHistoryWrapper) event.body();
      if (body.getHistory() != null && !body.getHistory().isEmpty()) {
        this.planeHistoryGzip = body.getHistory();
      }
    });
  }

  @Override
  public void handle(RoutingContext ctx) {
    
    String icao24 = ctx.pathParam("icao24");
    if (planeHistoryGzip == null || !planeHistoryGzip.containsKey(icao24)) {
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
    Buffer buffer = planeHistoryGzip.get(icao24);
    // Manual Gzip content
    ctx.response()
        .setChunked(true)
        .putHeader("Content-Type", "application/json charset=utf-8")
        .putHeader("Content-Encoding", "gzip")
        .putHeader("Content-Length", String.valueOf(buffer.length()))
        .write(buffer)
        .end();
  }

  private boolean supportsGzip(RoutingContext ctx) {
    String header = ctx.request().getHeader("Accept-Encoding");
    return header != null && header.contains("gzip");
  }

  
}
