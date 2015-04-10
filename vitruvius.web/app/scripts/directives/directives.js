angular.module('vitruvius.directives', [])
    .directive('ngAfterRender', [ '$timeout', function ($timeout) {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                scope.$watch(function () {
                        return scope.$eval(attrs.ngBindHtml);
                    },
                    function (value) {
                        $timeout(function () {
                            scope.$eval(attrs.ngAfterRender, { $element: element });
                        });
                    });
            }
        };
    }]);

