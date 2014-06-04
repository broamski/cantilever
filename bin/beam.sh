#!/bin/bash

if [ $# -lt 2 ]; then
	echo "USAGE: beam.sh <jar path> <config path>"
	exit
fi

JAR_PATH=$1
CONFIG_PATH=$2

java -cp $JAR_PATH com.dogeops.cantilever.beam.BeamServer --config $CONFIG_PATH