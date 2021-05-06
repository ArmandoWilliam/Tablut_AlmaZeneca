package it.unibo.ai.didattica.competition.tablut.almazeneca.heuristics;

import it.unibo.ai.didattica.competition.tablut.domain.GameAshtonTablut;
import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.domain.State.Pawn;

public class BlackHeuristics extends Heuristics{

	
	
	private int numBlackPawn;
	private int numWhitePawn;
	private int[] kingPosition;
	private State.Pawn[][] board;
	
	private int freeWaysOnQuadrant[];
	
	private static final double WEIGHT_BLACK_PAWN = 15;
	private static final double WEIGHT_WHITE_PAWN_EATEN = 18;
	private static final double WEIGHT_PAWN_NEAR_KING = 20;
	

	//bidimensional array that represent the rhombus strategy
//	private final int[][] rhombus = {
//									{1,2}, {1,6},
//											{2,1}, {2,7},
//													{6,1}, {6,7},
//															{7,2}, {7,6}
//	};
	
	private static final double WHEIGHT_OPEN_WAYS =15;

	public BlackHeuristics(State state) {
		super(state);
	}


	public double evaluateState() {
		
		
		this.board=this.state.getBoard();
		this.numBlackPawn=state.getNumberOf(Pawn.BLACK);
		this.numWhitePawn=state.getNumberOf(Pawn.WHITE);
		
		this.kingPosition=this.getKingPosition();
		double bonusEatWhite=1;
		
		
		
		double  pawnsNearKing =0;
		int[] nearPawn=checkNearPawns(State.Pawn.BLACK);
		for (int i=0; i<4; i++)
			if(nearPawn[i]==1)
				if (goodPosition(i))
					pawnsNearKing+=0.5;
				pawnsNearKing++;
		
		
		int quadrant=kingQuadrant();
		this.freeWaysOnQuadrant= this.checkOpenWaysOnQuadrant();
		double openWays=0;
		
		if(quadrant!=4) {
			openWays=freeWaysOnQuadrant[quadrant]/1.5;
		}else {
			for(int i=0; i<4; i++) {
				openWays+=freeWaysOnQuadrant[i];
			}
			openWays=openWays/8;
		}
		//divento più aggressivo
		if (this.kingIsprotected()) {
			openWays=openWays*0.5;
			bonusEatWhite=1.5;
			
		}
	
		
		//evaluation value
		double result=0;
		result+=WEIGHT_BLACK_PAWN*this.numBlackPawn/GameAshtonTablut.NUM_BLACK;
		result+=(bonusEatWhite*WEIGHT_WHITE_PAWN_EATEN*(GameAshtonTablut.NUM_WHITE-this.numWhitePawn)/GameAshtonTablut.NUM_WHITE);
		

		result+=WEIGHT_PAWN_NEAR_KING*(pawnsNearKing/this.pawnToEatKing());

		result+=WEIGHT_PAWN_NEAR_KING*pawnsNearKing/this.pawnToEatKing();
		
		result+=WHEIGHT_OPEN_WAYS*openWays;
		
		if (this.kingHasOpenWays())
			result-=50;
		
		if(state.getTurn().equals(State.Turn.WHITEWIN))
			result-=100;
		
		if(state.getTurn().equals(State.Turn.WHITEWIN))
			result+=100;
		
		return result;
		
		
	}
	
	/**
	 * This method returns the king position in the board represented with an array of int
	 * 
	 * @return the position of the king in the board
	 */
	public int[] getKingPosition() {
		
		int[] kingPosition = {4, 4};
		
		for(int i=0; i<this.board[0].length; i++) {
			for(int j=0; j<this.board[0].length; j++) {
				if(this.board[i][j].equals(State.Pawn.KING)) {
					kingPosition[0]=i;
					kingPosition[1]=j;
					return kingPosition;
				}
			}
		}
		return kingPosition;
	}
	/**
	 * This method returns int that represents how many pawn are necessaries to eat the king
	 * 
	 * @return how many pawn are necessaries to eat the king
	 */
	public int pawnToEatKing() {
		if (this.kingPosition[0]==4 && this.kingPosition[1]==4)
			return 4;
		if (this.kingIsNearThrone())
			return 3;
		return 2;
	}

        
	public boolean kingIsprotected() {
		
		if (this.kingIsNearThrone())
			return false; 
		
		int nearPawns[]=this.checkNearPawns(State.Pawn.WHITE);
		if ((nearPawns[0]==1 && nearPawns[1]==1)	||
			(nearPawns[1]==1 && nearPawns[2]==1)	||
			(nearPawns[2]==1 && nearPawns[3]==1)	||
			(nearPawns[3]==1 && nearPawns[0]==1)	)
			return true;
		return false;
	}
		
	

