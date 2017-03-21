angular.module('smartCampUZApp', ['ui.router', 'base64', 'angular-jwt'])

    .config(function ($stateProvider, $urlRouterProvider) {
        $stateProvider

            //starter screen
            .state('starter', {
                url: "/starter",
                templateUrl: "templates/starter.html",
                controller: "starterCtrl",
                onEnter: function ($state, auth) {
                    if (auth.isAuthenticated() && (auth.getType() == 'admin')) {
                        $state.go('admin');
                    } else if (auth.isAuthenticated() && (auth.getType() == 'maintenance')) {
                        $state.go('maintenance');
                    }
                }
            })

            //starter screen
            .state('admin', {
                url: "/admin",
                templateUrl: "templates/admin.html",
                controller: "adminCtrl",
                onEnter: function ($state, auth) {
                    if (!auth.isAuthenticated() || auth.isAuthenticated() && (auth.getType() == 'professor')) {
                        $state.go('starter');
                    } else if (auth.isAuthenticated() && (auth.getType() == 'maintenance')) {
                        $state.go('maintenance');
                    }
                }
            });

        $urlRouterProvider.otherwise('starter');
    });
