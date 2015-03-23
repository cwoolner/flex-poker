require('babel/polyfill');
import handleRoutes from './routes';
import chat from './chat/chat';

window.onhashchange = handleRoutes;

var flexpokerModule = angular.module('flexpoker', ['ngRoute']);

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

export default flexpokerModule;
