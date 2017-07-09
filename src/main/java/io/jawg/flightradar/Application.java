package io.jawg.flightradar;

/**
 * TODO class details.
 *
 * @author Loïc Ortola on 09/07/2017
 */

import io.jawg.flightradar.server.HttpServerVerticle;
import io.jawg.flightradar.server.SpringVerticleFactory;
import io.jawg.flightradar.server.worker.AsyncClientRefreshWorker;
import io.jawg.flightradar.server.worker.BufferWorker;
import io.netty.util.ResourceLeakDetector;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;


@SpringBootApplication
public class Application {

  private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

  @Autowired
  private AppConfiguration appConfiguration;

  @Autowired
  private Vertx vertx;
  
  @Autowired
  private SpringVerticleFactory verticleFactory;
  
  @Autowired
  private AsyncClientRefreshWorker asyncClientRefreshWorker;

  @Autowired
  private BufferWorker bufferWorker;
  
  public static void main(String[] args) {

    // This is basically the same example as the web-examples static site example but it's booted using
    // Spring Boot, not Vert.x
    SpringApplication.run(Application.class, args);
  }


  @Bean
  protected Vertx vertx() {
    return Vertx.vertx();
  }

  @PostConstruct
  public void deployVerticle() {
    ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.DISABLED);
    vertx.registerVerticleFactory(verticleFactory);
    
    // Scale verticles on cores
    int cores = Runtime.getRuntime().availableProcessors();
    DeploymentOptions options = new DeploymentOptions().setInstances(cores);
    vertx.deployVerticle(verticleFactory.prefix() + ":" + HttpServerVerticle.class.getName(), options, ar -> {
      if (ar.succeeded()) {
        LOGGER.info("[HTTP] Listening on {}:{}", appConfiguration.httpHost(), appConfiguration.httpPort());
      } else {
        LOGGER.error("[HTTP] Failed to bind on " + appConfiguration.httpHost() + ":" + appConfiguration.httpPort(), ar.cause());
        System.exit(1);
      }
    });
    
    // Deploy API Worker Verticles
    vertx.deployVerticle(asyncClientRefreshWorker, new DeploymentOptions().setWorker(true));
    vertx.deployVerticle(bufferWorker, new DeploymentOptions().setWorker(true));

  }
}