angular.module('smartCampUZApp')

    .controller('adminCtrl', ['$scope', 'feedback', function ($scope, feedback) {
        /* FEEDBACK MESSAGES */
        // feedback handling variables
        $scope.error = false;
        $scope.errorMsg = "";

        // hide the error mensage
        $scope.hideError = function () {
            $scope.errorMsg = "";
            $scope.error = false;
        };
        // show the error mensage
        var showError = function (error) {
            $scope.errorMsg = error;
            $scope.error = true;
        };

        // LOGIC VIEW

        $scope.feedbackList = [];
        feedback.getFeedback(function(list) {
            $scope.feedbackList = list;
        }, showError);
        $scope.workerList = ["Paco", "María"];
        $scope.stateList = ["Aprobado", "Asignado", "Hecho", "Confirmado", "Denegado", "Notificado"];
        $scope.resetFilters = function () {
            $scope.titleFilter = "";
            $scope.workerFilter = "";
            $scope.stateFilter = "";
        };
    }]);
