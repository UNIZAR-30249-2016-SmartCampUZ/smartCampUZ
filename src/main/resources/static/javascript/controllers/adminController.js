angular.module('smartCampUZApp')

    .controller('adminCtrl', ['$scope', 'report', 'workers','userMap', 'Notification',
        function ($scope, report, workers, userMap, Notification) {
        /* FEEDBACK MESSAGES */
        // show the error message
        var showError = function (message) {
            Notification.error('&#10008' + message);
        };

        // show the success message
        var showSuccess = function (message) {
            Notification.success('&#10004' + message);
        };

        // LOGIC VIEW

        $scope.workerList = [];
        $scope.reportList = [];

        workers.getListOfWorkers(function () {
            $scope.workerList = workers.getWorkersName();
            report.getReports(function(list) {
                $scope.reportList = list;
            }, showError);
        }, showError);

        // Watches to control if the user have selected a location
        $scope.$watch(function() {
            return userMap.getCurrentLocation();
        }, function () {
            report.getReports(function(list) {
                $scope.reportList = list;
            }, showError);
        });

        $scope.stateList = ["Aprobado", "Asignado", "Hecho", "Confirmado", "Denegado", "Notificado"];
        $scope.resetFilters = function () {
            $scope.titleFilter = "";
            $scope.workerFilter = "";
            $scope.stateFilter = "";
        };
    }]);
