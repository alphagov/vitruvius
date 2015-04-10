angular.module('vitruvius.filters', [])
    .filter('typeFilter', ['$filter', function ($filter) {
        return function (inputArray, typeFilter) {
            if (!angular.isDefined(typeFilter) || typeFilter == '') {
                return inputArray;
            }
            var data = [];
            angular.forEach(inputArray, function (item) {
                if (item.metaData.type === typeFilter) {
                    data.push(item);
                }
            });
            return data;
        };
    }]);