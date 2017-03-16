import java.io.File;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//import javafx.scene.paint.Color;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javafx.collections.ObservableMap;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaMarkerEvent;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Model implements Serializable
{ 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static int raceCounter;
	static int sysId = 0;
	static Date dateOfRace;	private Car c1;
	private Car c2;
	private Car c3;
	private Car c4;
	private Car c5;
	private double [] carsSpeed;
	private int winCarIdx;
	private int gamblerCounter = 0;
	static String carsCompete;
	static double totalGamblingAmount = 0;
	static double sysProfit = 0;
	static HashMap<Integer, HashMap<Integer,Double>> gamblers;	
	static HashMap<Integer,String> gamblersNames;
	private String [] musicFile = {"3 - How Bad Do You Want It - Fast & Furious 7.mp3",
	"4 - Get Low - Fast & Furious 7.mp3"};
	private transient Media sound;
	private transient MediaPlayer mediaPlayer;
	private Duration length;
	private int times;
	private transient ObjectOutputStream outToClientOfThisModel;
	private Button btnRace;

	public Model(int raceCounter, int gamblerCount, ObjectOutputStream outputToClient) throws SQLException{	
		this.raceCounter = raceCounter;
		this.gamblerCounter = gamblerCount;
		this.outToClientOfThisModel = outputToClient;
		c1=new Car(0,raceCounter);
		c2=new Car(1,raceCounter);
		c3=new Car(2,raceCounter);
		c4=new Car(3,raceCounter);
		c5=new Car(4,raceCounter);
		carsSpeed = new double [5];
		gamblers = new HashMap<>();
		gamblersNames = new HashMap<>();
	}

	public ObjectOutputStream getOutToClientOfThisModel() {
		return outToClientOfThisModel;
	}
	public void setOutToClientOfThisModel(ObjectOutputStream outToClientOfThisModel) {
		this.outToClientOfThisModel = outToClientOfThisModel;
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

	public void setGambler(HashMap<Integer, Double> gamblerAmounts,String name) {
		gamblers.put(gamblerCounter, gamblerAmounts);
		gamblersNames.put(gamblerCounter, name);
		gamblerCounter++;
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
		sysProfit = totalGamblingAmount * 0.05;

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

	public void saveRaceDB(Connection con) throws SQLException {
		//totalGamblingAmount and carsCompete are null
		PreparedStatement pst = con.prepareStatement("insert into race values(?,?,?,?,?,?)");
		pst.setString(1, String.valueOf(raceCounter));
		pst.setString(2, carsCompete);
		pst.setDate(3, new java.sql.Date(dateOfRace.getTime()));
		pst.setString(4, String.valueOf(totalGamblingAmount));
		pst.setString(5, String.valueOf(winCarIdx + 1));
		pst.setString(6, String.valueOf(sysProfit));
		pst.executeUpdate();	
	}

	public void saveSystemCash(Connection con) throws SQLException
	{
		double currentCash = 0;
		Statement statement = con.createStatement();
		ResultSet sys = statement.executeQuery("select count(*) from system");
		while (sys.next()) 
		{
			currentCash =  sys.getDouble(1);
		}
		System.out.println("system cash = " + currentCash);
		PreparedStatement pst = con.prepareStatement("update system set cash = ? where sysId = ?");
		pst.setString(1,String.valueOf(currentCash+sysProfit));
		pst.setString(2, String.valueOf(sysId));
		pst.executeUpdate();
	}

	public void saveGamblersDB(Connection con) throws SQLException {
		Iterator<Entry<Integer, String>> itr = gamblersNames.entrySet().iterator();
		while(itr.hasNext())
		{
			Entry<Integer, String> pair = itr.next();
			System.out.println(pair.getKey() + " " + pair.getValue());
		}
			
		int idx = 1;
		Iterator it = gamblers.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry pair = (Map.Entry)it.next();
			PreparedStatement pst = con.prepareStatement("insert into gambler values(?,?,?,?,?,?,?,?)");
			pst.setString(idx++, String.valueOf((Integer)pair.getKey() + 1));
			pst.setString(idx++, gamblersNames.get((Integer)pair.getKey()));
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

	public void playSong() throws SQLException {
		/*
 		Connection con = DriverManager.getConnection
 				("jdbc:mysql://localhost/carsRace", "scott", "tiger");
 		int Low = 0;
 		int High = 2;
 		Random rand = new Random();
 		sound = new Media(new File(musicFile[rand.nextInt(High - Low) + Low]).toURI().toString());
 		mediaPlayer = new MediaPlayer(sound);
 		mediaPlayer.setOnEndOfMedia(new Runnable() {
 			@Override
 			public void run() {
 				//endRace();
 				changeSpeed(0, 0);
 				changeSpeed(1, 0);
 				changeSpeed(2, 0);
 				changeSpeed(3, 0);
 				changeSpeed(4, 0);
 				calculateWinners(length.toMinutes(),times);
 				try {
 					saveRaceDB(con);
 					saveSystemCash(con);
 					saveGamblersDB(con);					
 				} catch (SQLException e) {
 					System.out.println(e.getMessage());
 				}
 			}
 		});

 		try {
 			Thread.sleep(300);
 		} catch (InterruptedException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
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
		 */
		btnRace.setVisible(false);
		mediaPlayer.play();
	}

	public void randomSpeed(){
		//Iterator it = hashMap.entrySet().iterator();
		Random rand = new Random();
		int Low = 1;
		int High = 50;
		//while (it.hasNext()) {
		//Map.Entry pair = (Map.Entry)it.next();
		//changeSpeed((int) pair.getKey(), rand.nextInt(High - Low) + Low);
		//}	
		for(int i = 0 ; i < 5 ; i++)
			changeSpeed(i, rand.nextInt(High - Low) + Low);
	}



	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public void setDate(Date dateOfRace) {
		this.dateOfRace = dateOfRace;
	}
	public HashMap<Integer, HashMap<Integer, Double>> getGamblers() {
		return gamblers;
	}

	public void setGamblers(HashMap<Integer, HashMap<Integer, Double>> gamblers) {
		this.gamblers = gamblers;
	}

	public int getGamblerCounter() {
		return gamblerCounter;
	}

	public MediaPlayer getMediaPlayer() {
		return mediaPlayer;
	}
	public void setMediaPlayer(MediaPlayer mediaPlayer) {
		this.mediaPlayer = mediaPlayer;
	}

	public Date getDateOfRace() {
		return dateOfRace;
	}

	public void setDateOfRace(Date dateOfRace) {
		this.dateOfRace = dateOfRace;
	}


	public void configureMedia(Button btnRace) {
		this.btnRace = btnRace;
		Connection con;
		try {
			con = DriverManager.getConnection
					("jdbc:mysql://localhost/carsRace", "scott", "tiger");

			int Low = 0;
			int High = 2;
			Random rand = new Random();
			sound = new Media(new File(musicFile[rand.nextInt(High - Low) + Low]).toURI().toString());
			mediaPlayer = new MediaPlayer(sound);
			mediaPlayer.setOnReady(new Runnable() {
				
				@Override
				public void run() {
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
							randomSpeed();
						}
					});
					mediaPlayer.setOnEndOfMedia(new Runnable() {
						@Override
						public void run() {
							changeSpeed(0, 0);
							changeSpeed(1, 0);
							changeSpeed(2, 0);
							changeSpeed(3, 0);
							changeSpeed(4, 0);
							calculateWinners(length.toMinutes(),times);
							try {
								saveRaceDB(con);
								saveSystemCash(con);
								saveGamblersDB(con);					
							} catch (SQLException e) {
								System.out.println(e.getMessage());
							}
							btnRace.setVisible(true);
						}
					});
				}
			});
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


	}


}
