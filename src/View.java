import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
public class View implements Serializable 
{ 
	private Model model;
	private BorderPane border_pane;
	private GridPane cars_grid;
	private CarPane car_pane1, car_pane2, car_pane3, car_pane4, car_pane5;
	private Button btn;

	private Connection con;

	public View() throws SQLException
	{	
		border_pane = new BorderPane();
		createCarsGrid();
		border_pane.setCenter(cars_grid);
	}
	/*
	public void saveCarsToDB() throws SQLException {
		statement.execute("insert into car values('0','" + car_pane1.getCarModel().getSpeed() +"','"
	+ car_pane1.getCarModel().getColor().toString() 
	+ "','" + car_pane1.getCarModel().getRadius() +"')");

		statement.execute("insert into car values('1','" + car_pane2.getCarModel().getSpeed() +"','"
				+ car_pane2.getCarModel().getColor().toString() 
				+ "','" + car_pane2.getCarModel().getRadius() +"')");

		statement.execute("insert into car values('2','" + car_pane3.getCarModel().getSpeed() +"','"
				+ car_pane3.getCarModel().getColor().toString() 
				+ "','" + car_pane3.getCarModel().getRadius() +"')");

		statement.execute("insert into car values('3','" + car_pane4.getCarModel().getSpeed() +"','"
				+ car_pane4.getCarModel().getColor().toString() 
				+ "','" + car_pane4.getCarModel().getRadius() +"')");

		statement.execute("insert into car values('4','" + car_pane5.getCarModel().getSpeed() +"','"
				+ car_pane5.getCarModel().getColor().toString() 
				+ "','" + car_pane5.getCarModel().getRadius() +"')");
	}
	 */
	public void endRace() {
		car_pane1.getTimeline().pause();
		car_pane2.getTimeline().pause();
		car_pane3.getTimeline().pause();
		car_pane4.getTimeline().pause();
		car_pane5.getTimeline().pause();
	}

	public void setModel(Model myModel)
	{	
		model = myModel;
		if (model != null)
		{ 
			car_pane1.setCarModel(model.getCarById(0));
			car_pane2.setCarModel(model.getCarById(1));
			car_pane3.setCarModel(model.getCarById(2));
			car_pane4.setCarModel(model.getCarById(3));
			car_pane5.setCarModel(model.getCarById(4));
		}
	}
	public Model getModel(Model myModel)
	{	
		return model;
	}


	public void createCarsGrid()
	{	
		cars_grid = new GridPane();
		car_pane1 = new CarPane();
		car_pane2 = new CarPane();
		car_pane3 = new CarPane();
		car_pane4 = new CarPane();
		car_pane5 = new CarPane();
		cars_grid.add(car_pane1, 0, 0);
		cars_grid.add(car_pane2, 0, 1);
		cars_grid.add(car_pane3, 0, 2);
		cars_grid.add(car_pane4, 0, 3);
		cars_grid.add(car_pane5, 0, 4);
		cars_grid.setStyle("-fx-background-color: beige");
		cars_grid.setGridLinesVisible(true);
		ColumnConstraints column = new ColumnConstraints();
		column.setPercentWidth(100);
		cars_grid.getColumnConstraints().add(column);
		RowConstraints row = new RowConstraints();
		row.setPercentHeight(33);
		cars_grid.getRowConstraints().add(row);
		cars_grid.getRowConstraints().add(row);
		cars_grid.getRowConstraints().add(row);
		cars_grid.getRowConstraints().add(row);
		cars_grid.getRowConstraints().add(row);
	}
	public void createAllTimelines()
	{	
		car_pane1.createTimeline();
		car_pane2.createTimeline();
		car_pane3.createTimeline();
		car_pane4.createTimeline();
		car_pane5.createTimeline();
	}


	public BorderPane getBorderPane()
	{	
		return border_pane;
	}

	public GridPane getCarsGrid()
	{	
		return cars_grid;
	}
	public void setCarPanesMaxWidth(double newWidth)
	{	
		car_pane1.setMaxWidth(newWidth);
		car_pane2.setMaxWidth(newWidth);
		car_pane3.setMaxWidth(newWidth);
		car_pane4.setMaxWidth(newWidth);
		car_pane5.setMaxWidth(newWidth);	
	}
	public Pane getCarPane1()
	{	
		return car_pane1;
	}
	public Pane getCarPane2()
	{	
		return car_pane2;
	}
	public Pane getCarPane3()
	{	
		return car_pane3;
	}
	public Pane getCarPane4()
	{	
		return car_pane4;
	}
	public Pane getCarPane5()
	{	
		return car_pane5;
	}

	public Button getColorButton()
	{	
		return btn;
	}

}
