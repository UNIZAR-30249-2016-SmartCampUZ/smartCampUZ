angular.module('smartCampUZApp')

    // 'auth' service manage the authentication function of the page with the server
    .factory('auth', function ($state, $http, $base64) {

        var _identity = undefined,
            _authenticated = false;

        return {
            //return true if the user is authenticated
            isAuthenticated: function () {
                if (_authenticated) {
                    return _authenticated;
                } else {
                    var tmp = angular.fromJson(localStorage.userIdentity);
                    if (tmp !== undefined) {
                        this.authenticate(tmp);
                        return _authenticated;
                    } else {
                        return false;
                    }
                }
            },

            //authenticate the [identity] user
            authenticate: function (identity, jwt) {
                _identity = identity;
                _authenticated = identity !== undefined;
                localStorage.smartJWT = angular.toJson(jwt);
                localStorage.userIdentity = angular.toJson(_identity);
            },

            //logout function
            logout: function () {
                _identity = undefined;
                _authenticated = false;
                localStorage.removeItem('smartJWT');
                localStorage.removeItem('userIdentity');
                $state.go('starter');
            },

            getUserObject: function () {
                return _identity;
            },
            getUserName: function () {
              return _identity.userName;
            },
            getEmail: function () {
                return _identity.email;
            },
            getType: function () {
              return _identity.type;
            },

            //send the login info to the server
            login: function (email, password, callbackSuccess, callbackError) {
                var that = this;
                $http({
                    method: 'POST',
                    url: 'signIn',
                    headers: {
                        'Authorization': 'Basic ' +
                        $base64.encode(email + ":" + password)
                    }
                }).success(function (data, status, headers) {
                    that.authenticate(data, headers().token);
                    callbackSuccess();
                    if (data.type == 'admin') {
                        $state.go('admin');
                    } else if (data.type == 'maintenance') {
                        $state.go('maintenance');
                    }
                }).error(function (data) {
                    callbackError(data);
                });
            }
        };
    })

    // 'reserve' service manage the reserve service of the page with the server
    .factory('reserve', function ($state, $http, userMap, auth) {

        return {
            // Get the current day
            getCurrentDate: function (callbackSuccess, callbackError) {
                $http({
                    method: 'GET',
                    url: 'currentDate'
                }).success(function (data) {
                    callbackSuccess(data);
                }).error(function (data) {
                    var date = {month: 01, day: 23};
                    callbackSuccess(date);
                    //callbackError(data);
                });
            },
            // Get available hours of a [date]
            getAvailableHours: function (month, day, callbackSuccess, callbackError) {
                $http({
                    method: 'GET',
                    url: 'availableHours',
                    headers: {
                        month: month,
                        day: day,
                        location: userMap.getCurrentLocation().id
                    }
                }).success(function (data) {
                    callbackSuccess(data);
                }).error(function (data) {
                    var date = [0, 0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2];
                    callbackSuccess(date);
                    //callbackError(data);
                });
            },
            // Reserve [reserveHours] hours with specific [reserveInfo] information
            reserveHours: function (reserveInfo, reserveHours, callbackSuccess, callbackError) {
                var aux = {
                    info: reserveInfo,
                    hours: reserveHours,
                    location: userMap.getCurrentLocation().id,
                    logged: auth.isAuthenticated() ? true : false
                };
                var token = angular.fromJson(localStorage.smartJWT) !== undefined ? angular.fromJson(localStorage.smartJWT) : "";
                $http({
                    method: 'POST',
                    url: 'reserveHours',
                    headers: {'Authorization': 'Bearer ' + token},
                    data: JSON.stringify(aux)
                }).success(function (data) {
                    callbackSuccess(data);
                }).error(function (data) {
                    callbackError(data);
                });
            }
        };
    })

    // 'feedback' service manage the feedback service of the page with the server
    .factory('feedback', function ($state, $http, userMap) {

        return {
            // Report a description of a feedback
            reportFeedback: function (description, callbackSuccess, callbackError) {
                var aux = {
                    description: description,
                    location: userMap.getCurrentLocation().id
                };
                $http({
                    method: 'POST',
                    url: 'reportFeedback',
                    data: JSON.stringify(aux)
                }).success(function (data) {
                    callbackSuccess(data);
                }).error(function (data) {
                    callbackError(data);
                });
            },

            // List all feedback from the server
            getFeedback: function (callbackSuccess, callbackError) {
                var token = angular.fromJson(localStorage.smartJWT) !== undefined ? angular.fromJson(localStorage.smartJWT) : "";
                $http({
                    method: 'GET',
                    url: 'listFeedback',
                    headers: {
                        'Authorization': 'Bearer ' + token,
                        location: userMap.getCurrentLocation().id
                    }
                }).success(function (data) {
                    callbackSuccess(data.feedbacks);
                }).error(function (data) {
                    callbackError(data);
                });
            },

            // State management of a report
            setState: function(state) {
                var token = angular.fromJson(localStorage.smartJWT) !== undefined ? angular.fromJson(localStorage.smartJWT) : "";
                $http({
                    method: 'PUT',
                    url: 'state',
                    headers: {'Authorization': 'Bearer ' + token},
                    data: JSON.stringify(state)
                }).success(function (data) {

                }).error(function (data) {

                });
            }
        };
    })

    // 'userMap' service manage the user view of the map with the server
    .factory('userMap', function ($state, $http) {

    	
        var currentLocation = {
            id:0,
            name: ""
        };

        return {
            // Get the current location
            getCurrentLocation: function () {
                return currentLocation;
            },
            
            // Get the room from the given coordenates
            setLocationFromCoordenates: function (x, y, buildingFloors, callbackSuccess, callbackError) {
            	
            	$http({
                    method: 'GET',
                    url: 'locationFromCoords',
				    headers: {
				        x: x,
				    	y: y,
				    	buildingFloors: buildingFloors
				    }
                }).success(function (data) {
                	callbackSuccess(data);
                }).error(function (data) {
                	//callbackError(data);
                    var aux = {id: 123123, name: "L0.01"};
                    callbackSuccess(aux);
                });
            },
            
            
            
            // Set the current location
            setCurrentLocation: function (location) {        
            	currentLocation = location;
            }
        };
    });
