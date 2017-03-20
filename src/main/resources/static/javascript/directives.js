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
    .directive('leaftletMap', function () {
        return {
            restrict: 'E',
            templateUrl: 'templates/components/map.html',
            controller: 'mapCtrl',
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
    })

    // include the 'feedback.html' into the <feedback> tag
    .directive('feedback', function () {
        return {
            restrict: 'E',
            templateUrl: 'templates/components/feedback.html',
            controller: 'feedbackCtrl',
            scope: {}
        }
    });
