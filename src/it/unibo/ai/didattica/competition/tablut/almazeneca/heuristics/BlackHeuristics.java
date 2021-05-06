package it.unibo.ai.didattica.competition.tablut.almazeneca.heuristics;

import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.domain.State.Pawn;

public class BlackHeuristics extends Heuristics {
	
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
		super(state);
	}

	public double evaluate(State state) {
		
		this.numBlackPawn=state.getNumberOf(Pawn.BLACK);
		this.numWhitePawn=state.getNumberOf(Pawn.WHITE);
		
		double  pawnsNearKing = (double)  checkNearPawns(state, this.kingPosition, State.Pawn.BLACK);
		
		
		int openWays=this.checkOpenWays();
		
		//evaluation value
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
	
	/**
	 * @return the position of the king in the board
	 */
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
	
	/**
	 * 
	 * @return a specific type of Pawn near a specific position
	 * 
	 * @param state: the state of the board, position: the specified position we want to analyze, target: the specific pawn type we want to see if it's near the position
	 *  
	 */
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
	
	/**
	 * 
	 * @return true if king is in the squares near the throne, false if not
	 * 
	 */
	public boolean kingIsNearThrone(){
		
		//structure that represent the squares near the throne
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
		//structure that represent citadels squares
		final int[][] citadels= {{0, 4}, {0,5}, {0, 6}, {1, 5}, {3, 8}, {4, 8}, {5, 8}, {4, 7}, 
				{8, 5}, {8, 4}, {8, 3}, {7, 4}, {5, 0}, {4, 0}, {3, 0}, {4, 1}};
		
		for (int cit[] : citadels)
			if (x==cit[0] && y==cit[1])
				return true;
		return false;
	
	}
	
	
	/**
	 * 
	 * @return true if king has an open way to escape from the board, false if not
	 * 
	 */
	public boolean kingHasOpenWays() {
		
		//check if the king is near the throne
		if (this.kingIsNearThrone())
			return false;
		
		int row=this.kingPosition[0];
		int col= this.kingPosition[1];
		
		State.Pawn board[][]=this.state.getBoard();

		//control on the right
		for (int i=col; i<board[col].length; i++)
			if (board[col][i].equals(State.Pawn.EMPTY) && ! isCitadel(col, i))
				return true;
		
		//control on the left
		for (int i=col; i>=0; i--)
			if (board[col][i].equals(State.Pawn.EMPTY) && ! isCitadel(col, i))
				return true;
		
		//control above
		for (int i=row; i<board[col].length; i++)
			if (board[row][i].equals(State.Pawn.EMPTY) && ! isCitadel(row, i))
				return true;
		
		//control below
		for (int i=row; i>=0; i--)
			if (board[row][i].equals(State.Pawn.EMPTY) && ! isCitadel(row, i))
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
	
	/**
	 * 
	 * @return count how many open possible ways there are not closed by the rhombus strategy
	 * 
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
