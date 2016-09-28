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
                async = args.request.async;
            }

            console.log(url);
            var request = new XMLHttpRequest();
            request.onload = function () {
                var contentType = this.getResponseHeader('Content-Type');
                if (contentType === 'application/json') {
                    args.request.success(JSON.parse(this.response), this);
                } else {
                    args.request.success(this.response, this);
                }
            };
            request.onerror = function () {
                args.request.error(this);
            };
            request.open(method, url, async);
            request.send();
            return url;
        }
    }
};