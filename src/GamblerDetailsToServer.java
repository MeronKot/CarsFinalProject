import java.io.Serializable;
import java.util.HashMap;

public class GamblerDetailsToServer implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean gamblerClient = false;
	private String gamblerName;
	private int raceId;
	private HashMap<Integer,Double> gamblerAmounts;
	private View gView;
	private HashMap <Integer,View> gViewList;
	private boolean close;
	
	
	public GamblerDetailsToServer(){
	}
	
	public GamblerDetailsToServer(String name, HashMap<Integer,Double> gamblerAmounts, int raceId){
		this.raceId = raceId;
		this.gamblerAmounts = gamblerAmounts;
		gamblerName = name;
		gamblerClient = true;
	}
	
	public String getGamblerName() {
		return gamblerName;
	}

	public void setGamblerName(String gamblerName) {
		this.gamblerName = gamblerName;
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
	
	public boolean isClose() {
		return close;
	}

	public void setClose(boolean close) {
		this.close = close;
	}

}
