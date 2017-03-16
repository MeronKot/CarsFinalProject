import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Server extends Application{

	private TextArea ta = new TextArea();
	private ServerSocket serverSocket;
	private Socket socket;
	private HashMap <Integer,Model> modelList = new HashMap<>();
	private int races = 0;
	private ArrayList<Integer> availableRaces = new ArrayList<>();
	private Connection connection;
	private Statement statement;
	private Date dateOfRace;
	private int gamblerCount = 0;
	private PacketToClient packetToClient;

	@Override
	public void start(Stage primaryStage) throws Exception {
		startDB();
		ta.setEditable(false);
		VBox pane = new VBox();
		Button data = new Button("Current Data");
		Button stat = new Button("Statistics");
		data.setPrefSize(450, 10);
		stat.setPrefSize(450, 10);
		data.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Stage tableView = new Stage();
				try {
					new DataBaseView().start(tableView,connection);
				} catch (Exception e) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							ta.appendText(e.getMessage() + '\n');
						}
					});
				}
			}
		});

		stat.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Stage tableView = new Stage();
				try {
					new StatisticsView().start(tableView,connection);
				} catch (Exception e) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							ta.appendText(e.getMessage() + '\n');
						}
					});
				}
			}
		});

		pane.getChildren().addAll(new ScrollPane(ta),data,stat);
		Scene scene = new Scene(pane, 450, 200);
		primaryStage.setTitle("CarServer"); // Set the stage title
		primaryStage.setScene(scene); // Place the scene in the stage
		primaryStage.show(); // Display the stage
		primaryStage.setOnCloseRequest(
				new EventHandler<WindowEvent>()
				{ public void handle(WindowEvent event)
				{ 
					Platform.exit();
					System.exit(0);
				} 
				});
		new Thread(() -> {
			try{
				serverSocket = new ServerSocket(8000);
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						ta.appendText("Server started at " + new Date() + "\n");
					}
				});
				while(true){
					socket = serverSocket.accept();
					new Thread(new HandleRace(socket)).start();
				}
			}catch(IOException ex){
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						ta.appendText(ex.getMessage());
					}
				});				
			}
		}).start();
	}

	private void startDB() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection
					("jdbc:mysql://localhost/carsRace", "scott", "tiger");

			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					ta.appendText("Driver loaded\n");
					ta.appendText("Database connected\n");			
				}
			});

			statement = connection.createStatement();
			//createTables();
			ResultSet resRace = statement.executeQuery("select count(*) from race");
			while (resRace.next()) {
				races =  resRace.getInt(1);
			}

			ResultSet resGamblers = statement.executeQuery("select count(*) from gambler");
			while (resGamblers.next()) {
				gamblerCount  =  resGamblers.getInt(1);
			}

		} catch (ClassNotFoundException | SQLException e) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					ta.appendText(e.getMessage());
				}
			});
		}
	}

	private void createTables() throws SQLException {

		statement.execute("create table Race(raceId char(5) not null, cars varchar(25), dateOfRace date, totalAmount varchar(25), winCar varchar(25), systemCash varchar(25), "
				+ "constraint pkRace primary key (raceId))");

		statement.execute("create table Gambler(gamblerId char(5) not null, gambleName varchar(25), raceId varchar(25),"
				+ " car1 varchar(25), car2 varchar(25), car3 varchar(25), car4 varchar(25), car5 varchar(25),"
				+ " constraint pkGambler primary key (gamblerId),"
				+ " constraint fkRaceId foreign key (raceId) references Race(raceId))");

		statement.execute("create table Car(carId char(5) not null, speed varchar(25), color varchar(25), wheelRadius varchar(25),"
				+ " constraint pkCar primary key (carId))");

		statement.execute("create table System(sysId char(5) not null, cash varchar(25)," 
				+ "constraint pkCar primary key (sysId))");
				
	}

	class HandleRace implements Runnable
	{
		private Socket socket;

		public HandleRace(Socket socket){
			this.socket = socket;
		}

		@Override
		public void run() {
			try {
				ObjectInputStream inputFromClient = new ObjectInputStream(socket.getInputStream());
				ObjectOutputStream outputToClient = new ObjectOutputStream(socket.getOutputStream());

				//first send to client the available races for list view 
				outputToClient.writeObject(availableRaces);
				while (true){
					GamblerDetailsToServer packet = (GamblerDetailsToServer)inputFromClient.readObject();
					if (packet.isClose())
						break;
					if(packet.gamblerClient()){
						checkAndStartRace(packet);
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								HashMap<Integer, Double> gam = packet.getGamblerAmounts();
								ta.appendText("New gambler, race no: " + packet.getRaceId() + '\n');
								Iterator it = gam.entrySet().iterator();
								while (it.hasNext()) {
									Map.Entry pair = (Map.Entry)it.next();
									ta.appendText(((Integer)pair.getKey() + 1) + "\t" + pair.getValue() + '\n');
								}
							}
						});
						break;
					}else{
						races++;
						availableRaces.add(races);
						Model model = new Model(races,gamblerCount,outputToClient);
						dateOfRace = new Date();
						model.setDate(dateOfRace);
						packetToClient = new PacketToClient(connection,model,races);
						outputToClient.writeObject(packetToClient);
						modelList.put(races - 1, model);
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								ta.appendText("New race " + races + " is opened on " + dateOfRace + "\n");
							}
						});
						break;
					}

				}
			} catch (IOException | ClassNotFoundException | NullPointerException | SQLException e) {
				Platform.runLater(new Runnable() {

					@Override
					public void run() {	
						ta.appendText(e.getMessage());						
					}
				});

				try {
					serverSocket.close();
					socket.close();
				} catch (IOException e1) {
					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							ta.appendText(e1.getMessage());			
						}
					});

				}
			}

		}

	}

	public void checkAndStartRace(GamblerDetailsToServer packet) throws SQLException {
		int raceId = packet.getRaceId();
		Model modelOfCurrentRace = modelList.get(raceId - 1);
		Random rand = new Random();
		int Low = 1;
		int High = 50;

		
		modelOfCurrentRace.setGambler(packet.getGamblerAmounts(),packet.getGamblerName());
		if(modelOfCurrentRace.checkIfRaceReady())
		{
			try {
				PacketToClient play = new PacketToClient(true,modelOfCurrentRace.getGamblers().get(modelOfCurrentRace.getGamblerCounter() - 1));
				play.setGamModel(modelOfCurrentRace);
				play.setDateOfRace(modelOfCurrentRace.getDateOfRace());
				modelOfCurrentRace.getOutToClientOfThisModel().writeObject(play);
			} catch (IOException e) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						ta.appendText(e.getMessage());						
					}
				});
			}
		}else{
			try {
				PacketToClient play = new PacketToClient(false,modelOfCurrentRace.getGamblers().get(modelOfCurrentRace.getGamblerCounter()));
				play.setGamModel(modelOfCurrentRace);
				modelOfCurrentRace.getOutToClientOfThisModel().writeObject(play);
			} catch (IOException e) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						ta.appendText(e.getMessage());						
					}
				});
			}
		}
	}
}
