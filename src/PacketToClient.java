import java.io.Serializable;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class PacketToClient implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Model gamModel;
	//private Connection con;
	private int races;
	private boolean play = false;
	private HashMap<Integer, Double> hashMap;
	private Date dateOfRace;
	
	


	public PacketToClient(Connection con, Model model, int races)
	{
		this.gamModel = model;
		this.races = races;
	}

	
	public PacketToClient(boolean play, HashMap<Integer, Double> hashMap){
		this.play = play;
		this.hashMap = hashMap;
	}


	public HashMap<Integer, Double> getHashMap() {
		return hashMap;
	}

	public void setHashMap(HashMap<Integer, Double> hashMap) {
		this.hashMap = hashMap;
	}
	
	public boolean isPlay() {
		return play;
	}

	public void setPlay(boolean play) {
		this.play = play;
	}

	public int getRaces() {
		return races;
	}

	public void setRaces(int races) {
		this.races = races;
	}

	public Model getGamModel() {
		return gamModel;
	}

	public void setGamModel(Model gamModel) {
		this.gamModel = gamModel;
	}
	


	public Date getDateOfRace() {
		return dateOfRace;
	}


	public void setDateOfRace(Date dateOfRace) {
		this.dateOfRace = dateOfRace;
	}

	
}
