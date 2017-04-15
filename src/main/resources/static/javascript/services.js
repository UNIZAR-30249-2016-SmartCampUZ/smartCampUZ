angular.module('smartCampUZApp')

    // 'auth' service manage the authentication function of the page with the server
    .factory('auth', function ($state, $http, $base64, userMap) {

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
                userMap.resetCurrentLocation();
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
                        $base64.encode(email + ":" + password),
                        'Content-Type': 'application/json; charset=UTF-8'
                    }
                }).success(function (data, status, headers) {
                    that.authenticate(data, headers().token);
                    userMap.resetCurrentLocation();
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
            getCurrentDate: function () {
                var dateObject = new Date();
                var day = dateObject.getDate() - 1;
                var month = dateObject.getMonth();
                var date = {month: month, day: day};
                return date;
            },
            // Get available hours of a [date]
            getAvailableHours: function (month, day, callbackSuccess, callbackError) {
                $http({
                    method: 'GET',
                    url: 'availableHours',
                    headers: {
                        month: month,
                        day: day,
                        location: userMap.getCurrentLocation().id,
                        'Content-Type': 'application/json; charset=UTF-8'
                    }
                }).success(function (data) {
                    var hours = data.availableHours;
                    var date = [2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2];
                    for (i=0;i<hours.length;i++) {
                        date = hours[i] ? 2 : 0;
                    }
                    callbackSuccess(date);
                }).error(function (data) {
                    callbackError(data);
                });
            },
            // Reserve [reserveHours] hours with specific [reserveInfo] information
            reserveHours: function (reserveInfo, reserveHours, callbackSuccess, callbackError) {
                var hours = [false,false,false,false,false,false,false,false,false,false,false,false,
                    false,false, false,false,false,false,false,false,false,false,false,false];
                for (i=0;i<reserveHours.length;i++) {
                    hours = reserveHours[i] == 1;
                }
                var aux = {
                    email: $scope.emailReserve,
                    description: $scope.descriptionReserve,
                    day: $scope.currentDay,
                    month: $scope.currentMonth,
                    location: userMap.getCurrentLocation().id,
                    requestedHours: hours
                };
                var token = angular.fromJson(localStorage.smartJWT) !== undefined ? angular.fromJson(localStorage.smartJWT) : "";
                $http({
                    method: 'POST',
                    url: 'reservation',
                    headers: {
                        'Authorization': 'Bearer ' + token,
                        'Content-Type': 'application/json; charset=UTF-8'
                    },
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
                    headers: {
                        'Content-Type': 'application/json; charset=UTF-8'
                    },
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
                        location: userMap.getCurrentLocation().id,
                        'Content-Type': 'application/json; charset=UTF-8'
                    }
                }).success(function (data) {
                    var feedbacks = data.feedbacks;
                    for (i=0;i<feedbacks.length;i++) {
                        if(feedbacks[i].state == 'INBOX') {feedbacks[i].state = '' }
                        else if (feedbacks[i].state == 'NOTIFIED') {feedbacks[i].state = 'Notificado' }
                        else if (feedbacks[i].state == 'REFUSED') {feedbacks[i].state = 'Denegado' }
                        else if (feedbacks[i].state == 'APPROVED') {feedbacks[i].state = 'Aprobado' }
                        else if (feedbacks[i].state == 'ASSIGNED') {feedbacks[i].state = 'Asignado' }
                        else if (feedbacks[i].state == 'DONE') {feedbacks[i].state = 'Hecho' }
                        else if (feedbacks[i].state == 'CONFIRMED') {feedbacks[i].state = 'Confirmado' }
                    }
                    callbackSuccess(feedbacks);
                }).error(function (data) {
                    callbackError(data);
                });
            },

            // State management of a report
            setState: function (state, callbackSuccess, callbackError) {
                var stateToChange = state.state;
                if(state.state == '') {state.state = 'INBOX'}
                else if (state.state == 'Notificado') {state.state = 'NOTIFIED'}
                else if (state.state == 'Denegado') {state.state = 'REFUSED'}
                else if (state.state == 'Aprobado') {state.state = 'APPROVED'}
                else if (state.state == 'Asignado') {state.state = 'ASSIGNED'}
                else if (state.state == 'Hecho') {state.state = 'DONE'}
                else if (state.state == 'Confirmado') {state.state = 'CONFIRMED'}

                var token = angular.fromJson(localStorage.smartJWT) !== undefined ? angular.fromJson(localStorage.smartJWT) : "";
                $http({
                    method: 'PUT',
                    url: 'state',
                    headers: {
                        'Authorization': 'Bearer ' + token,
                        'Content-Type': 'application/json; charset=UTF-8'
                    },
                    data: JSON.stringify(state)
                }).success(function (data) {
                    callbackSuccess(data, stateToChange);
                }).error(function (data) {
                    callbackError(data);
                });
            }
        };
    })

    // 'workers' service manage the workers functionallities of the app with the server
    .factory('workers', function ($http) {

        var workers = [];

        return {
            // Get the list of the workers
            getListOfWorkers: function (callbackSuccess, callbackError) {
                var token = angular.fromJson(localStorage.smartJWT) !== undefined ? angular.fromJson(localStorage.smartJWT) : "";
                $http({
                    method: 'GET',
                    url: 'listWorkers',
                    headers: {
                        'Authorization': 'Bearer ' + token,
                        'Content-Type': 'application/json; charset=UTF-8'
                    }
                }).success(function (data) {
                    workers = data.workers;
                    callbackSuccess();
                }).error(function (data) {
                    callbackError(data);
                });
            },

            // Get an array containing all names of the workers
            getWorkersName: function () {
                var names = [];
                var i = workers.length;
                for (i=0;i<workers.length;i++) {
                    names.push(workers[i].name);
                }
                return names;
            },

            //
            getWorkerId: function (name) {
                var found = false;
                var id = 0;
                for (i=0;i<workers.length || !found;i++) {
                    if (workers[i].name == name) {
                        id = workers[i].id;
                        found = true;
                    }
                }
                return id;
            },

            // Assign worker to a report
            assignWorker: function (worker, callbackSuccess, callbackError) {
                var token = angular.fromJson(localStorage.smartJWT) !== undefined ? angular.fromJson(localStorage.smartJWT) : "";
                $http({
                    method: 'PUT',
                    url: 'assignWorker',
                    headers: {
                        'Authorization': 'Bearer ' + token,
                        'Content-Type': 'application/json; charset=UTF-8'
                    },
                    data: JSON.stringify(worker)
                }).success(function (data) {
                    callbackSuccess(data);
                }).error(function (data) {
                    callbackError(data);
                });
            }
        };
    })

    // 'userMap' service manage the user view of the map with the server
    .factory('userMap', function ($http) {

    	
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
                    url: 'locationFromCoords?x=' + x + '&y=' + y + '&buildingFloors=' + buildingFloors,
                    headers: {
                        'Content-Type': 'application/json; charset=UTF-8'
                    }
                }).success(function (data) {
                	callbackSuccess(data);
                }).error(function (data) {
                	callbackError(data);
                });
            },
            
            // Set the current location
            setCurrentLocation: function (location) {        
            	currentLocation = location;
            },

            // Reset the current location
            resetCurrentLocation: function () {
                var aux = {
                    id:0,
                    name: ""
                };
                currentLocation = aux;
            }
        };
    });
