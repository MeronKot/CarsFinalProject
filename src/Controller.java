import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
public class Controller implements CarEvents
{

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