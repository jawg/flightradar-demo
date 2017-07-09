package io.jawg.flightradar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * App configuration
 *
 * @author Loïc Ortola on 09/07/2017
 */

@Configuration
public class AppConfiguration {

  @Autowired
  Environment environment;

  public int httpPort() {
    return environment.getProperty("http.port", Integer.class, 8080);
  }
  
  public boolean loggingEnabled() {
    return environment.getProperty("logging.enabled", Boolean.class, true);
  }
  
  public String httpHost() {
    return environment.getProperty("http.host", String.class, "localhost");
  } 

}