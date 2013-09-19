var flexpokerModule = angular.module('flexpoker', ['AngularStomp', 'ngGrid']);

// taken from http://jsfiddle.net/thomporter/DwKZh/
flexpokerModule.directive('numbersOnly', function() {
    return {
      require: 'ngModel',
      link: function(scope, element, attrs, modelCtrl) {
        modelCtrl.$parsers.push(function (inputValue) {
            // this next if is necessary for when using ng-required on your input.
            // In such cases, when a letter is typed first, this parser will be called
            // again, and the 2nd time, the value will be undefined
            if (inputValue == undefined) {
                return '';
            }
            var transformedInput = inputValue.replace(/[^0-9]/g, '');
            if (transformedInput!=inputValue) {
               modelCtrl.$setViewValue(transformedInput);
               modelCtrl.$render();
            }

            return transformedInput;
        });
      }
    };
});

flexpokerModule.directive('chat', function() {
    return {
        templateUrl: rootUrl + 'resources/templates/chatWindow.html',
        restrict: 'A',
        compile: function compile(tElement, tAttrs) {
            return function postLink(scope, iElement, iAttrs) {
                iElement.find('button, input[type=submit]').button();
            }
        }
    }
});

flexpokerModule.directive('seat', function() {
    return {
        templateUrl: rootUrl + 'resources/templates/seat.html',
        restrict: 'A',
        link: function(scope, element, attrs) {
            alert('');
        }
    }
});