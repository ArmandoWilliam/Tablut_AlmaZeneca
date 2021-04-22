package it.unibo.ai.didattica.competition.tablut.almazeneca;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import it.unibo.ai.didattica.competition.tablut.client.TablutClient;
import it.unibo.ai.didattica.competition.tablut.client.TablutRandomClient;
import it.unibo.ai.didattica.competition.tablut.domain.GameAshtonTablut;
import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.domain.StateTablut;

public class TablutAlmaZenecaClient extends TablutClient {

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
		System.out.println("Selected client: " + args[0]);

		TablutRandomClient client = new TablutRandomClient(role, name, gametype, timeout, ipAddress);
		client.run();
	}

	@Override
	public void run() {
		try {
            this.declareName();
        } catch (Exception e) {
            e.printStackTrace();
        }
		
		//set state type and the white player as first
		State state = new StateTablut();
		state.setTurn(State.Turn.WHITE);
		
		//set game type
        GameAshtonTablut tablutGame = new GameAshtonTablut(0, -1, "logs", "white_ai", "black_ai");
        
        List<int[]> pawns = new ArrayList<int[]>();
		List<int[]> empty = new ArrayList<int[]>();
		
		System.out.println("You are player " + this.getPlayer().toString() + "!");
		
		while(true) {
			
			//update the current state from the server
            try {
                this.read();
            } catch (ClassNotFoundException | IOException e1) {
                e1.printStackTrace();
                System.exit(1);
            }
            //show the current state
            System.out.println("Current state:");
			state = this.getCurrentState();
			System.out.println(state.toString());
			
			//if white player
			if (this.getPlayer().equals(State.Turn.WHITE)) {
				//if is my turn (white player)
				if(state.getTurn().equals(State.Turn.WHITE)) {
					
				}
			}
		}
	}
	
}
