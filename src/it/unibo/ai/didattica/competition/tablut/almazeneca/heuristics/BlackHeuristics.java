package it.unibo.ai.didattica.competition.tablut.almazeneca.heuristics;

import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.domain.State.Pawn;

public class BlackHeuristics {
	
	protected State state;
	private int numBlackPawn;
	private int numWhitePawn;
	private int[] kingPosition;
	
	private static final double WEIGHT_BLACK_PAWN = 7;
	private static final double WEIGHT_WHITE_PAWN = 5;
	private static final double WEIGHT_PAWN_NEAR_KING = 8;
	
	private static final double WHEIGHT_OPEN_WAYS = 30;
	
	private final int[][] rhombus = {
      {1,2}, {1,6},
      {2,1}, {2,7},
      {6,1}, {6,7},
      {7,2}, {7,6}
};

	
	public BlackHeuristics(State state) {
		this.state = state;
	}



	public double evaluate(State state) {
		
		this.numBlackPawn=state.getNumberOf(Pawn.BLACK);
		this.numWhitePawn=state.getNumberOf(Pawn.WHITE);
		
		double  pawnsNearKing = (double)  checkNearPawns(state, this.kingPosition, State.Pawn.BLACK);
		
		
		int openWays=this.checkOpenWays();
		
		//valore valutazione
		double result=0;
		result+=WEIGHT_BLACK_PAWN*this.numBlackPawn;
		result-=WEIGHT_WHITE_PAWN*this.numWhitePawn;
		
		result+=WEIGHT_PAWN_NEAR_KING*(pawnsNearKing/this.pawnToEatKink());
		
		result-=WHEIGHT_OPEN_WAYS*openWays;
		
		if (this.kingHasOpenWays())
			result-=10;
		
		if(state.getTurn().equals(State.Turn.WHITEWIN))
			result-=100;
		
		if(state.getTurn().equals(State.Turn.WHITEWIN))
			result+=100;
		
		return result;
		
		
	}
	
	
	//get the position of the king in the board
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
	
	public int pawnToEatKink() {
		if (this.kingPosition[0]==4 && this.kingPosition[1]==4)
			return 4;
		if (this.kingIsNearThrone())
			return 3;
		return 2;
	}
	
	
	public int checkNearPawns(State state, int[] position, State.Pawn target){
        int count=0;
        //GET TURN
        State.Pawn[][] board = state.getBoard();
        String stringTarget=target.toString();
        if(board[position[0]-1][position[1]].equalsPawn(stringTarget))
            count++;
        if(board[position[0]+1][position[1]].equalsPawn(stringTarget))
            count++;
        if(board[position[0]][position[1]-1].equalsPawn(stringTarget))
            count++;
        if(board[position[0]][position[1]+1].equalsPawn(stringTarget))
            count++;
        return count;
    }
	
	public boolean kingIsNearThrone(){
		
		final int [][] nearThrone= {{3, 3}, {3, 4}, {3, 5},
				{4, 3}, {4, 4}, {4, 5},
				{5, 3}, {5, 4}, {5, 5}};
		
		for (int pos[] : nearThrone) {
			if(this.kingPosition[0]==pos[0] && this.kingPosition[1]==pos[1] )
				return true;
		}
		return false;
	}
	
	public boolean isCitadel(int x, int y) {
		final int[][] cittadelle= {{0, 4}, {0,5}, {0, 6}, {1, 5}, {3, 8}, {4, 8}, {5, 8}, {4, 7}, 
				{8, 5}, {8, 4}, {8, 3}, {7, 4}, {5, 0}, {4, 0}, {3, 0}, {4, 1}};
		
		for (int cit[] : cittadelle)
			if (x==cit[0] && y==cit[1])
				return true;
		return false;
	
	}
	
	
	
	public boolean kingHasOpenWays() {
		
		//controllo se il re è vicino al trono
		if (this.kingIsNearThrone())
			return false;
		
		int riga=this.kingPosition[0];
		int colonna= this.kingPosition[1];
		
		State.Pawn board[][]=this.state.getBoard();

		//controllo a destra
		for (int i=colonna; i<board[colonna].length; i++)
			if (board[colonna][i].equals(State.Pawn.EMPTY) && ! isCitadel(colonna, i))
				return true;
		//controllo a sinistra
		for (int i=colonna; i>=0; i--)
			if (board[colonna][i].equals(State.Pawn.EMPTY) && ! isCitadel(colonna, i))
				return true;
		
		//controllo sopra
		
		for (int i=riga; i<board[colonna].length; i++)
			if (board[riga][i].equals(State.Pawn.EMPTY) && ! isCitadel(riga, i))
				return true;
		//controllo sotto
		for (int i=riga; i>=0; i--)
			if (board[riga][i].equals(State.Pawn.EMPTY) && ! isCitadel(riga, i))
				return true;
		return false;
	}
		
		
		
	/*
	public boolean isFreeRow(int r) {
		State.Pawn board[][]=this.state.getBoard();
		for (int i=0; i<board[r].length; i++) {
			if (!(board[r][i].equals(State.Pawn.EMPTY)))
				return false;
						
		}
		return true;
	}
	
	
	public boolean isFreeColumn(int c) {
		State.Pawn board[][]=this.state.getBoard();
		for (int i=0; i<board[c].length; i++) {
			if (!(board[i][c].equals(State.Pawn.EMPTY)))
				return false;
						
		}
		return true;
	}
	*/
	
	public int checkOpenWays() {
		int count=8; 
		
		for (int pos[] : rhombus) {
			if (state.getPawn(pos[0], pos[1]).equalsPawn(State.Pawn.BLACK.toString())) {
                count--;
            }
        }	
		
		return count;
	}


//	public double evalKingPosition() {
//		if(kingPosition + [0][1]) {
//			
//		}
//	}
}
