#!/bin/bash
#================================================================================================
# a script to run the parsing program
# assumptions are made about the location of jar files and log4j properties.
#================================================================================================

export SPLITS="$HOME/workspace/splits/"

source $SPLITS/src/scripts/env.sh

#---------------------------------------------------------------------------------
# usage: display the usage and message
#---------------------------------------------------------------------------------

function usage {
	echo "usage: parse.sh -d -i <input file> -o <output file>";
	[[ -n $1 ]] && echo "$1";
	exit 1;
}

#---------------------------------------------------------------------------------
# launchParser:
#	accepts a single arg, the input file to parse
#	clear the output file
#	invoke a subshell the executes the parser and signals the parent shell when done
#	get the pid of the launched parser and store it in a a list of running parsers
#---------------------------------------------------------------------------------

function launchParser {

	inputFile=$1

	if [ ! -z ${parseOut} ]
	then
		# --- clear out previously existing output file
		[[ -e ${parseOut} ]] && >$parseOut

		# --- invoke the parser
		java -cp $CLASSPATH $log4j $parser $inputFile >>$parseOut 2>&1
	else
		java -cp $CLASSPATH $log4j $parser $inputFile 2>&1
	fi

}

#---------------------------------------------------------------------------------
# init: function to set all the variables and check initial conditions
#---------------------------------------------------------------------------------
function init {

	while [ $# -gt 0 ]
	do
		case "$1" in
		-d)
			set -vx
			;;
		-i)
			shift
			parseIn=$1
			;;
		-o)
			shift
			parseOut=$1
			;;
		*)
			usage "bad args: $@"
			;;
		esac

		shift
	done

	[[ -z $parseIn ]] && usage "missing input file to parse"

	#parseOut=${parseOut:-parse.out};

	printf "input is: %s, output is: %s\n" ${parseIn} ${parseOut}

	# --- parse program
	parser=org.splits.TenPinParser
}

#---------------------------------------------------------------------------------
# main
#---------------------------------------------------------------------------------

	init "$@"

	launchParser $parseIn
