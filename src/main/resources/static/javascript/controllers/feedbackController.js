angular.module('smartCampUZApp')

    .controller('feedbackCtrl', ['$scope', 'feedback', function ($scope, feedback) {
        $scope.workerList = ["Paco", "María"];
        $scope.selectedWorker = $scope.feedback.worker;
        $scope.currentState = $scope.feedback.state;
        $scope.changeState = function (state) {
            $scope.currentState = state;
            if ($scope.currentState == 'Aprobado' || $scope.currentState == 'Denegado'
                || $scope.currentState == 'Notificado') {
                $scope.selectedWorker = "";
            }
            if ($scope.currentState != 'Asignado') {
                var tmpState = {
                    id: $scope.feedback.id,
                    state: $scope.currentState
                };
                feedback.setState(tmpState);
            }
        };
        $scope.assignWorker = function () {
            if ($scope.currentState == 'Asignado') {
                var tmpState = {
                    id: $scope.feedback.id,
                    worker: $scope.selectedWorker
                };
                feedback.assignWorker(tmpState);
            }
        }
    }]);
