package it.unibo.ai.didattica.competition.tablut.almazeneca.heuristics;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.domain.State.Turn;

public class WhiteHeuristics extends Heuristics {	

	private int countB;
	private int countW;
	private int blackNearKing;
	private int whiteNearKing;
	private int kingFreeWay;
	private int kingOnThrone;
	private int kingNearThrone;
	private int kingOnStar;
	private int kingFromBorder;
	private int blackPawnsOverhanged;
	private int whitePawnsOverhanged;
	private int kingOverhanged;
	private int kingOnFavourite;
	private int guards;
	private int strategy;
	private State.Pawn[][] board;
	private int[] kingPosition;
	
	//pesi 
	/*private double WHITE_WEIGHT_COUNT_WHITE_PAWNS= 7.0;
	private double WHITE_WEIGHT_COUNT_BLACK_PAWNS= 5.0;
	private double WHITE_WEIGHT_SINGLE_FREE_WAY_KING= 8.0;
	private double WHITE_WEIGHT_MULTIPLE_FREE_WAY_KING=12.0;
	private double WHITE_WEIGHT_KING_OVERHANGED = 22.0;
	private double WHITE_WEIGHT_KING_ON_BLUE= 15.0;
	private double WHITE_WEIGHT_STRATEGY = 7.0;
	private double WHITE_WEIGHT_KING_ON_THRONE = 4.0;
	private double WHITE_WEIGHT_BLACK_PAWNS_OVERHANGED = 3.0; 
	private double WHITE_WEIGHT_WHITE_PAWNS_OVERHANGED = 1.5;
	private double WHITE_WEIGHT_KING_FAVOURITE = 5.0; */
	private int pawnsB;
	private int pawnsW;
	
	private  double capturedBlack = 1.0;
	private  double capturedWhite = -1.0;
	private  double protectedKingOneSide = 8.0;
	private  double protectedKingTwoSide = 6.0;
	private  double protectedKingThreeSide = -10.0; 
	private  double protectedKingFourSide = -15.0;
	private  double distanceEscapePoint = 5.0; 
	private  double rowColumnFree = 800.0;
	private  double kingCaptured = -2500.0;
	private  double win = 2000.0;
	private  double kingInCastle = -200.0;
	private double freeWays=500.0;

	private Random r;
	private List<String> citadels;
	private List<String> stars;
	private List<String> nearsThrone;
	private List<String> guardsPos;
	private String throne;

	private List<String> blackBarrier;
	
