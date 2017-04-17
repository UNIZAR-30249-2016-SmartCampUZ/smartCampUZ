angular.module('smartCampUZApp')

    .controller('reportCtrl', ['$scope', 'report', 'workers', 'Notification',
        function ($scope, report, workers, Notification) {

        // show the error message
        var showError = function (message) {
            Notification.error('&#10008' + message);
        };

        // show the success message
        var showSuccess = function (message) {
            Notification.success('&#10004' + message);
        };

        $scope.workerList = workers.getWorkersName();
        $scope.selectedWorker = $scope.report.worker;
        $scope.currentState = $scope.report.state;
        $scope.disableWorkers = true;

        $scope.changeState = function (state) {
            $scope.disableWorkers = state != 'Asignado';
            if (state != 'Asignado') {
                var tmpState = {
                    id: $scope.report.id,
                    state: state
                };
                report.setState(tmpState,function (message, state) {
                    if (state == 'Pendiente' || state == 'Aprobado' || state == 'Denegado' ||
                        state == 'Notificado') {
                        $scope.selectedWorker = "";
                    }
                    $scope.report.state = state;
                    $scope.currentState = state;
                    showSuccess(message);
                },showError);
            } else {
                $scope.currentState = 'Asignado';
            }
        };
        $scope.assignWorker = function () {
            if (!$scope.disableWorkers) {
                var tmpState = {
                    id: $scope.report.id,
                    worker: workers.getWorkerId($scope.selectedWorker)
                };
                workers.assignWorker(tmpState,function (message) {
                    $scope.report.worker = $scope.selectedWorker;
                    $scope.report.state = 'Asignado';
                    showSuccess(message);
                }, function (message) {
                    $scope.selectedWorker = "";
                    $scope.currentState = $scope.report.state;
                    showError(message);
                });
            }
        }
    }]);
