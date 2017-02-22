import java.util.ArrayList;
import javafx.application.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
public class CarRaceMVC extends Application
{ private Button btnNewWindow = new Button("New Race");
  private ArrayList<Controller> controllerList;
  private ArrayList<View> viewList;
  private ArrayList<Model> modelList;
  private int raceCounter = 0;
  private CarLog log;
  @Override
  public void start(Stage primaryStage)
  {	BorderPane pane = new BorderPane();
	pane.setCenter(btnNewWindow);
	pane.setStyle("-fx-background-color: orange");
	Scene scene = new Scene(pane, 400, 100);
	primaryStage.setScene(scene); // Place the scene in the stage
	primaryStage.setTitle("CarRaceMVC"); // Set the stage title
	primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>()
	{ @Override
	  public void handle(WindowEvent event)
	  {	try
	    { Platform.exit();
		} 
	    catch (Exception e)
	    { // TODO Auto-generated catch block
		  e.printStackTrace();
		}
	  }
	});
	controllerList = new ArrayList<Controller>();
	viewList = new ArrayList<View>();
	modelList = new ArrayList<Model>();
	btnNewWindow.setOnAction(new EventHandler<ActionEvent>()
	{ @Override
	  public void handle(ActionEvent event)
	  {	createNewWindow();
	  }
	});
	primaryStage.show(); // Display the stage
	primaryStage.setAlwaysOnTop(true);
  }
  public static void main(String[] args)
  {	launch(args);
  }
  public void createNewWindow()
  {	Model model = new Model(raceCounter);
	View view = new View();
	Controller controller = new Controller(model, view);
	view.setModel(model);
	modelList.add(model);
	viewList.add(view);
	controllerList.add(controller);
	Stage stg = new Stage();
	Scene scene = new Scene(view.getBorderPane(), 750, 500);
	controller.setOwnerStage(stg);
	view.createAllTimelines();
	stg.setScene(scene);
	raceCounter++;
	stg.setTitle("CarRaceView" + raceCounter);
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
}