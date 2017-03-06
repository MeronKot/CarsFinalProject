import java.io.File;
import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;
public class CarPane extends Pane implements CarEvents
{ 
	class ColorEvent implements  EventHandler<Event>
	{ 
		@Override
		public void handle(Event event)
		{	
			setColor(car.getColor());
		}
	}

	class RadiusEvent implements EventHandler<Event>
	{ 
		@Override
		public void handle(Event event)
		{	
			setRadius(car.getRadius());
		}
	}

	class SpeedEvent implements EventHandler<Event>
	{ 
		@Override
		public void handle(Event event)
		{ 
			setSpeed(car.getSpeed());
		}
	}

	final int MOVE=1;
	final int STOP=0;
	private double xCoor;
	private double yCoor;
	private double shade = 1;
	private Timeline tl; // speed=setRate()
	private Color color;
	private int r;// radius
	private Car car;
	private Color colors[] = { Color.RED, Color.AQUA, Color.BLUE, Color.GREEN,
			Color.YELLOW, Color.ORANGE, Color.PINK, Color.VIOLET, 
			Color.WHITE, Color.TRANSPARENT
	};
	Polygon back,front,rightUp,rightDown,leftUp,leftDown,top,left,right;
	Group carGroup;

	
	public CarPane()
	{	
		xCoor = 0;
		r = 5;
		int idx = (int)(Math.random() * 10);
		color = colors[idx];
	}

	public void setCarModel(Car myCar)
	{	
		car = myCar;
		car.setColor(color);
		if (car != null)
		{ 
			car.addEventHandler(new ColorEvent(), eventType.COLOR);
			car.addEventHandler(new RadiusEvent(), eventType.RADIUS);
			car.addEventHandler(new SpeedEvent(), eventType.SPEED);
		}
	}

	public Car getCarModel()
	{	
		return car;
	}

