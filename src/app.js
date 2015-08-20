define(function () {
    var app;
    app = function (requirejs, application) {
        requirejs(['commander'], function (program) {
            program
                .usage('<top level plugin name>')
                .parse(application.argv);
            if (program.args.length != 1) {
                // The application did not receive the expected command line arguments.
                program.outputHelp();
                application.exit(1);
            }
            else {
                // TODO: Remove this.
                console.log("top level plugin name: ", program.args[0]);
            }
        });
    };
    return app;
});
