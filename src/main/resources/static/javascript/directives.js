angular.module('smartCampUZApp')

    // include the 'navbar.html' into the <navbar> tag
    .directive('navbar', function () {
        return {
            restrict: 'E',
            templateUrl: 'templates/components/navbar.html',
            controller: 'navbarCtrl',
            scope: {}
        }
    })

    // include the 'loginForm.html' into the <login-form> tag
    .directive('loginForm', function () {
        return {
            restrict: 'E',
            templateUrl: 'templates/components/loginForm.html',
            controller: 'loginFormCtrl',
            scope: {}
        }
    });
