angular.module('smartCampUZApp')

    .controller('mapCtrl', ['$scope', '$state', 'userMap', function ($scope, $state, userMap) {
    	
        $(document).ready(function(){
        	//var map = L.map('mapid').setView([41.68306, -0.88707], 17);
        	var map = L.map('mapid', {
        	    minZoom: 17
        	}).setView([41.68306, -0.88627],17);
        	
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
        	 * Scale control
        	 */
        	L.control.scale({ position: 'topright' }).addTo(map);


        	/**
        	 * BuildingLayers
        	 */
        	var AdaByronMarker = L.marker([41.68363, -0.88891]).bindPopup("<b>Edificio Ada Byron</b>");
        	var TorresMarker = L.marker([41.68363, -0.88736]).bindPopup("<b>Edificio Torres Quevedo</b>");
        	var BetancourtMarker = L.marker([41.68375, -0.88411]).bindPopup("<b>Edificio Agustín de Betancourt</b>");
            var RoomMarker = L.marker([41.6839, -0.88598]);

        	/**
        	 * Popup de coordenadas
        	 */
        	var popup2 = L.popup();
        	function onMapClick(e) {
                map.removeLayer(RoomMarker);
                RoomMarker = L.marker([ e.latlng.lat, e.latlng.lng]).addTo(map);

        	    $scope.coordsPseudoMerkator = L.Projection.SphericalMercator.project(e.latlng);
        	    userMap.setLocationFromCoordenates($scope.coordsPseudoMerkator.x, $scope.coordsPseudoMerkator.y, $scope.floors, successMap, showError);
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

        	$scope.floors = ["00","00","00"];
            

        	document.getElementById('campus').addEventListener('click', function () {
        		map.setView([41.68306, -0.88707], 17);
        	});
        	
        	$scope.determineBuildingAndFloor = function(buildingAndFloor) {
        		
        		var building = buildingAndFloor.substring(0,1);
        		var floor = buildingAndFloor.substring(1,3);
        		
        		if (building=='A'){
            		$scope.floors[0]=floor;
            		map.setView([41.68363, -0.88891], 19);
            		
            	}else if (building=='T'){
            		$scope.floors[1]=floor;
            		map.setView([41.68363, -0.88736], 19);
            	}else{
            		$scope.floors[2]=floor;
            		map.setView([41.68347, -0.88394], 19);
            	}
        	};
        	
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
        		layers: 'Smart_CampUZ:A00',
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


        	var overlaysAda = {
        		    "Ada Byron P00": PoligonosAda00,
        		    "Ada Byron P04": PoligonosAda04
        	};
        	L.control.layers(overlaysAda).addTo(map);
        });
    }]);
