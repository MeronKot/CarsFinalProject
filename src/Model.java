import java.awt.image.ColorModel;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import java.sql.Statement;

//import javafx.scene.paint.Color;

public class Model implements Serializable
{ 
	private int raceCounter;
	private Date dateOfRace;
	private Car c1;
	private Car c2;
	private Car c3;
	private Car c4;
	private Car c5;
	private double [] carsSpeed;
	private int winCarIdx;
	private int gamblerCounter = 0;
	private String carsCompete;
	private double totalGamblingAmount = 0;
	private HashMap<Integer, HashMap<Integer,Double>> gamblers;

	public Model(int raceCounter, int gamblerCount){	
		this.raceCounter = raceCounter;
		this.gamblerCounter = gamblerCount;
		c1=new Car(0,raceCounter);
		c2=new Car(1,raceCounter);
		c3=new Car(2,raceCounter);
		c4=new Car(3,raceCounter);
		c5=new Car(4,raceCounter);
		carsSpeed = new double [5];
		gamblers = new HashMap<>();
	}

	public void changeColor(int id,String color)
	{	
		//getCarById(id).setColor(color);
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
		carsCompete = "";
		boolean [] carsGambling = new boolean [5];
		Iterator it = gamblers.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry pair = (Map.Entry)it.next();
			HashMap<Integer, Double> amounts = (HashMap<Integer, Double>) pair.getValue();
			Iterator itValue = amounts.entrySet().iterator();
			while(itValue.hasNext()){
				Map.Entry valuePair = (Map.Entry)itValue.next();
				if((Double)valuePair.getValue() > 0)
				{
					carsGambling[(int) valuePair.getKey()] = true;
					totalGamblingAmount += (Double)valuePair.getValue();
				}
			}
		}
		
		int i = 0;			
		for(int j = 0 ; j < carsGambling.length ; j++){
			if(carsGambling[j])
			{
				i++;
				carsCompete += (j + 1) + ",";
			}			
		}
		
		if (i >= 3)
			return true;
		else return false;
	}

	public void saveRaceDB(Statement statement,Connection con) throws SQLException {
		//totalGamblingAmount and carsCompete are null
		PreparedStatement pst = con.prepareStatement("insert into race values(?,?,?,?,?)");
		pst.setString(1, String.valueOf(raceCounter));
		pst.setString(2, carsCompete);
		pst.setDate(3, new java.sql.Date(dateOfRace.getTime()));
		pst.setString(4, String.valueOf(totalGamblingAmount));
		pst.setString(5, String.valueOf(winCarIdx + 1));
		pst.executeUpdate();	
	}
	
	public void saveGamblersDB(Statement statement, Connection con) throws SQLException {
		int idx = 1;
		Iterator it = gamblers.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry pair = (Map.Entry)it.next();
			PreparedStatement pst = con.prepareStatement("insert into gambler values(?,?,?,?,?,?,?)");
			pst.setString(idx++, String.valueOf((Integer)pair.getKey() + 1));
			pst.setString(idx++, String.valueOf(raceCounter));
			HashMap<Integer, Double> amounts = (HashMap<Integer, Double>) pair.getValue();
			Iterator itValue = amounts.entrySet().iterator();
			while(itValue.hasNext()){
				Map.Entry valuePair = (Map.Entry)itValue.next();
				pst.setString((int)valuePair.getKey() + idx, String.valueOf(valuePair.getValue()));
			}
			pst.executeUpdate();
			idx = 1;
		}		
	}

	
	
	
	
	
	public void setDate(Date dateOfRace) {
		this.dateOfRace = dateOfRace;
	}
	
}
