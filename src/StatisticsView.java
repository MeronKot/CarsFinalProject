import java.sql.Connection;
import java.sql.Statement;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StatisticsView extends Application
{
	private Label txtMoney = new Label();
	private Button btnShow = new Button("Show");
	private TableView<?> table = new TableView<>();
	private Connection con;
	private Statement stmt;
	
	public void start(Stage primaryStage,Connection con) throws Exception 
	{
		this.con = con;
		start(primaryStage);
	}

	@Override
	public void start(Stage primaryStage) throws Exception 
	{
		HBox topPane = new HBox(2);
		topPane.getChildren().addAll(new Label("Current sum of cash:"),txtMoney);
		topPane.setAlignment(Pos.TOP_LEFT);
		VBox mainPane = new VBox(2);
		mainPane.getChildren().addAll(topPane,table);
		Scene scene = new Scene(mainPane,420,180);
		primaryStage.setTitle("CarRace Statistics");
		primaryStage.setScene(scene);
		primaryStage.setAlwaysOnTop(true);
		primaryStage.show();
		initializeDB();
		btnShow.setOnAction(e -> showDetails());
	}

	private Object showDetails() {
		// TODO Auto-generated method stub
		return null;
	}

	private void initializeDB() 
	{
		// TODO Auto-generated method stub
		
	}
}
