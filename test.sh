#!/bin/bash

source $HOME/.bashrc

export SPLITS="$HOME/workspace/splits"

source $SPLITS/src/scripts/env.sh

cd $SPLITS/target

[ ! -e SplitsLexer.g4  ] && ln -s ../src/main/antlr4/SplitsLexer.g4 SplitsLexer.g4
[ ! -e SplitsParser.g4 ] && ln -s ../src/main/antlr4/SplitsParser.g4 SplitsParser.g4

export antlr4="java -jar $antlr4_jar"

# --- compile the antlr lexer and grammar into java source code
$antlr4 *g4 || (echo "antlr4 grammar failed" && exit 1);

# --- compile all Splits java sources(including the antlr generated sources)
javac -cp $CLASSPATH *.java 
rc=$?

[[ $rc != 0 ]] && echo "antlr4 grammar compile failed" && exit 1;

# --- establish a default test file if none specified
testFile=${1:-../test/data/games};

printf "test file %s\n" "$testFile"

# --- execute the test
testrig=org.antlr.v4.runtime.misc.TestRig 
#testrig=org.antlr.v4.gui.TestRig
EntryPoint=${EntryPoint:-games} 
java -cp $CLASSPATH $log4j $testrig Splits $EntryPoint -tokens -trace -gui <$testFile > test.out 2>parse.err
