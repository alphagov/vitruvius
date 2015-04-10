'use strict';

var vitruvius = angular.module('vitruvius', [
    'ngRoute',
    'ngSanitize',
    'underscore',
    'vitruvius.controllers',
    'vitruvius.services',
    'vitruvius.directives',
    'vitruvius.filters',
    'elasticsearch'
]);



vitruvius.config(['$routeProvider',
    function ($routeProvider) {
        $routeProvider.
            when("/", {
                templateUrl: '/views/partials/about.html',
                controller: 'AboutVitruviusController'
            }).
            when("/platforms", {
                templateUrl: '/views/partials/list.html',
                controller: 'PlatformListController'
            }).
            when("/services", {
                templateUrl: '/views/partials/list.html',
                controller: 'ServiceListController'
            }).
            when('/details', {
                templateUrl: '/views/partials/detail.html',
                controller: 'ViewDetailsController'
            }).
            when('/search', {
                templateUrl: '/views/partials/results.html',
                controller: 'SearchController'
            }).
            when('/validate', {
                templateUrl: '/views/partials/validator.html',
                controller: 'VitruviusContentValidator'
            }).
            when('/add', {
                templateUrl: '/views/partials/register.html',
                controller: 'AddController'
            }).
            when("/components", {
                templateUrl: '/views/partials/list.html',
                controller: 'ComponentListController'
            }).
            when("/registers", {
                templateUrl: '/views/partials/list.html',
                controller: 'RegisterListController'
            }).
            otherwise({
                redirectTo: '/'
            });
    }])
