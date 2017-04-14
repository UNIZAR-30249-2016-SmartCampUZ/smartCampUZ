angular.module('smartCampUZApp')

    .controller('feedbackCtrl', ['$scope', 'feedback', 'workers', 'Notification',
        function ($scope, feedback, workers, Notification) {

        // show the error message
        var showError = function (message) {
            Notification.error('&#10008' + message);
        };

        // show the success message
        var showSuccess = function (message) {
            Notification.success('&#10004' + message);
        };

        $scope.workerList = workers.getWorkersName();
        $scope.selectedWorker = $scope.feedback.worker;
        $scope.currentState = $scope.feedback.state;
        $scope.disableWorkers = true;

        $scope.changeState = function (state) {
            $scope.disableWorkers = state != 'Asignado';
            if (state != 'Asignado') {
                var tmpState = {
                    id: $scope.feedback.id,
                    state: state
                };
                feedback.setState(tmpState,function (message, state) {
                    if (state == 'Aprobado' || state == 'Denegado' || state == 'Notificado') {
                        $scope.selectedWorker = "";
                    }
                    $scope.currentState = state;
                    showSuccess(message);
                },showError);
            }
        };
        $scope.assignWorker = function () {
            if (!$scope.disableWorkers) {
                var tmpState = {
                    id: $scope.feedback.id,
                    worker: workers.getWorkerId($scope.selectedWorker)
                };
                workers.assignWorker(tmpState,function (message) {
                    $scope.currentState = 'Asignado';
                    showSuccess(message);
                },showError);
            }
        }
    }]);
