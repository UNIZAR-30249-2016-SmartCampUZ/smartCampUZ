angular.module('smartCampUZApp')

    .controller('maintenanceCtrl', ['$scope', 'maintenance', 'userMap', 'Notification',
        function ($scope, maintenance, userMap, Notification) {
        $scope.reportsList = [];

        /* FEEDBACK MESSAGES */
        // show the error message
        var showError = function (message) {
            Notification.error('&#10008' + message);
        };

        // show the success message
        var showSuccess = function (message) {
            Notification.success('&#10004' + message);
        };

        maintenance.getReports(function (list) {
            $scope.reportsList = list;
        }, showError);

        // Watches to control if the user have selected a location
        $scope.$watch(function() {
            return userMap.getCurrentLocation();
        }, function () {
            maintenance.getReports(function (list) {
                $scope.reportsList = list;
            }, showError);
        });

    }]);
