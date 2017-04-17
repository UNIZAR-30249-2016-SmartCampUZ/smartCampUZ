angular.module('smartCampUZApp')

    .controller('adminCtrl', ['$scope', 'report', 'workers','userMap', 'Notification', 'reserve',
        function ($scope, report, workers, userMap, Notification, reserve) {
        /* FEEDBACK MESSAGES */
        // show the error message
        var showError = function (message) {
            Notification.error('&#10008' + message);
        };

        // show the success message
        var showSuccess = function (message) {
            Notification.success('&#10004' + message);
        };

        // show the success message
        var showWarning = function (message) {
            Notification.warning({title: '¡Atención!', delay: null, message: message + ' Haz' +
            ' click aquí para eliminar la notificación.'});
        };

        // LOGIC VIEW OF REPORTS

        $scope.workerList = [];
        $scope.reportList = [];

        workers.getListOfWorkers(function () {
            $scope.workerList = workers.getWorkersName();
            report.getReports(function(list) {
                $scope.reportList = list;
            }, showError);
        }, showError);

        $scope.stateList = ["Pendiente", "Aprobado", "Asignado", "Hecho", "Confirmado", "Problema",
            "Denegado", "Notificado"];
        $scope.resetFilters = function () {
            $scope.titleFilter = "";
            $scope.workerFilter = "";
            $scope.stateFilter = "";
        };

        // LOGIC VIEW OF RESERVATIONS
        $scope.reservationList = [];
        var sort_by;

        reserve.getReservations(function (list) {
            $scope.reservationList = list;
            $scope.reservationList.sort(sort_by('month', {
                name: 'day',
                primer: parseInt,
                reverse: false
            }));
        }, showError);

        $scope.approve = function(currentId) {
            reserve.approveDenyReservation(currentId, true, function (idList) {
                var index = $scope.reservationList.map(function(tmp) {return tmp.id;}).indexOf(currentId);
                $scope.reservationList.splice(index, 1);
                showSuccess('Reserva aprobada correctamente.');
                if (idList.length > 0) {
                    var message = 'Al aprobar esa reserva, se han cancelado por incompatibilidad' +
                        ' las siguientes:';
                    for (i=0;i<idList.length-1;i++) {
                        index = $scope.reservationList.map(function(tmp) {return tmp.id;}).indexOf(idList[i]);
                        $scope.reservationList.splice(index, 1);
                        message += (' ' + idList[i] + ',');
                    }
                    index = $scope.reservationList.map(function(tmp) {return tmp.id;}).indexOf(idList[idList.length-1]);
                    $scope.reservationList.splice(index, 1);
                    message += (' y ' + idList[i] + '.');
                    showWarning(message);
                }
            }, showError)
        };

        $scope.deny = function(currentId) {
            reserve.approveDenyReservation(currentId, false, function () {
                var index = $scope.reservationList.map(function(tmp) {return tmp.id;}).indexOf(currentId);
                $scope.reservationList.splice(index, 1);
                showSuccess('Reserva denegada correctamente.');
            }, showError)
        };

        // Watches to control if the user have selected a location
        $scope.$watch(function() {
            return userMap.getCurrentLocation();
        }, function () {
            report.getReports(function(list) {
                $scope.reportList = list;
            }, showError);
            reserve.getReservations(function (list) {
                $scope.reservationList = list;
                $scope.reservationList.sort(sort_by('month', {
                    name: 'day',
                    primer: parseInt,
                    reverse: false
                }));
            }, showError);
        });

        // FUNCTION TO ORDER RESERVATION LIST BY MONTH AND DAY
        (function() {
            // utility functions
            var default_cmp = function(a, b) {
                    if (a == b) return 0;
                    return a < b ? -1 : 1;
                },
                getCmpFunc = function(primer, reverse) {
                    var dfc = default_cmp, // closer in scope
                        cmp = default_cmp;
                    if (primer) {
                        cmp = function(a, b) {
                            return dfc(primer(a), primer(b));
                        };
                    }
                    if (reverse) {
                        return function(a, b) {
                            return -1 * cmp(a, b);
                        };
                    }
                    return cmp;
                };

            // actual implementation
            sort_by = function() {
                var fields = [],
                    n_fields = arguments.length,
                    field, name, reverse, cmp;

                // preprocess sorting options
                for (var i = 0; i < n_fields; i++) {
                    field = arguments[i];
                    if (typeof field === 'string') {
                        name = field;
                        cmp = default_cmp;
                    }
                    else {
                        name = field.name;
                        cmp = getCmpFunc(field.primer, field.reverse);
                    }
                    fields.push({
                        name: name,
                        cmp: cmp
                    });
                }

                // final comparison function
                return function(A, B) {
                    var a, b, name, result;
                    for (var i = 0; i < n_fields; i++) {
                        result = 0;
                        field = fields[i];
                        name = field.name;

                        result = field.cmp(A[name], B[name]);
                        if (result !== 0) break;
                    }
                    return result;
                }
            }
        }());
    }]);
