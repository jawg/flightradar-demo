package io.jawg.flightradar.entity;

import io.vertx.core.buffer.Buffer;

import java.util.Map;

/**
 * TODO class details.
 *
 * @author Loïc Ortola on 09/07/2017
 */
public class PlaneHistoryWrapper {
  private Map<String, Buffer> history;

  public PlaneHistoryWrapper(Map<String, Buffer> history) {
    this.history = history;
  }


  public Map<String, Buffer> getHistory() {
    return history;
  }

  public void setHistory(Map<String, Buffer> history) {
    this.history = history;
  }
}