	public WhiteHeuristics(State state) {
		super(state);
		this.pawnsB = 16;
		this.pawnsW = 9;
		this.r = new Random(System.currentTimeMillis());

		this.citadels = Arrays.asList("a4", "a5", "a6", "b5", "d1", "e1", "f1", "e2", "i4", "i5", "i6", "h5", "d9",
				"e9", "f9", "e8");

		this.stars = Arrays.asList("a2", "a3", "a7", "a8", "b1", "b9", "c1", "c9", "g1", "g9", "h1", "h9", "i2", "i3",
				"i7", "i8");

		this.nearsThrone = Arrays.asList("e4", "e6", "d5", "f5");
		this.throne = "e5";

		this.blackBarrier = Arrays.asList("b3", "b7", "c2", "c8", "g2", "g8", "h3", "h7");

		this.guardsPos = Arrays.asList("a1", "a2", "b1", "h1", "i1", "i2", "i8", "i9", "h9", "b9", "a9", "a8");

	}
	@Override
	public double evaluateState() {
		// TODO Auto-generated method stub
		//inizializza&resetta
		this.resetValues();
		
		//extractValues 
		this.extractValues(state);
		printValues();
		//calcolo euristica
		double result= 0;
		
		//controllo se il vincitore è il nero re catturato 
		if (state.getTurn().equalsTurn(Turn.BLACKWIN.toString())) {
			return -50; //valore -infinito?
		}/*
		//re inesperto  
				if(this.kingOverhanged>0) {
					result -= WHITE_WEIGHT_KING_OVERHANGED * this.kingOverhanged;
				}else {
					if (this.kingFreeWay == 1) {
						result += WHITE_WEIGHT_SINGLE_FREE_WAY_KING * this.kingFreeWay;
					} else {
						if (this.kingFreeWay > 1) {
							result += WHITE_WEIGHT_MULTIPLE_FREE_WAY_KING * (this.kingFreeWay/ 2.0);
						}
					}
				}
		*/
		/*
		result -= WHITE_WEIGHT_KING_ON_THRONE * this.kingOnThrone;
		result += WHITE_WEIGHT_KING_ON_BLUE * this.kingOnStar;
		result += WHITE_WEIGHT_BLACK_PAWNS_OVERHANGED * this.blackPawnsOverhanged;

		result += WHITE_WEIGHT_KING_ON_BLUE * this.kingOnStar;

		result += WHITE_WEIGHT_KING_FAVOURITE * this.kingOnFavourite;
*/
		//peso delle pedine nere
		/*if(this.countB< this.pawnsB) {
			result+= this.WHITE_WEIGHT_COUNT_BLACK_PAWNS*(this.pawnsB-this.countB)/this.pawnsB;
		}*/
		//peso delle pedine bianche
		/*if(this.countW< this.pawnsW) {
			//sottraggo il peso?
		
			result -= WHITE_WEIGHT_COUNT_WHITE_PAWNS * (this.pawnsW - this.countW)/this.pawnsW;
		}
		
		*/

		double valCapturedBlack= this.pawnsB-this.countB;
		double valCapturedWhite= this.pawnsW-this.countW;
		double valKingProtected = 0;
		
		if(this.whiteNearKing==1) {
			valKingProtected = protectedKingOneSide;
		}else if(this.whiteNearKing==2) {
			valKingProtected = protectedKingTwoSide;
		}else if(this.whiteNearKing==3) {
			valKingProtected = protectedKingThreeSide;
		}else if(this.whiteNearKing==4) {
			valKingProtected = protectedKingFourSide;
		}
		
		//double valEscapePointBlocked= 
		double valKingInCastle=0.0;
		
		double valKingFreeWays=this.kingFreeWay*freeWays;
		result= result+ valKingProtected+valKingFreeWays+valKingInCastle+ valCapturedBlack*this.capturedBlack+ valCapturedWhite-this.capturedWhite;
		
		return result;
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
				
									{0, 3}, {0, 4}, {0, 5}, 
											{1, 4}, 
											 						
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
	//posizione del re
	
			public int[] getKingPosition() {
				this.board=this.state.getBoard();
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
			
		/*	public int distanceBetweenKingEscape(int[] king) {
				int dist = 0;
				int distance[] = new int[this.stars.size()];*/
			/*	this.stars.forEach((position) -> {
					distance[escapePoints.indexOf(position)] = (Math.abs(king[0]-position.getRow())) + (Math.abs(king.getColumn() - position.getColumn()));
				});*/
			/*	int i=0;
				for(i=0;i<this.stars.size();i++) {
					distance[]
				}
				dist = distance[0];
				escape = escapePoints.get(0);
				for( i = 1; i < distance.length; i++) {
					if(distance[i] < dist) {
						dist = distance[i];
						escape = escapePoints.get(i);
					}
				}
				return dist;
			}
			
	*/		
	private void extractValues(State state) {
		
		
		
		//calcolo della strategia
		for (int i = 0; i < state.getBoard().length; i++) {
			for (int j = 0; j < state.getBoard().length; j++) {
				// conto le pedine bianche
				if (state.getPawn(i, j).equalsPawn(State.Pawn.WHITE.toString())
						|| state.getPawn(i, j).equalsPawn(State.Pawn.KING.toString())) {
					this.countW++;

				}

				// conto le pedine nere
				if (state.getPawn(i, j).equalsPawn(State.Pawn.BLACK.toString())) {
					this.countB++;

				}
		//conto delle pedine nere con una bianca o un accampamento o il trono vicino
				if (state.getPawn(i, j).equalsPawn(State.Pawn.BLACK.toString())
						&& !this.citadels.contains(state.getBox(i, j).toString())) {
					if (i > 0
							&& (state.getPawn(i - 1, j).equalsPawn(State.Pawn.WHITE.toString())
									|| state.getPawn(i - 1, j).equalsPawn(State.Pawn.KING.toString())
									|| this.citadels.contains(state.getBox(i - 1, j))
									|| state.getBox(i - 1, j).equals(this.throne))
							&& i < state.getBoard().length - 1
							&& (state.getPawn(i + 1, j).equalsPawn(State.Pawn.EMPTY.toString()))) {

						this.blackPawnsOverhanged++;
				}else if (i < state.getBoard().length - 1
						&& (state.getPawn(i + 1, j).equalsPawn(State.Pawn.WHITE.toString())
								|| state.getPawn(i + 1, j).equalsPawn(State.Pawn.KING.toString())
								|| this.citadels.contains(state.getBox(i + 1, j))
								|| state.getBox(i + 1, j).equals(this.throne))
						&& i > 0 && (state.getPawn(i - 1, j).equalsPawn(State.Pawn.EMPTY.toString()))) {
					this.blackPawnsOverhanged++;

				}else if (j > 0
						&& (state.getPawn(i, j - 1).equalsPawn(State.Pawn.WHITE.toString())
								|| state.getPawn(i, j - 1).equalsPawn(State.Pawn.KING.toString())
								|| this.citadels.contains(state.getBox(i, j - 1))
								|| state.getBox(i, j - 1).contentEquals(this.throne))
						&& j < state.getBoard().length - 1
						&& (state.getPawn(i, j + 1).equalsPawn(State.Pawn.EMPTY.toString()))) {
					this.blackPawnsOverhanged++;

				}else if (j < state.getBoard().length - 1
						&& (state.getPawn(i, j + 1).equalsPawn(State.Pawn.WHITE.toString())
								|| state.getPawn(i, j + 1).equalsPawn(State.Pawn.KING.toString())
								|| this.citadels.contains(state.getBox(i, j + 1))
								|| state.getBox(i, j - 1).contentEquals(this.throne))
						&& j > 0 && (state.getPawn(i, j - 1).equalsPawn(State.Pawn.EMPTY.toString()))) {
					this.blackPawnsOverhanged++;

					}
				}
					
		// conto le pedine bianche con una nera o un accampamento o il trono vicino
			
				if (state.getPawn(i, j).equalsPawn(State.Pawn.WHITE.toString())) {

					if (i > 0
							&& (state.getPawn(i - 1, j).equalsPawn(State.Pawn.BLACK.toString())
									|| this.citadels.contains(state.getBox(i - 1, j))
									|| state.getBox(i - 1, j).equals(this.throne))
							&& i < state.getBoard().length - 1
							&& (state.getPawn(i + 1, j).equalsPawn(State.Pawn.EMPTY.toString()))) {

						this.whitePawnsOverhanged++;

					}else if (i < state.getBoard().length - 1
							&& (state.getPawn(i + 1, j).equalsPawn(State.Pawn.BLACK.toString())
									|| this.citadels.contains(state.getBox(i + 1, j))
									|| state.getBox(i + 1, j).equals(this.throne))
							&& i > 0 && (state.getPawn(i - 1, j).equalsPawn(State.Pawn.EMPTY.toString()))) {

						this.whitePawnsOverhanged++;

					}

					else if (j > 0
							&& (state.getPawn(i, j - 1).equalsPawn(State.Pawn.BLACK.toString())
									|| this.citadels.contains(state.getBox(i, j - 1))
									|| state.getBox(i, j - 1).contentEquals(this.throne))
							&& j < state.getBoard().length - 1
							&& (state.getPawn(i, j + 1).equalsPawn(State.Pawn.EMPTY.toString()))) {

						this.whitePawnsOverhanged++;

					}

					else if (j < state.getBoard().length - 1
							&& (state.getPawn(i, j + 1).equalsPawn(State.Pawn.BLACK.toString())
									|| this.citadels.contains(state.getBox(i, j + 1))
									|| state.getBox(i, j + 1).contentEquals(this.throne))
							&& j > 0 && (state.getPawn(i, j - 1).equalsPawn(State.Pawn.EMPTY.toString()))) {

						this.whitePawnsOverhanged++;

					}

				}

		// controllo se il re ha pedine nere intorno o accampamenti o il trono
				
				if (state.getPawn(i, j).equalsPawn(State.Pawn.KING.toString())) {

					if (i > 0 && (state.getPawn(i - 1, j).equalsPawn(State.Pawn.BLACK.toString())
							|| this.citadels.contains(state.getBox(i - 1, j))
							|| state.getBox(i - 1, j).equals(this.throne))) {
						this.blackNearKing++;
					}

					if (i < state.getBoard().length - 1
							&& (state.getPawn(i + 1, j).equalsPawn(State.Pawn.BLACK.toString())
									|| this.citadels.contains(state.getBox(i + 1, j))
									|| state.getBox(i + 1, j).equals(this.throne))) {
						this.blackNearKing++;
					}

					if (j > 0 && (state.getPawn(i, j - 1).equalsPawn(State.Pawn.BLACK.toString())
							|| this.citadels.contains(state.getBox(i, j - 1))
							|| state.getBox(i, j - 1).contentEquals(this.throne))) {
						this.blackNearKing++;
					}

					if (j < state.getBoard().length - 1
							&& (state.getPawn(i, j + 1).equalsPawn(State.Pawn.BLACK.toString())
									|| this.citadels.contains(state.getBox(i, j + 1))
									|| state.getBox(i, j + 1).contentEquals(this.throne))) {
						this.blackNearKing++;
					}

				}
				
		// controllo se il re ha pedine bianche intorno
		
				if (state.getPawn(i, j).equalsPawn(State.Pawn.KING.toString())) {

					if (i > 0 && (state.getPawn(i - 1, j).equalsPawn(State.Pawn.WHITE.toString()))) {
						this.whiteNearKing++;
					}

					if (i < state.getBoard().length - 1
							&& (state.getPawn(i + 1, j).equalsPawn(State.Pawn.WHITE.toString()))) {
						this.whiteNearKing++;
					}

					if (j > 0 && (state.getPawn(i, j - 1).equalsPawn(State.Pawn.WHITE.toString())
							|| this.citadels.contains(state.getBox(i, j - 1))
							|| state.getBox(i, j - 1).contentEquals(this.throne))) {
						this.whiteNearKing++;

					}

					if (j < state.getBoard().length - 1
							&& (state.getPawn(i, j + 1).equalsPawn(State.Pawn.WHITE.toString()))) {
						this.whiteNearKing++;
					}

				}
		// controllo se il re ha vie libere per vincere
				if (state.getPawn(i, j).equalsPawn(State.Pawn.KING.toString()) && (i == 1 || i == 2 || i == 6 || i == 7)
						&& (j == 1 || j == 2 || j == 6 || j == 7)) {
					boolean free = true;
					for (int w = 0; w < i; w++) {

						if (!state.getPawn(w, j).equalsPawn(State.Pawn.EMPTY.toString())
								|| this.citadels.contains(state.getBox(w, j))) {
							free = false;
							break;

						}

					}

					if (free) {
						this.kingFreeWay++;
					}

					free = true;

					for (int w = i + 1; w < state.getBoard().length; w++) {

						if (!state.getPawn(w, j).equalsPawn(State.Pawn.EMPTY.toString())
								|| this.citadels.contains(state.getBox(w, j))) {
							free = false;
							break;

						}

					}

					if (free) {
						this.kingFreeWay++;
					}

					free = true;
					for (int w = 0; w < j; w++) {

						if (!state.getPawn(i, w).equalsPawn(State.Pawn.EMPTY.toString())
								|| this.citadels.contains(state.getBox(i, w))) {
							free = false;
							break;
						}

					}

					if (free) {
						this.kingFreeWay++;
					}

					free = true;

					for (int w = i + 1; w < state.getBoard().length; w++) {

						if (!state.getPawn(i, w).equalsPawn(State.Pawn.EMPTY.toString())
								|| this.citadels.contains(state.getBox(i, w))) {
							free = false;
							break;

						}

					}

					if (free) {
						this.kingFreeWay++;
					}

		// controllo se il re è sul trono
					if (state.getPawn(i, j).equalsPawn(State.Pawn.KING.toString())
							&& state.getBox(i, j).equals(this.throne)) {
						this.kingOnThrone = 1;
					}
		// controllo se il re è vicino al trono
					if (state.getPawn(i, j).equalsPawn(State.Pawn.KING.toString())
							&& this.nearsThrone.contains(state.getBox(i, j))) {
						this.kingNearThrone = 1;
					}
		
		// controllo se il re è su una stella(caselle blu)
					if (state.getPawn(i, j).equalsPawn(State.Pawn.KING.toString())
							&& this.stars.contains(state.getBox(i, j))) {
						this.kingOnStar = 1;
					}
		// controllo se il re è vicino al bordo
					if (state.getPawn(i, j).equalsPawn(State.Pawn.KING.toString())) {
						this.kingFromBorder = Math.min(state.getBoard().length - 1 - i, state.getBoard().length - 1 - j);
					}
				
		// controllo se il re è minacciato
					if (state.getPawn(i, j).equalsPawn(State.Pawn.KING.toString())) {
						// ho una nera sotto e controllo sopra-destra-sinistra
						if (i + 1 < state.getBoard().length - 1
								&& (state.getPawn(i + 1, j).equalsPawn(State.Pawn.BLACK.toString())
										|| citadels.contains(state.getBox(i + 1, j)))) {
							boolean minacciato = false;
							for (int itemp = i - 1; itemp >= 0 && !minacciato; itemp--) {
								if (state.getPawn(itemp, j).equalsPawn(State.Pawn.BLACK.toString()))
									minacciato = true;
								if (state.getPawn(itemp, j).equalsPawn(State.Pawn.THRONE.toString())
										|| citadels.contains(state.getBox(itemp, j))
										|| state.getPawn(itemp, j).equalsPawn(State.Pawn.WHITE.toString()))
									break;
							}
							for (int jtemp = j - 1; jtemp >= 0 && !minacciato; jtemp--) {
								if (state.getPawn(i - 1, jtemp).equalsPawn(State.Pawn.BLACK.toString()))
									minacciato = true;
								if (state.getPawn(i - 1, jtemp).equalsPawn(State.Pawn.THRONE.toString())
										|| citadels.contains(state.getBox(i - 1, jtemp))
										|| state.getPawn(i - 1, jtemp).equalsPawn(State.Pawn.WHITE.toString()))
									break;
							}
							for (int jtemp = j + 1; jtemp < state.getBoard().length - 1 && !minacciato; jtemp++) {
								if (state.getPawn(i - 1, jtemp).equalsPawn(State.Pawn.BLACK.toString()))
									minacciato = true;
								if (state.getPawn(i - 1, jtemp).equalsPawn(State.Pawn.THRONE.toString())
										|| citadels.contains(state.getBox(i - 1, jtemp))
										|| state.getPawn(i - 1, jtemp).equalsPawn(State.Pawn.WHITE.toString()))
									break;
							}
							if (minacciato) {
								kingOverhanged++;
							}
							
						}
					}
				}
			}
		}
					
						
						
	}
	
		
	
		private void resetValues() {
			this.countB = 0;
			this.countW = 0;
			this.blackNearKing = 0;
			this.whiteNearKing = 0;
			this.kingFreeWay = 0;
			this.kingOnThrone = 0;
			this.kingOnStar = 0;
			this.kingNearThrone = 0;
			this.kingFromBorder = 0;
			this.blackPawnsOverhanged = 0;
			this.whitePawnsOverhanged = 0;
			this.kingOverhanged = 0;
			this.kingOnFavourite = 0;
			this.guards = 0;

		}
		
		private void printValues() {

			double diff = countB - countW;

			System.out.println("countB - countW = " + diff);
			System.out.println("countB = " + this.countB);
			System.out.println("countW = " + this.countW);
			System.out.println("blackNearKing = " + this.blackNearKing);
			System.out.println("whiteNearKing = " + this.whiteNearKing);
			System.out.println("kingFreeWay = " + this.kingFreeWay);
			System.out.println("kingOnThrone = " + this.kingOnThrone);
			System.out.println("kingNearThrone = " + this.kingNearThrone);
			System.out.println("kingOnStar = " + this.kingOnStar);
			System.out.println("kingFromBorder = " + this.kingFromBorder);
			System.out.println("blackPawnsOverhanged = " + this.blackPawnsOverhanged);
			System.out.println("whitePawnsOverhanged = " + this.whitePawnsOverhanged);
			System.out.println("kingOverhanged = " + this.kingOverhanged);
			System.out.println("strategy = " + this.strategy);
			

		}

}