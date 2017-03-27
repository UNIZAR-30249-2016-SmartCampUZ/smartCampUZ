angular.module('smartCampUZApp')

    .controller('mapCtrl', ['$scope', '$state', 'userMap', function ($scope, $state, userMap) {
    	
        $(document).ready(function(){
        	var map = L.map('mapid').setView([41.68306, -0.88707], 17);
        	
        	
        	/**
        	 * GeoJSON LineString circling EINA
        	 */
        	var myLines = [{
        	    "type": "LineString",
        	    "coordinates": [[-0.88943, 41.68414], [-0.88243,41.68411]]
        	}, {
        	    "type": "LineString",
        	    "coordinates": [[-0.88943, 41.68414], [-0.88945,41.68305]]
        	}, {
        	    "type": "LineString",
        	    "coordinates": [[-0.88945,41.68305], [-0.8824,41.68300]]
        	}, {
        	    "type": "LineString",
        	    "coordinates": [[-0.8824,41.68300], [-0.88243,41.68411]]
        	}];

        	var myStyle = {
        	    "color": "#ff7800",
        	    "weight": 5,
        	    "opacity": 0.40
        	};

        	L.geoJSON(myLines, {
        	    style: myStyle
        	}).addTo(map);


        	/**
        	 * Marker azul
        	 */
        	var EINAmarker = L.marker([41.6839, -0.88598]).addTo(map);


        	/**
        	 * Popup binding
        	 */
        	EINAmarker.bindPopup("<b>EINA</b><br>Escuela de"+
        	    " Ingeniería y Arquitectura.").openPopup();


        	/**
        	 * Scale control
        	 */
        	L.control.scale({ position: 'topright' }).addTo(map);


        	/**
        	 * BuildingLayers
        	 */
        	var AdaByronMarker = L.marker([41.68363, -0.88891]).bindPopup("<b>Edificio Ada Byron</b>");
        	var TorresMarker = L.marker([41.68363, -0.88736]).bindPopup("<b>Edificio Torres Quevedo</b>");
        	var BetancourtMarker = L.marker([41.68347, -0.88394]).bindPopup("<b>Edificio Agustín de Betancourt</b>");
        	

        	/**
        	 * Popup de coordenadas
        	 */
        	var popup2 = L.popup();
        	function onMapClick(e) {
        	    popup2.setLatLng(e.latlng).setContent("<b>x=</b>" + e.latlng.lat.toString() + "<br>"+
        	            "<b>y=</b>" +e.latlng.lng.toString()+ "</br>"+e.latlng.toString())
        	        .openOn(map);
        	    
        	    $scope.lat=e.latlng.lat;
        	    $scope.lng=e.latlng.lng;
        	    $scope.latlng=e.latlng.lat + ", " + e.latlng.lng;
        	    
        	    $scope.sendCoordinates($scope.lat, $scope.lng);
        	}
        	map.on('click', onMapClick);
        	
        	// show the error login message when is false respectively
            var showError = function (error) {
                $scope.errorMsg = error;
                $scope.error = true;
            };
            
            var successMap = function (location) {
            	userMap.setCurrentLocation(location);
            };
            
        	$scope.sendCoordinates = function (lat, lng) {
        		userMap.setLocationFromCoordenates(lat, lng, successMap, showError);
            };

        	document.getElementById('campus').addEventListener('click', function () {
        		map.setView([41.68306, -0.88707], 17);
        		EINAmarker.addTo(map);
        		map.removeLayer(AdaByronMarker);
        		map.removeLayer(TorresMarker);
        		map.removeLayer(BetancourtMarker);
        		EINAmarker.openPopup();
        	});

        	document.getElementById('ada').addEventListener('click', function () {
        		map.setView([41.68363, -0.88891], 19);
        		AdaByronMarker.addTo(map);
        		map.removeLayer(TorresMarker);
        		map.removeLayer(EINAmarker);
        		map.removeLayer(BetancourtMarker);
        		AdaByronMarker.openPopup();
        	});

        	document.getElementById('torres').addEventListener('click', function () {
        		map.setView([41.68363, -0.88736], 19);
        		TorresMarker.addTo(map);
        		map.removeLayer(AdaByronMarker);
        		map.removeLayer(EINAmarker);
        		map.removeLayer(BetancourtMarker);
        		TorresMarker.openPopup();
        	});

        	document.getElementById('betan').addEventListener('click', function () {
        		map.setView([41.68347, -0.88394], 19);
        		BetancourtMarker.addTo(map);
        		map.removeLayer(TorresMarker);
        		map.removeLayer(AdaByronMarker);
        		map.removeLayer(EINAmarker);
        		BetancourtMarker.openPopup();
        	});
        	
        	/**
        	 * Background map
        	 */
        	var tileLayerBase = L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw', {
        	    maxZoom: 25,
        	    noWrap: true,
        	    attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, ' +
        	    '<a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
        	    'Imagery © <a href="http://mapbox.com">Mapbox</a>',
        	    id: 'mapbox.streets'
        	}).addTo(map);
        	 	
        	var PoligonosAda00 = L.tileLayer.wms("http://localhost:8080/geoserver/wms",{
        		request: 'GetMap',
        		maxZoom: 25,
        		layers: 'Smart_CampUZ:PoligonosAda00',
        	    noWrap:true,
        	    continuousWorld: false,
        	    transparent: true,
        	    format: 'image/png',
        	    version: '1.1.0',
        	    attribution: "myattribution",
        		zIndex: 2
        	}).addTo(map);

        	var PoligonosAda04 = L.tileLayer.wms("http://localhost:8080/geoserver/wms",{
        		request: 'GetMap',
        		maxZoom: 25,
        		layers: 'Smart_CampUZ:PoligonosAda04',
        	    noWrap:true,
        	    continuousWorld: false,
        	    transparent: true,
        	    format: 'image/png',
        	    version: '1.1.0',
        	    attribution: "myattribution",
        		zIndex: 2
        	}).addTo(map);


        	var LineasAda00 = L.tileLayer.wms("http://localhost:8080/geoserver/wms",{
        		request: 'GetMap',
        		maxZoom: 25,
        		layers: 'Smart_CampUZ:LineasAda00',
        	    noWrap:true,
        	    continuousWorld: false,
        	    transparent: true,
        	    format: 'image/png',
        	    version: '1.1.0',
        	    attribution: "myattribution",
        		zIndex: 2
        	}).addTo(map);


        	var Ada00 = L.layerGroup([LineasAda00,PoligonosAda00]);

        	var overlaysAda = {
        		    "Ada Byron P00": Ada00,
        		    "Ada Byron P04": PoligonosAda04
        	};
        	L.control.layers(overlaysAda).addTo(map);
        });
    }]);
