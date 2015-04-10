'use strict';

var resultsToBeReturned = {
    "took": 5,
    "timed_out": false,
    "_shards": {
        "total": 5,
        "successful": 5,
        "failed": 0
    },
    "hits": {
        "total": 3,
        "max_score": 1.0,
        "hits": [
            {
                "_index": "services",
                "_type": "service",
                "_id": "repoName3",
                "_score": 1.0,
                "_source": {"repoName": "repoName3", "repoUri": "test/PaymentService", "serviceName": "Payment Service", "link": "paymentService/vitruvius.md", "meta": {"Status": "Draft", "Version": "0.1", "Author": "Amin Mohammed-Coleman <amin.mohammed-coleman@digital.cabinet-office.gov.uk>", "Description": "Using to make a payment...test description", "LastUpdated": "22/08/2014"}, "services": [
                    {"name": "Credit Card Validation Service", "type": "Service", "link": ""}
                ]}
            },
            {
                "_index": "services",
                "_type": "service",
                "_id": "repoName2",
                "_score": 1.0,
                "_source": {"repoName": "repoName2", "repoUri": "test/PeopleLookUpService", "serviceName": "People LookUp Service", "link": "peopleLookUpService/vitruvius.md", "meta": {"Status": "Draft", "Version": "0.1", "Author": "Amin Mohammed-Coleman <amin.mohammed-coleman@digital.cabinet-office.gov.uk>", "Description": "Service that return information about an individual..for test purpose only!", "LastUpdated": "22/08/2014"}, "services": []}
            },
            {
                "_index": "services",
                "_type": "service",
                "_id": "repoName1",
                "_score": 1.0,
                "_source": {"repoName": "repoName1", "repoUri": "test/ExampleService", "serviceName": "My Custom Example Service", "link": "incomeTaxService/vitruvius.md", "meta": {"Status": "Draft", "Version": "0.1", "Author": "Amin Mohammed-Coleman <amin.mohammed-coleman@digital.cabinet-office.gov.uk>", "Description": "Example service using two other business services", "LastUpdated": "22/08/2014"}, "services": [
                    {"name": "People Look Up Service", "type": "Service", "link": "peopleLookUpService/vitruvius.md"},
                    {"name": "Payment Service", "type": "Service", "link": "paymentService/vitruvius.md"}
                ]}
            }
        ]
    }
}

describe("Controller: SearchController", function () {
    var controllerFactory, scope, location, routeParams , mockService = {};

    beforeEach(module('vitruvius.controllers'));

    beforeEach(inject(function ($controller, $rootScope, $location) {
        scope = $rootScope.$new();
        controllerFactory = $controller;
        location = $location;
        routeParams = {};
    }));
    function createController(searchQuery) {
        routeParams.query = searchQuery;
        return controllerFactory('SearchController', {
            $scope: scope,
            $location: location,
            $routeParams: routeParams,
            VitruviusWebApplicationService: mockService
        });
    }
    describe("Successful search", function () {
        beforeEach(inject(function ($q) {
            mockService.search = jasmine.createSpy('search');
            var searchDefer = $q.defer();
            searchDefer.resolve(resultsToBeReturned);
            mockService.search.andReturn(searchDefer.promise);
        }));

        it("should return search results", function () {
            var searchQuery = "my custom query";
            createController(searchQuery);

            scope.$digest();
            expect(mockService.search).toHaveBeenCalledWith(searchQuery);
            expect(scope.results).toBeDefined();
            expect(scope.results.hits.hits.length).toEqual(3);
            expect(scope.query).toEqual("");
            expect(scope.errorMessages.length).toEqual(0);

        });
    })
    describe("Unsuccessful search", function () {
        beforeEach(inject(function ($q) {
            mockService.search = jasmine.createSpy('search');
            var searchDefer = $q.defer();
            searchDefer.reject("Failure");
            mockService.search.andReturn(searchDefer.promise);
        }));

        it("should return search results", function () {
            var searchQuery = "my custom query";
            createController(searchQuery);
            scope.$digest();
            expect(mockService.search).toHaveBeenCalledWith(searchQuery);
            expect(scope.results).toBeUndefined();
            expect(scope.errorMessages.length).toEqual(1);
            expect(scope.errorMessages[0]).toEqual("An error occurred while trying to process your search query");
        });
    })


});