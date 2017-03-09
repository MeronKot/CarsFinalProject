import java.io.Serializable;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;


public class PacketToClient implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Model gamModel;
	private Statement statement;
	private Connection con;
	private int races;
	
	
	
	public PacketToClient(Statement statement, Connection con, Model model, int races)
	{
		this.statement = statement;
		this.con = con;
		this.gamModel = model;
		this.races = races;
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

	public Statement getStatement() {
		return statement;
	}

	public void setStatement(Statement statement) {
		this.statement = statement;
	}

	public Connection getCon() {
		return con;
	}

	public void setCon(Connection con) {
		this.con = con;
	}

}
