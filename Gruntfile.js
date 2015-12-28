module.exports = function(grunt) {
  grunt.initConfig({
      fileDefs: {
          browserifyFiles: [
              'src/main/webapp/resources/scripts/main.js',
              'src/main/webapp/resources/scripts/cardData.js',
              'src/main/webapp/resources/scripts/webSocketService.js',
              'src/main/webapp/resources/scripts/controllers/*.js',
              'src/main/webapp/resources/scripts/logout/logout.js',
              'src/main/webapp/resources/scripts/router/router.js',
              'src/main/webapp/resources/scripts/chat/chat.js'
          ]
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

  grunt.loadNpmTasks('grunt-browserify');
  grunt.registerTask('default', ['browserify']);
};
