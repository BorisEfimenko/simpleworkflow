var _ = require('lodash');

module.exports = function(config, requireJsConf) {
  'use strict';

  var options = {
    stubModules: ['text'],

    preserveLicenseComments: false,
    generateSourceMaps: true,

    baseUrl: '<%= pkg.config.pluginSourceDir %>/',

    paths: {
      'angular': 'empty:',
      'angular-data-depend': 'empty:',
      'camunda-bpm-sdk-js': 'empty:',
      'text': '<%= pkg.config.pluginSourceDir.split("/").map(function () { return ".." }).join("/") %>/node_modules/requirejs-text/text'
    }
  };


  requireJsConf.webapp_standaloneTask = {
    options: _.extend({}, options, {
      out: '<%= pkg.config.pluginBuildTarget %>/sampleTask/app/plugin.js',
      include: ['sampleTask/app/plugin'],
      exclude: ['text'],
      insertRequire: ['sampleTask/app/plugin']
    })
  };
};
