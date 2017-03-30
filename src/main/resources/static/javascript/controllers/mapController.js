angular.module('smartCampUZApp')

    .controller('mapCtrl', ['$scope', '$state', 'userMap', function ($scope, $state, userMap) {
    	
        $(document).ready(function(){

        	var map = L.map('mapid', {
        	    minZoom: 17,
        	    maxBoundsViscosity: 1.0
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

        	var southWest = L.latLng(41.68089, -0.89085),
        	northEast = L.latLng(41.68607, -0.88141);
        	var bounds = L.latLngBounds(southWest, northEast);

        	map.setMaxBounds(bounds);
        	map.on('drag', function() {
        	    map.panInsideBounds(bounds, { animate: false });
        	});

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
        	
        	
        	// Removes the respective layers
        	var removeLayers = function (building, floor) {
        		var Ada = ['00','01','02','03','04','S1','PT'];
        		var Torres = ['00','01','02','03','S1'];
        		var Betan = ['00','01','02','03','S1'];
        		
        		switch (building) {
        		case 'A': 
        			var n = Ada.indexOf(floor);
        			for (i = 0; i < Ada.length; i++) {
        			    if (i == n){}
        			    else  {
        			    	map.removeLayer(eval("A"+Ada[i]));
        			    }
        			}
        			break;
        		case 'T':
        			var n = Torres.indexOf(floor);
        			for (i = 0; i < Torres.length; i++) {
        			    if (i == n){}
        			    else  {
        			    	map.removeLayer(eval("T"+Torres[i]));
        			    }
        			}
        			break;
        		case 'B':
        			var n = Betan.indexOf(floor);
        			for (i = 0; i < Betan.length; i++) {
        			    if (i == n){}
        			    else  {
        			    	map.removeLayer(eval("B"+Betan[i]));
        			    }
        			}
        			break;
        		}
        		
        	};
        	
        	//Focuses on a building and changes floors
        	$scope.determineBuildingAndFloor = function(buildingAndFloor) {
        		
        		var building = buildingAndFloor.substring(0,1);
        		var floor = buildingAndFloor.substring(1,3);
        		
        		if (building=='A') {
            		$scope.floors[0]=floor;
            		map.setView([41.68363, -0.88891], 19);
            		
            		switch (floor) {
            	    case '00':
            	        removeLayers('A','00');
            	        A00.addTo(map);
            	        break;
            	    case '01':
            	        removeLayers('A','01');
            	        A01.addTo(map);
            	        break;
            	    case '02':
            	    	removeLayers('A','02');
            	        A02.addTo(map);
            	        break;
            	    case '03':
            	        removeLayers('A','03');
            	        A03.addTo(map);
            	        break;
            	    case '04':
            	        removeLayers('A','04');
            	        A04.addTo(map);
            	        break;
            	    case 'S1':
            	        removeLayers('A','S1');
            	        AS1.addTo(map);
            	        break;
            	    case 'PT':
            	        removeLayers('A','PT');
            	        APT.addTo(map);
            	        break;
            		}
            	} else if (building=='T') {
            		$scope.floors[1]=floor;

            		map.setView([41.68363, -0.88736], 19);
            		
            		switch (floor) {
            	    case '00':
            	        removeLayers('T','00');
            	        T00.addTo(map);
            	        break;
            	    case '01':
            	        removeLayers('T','01');
            	        T01.addTo(map);
            	        break;
            	    case '02':
            	    	removeLayers('T','02');
            	        T02.addTo(map);
            	        break;
            	    case '03':
            	        removeLayers('T','03');
            	        T03.addTo(map);
            	        break;
            	    case 'S1':
            	        removeLayers('T','S1');
            	        TS1.addTo(map);
            	        break;
            		}
            	} else {
            		$scope.floors[2]=floor;
            		map.setView([41.68347, -0.88394], 19);
            		
            		switch (floor) {
            	    case '00':
            	        removeLayers('B','00');
            	        B00.addTo(map);
            	        break;
            	    case '01':
            	        removeLayers('B','01');
            	        B01.addTo(map);
            	        break;
            	    case '02':
            	    	removeLayers('B','02');
            	        B02.addTo(map);
            	        break;
            	    case '03':
            	        removeLayers('B','03');
            	        B03.addTo(map);
            	        break;
            	    case 'S1':
            	        removeLayers('B','S1');
            	        BS1.addTo(map);
            	        break;
            		} 
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

        	var AS1 = L.tileLayer.wms("http://localhost:8080/geoserver/wms",{
        		request: 'GetMap',
        		maxZoom: 25,
        		layers: 'Smart_CampUZ:AS1',
        	    noWrap:true,
        	    continuousWorld: false,
        	    transparent: true,
        	    format: 'image/png',
        	    version: '1.1.0',
        	    attribution: "myattribution",
        		zIndex: 2
        	});
        	 	
        	var A00 = L.tileLayer.wms("http://localhost:8080/geoserver/wms",{
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

        	var A01 = L.tileLayer.wms("http://localhost:8080/geoserver/wms",{
        		request: 'GetMap',
        		maxZoom: 25,
        		layers: 'Smart_CampUZ:A01',
        	    noWrap:true,
        	    continuousWorld: false,
        	    transparent: true,
        	    format: 'image/png',
        	    version: '1.1.0',
        	    attribution: "myattribution",
        		zIndex: 2
        	});

        	var A02 = L.tileLayer.wms("http://localhost:8080/geoserver/wms",{
        		request: 'GetMap',
        		maxZoom: 25,
        		layers: 'Smart_CampUZ:A02',
        	    noWrap:true,
        	    continuousWorld: false,
        	    transparent: true,
        	    format: 'image/png',
        	    version: '1.1.0',
        	    attribution: "myattribution",
        		zIndex: 2
        	});

        	var A03 = L.tileLayer.wms("http://localhost:8080/geoserver/wms",{
        		request: 'GetMap',
        		maxZoom: 25,
        		layers: 'Smart_CampUZ:A03',
        	    noWrap:true,
        	    continuousWorld: false,
        	    transparent: true,
        	    format: 'image/png',
        	    version: '1.1.0',
        	    attribution: "myattribution",
        		zIndex: 2
        	});
        	
        	var A04 = L.tileLayer.wms("http://localhost:8080/geoserver/wms",{
        		request: 'GetMap',
        		maxZoom: 25,
        		layers: 'Smart_CampUZ:A04',
        	    noWrap:true,
        	    continuousWorld: false,
        	    transparent: true,
        	    format: 'image/png',
        	    version: '1.1.0',
        	    attribution: "myattribution",
        		zIndex: 2
        	});

        	var APT = L.tileLayer.wms("http://localhost:8080/geoserver/wms",{
        		request: 'GetMap',
        		maxZoom: 25,
        		layers: 'Smart_CampUZ:APT',
        	    noWrap:true,
        	    continuousWorld: false,
        	    transparent: true,
        	    format: 'image/png',
        	    version: '1.1.0',
        	    attribution: "myattribution",
        		zIndex: 2
        	});

        	var BS1 = L.tileLayer.wms("http://localhost:8080/geoserver/wms",{
        		request: 'GetMap',
        		maxZoom: 25,
        		layers: 'Smart_CampUZ:BS1',
        	    noWrap:true,
        	    continuousWorld: false,
        	    transparent: true,
        	    format: 'image/png',
        	    version: '1.1.0',
        	    attribution: "myattribution",
        		zIndex: 2
        	});
        	 	
        	var B00 = L.tileLayer.wms("http://localhost:8080/geoserver/wms",{
        		request: 'GetMap',
        		maxZoom: 25,
        		layers: 'Smart_CampUZ:B00',
        	    noWrap:true,
        	    continuousWorld: false,
        	    transparent: true,
        	    format: 'image/png',
        	    version: '1.1.0',
        	    attribution: "myattribution",
        		zIndex: 2
        	}).addTo(map);

        	var B01 = L.tileLayer.wms("http://localhost:8080/geoserver/wms",{
        		request: 'GetMap',
        		maxZoom: 25,
        		layers: 'Smart_CampUZ:B01',
        	    noWrap:true,
        	    continuousWorld: false,
        	    transparent: true,
        	    format: 'image/png',
        	    version: '1.1.0',
        	    attribution: "myattribution",
        		zIndex: 2
        	});

        	var B02 = L.tileLayer.wms("http://localhost:8080/geoserver/wms",{
        		request: 'GetMap',
        		maxZoom: 25,
        		layers: 'Smart_CampUZ:B02',
        	    noWrap:true,
        	    continuousWorld: false,
        	    transparent: true,
        	    format: 'image/png',
        	    version: '1.1.0',
        	    attribution: "myattribution",
        		zIndex: 2
        	});

        	var B03 = L.tileLayer.wms("http://localhost:8080/geoserver/wms",{
        		request: 'GetMap',
        		maxZoom: 25,
        		layers: 'Smart_CampUZ:B03',
        	    noWrap:true,
        	    continuousWorld: false,
        	    transparent: true,
        	    format: 'image/png',
        	    version: '1.1.0',
        	    attribution: "myattribution",
        		zIndex: 2
        	});
        	
        	var TS1 = L.tileLayer.wms("http://localhost:8080/geoserver/wms",{
        		request: 'GetMap',
        		maxZoom: 25,
        		layers: 'Smart_CampUZ:TS1',
        	    noWrap:true,
        	    continuousWorld: false,
        	    transparent: true,
        	    format: 'image/png',
        	    version: '1.1.0',
        	    attribution: "myattribution",
        		zIndex: 2
        	});
        	 	
        	var T00 = L.tileLayer.wms("http://localhost:8080/geoserver/wms",{
        		request: 'GetMap',
        		maxZoom: 25,
        		layers: 'Smart_CampUZ:T00',
        	    noWrap:true,
        	    continuousWorld: false,
        	    transparent: true,
        	    format: 'image/png',
        	    version: '1.1.0',
        	    attribution: "myattribution",
        		zIndex: 2
        	}).addTo(map);

        	var T01 = L.tileLayer.wms("http://localhost:8080/geoserver/wms",{
        		request: 'GetMap',
        		maxZoom: 25,
        		layers: 'Smart_CampUZ:T01',
        	    noWrap:true,
        	    continuousWorld: false,
        	    transparent: true,
        	    format: 'image/png',
        	    version: '1.1.0',
        	    attribution: "myattribution",
        		zIndex: 2
        	});

        	var T02 = L.tileLayer.wms("http://localhost:8080/geoserver/wms",{
        		request: 'GetMap',
        		maxZoom: 25,
        		layers: 'Smart_CampUZ:T02',
        	    noWrap:true,
        	    continuousWorld: false,
        	    transparent: true,
        	    format: 'image/png',
        	    version: '1.1.0',
        	    attribution: "myattribution",
        		zIndex: 2
        	});

        	var T03 = L.tileLayer.wms("http://localhost:8080/geoserver/wms",{
        		request: 'GetMap',
        		maxZoom: 25,
        		layers: 'Smart_CampUZ:T03',
        	    noWrap:true,
        	    continuousWorld: false,
        	    transparent: true,
        	    format: 'image/png',
        	    version: '1.1.0',
        	    attribution: "myattribution",
        		zIndex: 2
        	});

        });
    }]);
