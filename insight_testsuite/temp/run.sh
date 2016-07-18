#!/usr/bin/env bash

# example of the run script for running the rolling_median calculation with a python file, 
# but could be replaced with similar files from any major language

# I'll execute my programs, with the input directory venmo_input and output the files in the directory venmo_output
#python ./src/rolling_median.py ./venmo_input/venmo-trans.txt ./venmo_output/output.txt

WORKING_DIR=$PWD

java -cp $WORKING_DIR/../../lib/javax.json-1.0.4.jar:$WORKING_DIR/../../bin MedianDegree ./venmo_input/venmo-trans.txt ./venmo_output/output.txt

