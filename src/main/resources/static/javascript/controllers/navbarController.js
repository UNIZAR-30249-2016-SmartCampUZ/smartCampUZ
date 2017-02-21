angular.module('smartCampUZApp')

    .controller('navbarCtrl', ['$scope', 'auth', function ($scope, auth) {

        $scope.openLoginForm = function () {
            document.getElementById('loginForm').style.display = "block";
        };

        $scope.logged = function () {
            return auth.isAuthenticated();
        };

        $scope.logout = function () {
            auth.logout();
        }
    }]);
