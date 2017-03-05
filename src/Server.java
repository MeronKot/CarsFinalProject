import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sun.util.xml.PlatformXmlPropertiesProvider;

public class Server extends Application{

	private static TextArea ta = new TextArea();
	private ServerSocket serverSocket;
	private Socket socket;
	private ArrayList <View> viewList = new ArrayList<>();
	private ArrayList <Controller> controllerList = new ArrayList<>();
	private ArrayList <Model> modelList = new ArrayList<>();
	private ArrayList <PacketToServer> gamblers = new ArrayList<>();
	private int races = 0;
	@Override
	public void start(Stage primaryStage) throws Exception {
		ta.setEditable(false);
		Scene scene = new Scene(new ScrollPane(ta), 450, 200);
		primaryStage.setTitle("CarServer"); // Set the stage title
		primaryStage.setScene(scene); // Place the scene in the stage
		primaryStage.show(); // Display the stage
		//primaryStage.setAlwaysOnTop(true);
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
				ta.appendText(ex.getMessage());
			}
		}).start();
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
				outputToClient.writeObject(races);
				while (true){
					PacketToServer packet = (PacketToServer)inputFromClient.readObject();
					if(packet.gamblerClient()){
						gamblers.add(packet);
						//modelList.get(packet.getRaceId() - 1).setGambler(packet.getGamblerAmounts());
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
					}else{
						races++;
						View view = new View();
						Model model = new Model(races);
						Controller controller = new Controller(model, view);
						view.setModel(model);
						modelList.add(model);
						viewList.add(view);
						controllerList.add(controller);

						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								Stage stg = new Stage();
								Scene scene = new Scene(view.getBorderPane(), 750, 500);
								controller.setOwnerStage(stg);
								view.createAllTimelines();
								stg.setScene(scene);
								stg.setTitle("CarRaceView" + races);
								stg.setAlwaysOnTop(true);
								stg.show();
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
							}
						});

						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								ta.appendText("New race " + races + " is opened on " + new Date() + "\n");
							}
						});
					}
				}
			} catch (IOException | ClassNotFoundException e) {
				ta.appendText(e.getMessage());
				try {
					serverSocket.close();
					socket.close();
				} catch (IOException e1) {
					ta.appendText(e1.getMessage());
				}
			}

		}

	}

	public void checkAndStartRace(PacketToServer packet) {
		int raceId = packet.getRaceId();
		Model modelOfCurrentRace = modelList.get(raceId - 1);
		View viewOfCurrentRace = viewList.get(raceId - 1);
		Random rand = new Random();
		int Low = 1;
		int High = 50;

		modelOfCurrentRace.setGambler(packet.getGamblerAmounts());
		if(packet.getGamblerAmounts().size() >= 3)
			viewOfCurrentRace.playSong(packet.getGamblerAmounts());
	}
}
