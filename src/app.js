define(function () {
    var app;
    app = function (requirejs) {
        requirejs(['commander'], function (program) {
            program.parse(process.argv);
            console.log(program.args);
        });
    };
    return app;
});
