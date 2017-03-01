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
            authenticate: function (identity) {
                _identity = identity;
                _authenticated = identity !== undefined;
                localStorage.userIdentity = angular.toJson(_identity);
            },

            //logout function
            logout: function () {
                _identity = undefined;
                _authenticated = false;
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
            login: function (user, password, callbackSuccess, callbackError) {
                var that = this;
                $http({
                    method: 'POST',
                    url: 'signIn',
                    headers: {
                        'Authorization': 'Basic ' +
                        $base64.encode(user + ":" + password)
                    }
                }).success(function (data) {
                    that.authenticate(data);
                    callbackSuccess();
                    if (data.type == 'admin') {
                        $state.go('admin');
                    } else if (data.type == 'maintenance') {
                        $state.go('maintenance');
                    }
                }).error(function (data) {
                    var aux = {
                        userName: "paco",
                        email: "paco@paco.paco",
                        type: "profesor"
                    };
                    that.authenticate(aux);
                    callbackSuccess();
                    if (aux.type == 'admin') {
                        $state.go('admin');
                    } else if (aux.type == 'maintenance') {
                        $state.go('maintenance');
                    }
                    //callbackError(data);
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
                        location: userMap.getCurrentLocation()
                    }
                }).success(function (data) {
                    callbackSuccess(data);
                }).error(function (data) {
                    var date = [0, 0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2];
                    callbackSuccess(date);
                    //callbackError(data);
                });
            },
            // Get available hours of a [date]
            reserveHours: function (reserveInfo, reserveHours, callbackSuccess, callbackError) {
                var aux = {
                    info: reserveInfo,
                    hours: reserveHours,
                    location: userMap.getCurrentLocation(),
                    profesor: auth.getType() == 'profesor' ? true : false
                };
                $http({
                    method: 'POST',
                    url: 'availableHours',
                    data: JSON.stringify(aux)
                }).success(function (data) {
                    callbackSuccess(data);
                }).error(function (data) {
                    console.log("Entro en error");
                    console.log(data);
                    callbackError(data);
                });
            }
        };
    })

    // 'userMap' service manage the user view of the map with the server
    .factory('userMap', function ($state, $http) {

        var currentLocation = "provisional";

        return {
            // Get the current day
            getCurrentLocation: function () {
                return currentLocation;
            }
        };
    });
