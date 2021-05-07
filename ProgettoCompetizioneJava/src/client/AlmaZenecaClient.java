package client;

import java.io.IOException;
import java.net.UnknownHostException;

import Domain.MyRules;
import it.unibo.ai.didattica.competition.tablut.domain.*;
import strategy.MyIterativeDeepeningAlphaBetaSearch;

public class AlmaZenecaClient extends TablutClient {

	public AlmaZenecaClient(String player, String name, int timeout, String ipAddress)
			throws UnknownHostException, IOException {
		super(player, name, timeout, ipAddress);
		// TODO Auto-generated constructor stub
		MyRules rules = null;
		//gestione varie rules
		//gestione varie strategie
		
	}

	
	public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException{
	
		String role = "";
		String name = "AlmaZeneca";
		int timeout = 60;
		String ipAddress = "localhost";
		//parametri
		int depth = 8;
		int numThread = 8;
		
		String usage = "Usage: java AlmaZeneca COLOR [-t <timeout>] \n"
				+ "\t<timeout> must be an integer (default 55)\n";
		
		
		
		
				if (args.length < 1) {
					System.out.println("You must specify which player you are (WHITE or BLACK)");
					System.exit(-1);
				} else {
					// System.out.println(args[0]);
					role = (args[0]);
				}
				
				
		for (int i = 1; i < args.length - 1; i++) {
			if (args[i].equals("-t")) {
				i++;
				try {
					timeout = Integer.parseInt(args[i]) - 2;
					if (timeout < 1) {
						System.out.println("Timeout format not allowed!");
						System.out.println(args[i]);
						System.exit(1);
					}
				} catch (Exception e) {
					System.out.println("Timeout format is not correct!");
					System.out.println(usage);
					System.exit(1);
				}
			}
			//GameManager.getInstance().setParameters(timeout, depth, role.toLowerCase());
			AlmaZenecaClient client = new AlmaZenecaClient(role, name, timeout, ipAddress);
			client.run();
		}

	}
	
	@Override
    public void run() {

        // send name of your group to the server saved in variable "name"
        try {
            this.declareName();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // set type of state and WHITE must do the first player
        State state = new StateTablut();
        state.setTurn(State.Turn.WHITE);

        // set type of game 
        GameAshtonTablut tablutGame = new GameAshtonTablut(0, -1, "logs", "white_ai", "black_ai");	//capire differenza tra GameTablut e GameAshtonTablut
        
        
     // still alive until you are playing
        while (true) {

            // update the current state from the server
            try {
                this.read();
            } catch (ClassNotFoundException | IOException e1) {
                e1.printStackTrace();
                System.exit(1);
            }

            // print current state
            System.out.println("Current state:");
            state = this.getCurrentState();
            System.out.println(state.toString());



            // if i'm WHITE
            if (this.getPlayer().equals(State.Turn.WHITE)) {

                // if is my turn (WHITE)
                if (state.getTurn().equals(StateTablut.Turn.WHITE)) {

                    System.out.println("\nSearching a suitable move... ");

                    // search the best move in search tree
                    Action a = findBestMove(tablutGame, state);

                    System.out.println("\nAction selected: " + a.toString());
                    try {
                        this.write(a);
                    } catch (ClassNotFoundException | IOException e) {
                        e.printStackTrace();
                    }

                }

                // if is turn of oppenent (BLACK)
                else if (state.getTurn().equals(StateTablut.Turn.BLACK)) {
                    System.out.println("Waiting for your opponent move...\n");
                }
                // if I WIN
                else if (state.getTurn().equals(StateTablut.Turn.WHITEWIN)) {
                    System.out.println("YOU WIN!");
                    System.exit(0);
                }
                // if I LOSE
                else if (state.getTurn().equals(StateTablut.Turn.BLACKWIN)) {
                    System.out.println("YOU LOSE!");
                    System.exit(0);
                }
                // if DRAW
                else if (state.getTurn().equals(StateTablut.Turn.DRAW)) {
                    System.out.println("DRAW!");
                    System.exit(0);
                }

            }
            // if i'm BLACK
            else {

                // if is my turn (BLACK)
                if (this.getCurrentState().getTurn().equals(StateTablut.Turn.BLACK)) {

                    System.out.println("\nSearching a suitable move... ");

                    // search the best move in search tree
                    Action a = findBestMove(tablutGame, state);

                    System.out.println("\nAction selected: " + a.toString());
                    try {
                        this.write(a);
                    } catch (ClassNotFoundException | IOException e) {
                        e.printStackTrace();
                    }

                }

                // if is turn of oppenent (WHITE)
                else if (state.getTurn().equals(StateTablut.Turn.WHITE)) {
                    System.out.println("Waiting for your opponent move...\n");
                }

                // if I LOSE
                else if (state.getTurn().equals(StateTablut.Turn.WHITEWIN)) {
                    System.out.println("YOU LOSE!");
                    System.exit(0);
                }

                // if I WIN
                else if (state.getTurn().equals(StateTablut.Turn.BLACKWIN)) {
                    System.out.println("YOU WIN!");
                    System.exit(0);
                }

                // if DRAW
                else if (state.getTurn().equals(StateTablut.Turn.DRAW)) {
                    System.out.println("DRAW!");
                    System.exit(0);
                }
            }
        }
    }


    /**
     * Method that find a suitable moves searching in game tree
     * @param tablutGame Current game
     * @param state Current state
     * @return Action that is been evaluated as best
     */
    private Action findBestMove(GameAshtonTablut tablutGame, State state) {

        MyIterativeDeepeningAlphaBetaSearch search = new MyIterativeDeepeningAlphaBetaSearch(tablutGame, Double.MIN_VALUE, Double.MAX_VALUE, this.timeout - 2 );
        //search.setLogEnabled(debug);
        return search.makeDecision(state);
    }
	


}
