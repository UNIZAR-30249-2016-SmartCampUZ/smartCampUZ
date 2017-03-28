angular.module('smartCampUZApp')

    .controller('adminCtrl', ['$scope', 'feedback', 'workers', function ($scope, feedback, workers) {
        /* FEEDBACK MESSAGES */
        // feedback handling variables
        $scope.error = false;
        $scope.success = false;
        $scope.successMsg = "";
        $scope.errorMsg = "";

        // hide the error message
        $scope.hideError = function () {
            $scope.errorMsg = "";
            $scope.error = false;
        };
        // show the error message
        var showError = function (error) {
            $scope.errorMsg = error;
            $scope.error = true;
        };

        // show the success message
        var showSuccess = function (message) {
            $scope.successMsg = message;
            $scope.success = true;
        };

        // hide the success message
        $scope.hideSuccess = function () {
            $scope.success = false;
            $scope.successMsg = "";
        };

        // LOGIC VIEW

        $scope.workerList = [];
        $scope.feedbackList = [];

        workers.getListOfWorkers(function () {
            $scope.workerList = workers.getWorkersName();
            feedback.getFeedback(function(list) {
                $scope.feedbackList = list;
            }, showError);
        });

        $scope.stateList = ["Aprobado", "Asignado", "Hecho", "Confirmado", "Denegado", "Notificado"];
        $scope.resetFilters = function () {
            $scope.titleFilter = "";
            $scope.workerFilter = "";
            $scope.stateFilter = "";
        };
    }]);
