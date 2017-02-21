angular.module('smartCampUZApp')

    .controller('loginFormCtrl', ['$scope', 'auth', function ($scope, auth) {

        // Get the modal
        var modal = document.getElementById('loginForm');

        $scope.closeLoginForm = function () {
            modal.style.display = "none";
        };

        // When the user clicks anywhere outside of the modal, close it
        window.onclick = function(event) {
            if (event.target == modal) {
                modal.style.display = "none";
            }
        }
    }]);
