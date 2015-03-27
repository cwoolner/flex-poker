module.exports = function(grunt) {
  grunt.initConfig({
      fileDefs: {
          bowerJsFiles: [
              'stomp-websocket/lib/stomp.min.js',
              'sockjs-client/dist/sockjs.js',
              'angular/angular.min.js',
              'angular/angular.min.js.map',
              'angular-route/angular-route.min.js',
              'angular-route/angular-route.min.js.map',
              'jquery/dist/jquery.min.js',
              'jquery/dist/jquery.min.map',
              'jquery-ui/ui/minified/core.min.js',
              'jquery-ui/ui/minified/widget.min.js',
              'jquery-ui/ui/minified/position.min.js',
              'jquery-ui/ui/minified/dialog.min.js',
              'jquery-ui/ui/minified/button.min.js',
              'jquery-ui/ui/minified/mouse.min.js',
              'jquery-ui/ui/minified/draggable.min.js'
          ],
          bowerCssFiles:[
              'jquery-ui/themes/smoothness/jquery-ui.min.css'
          ],
          bowerImgFiles:[
              'jquery-ui/themes/smoothness/images/*.png'
          ],
          cssminFiles: [
              'src/main/webapp/resources/css/libs/jquery-ui.min.css',
              'src/main/webapp/resources/css/main.css'
          ],
          browserifyFiles: [
              'src/main/webapp/resources/scripts/routes.js',
              'src/main/webapp/resources/scripts/main.js',
              'src/main/webapp/resources/scripts/cardData.js',
              'src/main/webapp/resources/scripts/webSocketService.js',
              'src/main/webapp/resources/scripts/controllers/*.js',
              'src/main/webapp/resources/scripts/logout/logout.js',
              'src/main/webapp/resources/scripts/game/gameController.js',
              'src/main/webapp/resources/scripts/router/router.js'
          ]
      },

      watch: {
          scripts: {
              files: '<%= fileDefs.browserifyFiles %>',
              tasks: ['browserify']
          }
      },

    bowercopy: {
        libs: {
            files: {
                'target/flexpoker/resources/js/libs/': '<%= fileDefs.bowerJsFiles %>',
                'src/main/webapp/resources/js/libs/': '<%= fileDefs.bowerJsFiles %>',
                'target/flexpoker/resources/css/libs/': '<%= fileDefs.bowerCssFiles %>',
                'src/main/webapp/resources/css/libs/': '<%= fileDefs.bowerCssFiles %>',
                'target/flexpoker/resources/css/dist/images': '<%= fileDefs.bowerImgFiles %>',
                'src/main/webapp/resources/css/dist/images': '<%= fileDefs.bowerImgFiles %>'
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
            'src/main/webapp/resources/css/dist/bundle.css': '<%= fileDefs.cssminFiles %>',
            'target/flexpoker/resources/css/dist/bundle.css': '<%= fileDefs.cssminFiles %>'
          }
        }
      },

    browserify: {
      dist: {
        files: {
          'src/main/webapp/resources/js/libs/bundle.js': '<%= fileDefs.browserifyFiles %>',
          'target/flexpoker/resources/js/libs/bundle.js': '<%= fileDefs.browserifyFiles %>'
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
