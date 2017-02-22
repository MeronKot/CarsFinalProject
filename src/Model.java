import javafx.scene.paint.Color;
public class Model
{ private CarLog log;
  private int raceCounter;
  private Car c1;
  private Car c2;
  private Car c3;	
  public Model(int raceCounter)
  {	this.raceCounter = raceCounter;
	this.log=new CarLog(this.raceCounter);
	c1=new Car(0,raceCounter,log);
	c2=new Car(1,raceCounter,log);
	c3=new Car(2,raceCounter,log);	
  }
  public void changeColor(int id,Color color)
  {	getCarById(id).setColor(color);
  }
  public void changeRadius(int id,int radius)
  { getCarById(id).setRadius(radius);
  }
  public void changeSpeed(int id,double speed)
  {	getCarById(id).setSpeed(speed);
  }
  public Car getCarById(int id)
  {	if (id == 0) return c1;
	else
	if (id==1) return c2;
	else return c3;
  }
  public int getRaceCounter()
  {	return raceCounter;
  }
}
