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
		
		//valore valutazione
		double result=0;
		result+= WEIGHT_BLACK_PAWN*this.numBlackPawn;
		result+=WEIGHT_WHITE_PAWN*this.numWhitePawn;
//		result+=WEIGHT_KING_POS*this.kingPosition;
		
		return result;
		
		
	}
	
	
	
	public int[] getKingPosition() {
		
		int[] kingPosition = {};
		
		for(int i=0; i<9; i++) {
			for(int z=0; z<9; z++) {
				if(state.getBox(i, z).equals("K")) {
					kingPosition[0]=i;
					kingPosition[1]=z;
					return kingPosition;
				}
			}
		}
		return kingPosition;
	}



//	public double evalKingPosition() {
//		if(kingPosition + [0][1]) {
//			
//		}
//	}
}
