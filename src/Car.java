import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
public class Car implements CarEvents,Serializable
{ 
	private int id;
	private int model_id;
	private double speed;
	private int wheelRadius;
	private Map<eventType, ArrayList<EventHandler<Event>>> carHashMap;
	
	public Car(int id, int model_id)
	{	
		this.id = id;
		this.model_id = model_id;
		this.speed = 1;
		this.wheelRadius = 5;
		carHashMap = new HashMap<eventType, ArrayList<EventHandler<Event>>>();
		for (eventType et : eventType.values())
			carHashMap.put(et, new ArrayList<EventHandler<Event>>());
	}
	public int getId()
	{	return id;
	}
	
	public int getModelId()
	{	return model_id;
	}

	public int getRadius()
	{	return wheelRadius;
	}
	public double getSpeed()
	{	return speed;
	}

	public void setSpeed(double speed)
	{	this.speed = speed;
	processEvent(eventType.SPEED, new ActionEvent());
	}
	public void setRadius(int wheelRadius)
	{	this.wheelRadius = wheelRadius;
	processEvent(eventType.RADIUS, new ActionEvent());
	}
	
	public synchronized void addEventHandler(EventHandler<Event> l, eventType et)
	{	
		ArrayList<EventHandler<Event>> al;
		al = carHashMap.get(et);
		if (al == null)
			al = new ArrayList<EventHandler<Event>>();
		al.add(l);
		carHashMap.put(et, al);
	}

	public synchronized void removeEventHandler(EventHandler<Event> l, eventType et)
	{	
		ArrayList<EventHandler<Event>> al;
		al = carHashMap.get(et);
		if (al != null && al.contains(l))
			al.remove(l);
		carHashMap.put(et, al);
	}

	private void processEvent(eventType et, Event e)
	{	
		String msg;
		ArrayList<EventHandler<Event>> al;
		synchronized (this)
		{ 
			al = carHashMap.get(et);
			if (al == null) return;
		}
		msg = "CarRaceView" + (getModelId() + 1) + " | car number: "
				+ (getId() + 1) + " | actionCommand: " + et.toString()
				+ " | array size is: " + al.size();
		for (int i = 0; i < al.size(); i++)
		{ 
			EventHandler<Event> handler = (EventHandler<Event>) al.get(i);
			handler.handle(e);
		}
	}
}