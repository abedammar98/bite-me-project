package Server;

import javafx.application.Application;
import javafx.stage.Stage;

import gui.ServerPortFrameController;

public class ServerUI extends Application {
	final public static int DEFAULT_PORT = 5555;
	public static EchoServer sv;

	public static void main(String args[]) throws Exception {
		launch(args);
	} // end main

	
	@Override
	public void start(Stage primaryStage) throws Exception {
		ServerPortFrameController aFrame = new ServerPortFrameController(); // create StudentFrame

		aFrame.start(primaryStage);
		runServer("5555");
	}
	
	public static void runServer(String p) {
		int port = 0; // Port to listen on

		try {
			port = Integer.parseInt(p); // Set port to 5555

		} catch (Throwable t) {
			System.out.println("ERROR - Could not connect!");
		}

		sv = new EchoServer(port);

		try {
			sv.listen(); // Start listening for connections
		} catch (Exception ex) {
			System.out.println("ERROR - Could not listen for clients!");
		}
	}

}
