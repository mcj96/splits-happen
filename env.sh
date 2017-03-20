#!/bin/bash
#================================================================================================
# a script to set common path information used by other scripts
# assumptions are made about the location of jar files and log4j properties.
#
# NB: SPLITS must ALREADY be defined by the invoking script
#================================================================================================

export repo="$HOME/.m2/repository"
export apache="$repo/org/apache"

export log4j_dir="$apache/logging/log4j"
export log4j_jar="$log4j_dir/log4j-core/2.5/log4j-core-2.5.jar:$log4j_dir/log4j-api/2.5/log4j-api-2.5.jar"

export antlr4_jar="$repo/org/antlr/antlr4/4.5/antlr4-4.5.jar"

export collectionUtils="$apache/commons/commons-collections4/4.0/commons-collections4-4.0.jar"
export commonsIo="$repo/commons-io/commons-io/2.4/commons-io-2.4.jar"
export commonsCodec="$repo/commons-codec/commons-codec/1.10/commons-codec-1.10.jar"
export commonsLang="$apache/commons/commons-lang3/3.4/commons-lang3-3.4.jar"
export apacheLibs="$collectionUtils:$commonsIo:$commonsCodec:$commonsLang"

export CLASSPATH="$SPLITS/target/splits-SPLITS-1.0-PARSER-SNAPSHOT.jar:$apacheLibs:$antlr4_jar:$log4j_jar:."

export testDataDir=$SPLITS/test/data/;

export log4j="-Dlog4j.configurationFile=$HOME/tmp/log4j.properties.xml";
