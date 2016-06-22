#!/bin/sh
NAME="cnvs"
ELEMENT="cnvs-element"
BP=build

rm -rf $BP
mkdir -p $BP
mkdir -p $BP/fontawesome
mkdir -p $BP/webcomponentsjs

vulcanize --inline-scripts --inline-css --strip-comments $ELEMENT.html > $BP/$ELEMENT.html

cp -r $NAME-index.html $BP/index.html
cp -r conf/ $BP/
cp -r images/ $BP/
cp -r bower_components/stevia-elements/fonts/ $BP/
cp -r bower_components/stevia-elements/css/ $BP/
cp -r bower_components/fontawesome/css $BP/fontawesome/
cp -r bower_components/fontawesome/fonts $BP/fontawesome/
cp -r bower_components/webcomponentsjs/*.min.js $BP/webcomponentsjs/

# GV deps
cp -r bower_components/underscore $BP/
cp -r bower_components/backbone $BP/
cp -r bower_components/jquery $BP/
cp -r bower_components/qtip2 $BP/
cp -r bower_components/uri.js $BP/
cp -r bower_components/uri.js $BP/
mkdir -p $BP/bower_components/jsorolla
cp -r bower_components/jsorolla/styles $BP/bower_components/jsorolla

#
# fix index.html css paths
#
sed -i s@'bower_components/stevia-elements/'@@g $BP/index.html
sed -i s@'bower_components/'@@g $BP/index.html
## end fix paths
