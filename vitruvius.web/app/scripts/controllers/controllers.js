'use strict';


var listRepoInformation = function ($scope, $location, VitruviusWebApplicationService, type, sort) {
    $scope.isFromSearch = false;
    $scope.dataReloaded = false;
    $scope.isLoading = true;
    $scope.view = function (link) {
        $location.path("/details").search({link: link});
    }

    $scope.errorMessages = [];
    var promise = VitruviusWebApplicationService.getRepoInfo(type, sort);

    promise.then(function (result) {
        $scope.repositoryData = result;
        $scope.isLoading = false;
    }, function (err) {
        $scope.isLoading = false;
        $scope.errorMessages.push(err);
    });

    $scope.isGithub = function (url) {
        return url.indexOf("github.com") > -1;
    }

    $scope.fieldName = function (field) {
        return getLabel(field);
    }

    $scope.sort = function(sort) {
       listRepoInformation($scope, $location, VitruviusWebApplicationService, type, sort);
    }
}

angular.module('vitruvius.controllers', [])
    .controller("AboutVitruviusController", ['$scope', '$location', '$routeParams', 'VitruviusWebApplicationService',
        function ($scope, $location, $routeParams, VitruviusWebApplicationService) {

        }])
    .controller("PlatformListController", ['$scope', '$location', 'VitruviusWebApplicationService',
        function ($scope, $location, VitruviusWebApplicationService) {
            listRepoInformation($scope, $location, VitruviusWebApplicationService, 'platform')
        }])
    .controller("ServiceListController", ['$scope', '$location', 'VitruviusWebApplicationService',
        function ($scope, $location, VitruviusWebApplicationService) {
            listRepoInformation($scope, $location, VitruviusWebApplicationService, 'service')
        }])
    .controller("RegisterListController", ['$scope', '$location', '$routeParams', 'VitruviusWebApplicationService',
        function ($scope, $location, $routeParams, VitruviusWebApplicationService) {
            listRepoInformation($scope, $location, VitruviusWebApplicationService, 'register')

        }])
    .controller("ComponentListController", ['$scope', '$location', '$routeParams', 'VitruviusWebApplicationService',
        function ($scope, $location, $routeParams, VitruviusWebApplicationService) {
            listRepoInformation($scope, $location, VitruviusWebApplicationService, 'component')

        }])
    .controller("ViewDetailsController", ['$scope', '$routeParams', 'VitruviusWebApplicationService',
        function ($scope, $routeParams, VitruviusWebApplicationService) {
            $scope.isLoading = true;
            $scope.dataReloaded = false;
            var link = $routeParams.link;
            var promise = VitruviusWebApplicationService.getServiceDetail($routeParams.link);
            $scope.messages = [];
            promise.then(function (result) {
                if (result) {
                    $scope.html = result;
                } else {
                    $scope.html = "<div class='alert alert-danger' role='alert'>Could not load content from " + link + "</div>"
                }
                $scope.isLoading = false;

            }, function (err) {
                $scope.html = "<div class='alert alert-danger' role='alert'>Could not load content from " + link + "</div>"
                $scope.isLoading = false;
                $scope.messages.push("An exception occurred..." + err);
            })

            $scope.applyDiagram = function ($element) {
                $(".diagram").sequenceDiagram({theme: 'simple'});
            };


        }])
    .controller("HeaderController", ['$scope', '$location', '$rootScope', function ($scope, $location, $rootScope) {

        $scope.isActive = function (viewLocation) {
            var type = "service";
            if ($location.url().indexOf("components") > -1) {
                type = "component"
            }
            if ($location.url().indexOf("registers") > -1) {
                type = "register"
            }
            if ($location.url().indexOf("platforms") > -1) {
                type = "platform"
            }
            if ($location.url().indexOf("search") > -1) {
                type = null;
            }

            if (type) {
                $rootScope.typeField = " AND type:" + type;
            } else {
                $rootScope.typeField = "";
            }

            return viewLocation === $location.path();
        };
        $scope.dataReloaded = false;

        $.notify.addStyle('foo', {
            html: "<div>" +
            "<div>" +
            "<div class='title' style='white-space: normal;' data-notify-html='message'/>" +
            "<button class='btn btn-primary' data-notify-text='button'></button>" +
            "</div>" +
            "</div>",
            classes: {
                base: {
                    "font-weight": "bold",
                    "padding": "8px 15px 8px 14px",
                    "text-shadow": "0 1px 0 rgba(255, 255, 255, 0.5)",
                    "background-color": "#fcf8e3",
                    "border": "1px solid #fbeed5",
                    "border-radius": "4px",
                    "white-space": "nowrap",
                    "padding-left": "25px",
                    "background-repeat": "no-repeat",
                    "background-position": "3px 7px"
                },
                info: {
                    "color": "#3A87AD",
                    "background-color": "#D9EDF7",
                    "border-color": "#BCE8F1",
                    "background-image": "url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAABmJLR0QA/wD/AP+gvaeTAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAB3RJTUUH3QYFAhkSsdes/QAAA8dJREFUOMvVlGtMW2UYx//POaWHXg6lLaW0ypAtw1UCgbniNOLcVOLmAjHZolOYlxmTGXVZdAnRfXQm+7SoU4mXaOaiZsEpC9FkiQs6Z6bdCnNYruM6KNBw6YWewzl9z+sHImEWv+vz7XmT95f/+3/+7wP814v+efDOV3/SoX3lHAA+6ODeUFfMfjOWMADgdk+eEKz0pF7aQdMAcOKLLjrcVMVX3xdWN29/GhYP7SvnP0cWfS8caSkfHZsPE9Fgnt02JNutQ0QYHB2dDz9/pKX8QjjuO9xUxd/66HdxTeCHZ3rojQObGQBcuNjfplkD3b19Y/6MrimSaKgSMmpGU5WevmE/swa6Oy73tQHA0Rdr2Mmv/6A1n9w9suQ7097Z9lM4FlTgTDrzZTu4StXVfpiI48rVcUDM5cmEksrFnHxfpTtU/3BFQzCQF/2bYVoNbH7zmItbSoMj40JSzmMyX5qDvriA7QdrIIpA+3cdsMpu0nXI8cV0MtKXCPZev+gCEM1S2NHPvWfP/hL+7FSr3+0p5RBEyhEN5JCKYr8XnASMT0xBNyzQGQeI8fjsGD39RMPk7se2bd5ZtTyoFYXftF6y37gx7NeUtJJOTFlAHDZLDuILU3j3+H5oOrD3yWbIztugaAzgnBKJuBLpGfQrS8wO4FZgV+c1IxaLgWVU0tMLEETCos4xMzEIv9cJXQcyagIwigDGwJgOAtHAwAhisQUjy0ORGERiELgG4iakkzo4MYAxcM5hAMi1WWG1yYCJIcMUaBkVRLdGeSU2995TLWzcUAzONJ7J6FBVBYIggMzmFbvdBV44Corg8vjhzC+EJEl8U1kJtgYrhCzgc/vvTwXKSib1paRFVRVORDAJAsw5FuTaJEhWM2SHB3mOAlhkNxwuLzeJsGwqWzf5TFNdKgtY5qHp6ZFf67Y/sAVadCaVY5YACDDb3Oi4NIjLnWMw2QthCBIsVhsUTU9tvXsjeq9+X1d75/KEs4LNOfcdf/+HthMnvwxOD0wmHaXr7ZItn2wuH2SnBzbZAbPJwpPx+VQuzcm7dgRCB57a1uBzUDRL4bfnI0RE0eaXd9W89mpjqHZnUI5Hh2l2dkZZUhOqpi2qSmpOmZ64Tuu9qlz/SEXo6MEHa3wOip46F1n7633eekV8ds8Wxjn37Wl63VVa+ej5oeEZ/82ZBETJjpJ1Rbij2D3Z/1trXUvLsblCK0XfOx0SX2kMsn9dX+d+7Kf6h8o4AIykuffjT8L20LU+w4AZd5VvEPY+XpWqLV327HR7DzXuDnD8r+ovkBehJ8i+y8YAAAAASUVORK5CYII=)"
                }
            }
        });


        $(document).on('click', '.notifyjs-foo-base .btn', function () {
            refresh();
        });

        var eb = new vertx.EventBus(eventBusUrl);
        eb.onopen = function () {
            $scope.dataReloaded = false;
            eb.registerHandler("vitruvius.data.reloaded", function (data) {
                if (data) {
                    if (!$scope.dataReloaded) {
                        $scope.dataReloaded = true;
                        var parsedData = JSON.parse(data);
                        var dataUpdatedMessage = "Vitruvius document was updated for '" + parsedData.uri + "' by '" + parsedData.updatedBy + "'";
                        $.notify({message: dataUpdatedMessage, button: 'Reload'}, {
                            style: 'foo',
                            autoHide: false,
                            clickToHide: false,
                            className: 'info',
                            globalPosition: 'bottom center',
                            arrowShow: false
                        });
                        $scope.$apply();
                    }
                }
            });
        }

        eb.onclose = function () {
            eb = null;
            $scope.dataReloaded = false;
        };

        var refresh = function () {
            $scope.dataReloaded = false;
            $(this).trigger('notify-hide');
            window.location.reload(false);
        }

        $scope.submit = function () {
            var queryToProcess = this.query
            this.query = "";
            $location.path("/search").search({query: queryToProcess});
        }
    }])
    .controller("VitruviusContentValidator", ["$scope", function ($scope) {

        $scope.reset = function () {
            $scope.validationPerformed = false;
            $scope.isLoading = false;
            $scope.validationErrors = [];
            $scope.validationWarnings = [];
        }

        $scope.validate = function () {
            $scope.isLoading = true;
            $scope.isValidating = true;
            var eb = new vertx.EventBus(eventBusUrl);
            eb.onopen = function () {
                eb.send("vitruvius.data.validation", {
                    "markdown.content": $scope.markdown
                }, function (reply) {
                    $scope.validationErrors = [];
                    $scope.validationWarnings = [];
                    angular.forEach(JSON.parse(reply), function (data) {
                        if (data.type == "ERROR") {
                            $scope.validationErrors.push(data);
                        }
                        if (data.type == "WARNING") {
                            $scope.validationWarnings.push(data);
                        }
                    })
                    $scope.validationPerformed = true;
                    $scope.isLoading = false;
                    $scope.isValidating = false;
                    $scope.$apply();

                });
            }

            eb.onclose = function () {
                $scope.isLoading = false;
                $scope.isValidating = false;
                eb = null;
            };


        }

    }])
    .controller("AddController", ["$scope", "$location", function ($scope, $location) {
        $scope.message = "Add Entry to Vitruvius";


        $scope.reset = function () {
            $scope.validationPerformed = false;
            $scope.isLoading = false;
            $scope.isValidating = false;
            $scope.validationErrors = [];
        }

        $scope.register = function () {
            $scope.isLoading = true;
            $scope.isValidating = true;
            var eb = new vertx.EventBus(eventBusUrl);
            eb.onopen = function () {
                eb.send("vitruvius.add.service", {
                        "address.content": $scope.address, "serviceName.content": $scope.serviceName
                    }, function (reply) {
                        $scope.validationErrors = [];
                        var hasError = false;
                        var replyParsed = JSON.parse(reply);
                        angular.forEach(JSON.parse(replyParsed.messages), function (data) {
                            if (data.type === 'ERROR') {
                                hasError = true;
                            }
                            $scope.validationErrors.push(data);
                        })

                        if (!hasError) {
                            var link = JSON.parse(replyParsed.repositoryInformation).repoUri;
                            $location.path("/details").search({link: link});
                        }

                        $scope.validationPerformed = true;
                        $scope.isLoading = false;
                        $scope.isValidating = false;
                        $scope.$apply();
                    }
                )

            }
        }
    }])
    .
    controller("FacetController", ['$scope', '$location', '$routeParams', 'VitruviusWebApplicationService', function ($scope, $location, $routeParams, VitruviusWebApplicationService) {

        $scope.facet = function (fieldName, facet) {
            return fieldName + ":" + "\"" + facet + "\"";
        }

        var query = "type: \"service\""
        if ($location.url().indexOf("platforms") > -1) {
            query = "type: \"platform\""
        }
        if ($location.url().indexOf("components") > -1) {
            query = "type: \"component\""
        }
        if ($location.url().indexOf("registers") > -1) {
            query = "type: \"register\""
        }
        if ($location.url().indexOf("search") > -1) {
            query = $routeParams.query
        }
        var aggregatePromise = VitruviusWebApplicationService.getAggregates(query);
        $scope.facetLoading = true;
        aggregatePromise.then(function (result) {
            var aggregations = result.aggregations;
            var statuses = aggregations.statuses.buckets;
            var departments = aggregations.departments.buckets;
            var tags = aggregations.tags.buckets

            $scope.statuses = statuses;
            $scope.tags = tags;
            $scope.departments = departments;

            $scope.facetLoading = false;
        }, function (err) {
            console.log("Error..." + err);
            $scope.facetLoading = false;
            $scope.errorMessages.push("Exception occurred while trying to extract aggregations");
        })

    }])
    .controller("SearchController", ['$scope', '$location', '$routeParams', 'VitruviusWebApplicationService', 'filterFilter', function ($scope, $location, $routeParams, VitruviusWebApplicationService, filterFilter) {
        $scope.dataReloaded = false;
        $scope.isLoading = true;
        $scope.isFromSearch = true;
        $scope.originalQuery = $routeParams.query;

        var promise = VitruviusWebApplicationService.search($routeParams.query);
        $scope.errorMessages = [];
        $scope.repositoryData = [];

        promise.then(function (result) {
            if (result) {
                $scope.repositoryData = result;
            }
            $scope.isLoading = false;
        }, function (err) {
            $scope.isLoading = false;
            console.log("error..." + err);
            $scope.errorMessages.push("An error occurred while trying to process your search query");
        });

        $scope.view = function (link) {
            $location.$$search = {};
            $location.path("/details").search({link: link});
        }
        $scope.query = "";

        $scope.fieldName = function (field) {
            return getLabel(field);
        }

        $scope.isGithub = function (url) {
            return url.indexOf("github.com") > -1;
        }

        $scope.setTypeFilter = function (type) {
            $scope.typeFilter = type;
        }

    }]);