	/**
	 * 
	 * @return a specific type of Pawn near a specific position
	 * 
	 * @param state: the state of the board, position: the specified position we want to analyze, target: the specific pawn type we want to see if it's near the position
	 *  
	 */
	public int[] checkNearPawns(State.Pawn target){
        int count[]= {-1, -1, -1, -1};
        //GET TURN

        String stringTarget=target.toString();
        if(this.board[this.kingPosition[0]-1][this.kingPosition[1]].equalsPawn(stringTarget))
            count[0]=1;
        if(this.board[this.kingPosition[0]+1][this.kingPosition[1]].equalsPawn(stringTarget))
            count[1]=1;
        if(this.board[this.kingPosition[0]][this.kingPosition[1]-1].equalsPawn(stringTarget))
            count[2]=1;
        if(this.board[this.kingPosition[0]][this.kingPosition[1]+1].equalsPawn(stringTarget))
            count[3]=1;
        return count;
    }
	

	/**
	 * This method returns a boolean that express if the king is in one of the squares near the throne or not
	 * 
	 * @return true if king is in the squares near the throne, false if not
	 * 
	 */

	public boolean goodPosition(int pos) {
		
		//valido solo quando il re è lontano dal castello
		
		if (this.pawnToEatKing()>2)
			return false;
		
		//pedina nera alla sinistra del re
		if (pos==0) {
			if(this.positionReachable(this.kingPosition[0]+1, this.kingPosition[1]))
				return true;
		}
		
		//pedina nera alla destra del re
		if (pos==1) {
			if(this.positionReachable(this.kingPosition[0]-1, this.kingPosition[1]))
				return true;
		}

		//pedina nera sotto al re
		if (pos==2) {
			if(this.positionReachable(this.kingPosition[0], this.kingPosition[1]-1))
				return true;
		}
		
		//pedina nera sotto al re
		if (pos==3) {
			if(this.positionReachable(this.kingPosition[0], this.kingPosition[1]+1))
				return true;
		}
		
		return false;
				
	}
	
	//vero se la posizione indicata e raggiongibile da una pedina nera (non scavalca pedine bianche)
	public boolean positionReachable(int x, int y) {
		//guardo a destra
		for (int i=x; i<this.board.length; i++)
			if (!this.board[i][y].equals(State.Pawn.EMPTY))
				if ( this.board[i][y].equals(State.Pawn.BLACK))
					return true;
				else
					return false;
		//guardo a sinistra
		for (int i=x; i>=0; i--)
			if (!this.board[i][y].equals(State.Pawn.EMPTY))
				if ( this.board[i][y].equals(State.Pawn.BLACK))
					return true;
				else
					return false;
		//guardo sotto
		for (int i=y; i<this.board.length; i++)
			if (!this.board[x][i].equals(State.Pawn.EMPTY))
				if ( this.board[x][i].equals(State.Pawn.BLACK))
					return true;
			else
					return false;
		//gardo sopra
		for (int i=y; i>=0; i--)
			if (!this.board[x][i].equals(State.Pawn.EMPTY))
				if ( this.board[x][i].equals(State.Pawn.BLACK))
					return true;
				else
					return false;
		return false;
	}


	public boolean kingIsNearThrone(){
		//structure that represent the squares near the throne
		final int [][] nearThrone= {	
				
										{3, 4},
								{4, 3}, {4, 4}, {4, 5},
										{5, 4}
		};
		
		for (int pos[] : nearThrone) {
			if(this.kingPosition[0]==pos[0] && this.kingPosition[1]==pos[1] )
				return true;
		}
		return false;
	}

