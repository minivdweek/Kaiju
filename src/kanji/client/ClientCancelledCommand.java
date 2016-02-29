package kanji.client;

public class ClientCancelledCommand implements ClientCommand {

	public ClientCancelledCommand(Client c, String setupInfo) {
	}

	@Override 
	public void execute() {
		System.out.println("Succesfully cancelled your request.");
	}

}
