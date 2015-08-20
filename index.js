var requirejs = require('requirejs');

requirejs.config({
    nodeRequire: require
});

requirejs(['src/app'], function (app) {
    app(requirejs, process);
});