	 /*
	 * This method returns a boolean that express if a specific square is inside one of the citadel
	 */
	public boolean isCitadel(int x, int y) {
		//structure that represent citadels squares
		final int[][] citadels= {
				
									{0, 4}, {0, 5}, {0, 6}, 
											{1, 5}, 
											 						
					{3, 0},											{3, 8}, 
					{4, 0}, {4, 1},							{4, 7}, {4, 8}, 
					{5, 0},											{5, 8}, 
											{7, 4},
									{8, 5}, {8, 4}, {8, 3}   
									
		};

		
		for (int cit[] : citadels)
			if (x==cit[0] && y==cit[1])
				return true;
		return false;
	
	}
	
	
	/**
	 * This method returns a boolean that express if there are or not open ways for the kind to escape from the board
	 * 
	 * @return true if king has an open way to escape from the board, false if not
	 * 
	 */
	public boolean kingHasOpenWays() {
		
		//check if the king is near the throne
		if (this.kingIsNearThrone())
			return false;
		
		int colonna=this.kingPosition[0];
		int riga= this.kingPosition[1];
		

		//controllo a destra
		for (int i=colonna; i<this.board[colonna].length; i++)
			if (!this.board[colonna][i].equals(State.Pawn.EMPTY) ||  isCitadel(colonna, i))
				return false;
		//controllo a sinistra
		for (int i=colonna; i>=0; i--)
			if (!this.board[colonna][i].equals(State.Pawn.EMPTY) || isCitadel(colonna, i))
				return false;
		//controllo sopra		
		for (int i=riga; i<board[colonna].length; i++)
			if (!this.board[riga][i].equals(State.Pawn.EMPTY) || isCitadel(riga, i))
				return false;
		//controllo sotto
		for (int i=riga; i>=0; i--)
			if (!this.board[riga][i].equals(State.Pawn.EMPTY) || isCitadel(riga, i))
				return false;
		
		return true;
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
	 * This method counts how many possible open ways there are not covered by the rhombus strategy
	 * 
	 * @return how many open possible ways there are, not closed by the rhombus strategy by black
	 * 
	 */

	public int[] checkOpenWaysOnQuadrant() {
		int count[]= {0, 0, 0, 0}; 

		
		if (state.getPawn(1, 2).equalsPawn(State.Pawn.BLACK.toString())) 
                count[0]++;
		if (state.getPawn(2, 1).equalsPawn(State.Pawn.BLACK.toString())) 
            count[0]++;
		if (state.getPawn(1, 6).equalsPawn(State.Pawn.BLACK.toString())) 
            count[1]++;
		if (state.getPawn(2, 7).equalsPawn(State.Pawn.BLACK.toString())) 
            count[1]++;
		if (state.getPawn(6, 1).equalsPawn(State.Pawn.BLACK.toString())) 
            count[2]++;
		if (state.getPawn(7, 2).equalsPawn(State.Pawn.BLACK.toString())) 
            count[2]++;
		if (state.getPawn(6, 7).equalsPawn(State.Pawn.BLACK.toString())) 
            count[3]++;
		if (state.getPawn(7, 6).equalsPawn(State.Pawn.BLACK.toString())) 
            count[3]++;

		
		return count;
	}
	

	/**
	 * This method find a pawn that can eat you and return the position if founded
	 * 
	 * @return the position of a pawn that can potentially eat your pawn in a specific position
	 * @param the position of the pawn that can be potentially captured by another enemy pawn
	 * 
	 */
	public int[] aboutToBeCaptured(int[] position) {
		
		State.Pawn board[][]=this.state.getBoard();
		int[] result = {-1,-1};
		
		//control on the right
		for (int i=position[1]; i<board[position[1]].length; i++)
			if(!board[position[1]][i].equals(State.Pawn.EMPTY))
				result=position;
				
		//control on the left
		for (int i=position[1]; i>=0; i--)
			if(!board[position[1]][i].equals(State.Pawn.EMPTY))
				result=position;
				
		//control above
		for (int i=position[0]; i<board[position[1]].length; i++)
			if(!board[position[1]][i].equals(State.Pawn.EMPTY))
				result=position;
				
		//control below
		for (int i=position[0]; i>=0; i--)
			if(!board[position[1]][i].equals(State.Pawn.EMPTY))
				result=position;
		
		return result;
	}
	
	public boolean canCapture() {
		boolean result = false;
		
		
		
		return result;
	}

	public int kingQuadrant() {
		if(this.kingPosition[0]<4 && this.kingPosition[1]<4)
			return 0;
		if(this.kingPosition[0]<4 && this.kingPosition[1]>4)
			return 1;
		if(this.kingPosition[0]>4 && this.kingPosition[1]<4)
			return 2;
		if(this.kingPosition[0]>4 && this.kingPosition[1]>4)
			return 3;
		return 4;
	}


//	public double evalKingPosition() {
//		if(kingPosition + [0][1]) {
//			
//		}
//	}
}
