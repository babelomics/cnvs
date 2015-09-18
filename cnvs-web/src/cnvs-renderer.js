/*
 * Copyright (c) 2015 Francisco Salavert (DCG-CIPF)
 * Copyright (c) 2015 Asunción Gallego (DCG-CIPF)
 * Copyright (c) 2015 Alejandro Alemán (DCG-CIPF)
 *
 * This file is part of JS Common Libs.
 *
 * JS Common Libs is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * JS Common Libs is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with JS Common Libs. If not, see <http://www.gnu.org/licenses/>.
 */

//any item with chromosome start end
CNVSRenderer.prototype = new Renderer({});

function CNVSRenderer(args) {
    Renderer.call(this, args);
    // Using Underscore 'extend' function to extend and add Backbone Events
    _.extend(this, Backbone.Events);

    this.fontClass = 'ocb-font-roboto ocb-font-size-11';
    this.toolTipfontClass = 'ocb-tooltip-font';

    if (args == null) {
        args = FEATURE_TYPES.undefined;
    }

    if (_.isObject(args)) {
        _.extend(this, args);
    }

    this.on(this.handlers);

    this.histogramHeight = 75;
    this.multiplier = 7;

    //this.maxValue = 10;
};


CNVSRenderer.prototype.render = function (features, args) {
    var _this = this;

    /****/
    var timeId = "write dom " + Utils.randomString(4);
    console.time(timeId);
    console.log(features.length);
    /****/

    if (features.length == 0) {
        return;
    }


    features.sort(function (a, b) {
        return a.start - b.start;
    });

    var start = features[0].start;
    var end = features[features.length - 1].end;

    var histogram_gain = new Array(end - start +1);
    var histogram_loss = new Array(end - start +1);
    var histogram_loh = new Array(end - start +1);
    histogram_gain.fill(0);
    histogram_loss.fill(0);
    histogram_loh.fill(0);

    for (var i = 0; i < features.length; i++) {
        var feature = features[i];

        if (feature.type === "gain") {
            for (var j = feature.start; j <= feature.end; j++) {
                var pos_array = (j - start);
                histogram_gain[pos_array]++;
            }
        } else if (feature.type === "loss") {
            for (var j = feature.start; j <= feature.end; j++) {
                var pos_array = (j - start);
                histogram_loss[pos_array]++;
            }
        } else if (feature.type === "LOH neutral") {
            for (var j = feature.start; j <= feature.end; j++) {
                var pos_array = (j - start);

                histogram_loh[pos_array]++;
            }
        }


    }



var colors = ['blue', 'red', 'green'];
var width = 1 * args.pixelBase;
var histo3 = new Array(histogram_gain, histogram_loss, histogram_loh);

for (var j = 0; j < histo3.length; j++) {
    var histogramaactual = histo3[j];
    var points = '';

    //Antes de que empiece las features
   /* for (i = 0; i < start; i++) {
       // var x = args.pixelPosition + i * args.pixelBase;
        var x = i * args.pixelBase;
        points += (x + (width / 2)).toFixed(1) + "," + (this.histogramHeight ).toFixed(1) + " ";
    }*/
    //Datos donde tengo las features
    for (var i = 0; i < histogramaactual.length ;  i++) {

        //var x = args.pixelPosition + ((i + start) * args.pixelBase);
        var proba = args.pixelBase;
        var proba2 = args.pixelPosition;

        var x = args.pixelPosition +35+ ((i+1)  * args.pixelBase);
        var height = histogramaactual[i] * this.multiplier;
        points += (x + (width / 2)).toFixed(1) + "," + (this.histogramHeight - height).toFixed(1) + " ";
    }
    //Datos posteriores de las features. Mirar si tengo la posicion final donde quiero la region yponer si es así

    //Crear el SVG con cada linea
    if (points !== '') {
        SVG.addChild(args.svgCanvasFeatures, "polyline", {
            "points": points,
            fill: "none",
            "stroke": colors[j],
            "cursor": "pointer"
        });

    }
}


var svgGroup = SVG.create('g');
for (var i = 0, leni = features.length; i < leni; i++) {
    this.draw(features[i], svgGroup, i, args);
}
args.svgCanvasFeatures.appendChild(svgGroup);


/****/
console.timeEnd(timeId);
/****/
}
;

