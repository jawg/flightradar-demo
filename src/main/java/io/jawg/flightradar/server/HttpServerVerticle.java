package io.jawg.flightradar.server;

/**
 * TODO class details.
 *
 * @author Loïc Ortola on 09/07/2017
 */

import io.jawg.flightradar.AppConfiguration;
import io.jawg.flightradar.server.handler.FailureHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.LoggerHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.TimeoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * @author Clement Gayvallet
 * @since 14/12/16
 */
@Component
// Prototype scope is needed as multiple instances of this verticle will be deployed
@Scope(SCOPE_PROTOTYPE)
public class HttpServerVerticle extends AbstractVerticle {

  private static final long TIMEOUT = 30000;

  private final FlightRadarRouter flightRadarRouter;
  private final AppConfiguration appConfiguration;

  @Autowired
  public HttpServerVerticle(FlightRadarRouter flightRadarRouter, AppConfiguration appConfiguration) {
    this.flightRadarRouter = flightRadarRouter;
    this.appConfiguration = appConfiguration;
  }

  @Override
  public void start(Future<Void> startFuture) throws Exception {
    // Register routes
    Router router = Router.router(vertx);
    // CORS
    router.route().handler(getCorsHandler());
    // Timeout
    router.route().handler(TimeoutHandler.create(TIMEOUT));
    // Logging
    if (appConfiguration.loggingEnabled()) {
      router.route().handler(LoggerHandler.create());
    }
    // API Endpoints
    router.mountSubRouter("/", flightRadarRouter.router());
    // Static Resources Handler
    router.route().handler(StaticHandler.create());
    // Failure Handler
    router.route().failureHandler(FailureHandler.create());
    
    
    // HttpServer
    HttpServerOptions options = new HttpServerOptions()
        .setHost(appConfiguration.httpHost())
        .setPort(appConfiguration.httpPort());
    vertx.createHttpServer(options)
        .requestHandler(router::accept)
        .listen(res -> {
          if (res.succeeded()) {
            startFuture.complete();
          } else {
            startFuture.fail(res.cause());
          }
        });
  }

  private CorsHandler getCorsHandler() {
    return CorsHandler.create("*")
          .allowedMethod(HttpMethod.GET)
          .allowedMethod(HttpMethod.POST)
          .allowedMethod(HttpMethod.PUT)
          .allowedMethod(HttpMethod.PATCH)
          .allowedMethod(HttpMethod.DELETE)
          .allowedMethod(HttpMethod.OPTIONS)
          .allowedHeader(HttpHeaders.AUTHORIZATION.toString())
          .allowedHeader(HttpHeaders.CONTENT_TYPE.toString());
  }
}