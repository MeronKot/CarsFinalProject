import java.io.Serializable;
import java.util.HashMap;

public class PacketToServer implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean gamblerClient = false;
	HashMap<Integer,Double> gamblerAmounts;
	
	public PacketToServer(){
		
	}
	
	public PacketToServer(HashMap<Integer,Double> gamblerAmounts){
		this.gamblerAmounts = gamblerAmounts;
		gamblerClient = true;
	}
	
	public boolean gamblerClient(){
		return gamblerClient;
	}
	
	public HashMap<Integer,Double> getGamblerAmounts(){
		return gamblerAmounts;
	}
	
}
