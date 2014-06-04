#!/bin/bash

if [ $# -lt 2 ]; then
	echo "USAGE: truss.sh <jar path> <config path>"
	exit
fi

JAR_PATH=$1
CONFIG_PATH=$2

java -cp $JAR_PATH com.dogeops.cantilever.truss.TrussClient --config $CONFIG_PATH