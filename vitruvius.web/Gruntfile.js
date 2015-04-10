module.exports = function (grunt) {

    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),
        uglify: {
            options: {
                banner: '/*! <%= pkg.name %> <%= grunt.template.today("yyyy-mm-dd") %> */\n'
            },
            build: {
                src: ['app/scripts/**/*.js'],
                dest: 'app/scripts/build/<%= pkg.name %>.min.js'
            }
        },
        watch: {
            scripts: {
                files: ['app/scripts/**/*.js'],
                tasks: ['uglify'],
                options: {
                    spawn: false
                }
            }
        },
        shell: {
            vertx: {
                command: function (file) {
                    grunt.log.writeln("Starting vertx....");
                    return 'vertx run ' + file + ' -cluster';
                }
            },
            test: {
                command : function() {
                   return "karma start test/karma.conf.js";
                }
            }
        }

    });

    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-shell');

    grunt.registerTask('default', ['uglify','watch']);

    grunt.registerTask('test', ['shell:test']);

}
