module.exports = function(grunt) {
	'use strict';
	var pkg = require('./package.json');
	var config = pkg.config || {};
	config.grunt = grunt;
	config.pkg = pkg;
	require('load-grunt-tasks')(grunt);

	// clean

	// requirejs
	var requireJsConf = {
		options : {
			optimize : '<%= (buildMode === "prod" ? "uglify2" : "none") %>'
		}
	};

	require('./grunt/config/requirejs')(config, requireJsConf);

	grunt.registerTask('build', function(mode, app) {

		if (typeof app !== 'undefined') {
			console.log(' ------------  will build ' + app + ' -------------');
			var objs = [ requireJsConf, gruntConf ];
			for (var i = 0; i < objs.length; i++) {
				var obj = objs[i];
				for ( var key in obj) {
					if (obj.hasOwnProperty(key)
							&& key.toLowerCase().indexOf(app) === -1
							&& key !== 'options'
							&& key.toLowerCase().indexOf('webapp') === -1
							&& key.toLowerCase().indexOf('sdk') === -1) {
						delete obj[key];
					}
				}
			}
		}

		grunt.config.data.buildMode = mode || 'prod';

		grunt.task.run([ 'clean', 'requirejs' ]);

	});

	grunt.registerTask('default', [ 'build' ]);

	grunt.initConfig({
		buildMode : 'dev',
		pkg : pkg,
		clean : [ config.pluginBuildTarget + "/*" ],
		requirejs : requireJsConf
	});

};
