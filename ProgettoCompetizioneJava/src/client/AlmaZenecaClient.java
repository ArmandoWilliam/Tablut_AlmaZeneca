package client;

import java.io.IOException;
import java.net.UnknownHostException;

import Domain.MyRules;
import it.unibo.ai.didattica.competition.tablut.client.TablutClient;

public class AlmaZenecaClient extends TablutClient {

	public AlmaZenecaClient(String player, String name, int timeout, String ipAddress)
			throws UnknownHostException, IOException {
		super(player, name, timeout, ipAddress);
		// TODO Auto-generated constructor stub
		MyRules rules = null;
		//gestione varie rules
		//gestione varie strategie
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
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

}
