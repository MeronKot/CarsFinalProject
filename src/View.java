import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
public class View
{ private Model model;
  private BorderPane border_pane;
  private GridPane details_grid, cars_grid;
  private CarPane car_pane1, car_pane2, car_pane3;
  private Label lbl1, lbl2, lbl3;
  private Slider slRadius;
  private TextField spd_txt1, spd_txt2, spd_txt3;
  private ComboBox<String> colorComBox, carIdComBox;
  private ObservableList<String> items_color, items_car;
  private Button btn;
  public View()
  {	border_pane = new BorderPane();
	createDetailsGrid();
	border_pane.setTop(details_grid);
	createCarsGrid();
	border_pane.setCenter(cars_grid);
  }
  public void setModel(Model myModel)
  {	model = myModel;
	if (model != null)
	{ car_pane1.setCarModel(model.getCarById(0));
	  car_pane2.setCarModel(model.getCarById(1));
	  car_pane3.setCarModel(model.getCarById(2));
	}
  }
  public Model getModel(Model myModel)
  {	return model;
  }
  public void createColorsComBox()
  {	String colorNames[] =
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
  {	items_car = FXCollections.observableArrayList();
	for (int i = 1; i <= 3; i++)
	  items_car.add("car #" + i);
	carIdComBox = new ComboBox<>();
	carIdComBox.getItems().addAll(items_car);
	carIdComBox.setMinWidth(120);
	carIdComBox.setValue("car #1");
  }
  public void createSlider()
  {	slRadius = new Slider(5, 20, 0);
	slRadius.setShowTickLabels(true);
	slRadius.setShowTickMarks(true);
	slRadius.setMajorTickUnit(5);
	slRadius.setBlockIncrement(1);
  }
  public void createCarsGrid()
  {	cars_grid = new GridPane();
	car_pane1 = new CarPane();
	car_pane2 = new CarPane();
	car_pane3 = new CarPane();
	cars_grid.add(car_pane1, 0, 0);
	cars_grid.add(car_pane2, 0, 1);
	cars_grid.add(car_pane3, 0, 2);
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
  }
  public void createAllTimelines()
  {	car_pane1.createTimeline();
	car_pane2.createTimeline();
	car_pane3.createTimeline();
  }
  public void createDetailsGrid()
  {	GridPane pane = new GridPane();
	details_grid = new GridPane();
	btn = new Button("Change Color");
	btn.setMinWidth(200);
	lbl1 = new Label("car #1: ");
	lbl2 = new Label("car #2: ");
	lbl3 = new Label("car #3: ");
	spd_txt1 = new TextField();
	spd_txt2 = new TextField();
	spd_txt3 = new TextField();
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
	details_grid.add(pane, 3, 0);
	details_grid.add(slRadius, 3, 1);
  }
  public BorderPane getBorderPane()
  {	return border_pane;
  }
  public GridPane getDetailsGrid()
  {	return details_grid;
  }
  public GridPane getCarsGrid()
  {	return cars_grid;
  }
  public void setCarPanesMaxWidth(double newWidth)
  {	car_pane1.setMaxWidth(newWidth);
	car_pane2.setMaxWidth(newWidth);
	car_pane3.setMaxWidth(newWidth);
  }
  public Pane getCarPane1()
  {	return car_pane1;
  }
  public Pane getCarPane2()
  {	return car_pane2;
  }
  public Pane getCarPane3()
  {	return car_pane3;
  }
  public TextField getSpeedTxt1()
  {	return spd_txt1;
  }
  public TextField getSpeedTxt2()
  {	return spd_txt2;
  }
  public TextField getSpeedTxt3()
  {	return spd_txt3;
  }
  public ObservableList<String> getItemsCar()
  {	return items_car;
  }
  public ObservableList<String> getItemsColor()
  {	return items_color;
  }
  public ComboBox<String> getColorComBox()
  {	return colorComBox;
  }
  public ComboBox<String> getCarIdComBox()
  {	return carIdComBox;
  }
  public Button getColorButton()
  {	return btn;
  }
  public Slider getRadSlider()
  {	return slRadius;
  }
}
