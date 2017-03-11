import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
public class CarRaceMVC extends Application
{ 
	private Button btnServer = new Button("Server");
	private Button btnRace = new Button("Race");
	private Button btnGambler = new Button("Gambler");
	private int raceCounter = 0;
	private boolean isServerOn = false;
	private Socket socket;
	private ObjectOutputStream toServer;
	private ObjectInputStream fromServer;
	private Model model;

	@Override
	public void start(Stage primaryStage)
	{			
		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(40));
		pane.setLeft(btnServer);
		BorderPane.setAlignment(btnServer, Pos.CENTER_LEFT);
		pane.setCenter(btnRace);
		pane.setRight(btnGambler);
		BorderPane.setAlignment(btnGambler, Pos.CENTER_RIGHT);
		pane.setStyle("-fx-background-color: orange");
		Scene scene = new Scene(pane, 400, 100);
		primaryStage.setScene(scene); // Place the scene in the stage
		primaryStage.setTitle("CarRaceMVC"); // Set the stage title
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>()
		{ @Override
			public void handle(WindowEvent event)
		{	
			try
			{ 
				Platform.exit();
			} 
			catch (Exception e)
			{ // TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		});

		//button to start the multi thread server. must be the first click.
		//create separate thread to each race.
		btnServer.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					Stage server = new Stage();
					new Server().start(server);
					isServerOn = true;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		//button to start the race. check that server is on.
		btnRace.setOnAction(new EventHandler<ActionEvent>()
		{ @Override
			public void handle(ActionEvent event)
		{	
			if(isServerOn)
			{

				//open connection to server to send the race and then gambler
				try
				{
					socket = new Socket("localhost", 8000);
					toServer = new ObjectOutputStream(socket.getOutputStream());
					fromServer = new ObjectInputStream(socket.getInputStream());
				}catch(IOException e){
					System.out.println(e.getMessage());
				}

				createNewWindow();
			}else{
				alert(AlertType.ERROR, "You should run the server first");
			}
		}
		});

		//button to start gambler. get list view of available races
		//from server (kind of client) and gamble on cars he want.
		//send which race and the amount of money of each car to server.
		btnGambler.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Stage gambler = new Stage();
				try {
					if(isServerOn){
						new Gambler().start(gambler);

						new Thread(new Runnable() {

							@Override
							public void run() {
								while(true){
									try {
										PacketToClient play = (PacketToClient) fromServer.readObject();
										if (play.isPlay())
										{
											play.getGamModel().playSong(play.getHashMap());
										}
									} catch (SQLException | ClassNotFoundException | IOException e) {
										// TODO Auto-generated catch block
										System.out.println(e.getMessage());
									}
								}								
							}
						}).start();

					}else{
						alert(AlertType.ERROR, "You should run the server first");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		primaryStage.show(); // Display the stage
	}

	public static void main(String[] args)
	{	
		launch(args);
	}

	public void createNewWindow()
	{	
		try {
			GamblerDetailsToServer packet = new GamblerDetailsToServer();
			toServer.writeObject(packet);
			ArrayList<Integer> available = (ArrayList<Integer>) fromServer.readObject();
			PacketToClient input = (PacketToClient) fromServer.readObject();
			View view = new View();
			model = input.getGamModel();
			view.setModel(model);
			Controller controller = new Controller(input.getGamModel(),view);
			Stage race = new Stage();
			Scene scene = new Scene(view.getBorderPane(),750,500);
			view.createAllTimelines();
			race.setScene(scene);
			race.show();
			scene.widthProperty().addListener(
					new ChangeListener<Number>()
					{ @Override
						public void changed(
								ObservableValue<? extends Number> observable,
								Number oldValue, Number newValue)
					{	// TODO Auto-generated method stub
						view.setCarPanesMaxWidth(newValue.doubleValue());
					}
					});
		} catch (IOException | SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
	}


	public void alert(AlertType type,String msg)
	{ 
		Alert alert = new Alert(type);
		alert.setContentText(msg);
		alert.setHeaderText(null);
		alert.showAndWait();
	}
}