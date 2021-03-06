import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Gambler extends Application {

	private final int MAX_CARS = 5;
	private ArrayList<Integer> availableRaces;
	private Text [] cars = new Text[MAX_CARS];
	private TextArea [] money = new TextArea[MAX_CARS];
	private ObservableList<Integer> items = FXCollections.observableArrayList();
	private ListView<Integer> races;
	private Socket socket;
	private ObjectOutputStream toServer;
	private ObjectInputStream fromServer;
	private Stage currentStage;
	private int raceId;
	private String name;
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {

		//open connection to server to send the gambling
		try
		{
			socket = new Socket("localhost", 8000);
			toServer = new ObjectOutputStream(socket.getOutputStream());
			fromServer = new ObjectInputStream(socket.getInputStream());
			availableRaces = (ArrayList<Integer>)fromServer.readObject();
		}catch(IOException e){
		}

		BorderPane mainPane = new BorderPane();

		//Active races: + listView of races
		HBox activeRacesPane = createActiveRacesPane();		
		mainPane.setCenter(activeRacesPane);

		//Car #n + text area for each car to put money
		GridPane carsGambling = createCarsGamblingPane();
		HBox userDetails = createUserDetails();
		mainPane.setTop(userDetails);
		mainPane.setBottom(carsGambling);
		

		Scene scene = new Scene(mainPane,250,350);
		currentStage = primaryStage;
		primaryStage.setTitle("Gambler");
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setOnCloseRequest(
				new EventHandler<WindowEvent>()
				{ 
					public void handle(WindowEvent event)
					{ 
						try {
							GamblerDetailsToServer close = new GamblerDetailsToServer();
							close.setClose(true);
							toServer.writeObject(close);
							socket.close();
						} catch (IOException e) {
							System.out.println("i am here x");
						}
						primaryStage.close();
					} 
				});
	}

	private HBox createUserDetails() {
		HBox userDetails = new HBox(20);
		userDetails.setAlignment(Pos.CENTER);
		Label nameL = new Label("Name: ");
		TextArea nameField = new TextArea();
		nameField.setPrefSize(100, 5);
		Button ok = new Button("Submit");
		userDetails.getChildren().addAll(nameL,nameField,ok);
		ok.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if(nameField.getText().equals(""))
					alert(AlertType.ERROR, "You need to enter name");
				else{
					name = nameField.getText().trim();
					alert(AlertType.CONFIRMATION, "hello " + name);
				}
			}
		});
		return userDetails;
	}

	private HBox createActiveRacesPane(){
		HBox activeRacesPane = new HBox(40);
		Label active = new Label("Active races: ");
		activeRacesPane.getChildren().add(active);

		//to do: get active races from server for gambler offers
		races = new ListView<>();
		races.setItems(items);

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				for (Integer i : availableRaces)
					items.add(i);
				races.getSelectionModel().selectFirst();
			};
		});

		//races.getSelectionModel().selectFirst();
		races.setStyle("-fx-background-color: lightgrey");
		races.setOrientation(Orientation.HORIZONTAL);
		races.setPrefSize(100, 40);

		activeRacesPane.setAlignment(Pos.CENTER);
		activeRacesPane.getChildren().add(races);

		return activeRacesPane; 
	}

	private GridPane createCarsGamblingPane() {
		GridPane grid = new GridPane();
		//grid.setGridLinesVisible(true);
		grid.setAlignment(Pos.CENTER);
		grid.setVgap(10);
		grid.setHgap(10);
		grid.setPadding(new Insets(10,10,10,10));



		for (int i = 0 ; i < MAX_CARS ; i++)
		{
			cars[i] = new Text("Car #" + (i + 1));
			money[i] = new TextArea();
			money[i].setPrefSize(100, 5);
			grid.add(cars[i],0,i);
			grid.add(money[i],1,i);
		}

		Button go = new Button("Go!");
		go.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (name == null){
					alert(AlertType.ERROR, "Enter name please");
					return;
				}
				
				
				if (availableRaces.size() > 0){
					HashMap<Integer,Double> arr = new HashMap<>();
					try
					{
						for (int i = 0 ; i < MAX_CARS ; i++)
							if(!(money[i].getText().equals("")))
								arr.put(i,Double.parseDouble(money[i].getText()));
							else{
								arr.put(i,0.0);
							}

						raceId = races.getSelectionModel().getSelectedItem();
						GamblerDetailsToServer packet = new GamblerDetailsToServer(name,arr,raceId);
						toServer.writeObject(packet);
						alert(AlertType.INFORMATION,"Goodluck");
						try {
							GamblerDetailsToServer close = new GamblerDetailsToServer();
							close.setClose(true);
							toServer.writeObject(close);
							socket.close();
						} catch (IOException e) {
							System.out.println("i am here goodLuck");
						}
						currentStage.close();
					}catch(IOException | NumberFormatException e){
						alert(AlertType.ERROR,e.getMessage());
						try {
							GamblerDetailsToServer close = new GamblerDetailsToServer();
							close.setClose(true);
							toServer.writeObject(close);
							socket.close();
						} catch (IOException e1) {
							System.out.println("i am here notGood");
						}
						currentStage.close();
					}
				}else{
					alert(AlertType.ERROR,"No available races");
					try {
						GamblerDetailsToServer close = new GamblerDetailsToServer();
						close.setClose(true);
						toServer.writeObject(close);
						socket.close();
					} catch (IOException e1) {
						System.out.println("i am here no races");
					}
					currentStage.close();
				}
			}
		});
		grid.add(go, 1, cars.length);
		return grid;
	}

	public void alert(AlertType type,String msg)
	{ 
		Alert alert = new Alert(type);
		alert.setContentText(msg);
		alert.setHeaderText(null);
		alert.showAndWait();
	}
}
