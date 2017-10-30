#!/bin/sh

set -e

git_user=$1
git_password=$2

cd documentation
rm -rf gh-pages/
git clone -b gh-pages https://"$git_user":"$git_password"@github.com/gini/gini-switch-sdk-android gh-pages
rm -rf gh-pages/*
cp -a _build/html/ gh-pages/
cd gh-pages/
git add .
git commit -am "updated documentation"
git push
cd ..
rm -rf gh-pages/
