/*You must complete this class such that it can be used as nodes in a sparse table.
Read the comments to determine what each aspect of the class is supposed to do.
You may add any additional features (methods, references) which may aid you in your
task, BUT you are not allowed to remove or change the names or properties of any of 
the features you where given.

Importing Java's built in data structures will result in a mark of 0.*/

public class Event
{
	public Event(String descr)
	{
		/*You may implement this constructor to suit your needs, or you may add additional constructors.  This is the constructor which will be used for marking*/ 
		description = descr;

		down = null;
		up = null;
		right = null;
	}	

	public Event(String time, String day, String descr){
		description = descr;
		nodeTime = time;
		nodeDay = day;

		down = null;
		up = null;
		right = null;
	}
	
	public void setDescription(String descr)
	{
		/*Implement this method to set the description for this event*/
		description = descr;
	}	
	
	public String getDescription()
	{
		/*This method returns the description of this event*/
		return description;
	}		
	
	public String getTime()
	{
		/*This method returns the description of this event*/
		return nodeTime;
	}	
	public String getDay()
	{
		/*This method returns the description of this event*/
		return nodeDay;
	}	

	public Event down; //The next event (down) of this event on the same day
	public Event up; //The previous event (up) of this event on the same day.
	public Event right;//The next event (right) of this event at the same time.
	
	private String description;//A description for this event.

	//Not Given
	public String nodeTime, nodeDay;
	
}
