package kanji.protocol;

import kanji.client.Client;

public class ClientGameStartCommand implements Command {
	private String[] subcommands;
	private Client client;
	
	public ClientGameStartCommand(Client c, String setupInfo) {
		subcommands = setupInfo.split(DELIMITER);
		client = c;
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		if (subcommands.length >= 4) {
			int size = Integer.parseInt(subcommands[2].trim());
			String color = subcommands[3].trim();
			client.setPlayer(client.getClientName());
			client.setOpponent(subcommands[1].trim());
			if (color.equals(WHITE)) {
				client.getPlayer().setStone(WHITE);
				client.getOpponent().setStone(BLACK);
				System.out.println("You're WHITE, so wait for your opponent.");
			} else if (color.equals(BLACK)) {
				client.getPlayer().setStone(BLACK);
				client.getOpponent().setStone(WHITE);
				System.out.println("You're BLACK, so you start.");
			} else {
				client.sendCommand(FAILURE + DELIMITER + ILLEGALARGUMENT);
				return;
			}
			client.startGame(size);
			
		} else {
			client.sendCommand(FAILURE + DELIMITER + ARGUMENTSMISSING);
		}
	}

}
