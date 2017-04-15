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

    // include the 'report.html' into the <report> tag
    .directive('report', function () {
        return {
            restrict: 'E',
            templateUrl: 'templates/components/report.html',
            controller: 'reportCtrl',
            scope: true
        }
    });
