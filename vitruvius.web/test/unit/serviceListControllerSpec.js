'use strict';

describe("Controller: ServiceListController", function () {
    var controllerFactory, scope, location, mockService = {};

    function createController() {
        return controllerFactory('ServiceListController', {
            $scope: scope,
            $location: location,
            VitruviusWebApplicationService: mockService
        });
    }


    beforeEach(module('vitruvius.controllers'));

    beforeEach(inject(function ($controller, $rootScope, $location) {
        scope = $rootScope.$new();
        controllerFactory = $controller;
        location = $location;
    }));

    beforeEach(inject(function ($q) {
        mockService.getRepoInfo = jasmine.createSpy('getRepoInfo');
        var repoInfoDefer = $q.defer();

        var info = new ServiceRepository("serviceName", "repoName", "repoUrl", "link", [], []);
        var repositoryDatas = [info];
        repoInfoDefer.resolve(repositoryDatas);

        mockService.getRepoInfo.andReturn(repoInfoDefer.promise);
    }));


    it("should load repository info data", function () {
        createController();
        expect(scope.repositoryData).toBeUndefined();
        expect(scope.isLoading).toBe(true);

        scope.$digest();
        expect(mockService.getRepoInfo).toHaveBeenCalled();

        expect(scope.isLoading).toBe(false);
        expect(scope.repositoryData.length).toEqual(1);

    });
});

