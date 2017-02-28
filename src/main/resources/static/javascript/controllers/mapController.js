aangular.module('smartCampUZApp')

    .controller('mapCtrl', ['$scope', '$state', 'auth', function ($scope, $state, auth) {
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
                "opacity": 0.65
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
             * Watermark
             */
            L.Control.Watermark = L.Control.extend({
                onAdd: function(map) {
                    var img = L.DomUtil.create('img');
                    img.src = 'SmartCampUZ_transparent.png';
                    img.style.width = '320px';
                    img.style.height = '45px';
                    return img;
                }
            });
            L.control.watermark = function(opts) {
                return new L.Control.Watermark(opts);
            }
            L.control.watermark({ position: 'bottomleft' }).addTo(map);


            /**
             * BuildingLayers
             */
            var AdaByronMarker = L.marker([41.68366, -0.88848]).addTo(map),
                TorresMarker = L.marker([41.6833, -0.88737]).addTo(map),
                BetancourtMarker = L.marker([41.68338, -0.88431]).addTo(map);
            var buildings = L.layerGroup([AdaByronMarker,TorresMarker,BetancourtMarker]);

            AdaByronMarker.bindPopup("<b>Edificio Ada Byron</b>");
            TorresMarker.bindPopup("<b>Edificio Torres Quevedo</b>");
            BetancourtMarker.bindPopup("<b>Edificio Agustín de Betancourt</b>");

            var overlays = {
                "Campus": EINAmarker,
                "Edificios": buildings
            };
            L.control.layers(overlays).addTo(map);


            /**
             * Popup de coordenadas
             */
            var popup2 = L.popup();
            function onMapClick(e) {
                popup2
                    .setLatLng(e.latlng)
                    .setContent("<b>x=</b>" + e.latlng.lat.toString() + "<br>"+
                        "<b>y=</b>" +e.latlng.lng.toString()+ "</br>"+e.latlng.toString() )
                    .openOn(map);
            }
            map.on('click', onMapClick);


            //Stairs_UP icon
            var upstairsIcon = L.icon({
                iconUrl: 'stairs_up.png',
                zIndexOffset: 1000,
                title: 'Floor above',
                riseOnHover: true,
                alt: 'Floor above',
                iconSize:     [45, 45], // size of the icon
                iconAnchor:   [45, 45], // point of the icon which will
                //correspond to marker's location
            });
            var stairsUpLatLng = L.latLng(map.getCenter().lat,map.getCenter().lng);
            var upstairs = new L.marker(stairsUpLatLng,
                {icon: upstairsIcon, clickable:true});
            upstairs.addTo(map);


            //Stairs_DOWN icon
            var downstairsIcon = L.icon({
                iconUrl: 'stairs_down.png',
                zIndexOffset: 1000,
                title: 'Floor below',
                alt: 'Floor below',
                riseOnHover:true,
                iconSize:     [45, 45], // size of the icon
                iconAnchor:   [45, 45], // point of the icon which will
                //correspond to marker's location
            });
            var stairsDownLatLng = L.latLng(map.getCenter().lat,map.getCenter().lng);
            var downstairs = new L.marker(stairsDownLatLng,
                {icon: downstairsIcon, clickable:true});
            downstairs.addTo(map);
            /**
             * When map moves, move the icons
             */
            map.on('move', function(e) {
                stairsUpLatLng = L.latLng(map.getCenter().lat, map.getCenter().lng);
                stairsDownLatLng = L.latLng(map.getCenter().lat, map.getCenter().lng);
                upstairs.setLatLng(stairsUpLatLng);
                downstairs.setLatLng(stairsDownLatLng)
            });



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
