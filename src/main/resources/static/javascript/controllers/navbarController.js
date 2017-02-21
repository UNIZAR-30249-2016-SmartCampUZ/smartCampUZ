angular.module('smartCampUZApp')

    .controller('navbarCtrl', ['$scope', 'auth', function ($scope, auth) {

        $scope.logged = function () {
            return auth.isAuthenticated();
        };

        $scope.logout = function () {
            auth.logout();
        }
    }]);
