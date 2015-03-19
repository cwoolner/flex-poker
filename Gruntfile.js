module.exports = function(grunt) {
  grunt.initConfig({
      watch: {
          scripts: {
              files: ['src/main/webapp/resources/scripts/**/*.js'],
              tasks: ['browserify']
          }
      },

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
                'target/flexpoker/resources/css/jquery-ui.min.css': 'jquery-ui/themes/smoothness/jquery-ui.min.css',
                'src/main/webapp/resources/css/jquery-ui.min.css': 'jquery-ui/themes/smoothness/jquery-ui.min.css',
                'target/flexpoker/resources/css/images/': 'jquery-ui/themes/smoothness/images/*.png',
                'src/main/webapp/resources/css/images/': 'jquery-ui/themes/smoothness/images/*.png'
            }
        }
    },

    cssmin: {
        options: {
          shorthandCompacting: false,
          roundingPrecision: -1
        },
        target: {
          files: {
            'src/main/webapp/resources/css/bundle.css': [
                                                         'src/main/webapp/resources/css/ng-grid.min.css',
                                                         'src/main/webapp/resources/css/jquery-ui.min.css',
                                                         'src/main/webapp/resources/css/main.css'
                                                         ],
           'target/flexpoker/resources/css/bundle.css': [
                                                         'src/main/webapp/resources/css/ng-grid.min.css',
                                                         'src/main/webapp/resources/css/jquery-ui.min.css',
                                                         'src/main/webapp/resources/css/main.css'
                                                        ]

          }
        }
      },

    browserify: {
      dist: {
        files: {
          'src/main/webapp/resources/js/libs/bundle.js': [
            'src/main/webapp/resources/scripts/routes.js',
            'src/main/webapp/resources/scripts/main.js',
            'src/main/webapp/resources/scripts/cardData.js',
            'src/main/webapp/resources/scripts/webSocketService.js',
            'src/main/webapp/resources/scripts/controllers/*.js',
            'src/main/webapp/resources/scripts/logout/logout.js'
          ],
          'target/flexpoker/resources/js/libs/bundle.js': [
            'src/main/webapp/resources/scripts/routes.js',
            'src/main/webapp/resources/scripts/main.js',
            'src/main/webapp/resources/scripts/cardData.js',
            'src/main/webapp/resources/scripts/webSocketService.js',
            'src/main/webapp/resources/scripts/controllers/*.js',
            'src/main/webapp/resources/scripts/logout/logout.js'
          ]
        },
        options: {
          transform: ['babelify']
        }
      }
    }
  });

  grunt.loadNpmTasks('grunt-bowercopy');
  grunt.loadNpmTasks('grunt-contrib-uglify');
  grunt.loadNpmTasks('grunt-contrib-cssmin');
  grunt.loadNpmTasks('grunt-browserify');
  grunt.loadNpmTasks('grunt-contrib-watch');
  grunt.registerTask('default', ['bowercopy', 'cssmin', 'browserify']);
};
