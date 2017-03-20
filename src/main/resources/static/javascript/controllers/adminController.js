angular.module('smartCampUZApp')

    .controller('adminCtrl', ['$scope', 'feedback', function ($scope, feedback) {
        $scope.feedbackList = [];
        feedback.getFeedback(function(list) {
            $scope.feedbackList = list;
        });

    }]);
