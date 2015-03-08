module.exports = function(grunt) {
  grunt.initConfig({
    bowercopy: {
        options: {
          clean: true
        },
        libs: {
            files: {
                'target/flexpoker/resources/js/libs/stomp.min.js': 'stomp-websocket/lib/stomp.min.js',
                'src/main/webapp/resources/js/libs/stomp.min.js': 'stomp-websocket/lib/stomp.min.js',
                'target/flexpoker/resources/js/libs/sockjs.js': 'sockjs-client/dist/sockjs.js',
                'src/main/webapp/resources/js/libs/sockjs.js': 'sockjs-client/dist/sockjs.js',
                'target/flexpoker/resources/js/libs/angular.min.js': 'angular/angular.min.js',
                'src/main/webapp/resources/js/libs/angular.min.js': 'angular/angular.min.js',
                'target/flexpoker/resources/js/libs/angular.min.js.map': 'angular/angular.min.js.map',
                'src/main/webapp/resources/js/libs/angular.min.js.map': 'angular/angular.min.js.map',
                'target/flexpoker/resources/js/libs/angular-route.min.js': 'angular-route/angular-route.min.js',
                'src/main/webapp/resources/js/libs/angular-route.min.js': 'angular-route/angular-route.min.js',
                'target/flexpoker/resources/js/libs/angular-route.min.js.map': 'angular-route/angular-route.min.js.map',
                'src/main/webapp/resources/js/libs/angular-route.min.js.map': 'angular-route/angular-route.min.js.map',
                'target/flexpoker/resources/js/libs/jquery.min.js': 'jquery/dist/jquery.min.js',
                'src/main/webapp/resources/js/libs/jquery.min.js': 'jquery/dist/jquery.min.js',
                'target/flexpoker/resources/js/libs/jquery.min.map': 'jquery/dist/jquery.min.map',
                'src/main/webapp/resources/js/libs/jquery.min.map': 'jquery/dist/jquery.min.map',
                'target/flexpoker/resources/js/libs/ng-grid-2.0.14.min.js': 'angular-grid/ng-grid-2.0.14.min.js',
                'src/main/webapp/resources/js/libs/ng-grid-2.0.14.min.js': 'angular-grid/ng-grid-2.0.14.min.js',
                'target/flexpoker/resources/css/ng-grid.min.css': 'angular-grid/ng-grid.min.css',
                'src/main/webapp/resources/css/ng-grid.min.css': 'angular-grid/ng-grid.min.css',
                'target/flexpoker/resources/js/libs/lodash.min.js': 'lodash/lodash.min.js',
                'src/main/webapp/resources/js/libs/lodash.min.js': 'lodash/lodash.min.js',
                'target/flexpoker/resources/js/libs/jquery-ui/core.min.js': 'jquery-ui/ui/minified/core.min.js',
                'src/main/webapp/resources/js/libs/jquery-ui/core.min.js': 'jquery-ui/ui/minified/core.min.js',
                'target/flexpoker/resources/js/libs/jquery-ui/widget.min.js': 'jquery-ui/ui/minified/widget.min.js',
                'src/main/webapp/resources/js/libs/jquery-ui/widget.min.js': 'jquery-ui/ui/minified/widget.min.js',
                'target/flexpoker/resources/js/libs/jquery-ui/position.min.js': 'jquery-ui/ui/minified/position.min.js',
                'src/main/webapp/resources/js/libs/jquery-ui/position.min.js': 'jquery-ui/ui/minified/position.min.js',
                'target/flexpoker/resources/js/libs/jquery-ui/dialog.min.js': 'jquery-ui/ui/minified/dialog.min.js',
                'src/main/webapp/resources/js/libs/jquery-ui/dialog.min.js': 'jquery-ui/ui/minified/dialog.min.js',
                'target/flexpoker/resources/js/libs/jquery-ui/button.min.js': 'jquery-ui/ui/minified/button.min.js',
                'src/main/webapp/resources/js/libs/jquery-ui/button.min.js': 'jquery-ui/ui/minified/button.min.js',
                'target/flexpoker/resources/js/libs/jquery-ui/mouse.min.js': 'jquery-ui/ui/minified/mouse.min.js',
                'src/main/webapp/resources/js/libs/jquery-ui/mouse.min.js': 'jquery-ui/ui/minified/mouse.min.js',
                'target/flexpoker/resources/js/libs/jquery-ui/draggable.min.js': 'jquery-ui/ui/minified/draggable.min.js',
                'src/main/webapp/resources/js/libs/jquery-ui/draggable.min.js': 'jquery-ui/ui/minified/draggable.min.js',
                'target/main/webapp/resources/css/jquery-ui.min.css': 'jquery-ui/themes/smoothness/jquery-ui.min.css',
                'src/main/webapp/resources/css/jquery-ui.min.css': 'jquery-ui/themes/smoothness/jquery-ui.min.css'
            }
        }
    }
  });

  grunt.loadNpmTasks('grunt-bowercopy');
  grunt.registerTask('default', ['bowercopy']);
};

