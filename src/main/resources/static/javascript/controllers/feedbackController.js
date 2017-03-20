angular.module('smartCampUZApp')

    .controller('feedbackCtrl', ['$scope', 'auth', function ($scope, auth) {
        $scope.workerList = ["Paco", "María"];
        $scope.selectedWorker = $scope.feedback.worker;
        $scope.currentState = $scope.feedback.state;
        $scope.changeState = function (state) {
            $scope.currentState = state;
        }
    }]);
