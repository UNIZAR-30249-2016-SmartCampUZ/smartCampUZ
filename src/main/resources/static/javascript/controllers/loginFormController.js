angular.module('smartCampUZApp')

    .controller('loginFormCtrl', ['$scope', 'auth', function ($scope, auth) {

        // inputs visual variables
        $scope.userName = "";
        $scope.password = "";

        // feedback handling variables
        $scope.errorMsg = "";
        $scope.error = false;

        // Get the modal
        var modal = document.getElementById('loginForm');

        $scope.closeLoginForm = function () {
            modal.style.display = "none";
            $scope.userName = "";
            $scope.password = "";
            $scope.errorMsg = "";
            $scope.error = false;
        };

        // When the user clicks anywhere outside of the modal, close it
        window.onclick = function(event) {
            if (event.target == modal) {
                $scope.closeLoginForm();
            }
        };

        // hide the error login message when is true respectively
        $scope.hideError = function () {
            $scope.errorMsg = "";
            $scope.error = false;
        };

        // show the error login message when is false respectively
        var showError = function (error) {
            $scope.errorMsg = error;
            $scope.error = true;
        };

        // send the login form to the auth service
        $scope.login = function () {
            var user = $scope.userName;
            var password = $scope.password;
            // Standard 'authorization basic'
            auth.login($scope.userName, $scope.password, $scope.closeLoginForm, showError);
        }
    }]);
