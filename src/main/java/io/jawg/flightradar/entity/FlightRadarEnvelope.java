package io.jawg.flightradar.entity;

import java.util.List;

/**
 * The main envelope for flight radar states
 *
 * @author Loïc Ortola on 09/07/2017
 */
public class FlightRadarEnvelope {
  private long lastUpdate;
  private List<FlightState> states;

  public long getLastUpdate() {
    return lastUpdate;
  }

  public List<FlightState> getStates() {
    return states;
  }

  @Override
  public String toString() {
    return "FlightRadarEnvelope{" +
        "lastUpdate=" + lastUpdate +
        ", states=" + states +
        '}';
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private long lastUpdate;
    private List<FlightState> states;

    private Builder() {
    }

    public Builder lastUpdate(long lastUpdate) {
      this.lastUpdate = lastUpdate;
      return this;
    }

    public Builder states(List<FlightState> states) {
      this.states = states;
      return this;
    }

    public FlightRadarEnvelope build() {
      FlightRadarEnvelope flightRadarEnvelopeDto = new FlightRadarEnvelope();
      flightRadarEnvelopeDto.states = this.states;
      flightRadarEnvelopeDto.lastUpdate = this.lastUpdate;
      return flightRadarEnvelopeDto;
    }
  }
}
