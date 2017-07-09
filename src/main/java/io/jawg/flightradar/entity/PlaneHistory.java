package io.jawg.flightradar.entity;

import java.util.LinkedList;
import java.util.List;

/**
 * TODO class details.
 *
 * @author Loïc Ortola on 09/07/2017
 */
public class PlaneHistory {
  private String callsign;
  private String icao24;
  private String originCountry;
  private Long lastPositionUpdate;
  private Long lastVelocityUpdate;
  private List<Object[]> history = new LinkedList<>();

  public PlaneHistory(FlightState state) {
    this.callsign = state.getCallsign();
    this.icao24 = state.getIcao24();
    this.originCountry = state.getOriginCountry();
    this.lastPositionUpdate = state.getLastPositionUpdate();
    this.lastVelocityUpdate = state.getLastVelocityUpdate();
  }
  
  public String getCallsign() {
    return callsign;
  }

  public String getIcao24() {
    return icao24;
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

  public List<Object[]> getHistory() {
    return history;
  }

  public void setCallsign(String callsign) {
    this.callsign = callsign;
  }

  public void setIcao24(String icao24) {
    this.icao24 = icao24;
  }

  public void setOriginCountry(String originCountry) {
    this.originCountry = originCountry;
  }

  public void setLastPositionUpdate(Long lastPositionUpdate) {
    this.lastPositionUpdate = lastPositionUpdate;
  }

  public void setLastVelocityUpdate(Long lastVelocityUpdate) {
    this.lastVelocityUpdate = lastVelocityUpdate;
  }

  public void setHistory(List<Object[]> history) {
    this.history = history;
  }


  public void addHistory(Long timestamp, Double heading, Double latitude, Double longitude, Double altitude, Double velocity, Double verticalRate, Boolean onGround) {
    this.history.add(new Object[]{timestamp, heading, latitude, longitude, altitude, velocity, verticalRate, onGround});
  } 
}
