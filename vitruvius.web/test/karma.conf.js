// Karma configuration
// Generated on Wed Sep 03 2014 09:01:33 GMT+0100 (BST)

module.exports = function (config) {
    config.set({

        // base path, that will be used to resolve files and exclude
        basePath: '',


        // frameworks to use
        frameworks: ['jasmine'],


        files: [
            '../app/scripts/vendor/jquery-2.0.0.js',
            '../app/scripts/vendor/bootstrap.min.js',
            '../app/scripts/vendor/angular.min.js',
            '../app/scripts/vendor/angular-resource.min.js',
            '../app/scripts/vendor/angular-route.min.js',
            '../app/scripts/vendor/angular-sanitize.min.js',
            '../app/scripts/vendor/underscore-min.js',
            '../app/scripts/vendor/raphael.js',
            '../app/scripts/vendor/raphael-min.js',
            '../app/scripts/vendor/angular-sanitize.min.js',
            '../app/scripts/vendor/sequence-diagram-min.js',
            '../app/scripts/vendor/sockjs-0.3.4.min.js',
            '../app/scripts/vendor/vertxbus-2.1.js',
            '../app/scripts/**/*.js',
            'js/angular-mocks.js',
            'unit/**/*.js'
        ],

        // list of files to exclude
        exclude: [

        ],


        // test results reporter to use
        // possible values: 'dots', 'progress', 'junit', 'growl', 'coverage'
        reporters: ['progress', 'dots'],


        // web server port
        port: 9876,


        // enable / disable colors in the output (reporters and logs)
        colors: true,


        // level of logging
        // possible values: config.LOG_DISABLE || config.LOG_ERROR || config.LOG_WARN || config.LOG_INFO || config.LOG_DEBUG
        logLevel: config.LOG_INFO,


        // enable / disable watching file and executing tests whenever any file changes
        autoWatch: true,


        // Start these browsers, currently available:
        // - Chrome
        // - ChromeCanary
        // - Firefox
        // - Opera (has to be installed with `npm install karma-opera-launcher`)
        // - Safari (only Mac; has to be installed with `npm install karma-safari-launcher`)
        // - PhantomJS
        // - IE (only Windows; has to be installed with `npm install karma-ie-launcher`)
        browsers: ['Chrome'],


        plugins: [
            'karma-chrome-launcher',
            'karma-firefox-launcher',
            'karma-jasmine'
        ],

        // If browser does not capture in given timeout [ms], kill it
        captureTimeout: 60000,


        // Continuous Integration mode
        // if true, it capture browsers, run tests and exit
        singleRun: false
    });
};
