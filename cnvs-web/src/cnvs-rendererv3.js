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

    this.histogramHeight = 50;
    this.multiplier = 7;

    //this.maxValue = 10;
};

CNVSRenderer.prototype.init = function () {
    //console.log(this.track.main);

    //var width = 5 * args.pixelBase;

    var ejebenign = SVG.addChild(this.track.main, "rect", {
        'x': 0,
        'y': 0,
        'width': 5,
        'height': 50,
        'stroke': '#3B0B0B',
        'stroke-width': 1,
        'stroke-opacity': 0.7,
        'fill': "blue",
        'cursor': 'pointer'
    });

    SVG.addChild(this.track.main, "line", {
        "x1": 0,
        "y1": 50,
        "x2": this.track.width,
        "y2": 50,
        "fill": "none",
        "stroke": "black"
    });

    var ejepatho = SVG.addChild(this.track.main, "rect", {
        'x': 0,
        'y': 60,
        'width': 5,
        'height': 50,
        'stroke': '#3B0B0B',
        'stroke-width': 1,
        'stroke-opacity': 0.7,
        'fill': "red",
        'cursor': 'pointer'
    });

    SVG.addChild(this.track.main, "line", {
        "x1": 0,
        "y1": 110,
        "x2": this.track.width,
        "y2": 110,
        "fill": "none",
        "stroke": "black"
    });

};

CNVSRenderer.prototype.render = function (features, args) {

    /*Cuando hay un cambio se llama al render solo una vez, este tiene todas las regiones

     /****/
    var timeId = "write dom " + Utils.randomString(4);
    //console.time(timeId);
    //console.log(features.length);
    /****/

    /**/
    /**/
    var histoGroup = SVG.create('g');


    var lastRegion = null;
    for (var i = 0, leni = args.cacheItems.length; i < leni; i++) {
        //console.log("cache items " + args.cacheItems.length);
        //console.log("cache items " + args.cacheItems);
        //var ultimospuntos = new Array(3);

        //this.drawHistogram(args.cacheItems[i], histoGroup, i, args, ultimospuntos);

        //console.log(args.cacheItems[i])
        this.drawHistogram(args.cacheItems[i], histoGroup, i, args);
    }
    args.svgCanvasFeatures.appendChild(histoGroup);
    /**/
    /**/

    var svgGroup = SVG.create('g');
    for (var i = 0, leni = features.length; i < leni; i++) {
        this.draw(features[i], svgGroup, i, args);
    }
    args.svgCanvasFeatures.appendChild(svgGroup);


    /****/
    //console.timeEnd(timeId);
    /****/
};

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


    var offset = 140;
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
                    position: {target: "mouse", adjust: {x: 15, y: 0}, effect: false, viewport: $(window)},
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

};


