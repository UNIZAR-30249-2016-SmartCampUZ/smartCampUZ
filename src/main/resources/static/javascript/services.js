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
            },

            // Get pending reservations of the system
            getReservations: function (callbackSuccess, callbackError) {
                var token = angular.fromJson(localStorage.smartJWT) !== undefined ? angular.fromJson(localStorage.smartJWT) : "";
                $http({
                    method: 'GET',
                    url: 'listReservations',
                    headers: {
                        'Authorization': 'Bearer ' + token,
                        'Content-Type': 'application/json; charset=UTF-8',
                        'location': userMap.getCurrentLocation().id
                    }
                }).success(function (data) {
                    callbackSuccess(data.reservations);
                }).error(function (data) {
                    /*var temp = [
                        {id: 3, location: "3", day: 17, month: 03, professor: false, email: "3@3", description: "33"},
                        {id: 1, location: "1", day: 16, month: 03, professor: false, email: "1@1", description: "11"},
                        {id: 4, location: "4", day: 16, month: 04, professor: false, email: "4@4", description: "44"},
                        {id: 2, location: "2", day: 16, month: 03, professor: true, email: "2@2", description: "22"}
                    ];
                    callbackSuccess(temp);*/
                    callbackError(data);
                });
            },

            // Approve or deny a reservation
            approveDenyReservation: function (id, approve, callbackSuccess, callbackError) {
                var token = angular.fromJson(localStorage.smartJWT) !== undefined ? angular.fromJson(localStorage.smartJWT) : "";
                var temp = {
                    id: id,
                    approved: approve
                };
                $http({
                    method: 'PUT',
                    url: 'reservation',
                    headers: {
                        'Authorization': 'Bearer ' + token,
                        'Content-Type': 'application/json; charset=UTF-8'
                    },
                    data: JSON.stringify(temp)
                }).success(function (data) {
                    callbackSuccess(data.deletedRequests);
                }).error(function (data) {
                    /*var temp = [2,3];
                    callbackSuccess(temp);*/
                    callbackError(data);
                });
            }
        };
    })

    // 'report' service manage the report service of the page with the server
    .factory('report', function ($state, $http, userMap) {

        return {
            // Make a description of a report
            makeReport: function (description, callbackSuccess, callbackError) {
                var aux = {
                    description: description,
                    location: userMap.getCurrentLocation().id
                };
                $http({
                    method: 'POST',
                    url: 'report',
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

            // List all report from the server
            getReports: function (callbackSuccess, callbackError) {
                var token = angular.fromJson(localStorage.smartJWT) !== undefined ? angular.fromJson(localStorage.smartJWT) : "";
                $http({
                    method: 'GET',
                    url: 'listReports',
                    headers: {
                        'Authorization': 'Bearer ' + token,
                        location: userMap.getCurrentLocation().id,
                        'Content-Type': 'application/json; charset=UTF-8'
                    }
                }).success(function (data) {
                    var reports = data.reports;
                    for (i=0;i<reports.length;i++) {
                        if(reports[i].state == 'INBOX') {reports[i].state = 'Pendiente' }
                        else if (reports[i].state == 'NOTIFIED') {reports[i].state = 'Notificado' }
                        else if (reports[i].state == 'REFUSED') {reports[i].state = 'Denegado' }
                        else if (reports[i].state == 'APPROVED') {reports[i].state = 'Aprobado' }
                        else if (reports[i].state == 'ASSIGNED') {reports[i].state = 'Asignado' }
                        else if (reports[i].state == 'DONE') {reports[i].state = 'Hecho' }
                        else if (reports[i].state == 'CONFIRMED') {reports[i].state = 'Confirmado' }
                        else if (reports[i].state == 'TROUBLE') {reports[i].state = 'Problema' }
                    }
                    callbackSuccess(reports);
                }).error(function (data) {
                    callbackError(data);
                });
            },

            // State management of a report
            setState: function (state, callbackSuccess, callbackError) {
                var stateToChange = state.state;
                if(state.state == 'Pendiente') {state.state = 'INBOX'}
                else if (state.state == 'Notificado') {state.state = 'NOTIFIED'}
                else if (state.state == 'Denegado') {state.state = 'REFUSED'}
                else if (state.state == 'Aprobado') {state.state = 'APPROVED'}
                else if (state.state == 'Asignado') {state.state = 'ASSIGNED'}
                else if (state.state == 'Hecho') {state.state = 'DONE'}
                else if (state.state == 'Confirmado') {state.state = 'CONFIRMED'}
                else if (state.state == 'Problema') {state.state = 'TROUBLE'}

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

    // 'maintenance' service manage the maintenance functionallities of the app with the server
    .factory('maintenance', function ($http, auth, userMap) {

        return {
            // Get the list of the reports assigned to a maintenance guy
            getReports: function (callbackSuccess, callbackError) {
                var token = angular.fromJson(localStorage.smartJWT) !== undefined ? angular.fromJson(localStorage.smartJWT) : "";
                $http({
                    method: 'GET',
                    url: 'listWorkerReports',
                    headers: {
                        'Authorization': 'Bearer ' + token,
                        'Content-Type': 'application/json; charset=UTF-8',
                        'location': userMap.getCurrentLocation().id,
                        'emailWorker': auth.getEmail()
                    }
                }).success(function (data) {
                    callbackSuccess(data.reports);
                }).error(function (data) {
                    /*var temp = [
                        {id: 3, location: "3", title: "3", description: "33"},
                        {id: 1, location: "1", title: "1", description: "11"},
                        {id: 4, location: "4", title: "4", description: "44"},
                        {id: 2, location: "2", title: "2", description: "22"}
                    ];
                    callbackSuccess(temp);*/
                    callbackError(data);
                });
            },

            // Mark a report as done or trouble
            doneTroubleReport: function (id, done, callbackSuccess, callbackError) {
                var token = angular.fromJson(localStorage.smartJWT) !== undefined ? angular.fromJson(localStorage.smartJWT) : "";
                var temp = {
                    idReport: id,
                    done: done
                };
                $http({
                    method: 'PUT',
                    url: 'markReport',
                    headers: {
                        'Authorization': 'Bearer ' + token,
                        'Content-Type': 'application/json; charset=UTF-8'
                    },
                    data: JSON.stringify(temp)
                }).success(function (data) {
                    callbackSuccess(data);
                }).error(function (data) {
                    //callbackSuccess(data);
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
