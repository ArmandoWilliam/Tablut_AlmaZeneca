package it.unibo.ai.didattica.competition.tablut.almazeneca.heuristics;

import it.unibo.ai.didattica.competition.tablut.domain.State;

public class BlackHeuristics {
	
	protected State state;
	private int numBlackPawn;
	private int numWhitePawn;
	private int[][] kingPosition;
	
	private static final double WEIGHT_BLACK_PAWN = 7;
	private static final double WEIGHT_WHITE_PAWN = 5;
	private static final double WEIGHT_KING_POS = 8;
	
	public BlackHeuristics(State state) {
		this.state = state;
	}



	public double evaluate(State state) {
		
		return numBlackPawn;
		
	}
}
