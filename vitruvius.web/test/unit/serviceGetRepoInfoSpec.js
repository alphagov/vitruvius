'use strict';

//getRepoInfo: function () {
//    var eb = new vertx.EventBus(eventBusUrl);
//    var deferred = $q.defer();
//    eb.onopen = function () {
//        eb.send("vitruvius.list.service.address", {}, function (message) {
//            if (message) {
//                var rawData = JSON.parse(JSON.stringify(message));
//                deferred.resolve(ServiceRepository.build(rawData));
//            } else {
//                deferred.reject("Unable to load service registry list");
//            }
//        });
//    }
//
//    eb.onclose = function () {
//        eb = null;
//    };
//
//    return deferred.promise;
//},

describe("vitruvius web application service get repo info", function () {
    it ("should return a promise of repo information", function() {

    })
});