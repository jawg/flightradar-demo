<template>
  <div class="map-container">
    <div id="mapview">
    </div>
  </div>
</template>

<script>
import Vue from 'vue';
import VueResource from 'vue-resource';
Vue.use(VueResource);

function getPreviousState(data, icao24) {
 for (var i in data) {
   if (data[i].icao24 === icao24) {
    return data[i];
   }
 }
}

function updateState(oldState, newState) {
 oldState.heading = newState.heading;
 oldState.latitude = newState.latitude;
 oldState.longitude = newState.longitude;
 oldState.altitude = newState.altitude;
 oldState.onGround = newState.onGround;
 oldState.originCountry = newState.originCountry;
 oldState.lastPositionUpdate = newState.lastPositionUpdate;
 oldState.lastVelocityUpdate = newState.lastVelocityUpdate;
 oldState.velocity = newState.velocity;
 oldState.verticalRate = newState.verticalRate;
 oldState.marker.setLngLat(new mapboxgl.LngLat(newState.longitude, newState.latitude));
 rotateMarker(oldState.el.childNodes[0], newState.heading);
 oldState.reset = true;
}

function mergeMarkers(map, oldData, newData) {
 var states = [];
 for (var i in newData) {
   var previousState = getPreviousState(oldData, newData[i].icao24);
   if (previousState) {
    // The plane was already here. Update its position and content.
    updateState(previousState, newData[i]);
    states.push(previousState);
   } else {
    // The plane is new. Add marker on map.
    addMarkerOnMap(map, newData[i]);
    states.push(newData[i]);
   }
   // TODO: remove old planes
 }
 return states;
}
// Earth radius in meters
var R = 6378137;
 
function computeNextCoordinates(elapsedTime, speedKn, headingDeg, lat, lng) {
  // Translate speed into meters per second
  var speedMs = speedKn * 1.852 / 3.600;
  // Distance flown in meters at current heading since elapsedTime 
  var distanceFlown = speedMs * elapsedTime / 1000;
  var headingRad = headingDeg * (Math.PI / 180);
  var dn = distanceFlown * Math.cos(headingRad);
  var de = distanceFlown * Math.sin(headingRad);
  // Coordinate offsets in radians
  var dLat = dn / R / (Math.PI / 180);
  var dLon = de / (R * Math.cos(lat * (Math.PI / 180))) / (Math.PI / 180);
  // OffsetPosition, decimal degrees
  var latO = lat + dLat;
  var lonO = lng + dLon;
  return [lonO, latO];
}

function addMarkerOnMap(map, state) {
 state.el = getMarker(state);
 state.marker = new mapboxgl.Marker(state.el, {offset: [-23, -25]})
   .setLngLat([state.longitude, state.latitude])
   .addTo(map);
 
 function animateMarker(timestamp) {
   state.marker.setLngLat(computeNextCoordinates(timestamp, state.velocity, state.heading, state.latitude, state.longitude));
   // Request the next frame of the animation.
   if (state.reset) {
    state.reset = false;
    animateMarker(0);
   } else {
    requestAnimationFrame(animateMarker);
   }
 }
 // Start the animation.
 animateMarker(0);   
}


function rotateMarker(el, heading) {
  el.style['-ms-transform'] = 'rotate(' + heading + 'deg)';
  el.style['-webkit-transform'] = 'rotate(' + heading + 'deg)';
  el.style['transform'] = 'rotate(' + heading + 'deg)';
}

function getMarker(data) {
  var el = document.createElement('div');
  el.className = 'marker';
  el.style.backgroundImage = 'url(\'img/plane.png\')';
  el.style.width = '46px';
  el.style.height = '50px';
  rotateMarker(el, data.heading);
    
  el.addEventListener('click', function() {
    window.alert("name:" + data.callsign + " | origin: " + data.originCountry + " | altitude:" + data.altitude + "m | speed: " + data.velocity + "kn | heading " + data.heading);
  });
  var parent = document.createElement('div');
  parent.appendChild(el);
  return parent;
}

function mapState(data) {
  var state = {}
  state.callsign = data[0];
  state.icao24 = data[1];
  state.originCountry = data[2];
  state.lastPositionUpdate = data[3];
  state.lastVelocityUpdate = data[4];
  state.heading = data[5];
  state.latitude = data[6];
  state.longitude = data[7];
  state.altitude = data[8];
  state.velocity = data[9];
  state.verticalRate = data[10];
  state.onGround = data[11];
  return state;
}

export default {
  name: 'Techs',
  data() {
    return {
      states: [],
      selected: null
    };
  },
  mounted() {
    mapboxgl.accessToken = 'pk.eyJ1IjoibG9ydG9sYSIsImEiOiI0ZTczMDEzOGQ4NDJhMGVmZDg5YmQzMGNjOWIxNWI2NiJ9.fu_Bb9EXGFDM6YMHHj0F6g';
    var map = new mapboxgl.Map({
      container: 'mapview',
      style: 'mapbox://styles/lortola/ciwm8jpo6001z2pnv25xqgcc9',
      zoom: 6,
      center: [1.582031,47.062638]
    });
    var self = this;
    
    function reload() {
     return self.$http.get('http://localhost:8080/api/states')
                               .then(response => response.json())
                               .then(response => {
                                 var states = [];
                                 for (var i in response.states) {
                                   var state = mapState(response.states[i]);
                                   states.push(state);
                                 }
                                 self.states = mergeMarkers(map, self.states, states);
                               });
    }
    setInterval(reload, 10000);
    return reload();
  }
};
</script>