CNVSRenderer.prototype.draw = function (feature, svgGroup, i, args) {
    var _this = this;

    if ('featureType' in feature) {
        _.extend(this, FEATURE_TYPES[feature.featureType]);
    }
    if ('featureClass' in feature) {
        _.extend(this, FEATURE_TYPES[feature.featureClass]);
    }

    //Temporal fix for clinical
    if (args.featureType == 'clinical') {
        if ('clinvarSet' in feature) {
            _.extend(this, FEATURE_TYPES['Clinvar'])
        } else if ('mutationID' in feature) {
            _.extend(this, FEATURE_TYPES['Cosmic'])
        } else {
            _.extend(this, FEATURE_TYPES['GWAS'])
        }
    }


    ////check feature class
    //if (feature.featureClass != null) {//regulatory
    //    _.extend(this, FEATURE_TYPES[feature.featureClass]);
    //} else if (feature.source != null) {//clinical
    //    _.extend(this, FEATURE_TYPES[feature.source]);
    //}

    //get feature render configuration
    var color = _.isFunction(this.color) ? this.color(feature) : this.color;
    var label = _.isFunction(this.label) ? this.label(feature) : this.label;
    var height = _.isFunction(this.height) ? this.height(feature) : this.height;
    var tooltipTitle = _.isFunction(this.tooltipTitle) ? this.tooltipTitle(feature) : this.tooltipTitle;
    var tooltipText = _.isFunction(this.tooltipText) ? this.tooltipText(feature) : this.tooltipText;
    var infoWidgetId = _.isFunction(this.infoWidgetId) ? this.infoWidgetId(feature) : this.infoWidgetId;

    //get feature genomic information
    var start = feature.start;
    var end = feature.end;
    var length = (end - start) + 1;

    //check genomic length
    length = (length < 0) ? Math.abs(length) : length;
    length = (length == 0) ? 1 : length;

    //transform to pixel position
    var width = length * args.pixelBase;

//        var svgLabelWidth = this.getLabelWidth(label, args);
    var svgLabelWidth = label.length * 6.4;

    //calculate x to draw svg rect
    var x = this.getFeatureX(start, args);

    var maxWidth = Math.max(width, 2);
    var textHeight = 0;
    if (args.maxLabelRegionSize > args.regionSize) {
        textHeight = 9;
        maxWidth = Math.max(width, svgLabelWidth);
    }


    var offset = 100;
    var rowY = offset;
    var textY = textHeight + height + offset;
    var rowHeight = textHeight + height + 2;

    while (true) {
        if (!(rowY in args.renderedArea)) {
            args.renderedArea[rowY] = new FeatureBinarySearchTree();
        }
        var foundArea = args.renderedArea[rowY].add({start: x, end: x + maxWidth - 1});

        if (foundArea) {
            var featureGroup = SVG.addChild(svgGroup, "g", {'feature_id': feature.id});
            var rect = SVG.addChild(featureGroup, "rect", {
                'x': x,
                'y': rowY,
                'width': width,
                'height': height,
                'stroke': '#3B0B0B',
                'stroke-width': 1,
                'stroke-opacity': 0.7,
                'fill': color,
                'cursor': 'pointer'
            });
            if (args.maxLabelRegionSize > args.regionSize) {
                var text = SVG.addChild(featureGroup, "text", {
                    'i': i,
                    'x': x,
                    'y': textY,
                    'font-weight': 400,
                    'opacity': null,
                    'fill': 'black',
                    'cursor': 'pointer',
                    'class': this.fontClass
                });
                text.textContent = label;
            }

            if ('tooltipText' in this) {
                $(featureGroup).qtip({
                    content: {text: tooltipText, title: tooltipTitle},
//                        position: {target: "mouse", adjust: {x: 15, y: 0}, effect: false},
                    position: {target: "mouse", adjust: {x: 25, y: 15}},
                    style: {width: true, classes: this.toolTipfontClass + ' ui-tooltip ui-tooltip-shadow'},
                    show: {delay: 300},
                    hide: {delay: 300}
                });
            }

            $(featureGroup).mouseover(function (event) {
                _this.trigger('feature:mouseover', {
                    query: feature[infoWidgetId],
                    feature: feature,
                    featureType: feature.featureType,
                    mouseoverEvent: event
                })
            });

            $(featureGroup).click(function (event) {
                _this.trigger('feature:click', {
                    query: feature[infoWidgetId],
                    feature: feature,
                    featureType: feature.featureType,
                    clickEvent: event
                })
            });
            break;
        }
        rowY += rowHeight;
        textY += rowHeight;
    }

}
