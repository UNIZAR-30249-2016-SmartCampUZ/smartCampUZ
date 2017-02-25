angular.module('smartCampUZApp')

    .controller('mapCtrl', ['$scope', '$state', 'auth', function ($scope, $state, auth) {
        $(document).ready(function(){
        	var map = L.map('mapid').setView([41.68306, -0.88707], 17);

        	/**
        	 * Marker azul
        	 */
        	var marker = L.marker([41.6839, -0.88598]).addTo(map);    

            /**
             * Popup binding
             */
            marker.bindPopup("<b>EINA</b><br>Escuela de Ingeniería y Arquitectura.").openPopup();

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
                "opacity": 0.65
            };

            L.geoJSON(myLines, {
                style: myStyle
            }).addTo(map);
            
            
            /**
             * Watermark
             */
            L.Control.Watermark = L.Control.extend({
                onAdd: function(map) {
                    var img = L.DomUtil.create('img');
                    img.src = '../../img/logo.png';
                    img.style.width = '320px';
                    img.style.height = '45px';
                    return img;
                },
                onRemove: function(map) {
                }
            });
            L.control.watermark = function(opts) {
                return new L.Control.Watermark(opts);
            };
            L.control.watermark({ position: 'bottomleft' }).addTo(map);



            /**
             * Popup de coordenadas
             */
            var popup2 = L.popup();
            function onMapClick(e) {
                popup2
                    .setLatLng(e.latlng)
                    .setContent(e.latlng.toString())
                    .openOn(map);
            }
            map.on('click', onMapClick);



            /**
             * Background map
             */
            L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw', {
                maxZoom: 25,
                noWrap: true,
                attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, ' +
                '<a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
                'Imagery © <a href="http://mapbox.com">Mapbox</a>',
                id: 'mapbox.streets'
            }).addTo(map);
        });
    }]);
