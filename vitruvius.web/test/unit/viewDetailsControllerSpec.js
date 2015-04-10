'use strict';
debugger;

describe("Controller: ViewDetailsController", function () {
    var controllerFactory, scope, routeParams , mockService = {};

    beforeEach(module('vitruvius.controllers'));

    beforeEach(inject(function ($controller, $rootScope) {
        scope = $rootScope.$new();
        controllerFactory = $controller;
        routeParams = {};
    }));
    function createController(link) {
        routeParams.link = link;
        return controllerFactory('ViewDetailsController', {
            $scope: scope,
            $routeParams: routeParams,
            VitruviusWebApplicationService: mockService
        });
    }

    describe("Successful service detail", function () {
        beforeEach(inject(function ($q) {
            mockService.getServiceDetail = jasmine.createSpy('getServiceDetail');
            var deferred = $q.defer();
            deferred.resolve("service detail");
            mockService.getServiceDetail.andReturn(deferred.promise);
        }));

        it("should return details of requested service", function () {
            var link = "linktoservice";
            createController(link);
            scope.$digest();
            expect(mockService.getServiceDetail).toHaveBeenCalledWith(link);
            expect(scope.html).toBeDefined();
            expect(scope.html).toEqual("service detail");
            expect(scope.messages.length).toEqual(0);

        });
    })

});