package io.jawg.flightradar.server.handler;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

/**
 * @author Clement Gayvallet
 * @since 5/11/17.
 */
public class FailureHandler implements Handler<RoutingContext> {

  /**
   * Creates a new handler.
   * @return the handler
   */
  public static FailureHandler create() {
    return new FailureHandler();
  }

  private FailureHandler() {
  }

  @Override
  public void handle(RoutingContext ctx) {
    Throwable failure = ctx.failure();
    if (failure instanceof SecurityException) {
      ctx.response()
          .setStatusCode(401)
          .end(failure.getMessage());
    } else if (failure instanceof NumberFormatException) {
      ctx.response()
          .setStatusCode(400)
          .end(failure.getMessage());
    } else {
      ctx.next();
    }
  }
}
