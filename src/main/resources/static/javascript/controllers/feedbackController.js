angular.module('smartCampUZApp')

    .controller('feedbackCtrl', ['$scope', 'auth', function ($scope, auth) {
        $scope.workerList = ["Paco", "María"];
        $scope.selectedWorker = "";
    }]);
