package kanji.client;

public class ClientGameStartCommand implements ClientCommand {
	private String[] subcommands;
	private Client client;
	
	public ClientGameStartCommand(Client c, String setupInfo) {
		subcommands = setupInfo.split(DELIMITER);
		client = c;
	}

	@Override
	public void execute() {
		if (subcommands.length >= 4) {
			int size = Integer.parseInt(subcommands[2].trim());
			String color = subcommands[3].trim();
			client.setPlayer(client.getClientName());
			client.setOpponent(subcommands[1].trim());
			if (color.equals(WHITE)) {
				client.getPlayer().setStone(WHITE);
				client.getOpponent().setStone(BLACK);
				System.out.println("You're WHITE, so wait for your opponent.");
				client.startGame(size, false);
			} else if (color.equals(BLACK)) {
				client.getPlayer().setStone(BLACK);
				client.getOpponent().setStone(WHITE);
				System.out.println("You're BLACK, so you start.");
				client.startGame(size, true);
			} else {
				client.sendCommand(FAILURE + DELIMITER + ILLEGALARGUMENT);
				return;
			}
			
		} else {
			client.sendCommand(FAILURE + DELIMITER + ARGUMENTSMISSING);
		}
	}

}
