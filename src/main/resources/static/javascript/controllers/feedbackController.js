angular.module('smartCampUZApp')

    .controller('feedbackCtrl', ['$scope', 'feedback', function ($scope, feedback) {
        $scope.workerList = ["Paco", "María"];
        $scope.selectedWorker = $scope.feedback.worker;
        $scope.currentState = $scope.feedback.state;
        $scope.changeState = function (state) {
            $scope.currentState = state;
            if ($scope.currentState != 'Asignado') {
                var tmpState = {
                    id: $scope.feedback.id,
                    state: $scope.currentState,
                    worker: $scope.selectedWorker
                };
                feedback.setState(tmpState);
            }
        };
        $scope.asignWorker = function () {
            var tmpState = {
                id: $scope.feedback.id,
                state: $scope.currentState,
                worker: $scope.selectedWorker
            };
            feedback.setState(tmpState);
        }
    }]);
