package io.jawg.flightradar.server;

import io.jawg.flightradar.server.handler.StatesHandler;
import io.jawg.flightradar.server.handler.HistoryHandler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * @author Loïc Ortola on 09/07/2017
 */
@Component
@Scope(SCOPE_PROTOTYPE)
public class FlightRadarRouter {

  private final Router router;

  @Autowired
  private FlightRadarRouter(Vertx vertx, StatesHandler statesHandler, HistoryHandler historyHandler) {
    this.router = Router.router(vertx);
    this.router.get("/api/states").handler(statesHandler);
    this.router.get("/api/history/:icao24").handler(historyHandler);
    
  }

  /**
   * Get the router.
   *
   * @return the router
   */
  public Router router() {
    return router;
  }

}

