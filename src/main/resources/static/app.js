angular.module('smartCampUZApp', ['ui.router', 'base64', 'angular-jwt'])

    .config(function ($stateProvider, $urlRouterProvider) {
        $stateProvider

            //starter screen
            .state('starter', {
                url: "/starter",
                templateUrl: "templates/starter.html",
                controller: "starterCtrl",
                onEnter: function ($state, auth) {
                    if (auth.isAuthenticated()) {
                        $state.go('admin');
                    }
                }
            })

            //starter screen
            .state('admin', {
                url: "/admin",
                templateUrl: "templates/admin.html",
                controller: "adminCtrl",
                onEnter: function ($state, auth) {
                    if (!auth.isAuthenticated()) {
                        $state.go('starter');
                    }
                }
            });

        $urlRouterProvider.otherwise('starter');
    });
