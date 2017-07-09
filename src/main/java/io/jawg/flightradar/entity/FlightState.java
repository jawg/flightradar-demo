package io.jawg.flightradar.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class represents the flight state
 *
 * @author Loïc Ortola on 09/07/2017
 */
public class FlightState {
  private String callsign;
  private String icao24;
  private Double heading;
  private Double latitude;
  private Double longitude;
  private Double altitude;
  @JsonProperty("on_ground")
  private Boolean onGround;
  @JsonProperty("origin_country")
  private String originCountry;
  @JsonProperty("last_position_update")
  private Long lastPositionUpdate;
  @JsonProperty("last_velocity_update")
  private Long lastVelocityUpdate;
  private Double velocity;
  @JsonProperty("vertical_rate")
  private Double verticalRate;

  public String getCallsign() {
    return callsign;
  }

  public String getIcao24() {
    return icao24;
  }
  
  public Double getHeading() {
    return heading;
  }

  public Double getLatitude() {
    return latitude;
  }

  public Double getLongitude() {
    return longitude;
  }

  public Double getAltitude() {
    return altitude;
  }

  public Boolean getOnGround() {
    return onGround;
  }

  public String getOriginCountry() {
    return originCountry;
  }

  public Long getLastPositionUpdate() {
    return lastPositionUpdate;
  }

  public Long getLastVelocityUpdate() {
    return lastVelocityUpdate;
  }

  public Double getVelocity() {
    return velocity;
  }

  public Double getVerticalRate() {
    return verticalRate;
  }
  
  @Override
  public String toString() {
    return "FlightState{" +
        "callsign='" + callsign + '\'' +
        ", icao24='" + icao24 + '\'' +
        ", heading=" + heading +
        ", latitude=" + latitude +
        ", longitude=" + longitude +
        ", altitude=" + altitude +
        ", onGround=" + onGround +
        ", originCountry='" + originCountry + '\'' +
        ", lastPositionUpdate=" + lastPositionUpdate +
        ", lastVelocityUpdate=" + lastVelocityUpdate +
        ", velocity=" + velocity +
        ", verticalRate=" + verticalRate +
        '}';
  }

  public static Builder builder() {
    return new Builder();
  }
  
  public static final class Builder {
    private String callsign;
    private String icao24;
    private Double heading;
    private Double latitude;
    private Double longitude;
    private Double altitude;
    private Boolean onGround;
    private String originCountry;
    private Long lastPositionUpdate;
    private Long lastVelocityUpdate;
    private Double velocity;
    private Double verticalRate;

    private Builder() {
    }


    public Builder callsign(String callsign) {
      this.callsign = callsign;
      return this;
    }

    public Builder icao24(String icao24) {
      this.icao24 = icao24;
      return this;
    }

    public Builder heading(Double heading) {
      this.heading = heading;
      return this;
    }

    public Builder latitude(Double latitude) {
      this.latitude = latitude;
      return this;
    }

    public Builder longitude(Double longitude) {
      this.longitude = longitude;
      return this;
    }

    public Builder altitude(Double altitude) {
      this.altitude = altitude;
      return this;
    }

    public Builder onGround(Boolean onGround) {
      this.onGround = onGround;
      return this;
    }

    public Builder originCountry(String originCountry) {
      this.originCountry = originCountry;
      return this;
    }

    public Builder lastPositionUpdate(Long lastPositionUpdate) {
      this.lastPositionUpdate = lastPositionUpdate;
      return this;
    }

    public Builder lastVelocityUpdate(Long lastVelocityUpdate) {
      this.lastVelocityUpdate = lastVelocityUpdate;
      return this;
    }

    public Builder velocity(Double velocity) {
      this.velocity = velocity;
      return this;
    }

    public Builder verticalRate(Double verticalRate) {
      this.verticalRate = verticalRate;
      return this;
    }

    public FlightState build() {
      FlightState flightState = new FlightState();
      flightState.icao24 = this.icao24;
      flightState.lastPositionUpdate = this.lastPositionUpdate;
      flightState.velocity = this.velocity;
      flightState.longitude = this.longitude;
      flightState.altitude = this.altitude;
      flightState.heading = this.heading;
      flightState.onGround = this.onGround;
      flightState.lastVelocityUpdate = this.lastVelocityUpdate;
      flightState.callsign = this.callsign;
      flightState.verticalRate = this.verticalRate;
      flightState.latitude = this.latitude;
      flightState.originCountry = this.originCountry;
      return flightState;
    }
  }
}
