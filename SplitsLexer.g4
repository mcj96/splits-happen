lexer grammar SplitsLexer;

@header{
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
}


@members {
	public static Logger log = LogManager.getLogger(SplitsLexer.class.getName());
}

WS:		[ \t]+;
NL:		[ \t]*([\r]*[\n])+;
CR:		[\r]+;
SPARE:	[/];
GUTTERBALL:	[0-];
STRIKE:	[xX];
PIN: [1-9];

//===========================================================================================================
// other patterns
//===========================================================================================================

ANY: .;