CNVSRenderer.prototype.drawHistogram = function (cacheItem, svgGroup, i, args) {
    console.log("histograma")
    var features = cacheItem.value;
    var region = cacheItem.region;
    var start = region.start;
    var end = region.end;

    var colors = ['blue', 'red'];


    //Creo los tres histogramas que contienen los datos en coordenadas genómicas
    var histogram_benign = new Array(args.width);
    var histogram_pathogenic = new Array(args.width);
    var histogram_total = new Array(args.width);


    console.time("fill")
    histogram_benign.fill(0);
    histogram_pathogenic.fill(0);
    histogram_total.fill(0);

    console.timeEnd("fill")
    var ajuste = Math.floor(1/args.pixelBase);
    var width = 1 * args.pixelBase;

    //Si no hay datos solo pinto los puntos inicial y final de cada histograma
    /*  if (features.length == 0) {
     var xini = this.getFeatureX(start, args);
     var xfin = this.getFeatureX(start, args);
     var height = (this.histogramHeight).toFixed(1);
     var points =(xini + (width / 2)).toFixed(1) + "," + height+ " " +(xfin + (width / 2)).toFixed(1) + "," + height;

     for ( var i=0; i< colors.length; i++) {
     if (points !== '') {
     SVG.addChild(svgGroup, "polyline", {
     "points": points,
     "fill": "none",
     "stroke": colors[i],
     "cursor": "pointer"
     });

     }
     }

     }else {*/

    console.time("sort")
    //Ordenar las features
    features.sort(function (a, b) {
        return a.start - b.start;
    });

    console.timeEnd("sort");

    console.time("arrays")

    //console.log(features.length);
    //Crear los arrays con los datos a pintar
    for (var i = 0; i < features.length; i++) {
        var feature = features[i];
        var inicio;
        var fin;
        if (feature.start < start) {
            inicio = start;
        } else {
            inicio = feature.start;
        }
        if (feature.end > end) {
            fin = end;
        } else {
            fin = feature.end
        }
        if (feature.clinicalSig === "benign") {
            //   for (var j = feature.start; j <= feature.end, j <= end; j++) {
            for (var j = inicio; j <= fin; j++) {
                var pos_array = Math.floor((j - start)*ajuste);
                histogram_benign[pos_array]++;
                histogram_total[pos_array]++;
            }
        } else if (feature.clinicalSig === "definitively pathogenic" || feature.clinicalSig === "definitely pathogenic") {
            for (var j = inicio; j <= fin; j++) {
                //for (var j = feature.start; j <= feature.end,j <= end; j++) {
                var pos_array = Math.floor((j - start)*ajuste);
                histogram_pathogenic[pos_array]++;
                histogram_total[pos_array]++;
            }
        }

    }
    console.timeEnd("arrays")

    var histo3 = new Array(histogram_benign, histogram_pathogenic);


    console.time("pintar")
    //Pintar los datos
    for (var j = 0; j < 2; j++) {
        var histogramaactual = histo3[j];


        var pointsArray = [];
        //if (ultimospuntos[j] != null) {
        //    pointsArray.push(ultimospuntos[j]);
        //}else{
        //    console.log("paso por aqui!");
        //}

        var ultimovalorx = 0;
        var ultimovalory = 0;
        var yAdjust = 60 * j;
        //Datos donde tengo las features
        var numpoints = 0;


        //Añado el primer punto a la linea (abajo)

        var x = this.getFeatureX(start, args) - args.pixelBase / 2;
        //var height = histogramaactual[0] * this.multiplier ;
        var height;
        if (histogram_total[0] != 0) {
            height = (histogramaactual[0] / histogram_total[0]) * this.histogramHeight;
        } else {
            height = 0;
        }
        pointsArray.push((x + (width / 2)).toFixed(1) + "," + (this.histogramHeight + yAdjust).toFixed(1));

        //Añado el primer punto a la linea (con la altura que toca)
        pointsArray.push((x + (width / 2)).toFixed(1) + "," + (this.histogramHeight - height + yAdjust).toFixed(1));

        ultimovalory = this.histogramHeight - height + yAdjust;
        ultimovalorx = (x + (width / 2)).toFixed(1);

        for (var i = 1; i < histogramaactual.length - 1; i++) {
            x = this.getFeatureX(i + start, args);

            if (histogram_total[i] != 0) {
                height = (histogramaactual[i] / histogram_total[i]) * this.histogramHeight;
            } else {
                height = 0;
            }

            if (ultimovalory != (this.histogramHeight - height + yAdjust)) {

                pointsArray.push(ultimovalorx + "," + ultimovalory.toFixed(1));
                pointsArray.push((x + (width / 2)).toFixed(1) + "," + (this.histogramHeight - height + yAdjust).toFixed(1));

            } else {
                numpoints++;
            }
            ultimovalory = this.histogramHeight - height + yAdjust;
            ultimovalorx = (x + (width / 2)).toFixed(1);

            // console.log("el del histograma es"+(this.histogramHeight - height).toFixed(1));
        }
        //pinto el ultimo valor
        var x = this.getFeatureX(histogramaactual.length - 1 + start, args);
        if ((histogram_total[histogramaactual.length - 1]) != 0) {
            height = (histogramaactual[histogramaactual.length - 1] / histogram_total[histogramaactual.length - 1]) * this.histogramHeight;
        } else {
            height = 0;
        }
        pointsArray.push((x + (width / 2)).toFixed(1) + "," + (this.histogramHeight - height + yAdjust).toFixed(1));

        //Último valor suelo
        pointsArray.push((x + (width / 2)).toFixed(1) + "," + (this.histogramHeight + yAdjust).toFixed(1));

        //guardo este ultimo punto para el siguiente histograma
        //ultimospuntos[j] = (x + (width / 2)).toFixed(1) + "," + (this.histogramHeight - height).toFixed(1);


        var points = pointsArray.join(" ");
       // var no_dup = stv.utils.removeDuplicates(pointsArray);
        //console.log(no_dup.length);
        console.log(pointsArray.length);
        //console.log(numpoints);

        //Añado al grupo de histogramas las lineas con los puntos calculados
        if (points !== '') {
            SVG.addChild(svgGroup, "polyline", {
                "points": points,
                "fill": colors[j],
                "stroke": colors[j],
                "cursor": "pointer"
            });

        }
    }
    console.timeEnd("pintar");

    //}
};
//Antes de que empiece las features
/*   for (i = 0; i < start; i++) {
 // var x = args.pixelPosition + i * args.pixelBase;
 var x = i * args.pixelBase;
 points += (x + (width / 2)).toFixed(1) + "," + (this.histogramHeight ).toFixed(1) + " ";
 }*/