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
                'src/main/webapp/resources/js/libs/sockjs.js': 'sockjs-client/dist/sockjs.js'
            }
        }
    }
  });

  grunt.loadNpmTasks('grunt-bowercopy');
  grunt.registerTask('default', ['bowercopy']);
};

