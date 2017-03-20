package org.splits;

/*==========================================================================================
 * This module implements visitors for the rules in the SplitsParser grammar, that produce
 * the model data for each game that then is used to compute the score as the parse is
 * complete.
 *========================================================================================== 
 */

import lombok.Getter;
//import lombok.extern.log4j.Log4j2;

import main.antlr4.SplitsParser.FrameContext;
import main.antlr4.SplitsParser.GameContext;
import main.antlr4.SplitsParser.PinContext;
import main.antlr4.SplitsParser.SpareContext;
import main.antlr4.SplitsParser.StrikeContext;
import main.antlr4.SplitsParser.TenthFrameContext;
import main.antlr4.SplitsParserBaseVisitor;

/**
 * @author mcjohnson
 *
 */
//@Log4j2
@Getter
public class SplitsVisitor extends SplitsParserBaseVisitor<Void>{

	int score = 0;
	int lastPin = 0;
	int frameCnt = 0;
	Frame currentFrame = null;

	/**
	 * 
	 */
	public SplitsVisitor() {
		super();
		score = 0;
	}

	/* (non-Javadoc)
	 * @see main.antlr4.SplitsParserBaseVisitor#visitGame(main.antlr4.SplitsParser.GameContext)
	 * 
	 * tell the parser to visit each child rule, at the end of the game parse compute the final score
	 */
	@Override
	public Void visitGame(GameContext ctx){
		visitChildren(ctx);
		score = Frame.computeScore();
		return null;
	}

	/* (non-Javadoc)
	 * @see main.antlr4.SplitsParserBaseVisitor#visitFrame(main.antlr4.SplitsParser.FrameContext)
	 * 
	 * create a new frame
	 */
	@Override
	public Void visitFrame(FrameContext ctx){
		frameCnt++;
		currentFrame = new Frame(frameCnt);
		visitChildren(ctx);
//		log.info(String.format("frame[%s]: %s", currentFrame, ctx.getText()));
		return null;
	}

	/* (non-Javadoc)
	 * @see main.antlr4.SplitsParserBaseVisitor#visitTenthFrame(main.antlr4.SplitsParser.TenthFrameContext)
	 * 
	 * 
	 * create the final frame
	 */
	@Override
	public Void visitTenthFrame(TenthFrameContext ctx){
		frameCnt++;
		currentFrame = new Frame(frameCnt);
		visitChildren(ctx);
//		log.info(String.format("frame[%s]: %s", currentFrame, ctx.getText()));
		return null;
	}

	/* (non-Javadoc)
	 * @see main.antlr4.SplitsParserBaseVisitor#visitStrike(main.antlr4.SplitsParser.StrikeContext)
	 * 
	 * this frame will only have a single pin count.
	 */
	@Override
	public Void visitStrike(StrikeContext ctx){
		visitChildren(ctx);
		currentFrame.setStrike(true);
		currentFrame.addRoll(10);
//		log.info(String.format("frame[%s]: pin: %s", currentFrame, ctx.getText()));
		return null;
	}

	/* (non-Javadoc)
	 * @see main.antlr4.SplitsParserBaseVisitor#visitSpare(main.antlr4.SplitsParser.SpareContext)
	 * 
	 * this frame will have a pin count of two(unless 10th) the first roll has already been added,
	 * compute the second roll's pin count
	 * set the spare flag
	 */
	@Override
	public Void visitSpare(SpareContext ctx){
		visitChildren(ctx);
		currentFrame.setSpare(true);
		lastPin = 10 - lastPin;
		currentFrame.addRoll(lastPin);
//		log.info(String.format("frame[%s]: pin: %s", currentFrame, ctx.getText()));
		return null;
	}

	/* (non-Javadoc)
	 * @see main.antlr4.SplitsParserBaseVisitor#visitPin(main.antlr4.SplitsParser.PinContext)
	 * 
	 * this is always visited for each roll that has a numeric pin count or gutterball.
	 */
	@Override
	public Void visitPin(PinContext ctx){
		visitChildren(ctx);
		lastPin = ctx.GUTTERBALL() != null ? 0 : Integer.parseInt(ctx.getText());
		currentFrame.addRoll(lastPin);
//		log.info(String.format("frame[%s]: pin: %s", currentFrame, ctx.getText()));
		return null;
	}
}
