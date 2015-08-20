var requireInjector,
    expect,
    sinon;

requireInjector = require('library/test_utilities/requireInjector');
sinon = require('sinon');
expect = require('expect.js');

describe('app', function () {
    var app,
        mockRequirejs,
        mockProcess,
        commander,
        expectedUsage;
    beforeEach(function (done) {
        var injector,
        injector = requireInjector.createInjector();
        mockRequirejs = sinon.stub();
        mockProcess = {
            exit: function () {}
        };
        sinon.spy(mockProcess, 'exit');
        commander = {
            usage: function () {},
            outputHelp: function () {},
            parse: function () {}
        };
        sinon.stub(commander);

        // Configure commander
        expectedUsage = '<top level plugin name>';
        commander.usage
            .withArgs(expectedUsage)
            .returnsThis();

        // Use a custom matcher here to initialize commander.args,
        // which is the side-effect when the application calls commander.parse.
        commander.parse
            .withArgs(sinon.match(function (value) {
                // If the value is mockProcess.argv, then initialize commander.args.
                if (value == mockProcess.argv) {
                    commander.args = mockProcess.argv;
                    return true;
                }
                // otherwise, fail the match.s
                return false;
            }));

        // Configure mockRequirejs
        mockRequirejs
            .withArgs(['commander'], sinon.match.func)
            .callsArgWith(1, commander);

        injector
            .require(['src/app'], function (theApp) {
                app = theApp;
                done();
            });
    });

    it('should output help if there are zero arguments', function (done) {
        mockProcess.argv = [];
        app(mockRequirejs, mockProcess);
        sinon.assert.calledWith(commander.usage, expectedUsage);
        sinon.assert.calledOnce(commander.outputHelp);
        sinon.assert.calledWith(mockProcess.exit, 1);
        done();
    });

    it('should not help if there is exactly one argument', function (done) {
        mockProcess.argv = ['plugin'];
        app(mockRequirejs, mockProcess);
        sinon.assert.calledWith(commander.usage, expectedUsage);
        sinon.assert.notCalled(commander.outputHelp);
        sinon.assert.notCalled(mockProcess.exit, 1);
        expect(commander.args).to.equal(mockProcess.argv);
        done();
    });


    it('should output help if there are more than 1 arguments', function (done) {
        mockProcess.argv = ['one', 'two'];
        app(mockRequirejs, mockProcess);
        sinon.assert.calledWith(commander.usage, expectedUsage);
        sinon.assert.calledOnce(commander.outputHelp);
        sinon.assert.calledWith(mockProcess.exit, 1);
        done();
    });
});
