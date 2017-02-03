var CNVSManager = {
    host: (typeof CNVS_HOST === 'undefined') ? 'http://localhost:8080/cnvs/rest' : CNVS_HOST,
    version: 'v3',

    regions: {
        fetch: function (args) {
            return CNVSManager._doRequest(args, 'regions', 'fetch');
        }
    },
    stats: {
        count: function (args) {
            return CNVSManager._doRequest(args, 'stats', 'count');
        }

    },
    management:{
        add: function (args) {
            return CNVSManager._doRequest(args, 'management', 'add');
        },
        search: function(args) {
            return CNVSManager._doRequest(args, 'management', 'search');
        }
    },
    _url: function (args, api, action) {
        var host = CNVSManager.host;
        if (typeof args.request.host !== 'undefined' && args.request.host != null) {
            host = args.request.host;
        }
        var id = '';
        if (typeof args.id !== 'undefined' && args.id != null) {
            id = '/' + args.id;
        }

        var url = host + '/' + api + id + '/' + action;
        url = Utils.addQueryParamtersToUrl(args.query, url);
        return url;
    },

    _doRequest: function (args, api, action) {


        var url = CNVSManager._url(args, api, action);
        if (args.request.url === true) {

            return url;
        } else {
            var method = 'GET';
            if (typeof args.request.method !== 'undefined' && args.request.method != null) {
                method = args.request.method;
            }
            var async = true;
            if (typeof args.request.async !== 'undefined' && args.request.async != null) {
                console.log("Dentro del async");
                async = args.request.async;
            }
            console.log(url);

            var request = new XMLHttpRequest();
            request.onload = function () {
                console.log("Dentro del onload");
                var contentType = this.getResponseHeader('Content-Type');
                if (contentType === 'application/json') {
                    args.request.success(JSON.parse(this.response), this);
                    console.log("Dentro del success 1");
                } else {
                    console.log("Dentro del success 2");
                    args.request.success(this.response, this);
                }
            };
            request.onerror = function () {
                args.request.error(this);
            };
            request.open(method, url, async);


            if (args.request.headers != null) {
                console.log("Dentro del headers");
                for (var header in args.request.headers) {
                    request.setRequestHeader(header, args.request.headers[header]);
                }
            }
            var body = null;
            if (args.request.body != null) {
                console.log("Dentro del body");
                body = args.request.body;

            }

            if (args.request.responseType != null) {
                console.log("Dentro del responseType");
                request.responseType = args.request.responseType;
            }
            request.send(body);
            return url;
        }
    }
};