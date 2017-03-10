import java.io.File;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import com.sun.xml.internal.ws.api.pipe.ThrowableContainerPropertySet;

import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.media.AudioClip;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaMarkerEvent;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
public class View implements Serializable 
{ 
	private Model model;
	private BorderPane border_pane;
	private GridPane details_grid, cars_grid;
	private CarPane car_pane1, car_pane2, car_pane3, car_pane4, car_pane5;
	private Label lbl1, lbl2, lbl3,lbl4,lbl5;
	private Slider slRadius;
	private TextField spd_txt1, spd_txt2, spd_txt3,spd_txt4,spd_txt5;
	private ComboBox<String> colorComBox, carIdComBox;
	private ObservableList<String> items_color, items_car;
	private Button btn;

	private Connection con;
	
	public View(/*Connection con*/) throws SQLException
	{	
		border_pane = new BorderPane();
		//createDetailsGrid();
		//border_pane.setTop(details_grid);
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
	/*
	public void randomSpeed(HashMap<Integer, Double> hashMap){
		Iterator it = hashMap.entrySet().iterator();
		Random rand = new Random();
		int Low = 1;
		int High = 50;
		while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        model.changeSpeed((int) pair.getKey(), rand.nextInt(High - Low) + Low);
	    }	
	}
	*/
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
	public void createColorsComBox()
	{	
		String colorNames[] =
			{ "RED", "AQUA", "BLUE", "GREEN", "YELLOW", 
					"ORANGE", "PINK", "VIOLET", "WHITE", "TRANSPARENT"
			};
		items_color = FXCollections.observableArrayList(colorNames);
		colorComBox = new ComboBox<>();
		colorComBox.getItems().addAll(items_color);
		colorComBox.setMinWidth(200);
		colorComBox.setValue("RED");
	}
	public void createCarIdComBox()
	{	
		items_car = FXCollections.observableArrayList();
		for (int i = 1; i <= 5; i++)
			items_car.add("car #" + i);
		carIdComBox = new ComboBox<>();
		carIdComBox.getItems().addAll(items_car);
		carIdComBox.setMinWidth(120);
		carIdComBox.setValue("car #1");
	}
	public void createSlider()
	{	
		slRadius = new Slider(5, 20, 0);
		slRadius.setShowTickLabels(true);
		slRadius.setShowTickMarks(true);
		slRadius.setMajorTickUnit(5);
		slRadius.setBlockIncrement(1);
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
	public void createDetailsGrid()
	{	
		GridPane pane = new GridPane();
		details_grid = new GridPane();
		btn = new Button("Change Color");
		btn.setMinWidth(200);
		lbl1 = new Label("car #1: ");
		lbl2 = new Label("car #2: ");
		lbl3 = new Label("car #3: ");
		lbl4 = new Label("car #4: ");
		lbl5 = new Label("car #5: ");
		spd_txt1 = new TextField();
		spd_txt2 = new TextField();
		spd_txt3 = new TextField();
		spd_txt4 = new TextField();
		spd_txt5 = new TextField();
		createColorsComBox();
		createCarIdComBox();
		createSlider();
		pane.add(colorComBox, 0, 0);
		pane.add(carIdComBox, 1, 0);
		pane.add(btn, 2, 0);
		details_grid.add(lbl1, 0, 0);
		details_grid.add(spd_txt1, 0, 1);
		details_grid.add(lbl2, 1, 0);
		details_grid.add(spd_txt2, 1, 1);
		details_grid.add(lbl3, 2, 0);
		details_grid.add(spd_txt3, 2, 1);
		details_grid.add(lbl4, 3, 0);
		details_grid.add(spd_txt4, 3, 1);
		details_grid.add(lbl5, 4, 0);
		details_grid.add(spd_txt5, 4, 1);
		details_grid.add(pane, 5, 0);
		details_grid.add(slRadius, 5, 1);
	}

	public BorderPane getBorderPane()
	{	
		return border_pane;
	}
	public GridPane getDetailsGrid()
	{	
		return details_grid;
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
	public TextField getSpeedTxt1()
	{	
		return spd_txt1;
	}
	public TextField getSpeedTxt2()
	{	
		return spd_txt2;
	}
	public TextField getSpeedTxt3()
	{	
		return spd_txt3;
	}
	public TextField getSpeedTxt4()
	{	
		return spd_txt4;
	}
	public TextField getSpeedTxt5()
	{	
		return spd_txt5;
	}
	public ObservableList<String> getItemsCar()
	{	
		return items_car;
	}
	public ObservableList<String> getItemsColor()
	{	
		return items_color;
	}
	public ComboBox<String> getColorComBox()
	{	
		return colorComBox;
	}
	public ComboBox<String> getCarIdComBox()
	{	
		return carIdComBox;
	}
	public Button getColorButton()
	{	
		return btn;
	}
	public Slider getRadSlider()
	{	
		return slRadius;
	}
	/*
	public void playSong(HashMap<Integer, Double> hashMap) throws SQLException {
		//mediaPlayer.play();
		//length = mediaPlayer.getTotalDuration();
		length = sound.getDuration();
		int interval = 1;//one second
		ObservableMap<String, Duration> partsMap = sound.getMarkers();
		times = 0;
		for (times = 0 ; times < length.toMinutes() ; times += interval){
			partsMap.put("part" + times, Duration.minutes(times));
		}
		
		mediaPlayer.setOnMarker(new EventHandler<MediaMarkerEvent>() {	
			@Override
			public void handle(MediaMarkerEvent event) {
				randomSpeed(hashMap);				
			}
		});
		new Thread(){
			@Override
			public void run() {
				mediaPlayer.play();
			}
		}.start();

	}
	*/
}
