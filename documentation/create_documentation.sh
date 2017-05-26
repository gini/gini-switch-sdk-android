#!/bin/sh

rm -rf gh-pages/
git clone -b gh-pages git@github.com:gini/gini-tariff-sdk-android gh-pages
make clean
make html
rm -rf gh-pages/*
cp -a _build/html/ gh-pages/
cd gh-pages/
git commit -am "updated documentation"
git push
rm -rf gh-pages/
