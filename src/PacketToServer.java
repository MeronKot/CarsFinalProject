import java.io.Serializable;
import java.util.HashMap;

public class PacketToServer implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean gamblerClient = false;
	private int raceId;
	private HashMap<Integer,Double> gamblerAmounts;
	
	public PacketToServer(){
	}
	
	public PacketToServer(HashMap<Integer,Double> gamblerAmounts, int raceId){
		this.raceId = raceId;
		this.gamblerAmounts = gamblerAmounts;
		gamblerClient = true;
	}
	
	public boolean gamblerClient(){
		return gamblerClient;
	}
	
	public HashMap<Integer,Double> getGamblerAmounts(){
		return gamblerAmounts;
	}
	
	public int getRaceId(){
		return raceId;
	}
}
