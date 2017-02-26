import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
public class Controller implements CarEvents
{
	private final int MAXSPEED = 200;
	private final int CAR1_ID = 0;
	private final int CAR2_ID = 1;
	private final int CAR3_ID = 2;
	private final int CAR4_ID = 3;
	private final int CAR5_ID = 4;
	private Stage stg;
	private Model model;
	private View view;
	private Color colors[] = 
		{ Color.RED, Color.AQUA, Color.BLUE, Color.GREEN,
				Color.YELLOW, Color.ORANGE, Color.PINK, Color.VIOLET, 
				Color.WHITE, Color.TRANSPARENT
		};
	public Controller(Model model, View view)
	{	this.model = model;
	this.view = view;
	//speedHandlers();
	// handle radius change
	Slider radSlider = view.getRadSlider();
	Button btn = view.getColorButton();
	
	//change all this to random, not input from user
	
	/*
	radSlider.valueProperty().addListener(
			e -> 
			{	int car_index = view.getItemsCar().indexOf(
					view.getCarIdComBox().getValue());
			int oldRad = model.getCarById(car_index).getRadius();
			int newRad = (int) radSlider.getValue();
			if (oldRad != newRad)
				model.changeRadius(car_index, newRad);
			});
			
	// handle color change
	Button btn = view.getColorButton();
	
	btn.setOnAction(new EventHandler<ActionEvent>()
	{	@Override
		public void handle(ActionEvent event)
	{ changeColorView();
	}
	});
	
	// make the slider's value suitable to the selected car in the combo box
	view.getCarIdComBox().setOnAction(
			new EventHandler<ActionEvent>()
			{	@Override
				public void handle(ActionEvent event)
			{ int car_index = view.getItemsCar().indexOf(
					view.getCarIdComBox().getValue());
			int r = model.getCarById(car_index).getRadius();
			view.getRadSlider().setValue(r);
			}
			});
	}
	*/
	}
	public void changeColorView()
	{ int car_index = view.getItemsCar().indexOf(
			view.getCarIdComBox().getValue());
	int color_index = view.getItemsColor().indexOf(
			view.getColorComBox().getValue());
	model.changeColor(car_index, colors[color_index]);
	}
	public void setSpeedModelView(TextField tf, int n)
	{ 
		String msg = null;
		try
		{ 
			if (!tf.getText().equals(""))
			{ 
				Double speed = Double.parseDouble(tf.getText());
				if (0 <= speed && speed <= MAXSPEED)
				{ 
					model.changeSpeed(n, speed);
				} 
				else
					if (speed > MAXSPEED)
					{ 
						msg = "You're driving too fast!!! Speed above "
								+ MAXSPEED + "!!!";
					} 
					else
					{ 
						msg = "Only Numbers Great or Equals 0 ";
					}
			}
		} 
		catch (Exception e)
		{ msg = "Only Numbers Great or Equals 0 ";
		}
		if (msg != null)
			try
		{ errorAlert(msg);
		} 
		catch (Exception e)
		{ System.out.println(e.toString());
		}
	}
	public void speedHandlers()
	{ TextField tf1 = view.getSpeedTxt1();
	tf1.setOnAction(new EventHandler<ActionEvent>()
	{ @Override
		public void handle(ActionEvent event)
	{ setSpeedModelView(tf1, CAR1_ID);
	}
	});
	TextField tf2 = view.getSpeedTxt2();
	tf2.setOnAction(new EventHandler<ActionEvent>()
	{ @Override
		public void handle(ActionEvent event)
	{ // TODO Auto-generated method stub
		setSpeedModelView(tf2, CAR2_ID);
	}
	});
	TextField tf3 = view.getSpeedTxt3();
	tf3.setOnAction(new EventHandler<ActionEvent>()
	{ @Override
		public void handle(ActionEvent event)
	{ // TODO Auto-generated method stub
		setSpeedModelView(tf3, CAR3_ID);
	}
	});
	TextField tf4 = view.getSpeedTxt4();
	tf4.setOnAction(new EventHandler<ActionEvent>()
	{ @Override
		public void handle(ActionEvent event)
	{ // TODO Auto-generated method stub
		setSpeedModelView(tf4, CAR4_ID);
	}
	});
	TextField tf5 = view.getSpeedTxt5();
	tf5.setOnAction(new EventHandler<ActionEvent>()
	{ @Override
		public void handle(ActionEvent event)
	{ // TODO Auto-generated method stub
		setSpeedModelView(tf5, CAR5_ID);
	}
	});
	}
	public void setOwnerStage(Stage stg)
	{ this.stg=stg;
	}
	public void errorAlert(String msg)
	{ Alert alert = new Alert(AlertType.ERROR);
	alert.initOwner(stg);
	alert.setTitle("Error");
	alert.setContentText(msg);
	alert.show();
	}
}