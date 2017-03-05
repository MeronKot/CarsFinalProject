import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javafx.scene.paint.Color;
public class Model
{ 
	private CarLog log;
	private int raceCounter;
	private Car c1;
	private Car c2;
	private Car c3;
	private Car c4;
	private Car c5;
	private double [] carsSpeed;
	private int winCarIdx;
	private int gamblerCounter = 0;
	private HashMap<Integer, HashMap<Integer,Double>> gamblers;

	public Model(int raceCounter)
	{	
		this.raceCounter = raceCounter;
		//this.log=new CarLog(this.raceCounter);
		c1=new Car(0,raceCounter,log);
		c2=new Car(1,raceCounter,log);
		c3=new Car(2,raceCounter,log);
		c4=new Car(3,raceCounter,log);
		c5=new Car(4,raceCounter,log);
		carsSpeed = new double [5];
		gamblers = new HashMap<>();
	}

	public void changeColor(int id,Color color)
	{	
		getCarById(id).setColor(color);
	}

	public void changeRadius(int id,int radius)
	{ 
		getCarById(id).setRadius(radius);
	}

	public void changeSpeed(int id,double speed)
	{	
		getCarById(id).setSpeed(speed);
		carsSpeed[id] += speed;
	}

	public Car getCarById(int id)
	{	
		switch(id){
		case 0:
			return c1;
		case 1:
			return c2;
		case 2:
			return c3;
		case 3:
			return c4;
		case 4:
			return c5;
		}
		return null;
	}

	public int getRaceCounter()
	{	
		return raceCounter;
	}
	
	public int getWinnerCar(){
		return winCarIdx;
	}

	public void calculateWinners(double t, int times) {		
		for (int i = 0 ; i < carsSpeed.length ; i++)
		{
			carsSpeed[i] =  carsSpeed[i] / times; // average speed (sum of speed divided by times changed)
			carsSpeed[i] = carsSpeed[i] * t; // calculate distance (x = v*t), t = length of song 
		}

		winCarIdx = 0;
		double max = carsSpeed[0];
		for(int i = 0 ; i < carsSpeed.length ; i++)
			if(carsSpeed[i] > max){
				max = carsSpeed[i];
				winCarIdx = i;
			}
		
		System.out.println("Car no. " + (winCarIdx + 1) + " win, " + "distance = " + max);
		Iterator it = gamblers.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry pair = (Map.Entry)it.next();
			System.out.print("Gambler no. " + pair.getKey() + " earned: ");
			HashMap<Integer, Double> amounts = (HashMap<Integer, Double>) pair.getValue();
			System.out.println((amounts.get(winCarIdx) * 1.5));
		}
	}

	public void setGambler(HashMap<Integer, Double> gamblerAmounts) {
		gamblers.put(gamblerCounter, gamblerAmounts);		
	}

	public boolean checkIfRaceReady() {
		boolean [] carsGambling = new boolean [5];
		Iterator it = gamblers.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry pair = (Map.Entry)it.next();
			HashMap<Integer, Double> amounts = (HashMap<Integer, Double>) pair.getValue();
			Iterator itValue = amounts.entrySet().iterator();
			while(itValue.hasNext()){
				Map.Entry valuePair = (Map.Entry)itValue.next();
				if((Double)valuePair.getValue() > 0)
					carsGambling[(int) valuePair.getKey()] = true;
			}
		}
		
		int i = 0;
		for(boolean carHaveGamble : carsGambling)
			if(carHaveGamble)
				i++;
		
		if (i >= 3)
			return true;
		else return false;
	}
}
