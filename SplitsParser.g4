parser grammar SplitsParser;

options {tokenVocab=SplitsLexer;}

@header{
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
}

@members {
	public int lineNo;
	public static Logger log = LogManager.getLogger(SplitsParser.class.getName());
}

games: game+;

game:
	frame frame frame
	frame frame frame
	frame frame frame
	tenthFrame
;

frame: strike | spare | pin pin;

tenthFrame:
	 (strike ( strike (strike|pin)| pin (strike |pin) ))
	|(spare pin)
	|(pin pin)
;

strike: STRIKE;

spare: pin SPARE;

pin: PIN | GUTTERBALL;
