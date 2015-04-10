angular.module('vitruvius.services', ['elasticsearch'])
    .service("VitruviusWebApplicationService", ['$q', function ($q) {
        return {
            getAggregates: function (query) {
                var deferred = $q.defer();
                var eb = new vertx.EventBus(eventBusUrl);
                eb.onopen = function () {
                    eb.send("vitruvius.aggregations", {"query" : query}, function (reply) {
                        if (reply) {
                            deferred.resolve(JSON.parse(reply));
                        } else {
                            deferred.reject("Unable to retrieve aggregations");
                        }
                    });
                }

                eb.onclose = function () {
                    eb = null;
                };
                return deferred.promise;
            },
            getRepoInfo: function (repoType, sort) {
                if (!sort) {
                    sort = "serviceName";
                }
                var eb = new vertx.EventBus(eventBusUrl);
                var deferred = $q.defer();
                eb.onopen = function () {
                    eb.send("vitruvius.repo.list.address", {"type":repoType, "sort": sort}, function (message) {
                        if (message) {
                            var rawData = JSON.parse(JSON.stringify(message));
                            deferred.resolve(ServiceRepository.build(rawData));
                        } else {
                            deferred.reject("Unable to load service registry list");
                        }
                    });
                }

                eb.onclose = function () {
                    eb = null;
                };

                return deferred.promise;
            },
            getServiceDetail: function (link) {
                var eb = new vertx.EventBus(eventBusUrl);
                var deferred = $q.defer();
                eb.onopen = function () {
                    eb.send("vitruvius.view.address", {"link": link}, function (reply) {
                        deferred.resolve(reply);
                    });
                }
                eb.onclose = function () {
                    eb = null;
                };
                return deferred.promise;
            },
            search: function (query) {
                var deferred = $q.defer();
                var eb = new vertx.EventBus(eventBusUrl);
                eb.onopen = function () {
                    eb.send("vitruvius.search", {"query": query}, function (reply) {
                        var response = JSON.parse(reply);
                        var data;
                        if (response.hits.total != 0) {
                            var rawServiceDefinitions = [];
                            angular.forEach(response.hits.hits, function (hit) {
                                rawServiceDefinitions.push(hit._source);
                            });
                            data = ServiceRepository.build(JSON.stringify(rawServiceDefinitions));
                        }
                        deferred.resolve(data);


                    });
                }
                return deferred.promise;
            }
        }
    }])

