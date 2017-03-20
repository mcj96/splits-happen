package org.splits;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.Getter;
import main.antlr4.SplitsLexer;
import main.antlr4.SplitsParser;

public class TenPinParser {

	protected static ParseTree tree;
	protected static final Logger log = LogManager.getLogger(TenPinParser.class);

	@Getter
	protected static int errorCount = 0;

	public boolean hasErrors() { return errorCount > 0; }

	protected SplitsParser parser = null;
	public static class VerboseListener extends BaseErrorListener {

		public VerboseListener() {
		}

		@Override
		public void syntaxError(Recognizer<?, ?> recognizer,
								Object offendingSymbol,
								int line, int charPositionInLine,
								String msg,
								RecognitionException e)
			{
				List<String> stack = ((Parser)recognizer).getRuleInvocationStack();
				Collections.reverse(stack);
				log.error("rule stack: " + stack);
				log.error("line " + line + ":" + charPositionInLine + " at " + offendingSymbol + ": " + msg);
				errorCount++;
			}
	}

	public TenPinParser(){
	}

	/**
	 * @param message
	 */
	public void parse(String game){

		ByteArrayInputStream bais = new ByteArrayInputStream(game.getBytes());

		ANTLRInputStream inStr = null;

		try {
			inStr = new ANTLRInputStream(bais);
		} catch (IOException e) {
			// --- this will NEVER happen because there is no file IO from a string
			//     but this is present to satisfy the interface to input streams
			e.printStackTrace();
		}

		errorCount = 0;

		// create a lexer that reads the input CharStream
		SplitsLexer lexer = new SplitsLexer(inStr);

		// create a stream of tokens produced by the lexer
		CommonTokenStream tokens = new CommonTokenStream(lexer);

		// create a parser that reads the token stream
		parser = new SplitsParser(tokens);
		parser.removeErrorListeners();
		parser.addErrorListener(new VerboseListener());

		tree = parser.game(); // begin parsing at the rule "game"

		SplitsVisitor visitor = new SplitsVisitor();
		visitor.visit(tree);
		log.info(String.format("game %s ,score is: %d\n", game, visitor.getScore()));
	}

	public static String fileToString(String filename){
		String messages = null;
		try {
			messages = FileUtils.readFileToString(new File(filename));
		} catch (IOException e) {
			System.err.println(String.format("could not read file %s because %s", filename, e.getMessage()));
			System.exit(1);
		}
		return messages;
	}

	public static void main(String[] args) {
		String[] games = fileToString(args[0]).split("\n");
		TenPinParser p = new TenPinParser();
		for(String game: games){
			p.parse(game);
		}
	}
}