package org.splits;

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

	@Override
	public Void visitGame(GameContext ctx){
		visitChildren(ctx);
		score = Frame.computeScore();
		return null;
	}

	@Override
	public Void visitFrame(FrameContext ctx){
		frameCnt++;
		currentFrame = new Frame(frameCnt);
		visitChildren(ctx);
//		log.info(String.format("frame[%s]: %s", currentFrame, ctx.getText()));
		return null;
	}

	@Override
	public Void visitTenthFrame(TenthFrameContext ctx){
		frameCnt++;
		currentFrame = new Frame(frameCnt);
		visitChildren(ctx);
//		log.info(String.format("frame[%s]: %s", currentFrame, ctx.getText()));
		return null;
	}

	@Override
	public Void visitStrike(StrikeContext ctx){
		visitChildren(ctx);
		currentFrame.setStrike(true);
		currentFrame.addRoll(10);
//		log.info(String.format("frame[%s]: pin: %s", currentFrame, ctx.getText()));
		return null;
	}

	@Override
	public Void visitSpare(SpareContext ctx){
		visitChildren(ctx);
		currentFrame.setSpare(true);
		lastPin = 10 - lastPin;
		currentFrame.addRoll(lastPin);
//		log.info(String.format("frame[%s]: pin: %s", currentFrame, ctx.getText()));
		return null;
	}

	@Override
	public Void visitPin(PinContext ctx){
		visitChildren(ctx);
		lastPin = ctx.GUTTERBALL() != null ? 0 : Integer.parseInt(ctx.getText());
		currentFrame.addRoll(lastPin);
//		log.info(String.format("frame[%s]: pin: %s", currentFrame, ctx.getText()));
		return null;
	}
}
