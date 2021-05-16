package it.unibo.ai.didattica.competition.tablut.almazeneca;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import it.unibo.ai.didattica.competition.tablut.almazeneca.minmax.MyIterativeDeepeningAlphaBetaSearch;
import it.unibo.ai.didattica.competition.tablut.client.TablutClient;
import it.unibo.ai.didattica.competition.tablut.client.TablutRandomClient;
import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.GameAshtonTablut;
import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.domain.StateTablut;
import it.unibo.ai.didattica.competition.tablut.domain.State.Turn;

public class TablutAlmaZenecaClient extends TablutClient {
	
    private boolean debug;

	public TablutAlmaZenecaClient(String player, String name, int timeout, String ipAddress)
			throws UnknownHostException, IOException {
		super(player, name, timeout, ipAddress);

	}

	public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
		int gametype = 4;
		String role = "";
		String name = "AlmaZeneca";
		String ipAddress = "localhost";
		int timeout = 60;

		if (args.length < 1) {
			System.out.println("You must specify which player you are (WHITE or BLACK)");
			System.exit(-1);
		} else {
			System.out.println(args[0]);
			role = (args[0]);
		}
		if (args.length == 2) {
			System.out.println(args[1]);
			timeout = Integer.parseInt(args[1]);
		}
		if (args.length == 3) {
			timeout = Integer.parseInt(args[1]);
			ipAddress = args[2];
		}

		TablutAlmaZenecaClient client = new TablutAlmaZenecaClient(role, name, timeout, ipAddress);
		client.run();
	}

	@Override
	public void run() {
		try {
			this.declareName();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// set state type and the white player as first
		State state = new StateTablut();
		state.setTurn(State.Turn.WHITE);

		// set game type
		GameAshtonTablut tablutGame = new GameAshtonTablut(0, -1, "logs", "white_ai", "black_ai");
		
		System.out.println("\n"+
                "+---  Ashton Tablut game challenge 2021 ---+");
         System.out.println(
        	    "|                                          |\n"+
                "|     ________________________________     |\n"+
                "|    |.                |             .|    |\n"+
                "|    | .               |           .  |    |\n"+
                "|    |  .              |         .    .    |\n"+
                "|    |   .             |       .     .|    |\n"+
                "|    |    .            |     .      . |    |\n"+
                "|    |     .           |   .       .  |    |\n"+
                "|    |      .          | .        .   |    |\n"+
                "|    |       .         .         .    |    |\n"+
                "|    |.........      . |        .     |    |\n"+
                "|    |         .   .   |       .      |    |\n"+
                "|    |          ..     |      .       |    |\n"+
                "|    |         . .     |     .        |    |\n"+
                "|    |       .    .    |    .         |    |\n"+
                "|    |     .       .   |   .          |    |\n"+
                "|    |   .          .  |  .           |    |\n"+
                "|    | .             . | .            |    |\n"+
                "|    |________________.|._____________|    |\n"+
                "|                                          |\n"+
                "|                 AlmaZeneca               |\n"+
                "|                                          |\n");

         System.out.println(
                "+----------- Armando Botticella -----------+\n"+
                "+------------ Giuliano De Vizio------------+\n"+
                "+------------- Andrea Porrazzo ------------+\n"+
                "+------------- Davide Tazzioli ------------+\n");
         
 		System.out.println("timeout: " + timeout);
 		System.out.println("server ip: " + this.serverIp);


		List<int[]> pawns = new ArrayList<int[]>();
		List<int[]> empty = new ArrayList<int[]>();

		System.out.println("You are player " + this.getPlayer().toString() + "!");

		if (this.getPlayer().equals(State.Turn.WHITE)) {
			executeClient(state, Turn.WHITE, Turn.BLACK, tablutGame);
		} else if (this.getPlayer().equals(State.Turn.BLACK)) {
			executeClient(state, Turn.BLACK, Turn.WHITE, tablutGame);
		}
	}

	private void executeClient(State state, Turn myTurn, Turn opponentTurn, GameAshtonTablut tablutGame) {
		while (true) {
			// update the current state from the server
			try {
				this.read();
			} catch (ClassNotFoundException | IOException e1) {
				System.out.println("Error during the reading of the state");
				e1.printStackTrace();
				System.exit(1);
			}
			
			// show the current state
			System.out.println("Current state:");
			state = this.getCurrentState();
			System.out.println(state.toString());

			// if is my turn
			if (state.getTurn().equals(myTurn)) {
				System.out.println("\nSearching a suitable move... ");

				// find the best move searching in the solution tree
				Action a = findBestMove(tablutGame, state);

				System.out.println("\nAction selected: " + a.toString());
				try {
					this.write(a);
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
			}

			// if is the opponent turn
			else if (state.getTurn().equals(opponentTurn)) {
				System.out.println("Waiting for your opponent move...\n");
			}
			// if I win
			else if (state.getTurn().equals(StateTablut.Turn.WHITEWIN)) {
				System.out.println("You won the game !");
				System.exit(0);
			}
			// if I lose
			else if (state.getTurn().equals(StateTablut.Turn.BLACKWIN)) {
				System.out.println("You lost the game !");
				System.exit(0);
			}
			// if it's draw
			else if (state.getTurn().equals(StateTablut.Turn.DRAW)) {
				System.out.println("It's draw !");
				System.exit(0);
			}
		}
	}

	public Action findBestMove(GameAshtonTablut tablutGame, State state) {
		MyIterativeDeepeningAlphaBetaSearch search = new MyIterativeDeepeningAlphaBetaSearch(tablutGame, Double.MIN_VALUE, Double.MAX_VALUE, this.timeout - 2 );
        //search.setLogEnabled(debug);
        return search.makeDecision(state);
	}

}