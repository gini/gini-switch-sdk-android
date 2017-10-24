#!/bin/sh

cd documentation
virtualenv ./virtualenv
source virtualenv/bin/activate
pip install -r requirements.txt
make clean
make html
