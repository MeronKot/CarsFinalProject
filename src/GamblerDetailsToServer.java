import java.io.Serializable;
import java.util.HashMap;

public class GamblerDetailsToServer implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean gamblerClient = false;
	private int raceId;
	private HashMap<Integer,Double> gamblerAmounts;
	private View gView;
	private HashMap <Integer,View> gViewList;
	
	public GamblerDetailsToServer(){
	}
	
	public GamblerDetailsToServer(HashMap<Integer,Double> gamblerAmounts, int raceId){
		this.raceId = raceId;
		this.gamblerAmounts = gamblerAmounts;
		gamblerClient = true;
	}
	
	public HashMap<Integer, View> getgViewList() {
		return gViewList;
	}

	public void setgViewList(HashMap<Integer, View> gViewList) {
		this.gViewList = gViewList;
	}

	public View getgView() {
		return gView;
	}

	
	public void setgView(View gView) {
		this.gView = gView;
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
