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
	private Color colors[] = { Color.RED, Color.AQUA, Color.BLUE, Color.GREEN,
			Color.YELLOW, Color.ORANGE, Color.PINK, Color.VIOLET, 
			Color.WHITE, Color.TRANSPARENT
	};
	
	public Controller(Model model, View view)
	{	
		this.model = model;
		this.view = view;
		//change all this to random, not input from user
	}
	

	public void setOwnerStage(Stage stg)
	{ 
		this.stg=stg;
	}

	public void errorAlert(String msg)
	{
		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(stg);
		alert.setTitle("Error");
		alert.setContentText(msg);
		alert.show();
	}
}