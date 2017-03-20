angular.module('smartCampUZApp', ['ui.router', 'base64', 'angular-jwt'])

    .config(function ($stateProvider, $urlRouterProvider) {
        $stateProvider

            //starter screen
            .state('starter', {
                url: "/starter",
                templateUrl: "templates/starter.html",
                controller: "starterCtrl",
                onEnter: function ($state, auth) {
                    if (auth.isAuthenticated() && (auth.getType() == 'manager')) {
                        $state.go('admin');
                    } else if (auth.isAuthenticated() && (auth.getType() == 'worker')) {
                        $state.go('worker');
                    }
                }
            })

            //starter screen
            .state('admin', {
                url: "/admin",
                templateUrl: "templates/admin.html",
                controller: "adminCtrl",
                onEnter: function ($state, auth) {
                    if (!auth.isAuthenticated() || auth.isAuthenticated() && (auth.getType() == 'teacher')) {
                        $state.go('starter');
                    } else if (auth.isAuthenticated() && (auth.getType() == 'worker')) {
                        $state.go('worker');
                    }
                }
            });

        $urlRouterProvider.otherwise('starter');
    });
