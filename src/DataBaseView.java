import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class DataBaseView extends Application{

	private ComboBox<String> cboTableName = new ComboBox<>();
	private TableView<?> tableView = new TableView<Object>();
	private Button btShowContents = new Button("Show Contents");
	private Label lblStatus = new Label();
	private Connection connection;
	private Statement stmt;


	public void start(Stage arg0, Connection connection) throws Exception {
		this.connection = connection;
		start(arg0);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		HBox hBox = new HBox(5);
		hBox.getChildren().addAll(new Label("Table Name"),cboTableName, btShowContents);
		hBox.setAlignment(Pos.CENTER);
		BorderPane pane = new BorderPane();
		pane.setCenter(tableView);
		pane.setTop(hBox);
		pane.setBottom(lblStatus);
		// Create a scene and place it in the stage
		Scene scene = new Scene(pane, 420, 180);
		primaryStage.setTitle("Cars Race DataBase"); // Set the stage title
		primaryStage.setScene(scene); // Place the scene in the stage
		primaryStage.setAlwaysOnTop(true);
		primaryStage.show(); // Display the stage  
		initializeDB();
		btShowContents.setOnAction(e -> showContents());
	}

	private void initializeDB()
	{ 
		try
		{ 
			stmt = connection.createStatement();
			DatabaseMetaData dbMetaData = connection.getMetaData();
			ResultSet rsTables = dbMetaData.getTables(null, null, null, new String[] {"TABLE"});
			while (rsTables.next())
			{ 
				cboTableName.getItems().add(rsTables.getString("TABLE_NAME"));
			}
			cboTableName.getSelectionModel().selectFirst();
		}
		catch (Exception ex)
		{ 
			ex.printStackTrace();
		}
	}

	private void showContents()
	{ 
		String tableName = cboTableName.getValue();
		try
		{ 
			String queryString = "select * from " + tableName;
			ResultSet resultSet = stmt.executeQuery(queryString);
			resultSet = stmt.executeQuery(queryString);
			populateTableView(resultSet, tableView);
		} 
		catch (SQLException ex)
		{ 
			ex.printStackTrace();
		}
	}

	private void populateTableView(ResultSet rs, TableView tableView)
	{
		tableView.getColumns().clear();
		ObservableList<String[]> data = FXCollections.observableArrayList();
		try
		{ 
			ResultSetMetaData metaData = rs.getMetaData();
			int count = metaData.getColumnCount();
			String columnName[] = new String[count];
			for (int i = 1; i <= count; i++)
			{
				int idx = i - 1;
				String str = metaData.getColumnLabel(i);
				TableColumn<String[], String> temp = new TableColumn<>(metaData.getColumnLabel(i));
				temp.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[idx]));
				temp.setMinWidth(100);
				tableView.getColumns().add(temp);
			}
			tableView.setItems(data);
			while(rs.next()){
				String[] info = new String[count];
				for(int i = 1 ; i <= count ; i++){
					info[i - 1] = rs.getString(i);
				}
				data.add(info);
			}

		} 
		catch (Exception e)
		{ 
			e.printStackTrace();
			System.out.println("Error on Building Data");
		}
	}



}
