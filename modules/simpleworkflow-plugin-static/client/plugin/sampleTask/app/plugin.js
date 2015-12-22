define([
  'angular',
  './navbar/main'
], function(
  angular,
  navbarModule
) {
  return angular.module('tasklist.plugin.sampleTask', [navbarModule.name]);
});