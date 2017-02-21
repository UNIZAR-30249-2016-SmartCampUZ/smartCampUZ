angular.module('smartCampUZApp')

    .controller('starterCtrl', ['$scope', '$state', 'auth', function ($scope, $state, auth) {
        $(document).ready(function(){
            var map = L.map('mapid').setView([41.672, -0.893], 20);

            /**
             * Marker azul
             */
            var marker = L.marker([41.685, -0.888]).addTo(map);

            /**
             * Popup binding
             */
            marker.bindPopup("<b>EINA</b><br>Escuela de Ingeniería y Arquitectura.").openPopup();

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
                maxZoom: 14,
                noWrap: true,
                attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, ' +
                '<a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
                'Imagery © <a href="http://mapbox.com">Mapbox</a>',
                id: 'mapbox.streets'
            }).addTo(map);
        });
    }]);
