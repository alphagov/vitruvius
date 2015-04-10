var vertx = require('vertx');
var container = require("vertx/container");
var console = require("vertx/console");
var httpServer = vertx.createHttpServer();
var routeMatcher = new vertx.RouteMatcher();
var eventbus = require("vertx/event_bus");


var hostname = container.env['OPENSHIFT_DIY_IP'] || '0.0.0.0';
var port = parseInt(container.env['OPENSHIFT_DIY_PORT'] || 8000);

var inboundPermittedAddresses = [
    {
        address: 'vitruvius.repo.list.address'
    },
    {
        address: 'vitruvius.view.address'
    },
    {
        address: 'vitruvius.data.reloaded'
    },
    {
        address: 'vitruvius.data.validation'
    },
    {
        address: 'vitruvius.add.service'
    },
    {
        address: 'vitruvius.search'
    }
    ,
    {
        address: 'vitruvius.aggregations'
    },
    {
        address: 'vitruvius.github.integration'
    }


];

var outboundPermitted = [
    {
        address_re: 'vitruvius.repo.list.address'
    },
    {
        address_re: 'vitruvius.view.address'
    },
    {
        address_re: 'vitruvius.data.reloaded'
    },
    {
        address_re: 'vitruvius.data.validation'
    },
    {
        address_re: 'vitruvius.add.service'
    }
    ,
    {
        address_re: 'vitruvius.search'
    },
    {
        address_re: 'vitruvius.aggregations'
    },
    {
        address_re: 'vitruvius.github.integration'
    }
];


var createServer = function () {
    routeMatcher.post("/github-int/payload", function (req) {
        var body = new vertx.Buffer();
        req.dataHandler(function (buffer) {
            body.appendBuffer(buffer);
        });
        req.endHandler(function () {
            eventbus.send("vitruvius.github.integration", JSON.parse(body));
        });
        req.response.end("Processed");
    });

    routeMatcher.get("/vitruvius/repositories", function (req) {
        var type = req.params().get('type');
        var sort = req.params().get('sort');
        if (!type) {
            type = "all";
        }
        if (!sort) {
            sort = "serviceName";
        }
        eventbus.send("vitruvius.repo.list.address", {"type": type, "sort": sort}, function (response) {
            req.response.end(JSON.stringify(JSON.parse(response), undefined, 2));
        });
    });

    routeMatcher.getWithRegEx("^.*\\.(jpg|JPG|pdf|png|PNG|PDF|json)", function(req) {
        req.response.sendFile('../repository/' + req.path());
    })

    routeMatcher.noMatch(function (req) {
        var file = '';
        if (req.path() == '/') {
            file = 'index.html';
        } else if (req.path().indexOf('..') == -1) {
            file = req.path();
        }
        req.response.sendFile('app/' + file);
    });


    httpServer.requestHandler(routeMatcher);

    var sockJSServer = vertx.createSockJSServer(httpServer);

    sockJSServer.bridge({prefix: '/eventbus'}, inboundPermittedAddresses, outboundPermitted);

    httpServer.listen(port, hostname, function (err) {
        if (!err) {
            console.log("Web started...");
        }
    });


}

var initialiseCore = function() {
    var moduleName = 'gov.uk~vitruvius.vertx~1.0-SNAPSHOT';
    var esHostName = container.env['OPENSHIFT_ES_HOST'];
    var postAddress = (container.env['OPENSHIFT_APP_DNS'] || '0.0.0.0:8000') + "/github-int/payload"

    console.log("Post address " + postAddress);
    vertx.getMap('config').put("vitruvius.post.address", postAddress);


    var vitruviusConfig = {
        "embedded.mode": (container.env['OPENSHIFT_ES_EMBEDDED'] || "true"),
        "root": (container.env.get("PWD")) + "/mods/" + moduleName + "/",
        "configLocation": "conf.json",
        "search.index.dir": container.env['OPENSHIFT_DATA_DIR'] || "/var/tmp/",
        "git.hub.int.port": 15000,
        "search.http.enabled": true,
        "search.host": esHostName || "0.0.0.0"
    }

    container.deployModule(moduleName, 1, vitruviusConfig, function (err) {
        if (!err) {
            console.log("Module started...");
        } else {
            console.log("Something went wrong.." + err);
        }
    });

}


createServer();
initialiseCore();







