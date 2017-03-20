package org.splits;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

@Getter
@Setter
@ToString
@Log4j2
public class Frame{
	private List<Integer> pins = null;
	private boolean strike = false;
	private boolean spare = false;
	private int number;

	public Frame(int number){
		this.number = number;
		pins = new ArrayList<Integer>();
		frames.put(number,this);
	}

	public static Map<Integer,Frame> frames = new HashMap<Integer,Frame>(10);
	public static List<Integer> rolls = new LinkedList<Integer>(); 
	public static Frame nextFrame(Frame f){
		return frames.get(f.getNumber()+1);
	}

	public void addRoll(int pinCount){
		rolls.add(pinCount);

		if ( CollectionUtils.isEmpty(getPins()) ){
			ArrayList<Integer> x = new ArrayList<Integer>(Arrays.asList(pinCount));
			setPins(x);
		} else {
			getPins().add(pinCount);
		}
	}

	public int firstPin(){
		return this.getPins().get(0);
	}

	public static int nextPin(Frame f){
		int score = 0;

		if( f.getNumber() != 10 ) {
			score += nextFrame(f).firstPin();
		}

		return score;
	}

	public static int nextTwoPins(Frame f){
		int score = 0;

		if( f.getNumber() == 10 ) {
		} else if( f.getNumber() == 9 ) {
			Frame next = nextFrame(f);
			score += next.getPins().get(0) + next.getPins().get(1);
		} else {
			Frame next = nextFrame(f);
			score += next.isStrike() ? next.score() + nextFrame(next).firstPin() : next.score();
		}

		return score;
	}

	public int score(){
		int score = 0;
		for(int pin: pins){
			score += pin;
		}
		return score;
	}

	public static int computeScore(){
		int score = 0;

		// --- iterate over each frame computing it's score
		for(int i: frames.keySet()){
			Frame f = frames.get(i);

			score += f.score();

			if ( f.isStrike() ) {
				score += nextTwoPins(f);
			} else if ( f.isSpare() ) {
				score += nextPin(f);
			}

			log.info(String.format("running score frame[%s]: %d", f, score ));
		}

		return score;
	}
}