	public void moveCar(int n)
	{	
		yCoor = getHeight();
		setMinSize(10 * r, 6 * r);
		if (xCoor > getWidth())
		{ xCoor = -10 * r;
		} 
		else
		{ xCoor += n;
		}
		// Draw the car
		carGroup = new Group();
		front = new Polygon(xCoor, yCoor - r,
							xCoor, yCoor - 4 * r,
							xCoor + 2 * r, yCoor - 4 * r, 
							xCoor + 4 * r, yCoor - 6 * r,
							xCoor + 6 * r, yCoor - 6 * r, 
							xCoor + 8 * r, yCoor - 4 * r,
							xCoor + 10 * r,yCoor - 4 * r,
							xCoor + 10 * r,yCoor - r);
		front.setFill(color);
		front.setStroke(Color.BLACK);

		back = new Polygon(xCoor+3, (yCoor - r)-5,
						   xCoor+3, (yCoor - 4 * r)-2,
						   (xCoor + 2 * r)-2, (yCoor - 4 * r)-2, 
						   (xCoor + 4 * r)+2 , (yCoor - 6 * r)-5,
						   (xCoor + 6 * r)+2, (yCoor - 6 * r)-5, 
						   (xCoor + 8 * r)+2, (yCoor - 4 * r)-4,
						   (xCoor + 10 * r)+4, (yCoor - 4 * r)-4,
						   (xCoor + 10 * r)+4,(yCoor - r)-5);
    	
		back.setTranslateZ(100);
    	back.setFill(color.deriveColor(0.0, 1.0, (1 - 0.6* shade), 1.0));
		back.setStroke(Color.BLACK);

    	top = new Polygon(xCoor + 4 * r, yCoor - 6 * r,
						  xCoor + 6 * r, yCoor - 6 * r,
						  (xCoor + 6 * r)+2, (yCoor - 6 * r)-5,
						  (xCoor + 4 * r)+2 , (yCoor - 6 * r)-5);
    	top.setFill(color.deriveColor(0.0, 1.0, (1 - 0.5 * shade), 1.0));
    	top.setTranslateZ(-0.5);
		top.setStroke(Color.BLACK);

    	
    	leftUp = new Polygon(xCoor + 2 * r, yCoor - 4 * r, 
    						 xCoor + 4 * r, yCoor - 6 * r,
    						 (xCoor + 4 * r)+2 , (yCoor - 6 * r)-4,
    						 (xCoor + 2 * r)-2, (yCoor - 4 * r)-2);
    	
    	leftUp.setFill(color.deriveColor(0.0, 1.0, (1 - 0.9 * shade), 1.0));
    	leftUp.setTranslateZ(-0.5);
		leftUp.setStroke(Color.BLACK);

    	leftDown = new Polygon(xCoor, yCoor - 4 * r,
    						   xCoor + 2 * r, yCoor - 4 * r,
    						  (xCoor + 2 * r)-2, (yCoor - 4 * r)-2,
    						   xCoor+3, (yCoor - 4 * r)-2); 
    	leftDown.setFill(color.deriveColor(0.0, 1.0, (1 - 0.6 * shade), 1.0));
    	leftDown.setTranslateZ(-0.5);
		leftDown.setStroke(Color.BLACK);

		left = new Polygon(xCoor, yCoor - r,
						   xCoor+3, (yCoor - r)-5,
						   xCoor+3, (yCoor - 4 * r)-2,
						   xCoor, yCoor - 4 * r);
    	left.setFill(color.deriveColor(0.0, 1.0, (1 - 0.6 * shade), 1.0));
		left.setStroke(Color.BLACK);
    	leftDown.setTranslateZ(-0.5);

    	rightUp = new Polygon(xCoor + 6 * r, yCoor - 6 * r, 
    						   xCoor + 8 * r, yCoor - 4 * r,
    						  (xCoor + 8 * r)+2, (yCoor - 4 * r)-3,
   						      (xCoor + 6 * r)+2, (yCoor - 6 * r)-5);
    	rightUp.setFill(color.deriveColor(0.0, 1.0, (1 - 0.6 * shade), 1.0));
		rightUp.setStroke(Color.BLACK);
		
		rightDown = new Polygon(xCoor + 8 * r, yCoor - 4 * r,
								xCoor + 10 * r,yCoor - 4 * r,
							   (xCoor + 10 * r)+4, (yCoor - 4 * r)-4,
							   (xCoor + 8 * r)+4, (yCoor - 4 * r)-4);
		rightDown.setFill(color.deriveColor(0.0, 1.0, (1 - 0.6 * shade), 1.0));
		rightDown.setStroke(Color.BLACK);
		
		right = new Polygon(xCoor + 10 * r,yCoor - 4 * r,
							xCoor + 10 * r,(yCoor - r)-1,
						   (xCoor + 10 * r)+4,(yCoor - r)-4,
						   (xCoor + 10 * r)+4, (yCoor - 4 * r)-3);
		right.setFill(color.deriveColor(0.0, 1.0, (1 - 0.6 * shade), 1.0));
		right.setStroke(Color.BLACK);
		
		Polygon light1 = new Polygon((xCoor + 10 * r)+3,yCoor - 4 * r,
									 (xCoor + 10 * r)+50,(yCoor - 4 * r)-10,
									 (xCoor + 10 * r)+50,(yCoor - 4 * r)+10);
		light1.setFill(Color.YELLOW);
		
		Polygon light2 = new Polygon((xCoor + 10 * r)+3,yCoor - 4 * r+10,
				 					 (xCoor + 10 * r)+50,(yCoor - 4 * r),
				 					(xCoor + 10 * r)+50,(yCoor - 4 * r)+20);
		light2.setFill(Color.YELLOW);
		
		// Draw the wheels
		Color colorW = Color.BLACK;
		Circle wheelFront1 = new Circle(xCoor + r * 3, yCoor - r, r,colorW);
		Circle wheelBack1 = new Circle((xCoor + r * 3)+3, yCoor - r, r, Color.GREY);
		Circle wheelFront2 = new Circle(xCoor + r * 7, yCoor - r, r, colorW);
		Circle wheelBack2 = new Circle((xCoor + r * 7)+3, yCoor - r, r, Color.GREY);
		
		carGroup.getChildren().clear();
		getChildren().clear();
		carGroup.getChildren().addAll(wheelBack1,wheelBack2,back,leftUp,leftDown,left,rightUp,rightDown,right,front,top, wheelFront1, wheelFront2,light1,light2);
		getChildren().add(carGroup);
	}

	public void createTimeline()
	{			
		Random rand = new Random();
		EventHandler<ActionEvent> eventHandler = e ->
		{ 
			//change to random speed
			//in future to write method to calculate according to song
			//or each half minute
			//moveCar(MOVE);
			moveCar(MOVE);// move car pane according to limits
		};
		tl = new Timeline();
		tl.setCycleCount(Timeline.INDEFINITE);
		KeyFrame kf = new KeyFrame(Duration.millis(50), eventHandler);
		tl.getKeyFrames().add(kf);
		tl.play();
	}

	public Timeline getTimeline()
	{	
		return tl;
	}

	public void setColor(Color color)
	{	
		this.color = color;
		if (car.getSpeed() == STOP) moveCar(STOP);
	}

	public void setRadius(int r)
	{	
		this.r = r;
		if (car.getSpeed() == STOP) moveCar(STOP);
	}

	public void setSpeed(double speed)
	{	
		if (speed == STOP)
		{ 
			tl.stop();
		}
		else
		{ 
			tl.setRate(speed);
			tl.play();
		}
	}

	public double getX()
	{	
		return xCoor;
	}

	public double getY()
	{	
		return yCoor;
	}
}
