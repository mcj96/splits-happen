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

/**
 * A class to hold info relevant to each frame of a ten pin game
 *
 */
@Getter
@Setter
@ToString
@Log4j2
public class Frame{
	private List<Integer> pins = null;
	private boolean strike = false;
	private boolean spare = false;
	private int number;

	/**
	 * CTOR must set the frame number and init the pins,
	 * add the frame to the map of frames
	 * @param number
	 */
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

	/**
	 * for each roll of the bowling ball, which occurs in the current frame
	 * say how many pins were knocked down.
	 * @param pinCount
	 */
	public void addRoll(int pinCount){
		rolls.add(pinCount);

		if ( CollectionUtils.isEmpty(getPins()) ){
			ArrayList<Integer> x = new ArrayList<Integer>(Arrays.asList(pinCount));
			setPins(x);
		} else {
			getPins().add(pinCount);
		}
	}

	/**
	 * @return pin count of the first roll in this frame
	 */
	public int firstPin(){
		return this.getPins().get(0);
	}

	/**
	 * @param f - current frame
	 * @return first pin of next frame(used in spares) 
	 */
	public static int nextPin(Frame f){
		int score = 0;

		if( f.getNumber() != 10 ) {
			score += nextFrame(f).firstPin();
		}

		return score;
	}

	/**
	 * A strike was rolled...get the next two pins... 
	 * if this strike is in the final frame disregard because we have already computed that frames total
	 * if this is the 9th frame next 2 pins are from the 10th frame regardless if strike is thrown
	 * any other frame ...if next pin is strike ...need to get it's next frame
	 * 
	 * wish I had used linked list instead ...future improvement
	 * 
	 * @param f - current frame
	 * @return
	 */
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

	/**
	 * @return total pins in this frame
	 */
	public int score(){
		int score = 0;
		for(int pin: pins){
			score += pin;
		}
		return score;
	}

	/**
	 * @return the total score for the game by iterating over each frame
	 * if spares or strikes occur, then we must peek into the next frame for pins
	 * 
	 * running score was computed as a debug ...although excluded from requirements
	 */
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
