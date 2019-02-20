

/*Complete this class to implement a fully functional sparse table.  Read the comments to determine what each aspect of the class is supposed to do.
You must add any additional features (methods, references) which may aid you in your
task BUT you are not allowed to remove or change the names or properties of any of the features you where given.

Importing Java's built in data structures will result in a mark of 0.*/

public class Schedule
{
	
	public String[] rootDayIndex = {
		"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"
	};

	public String[] rootTimeIndex = {
		"06:00", "06:30", "07:00", "07:30", "08:00",  "08:30",
		"09:00", "09:30", "10:00", "10:30", "11:00", "11:30",
		"12:00", "12:30", "13:00", "13:30", "14:00", "14:30",
		"15:00", "15:30", "16:00", "16:30", "17:00", "17:30",
		"18:00", "18:30", "19:00", "19:30", "20:00", "20:30",
		"21:00", "21:30", "22:00"
	};

	//return null if invalid time slot, otherwise index of timeslot
	public Integer timeConv(String time){
		int pos = -1;
		for (int i = 0; i < 33; i++) { //search through all timeslots for time Index
			if(time == rootTimeIndex[i]){
				pos = i;
				break;
			}
		}
		if(pos == -1){
			return null;
		} else {
			return pos;
		}
	}  

	//return null if invalid day slot, otherwise index of dayslot
	public Integer dayConv(String day){
			int pos = -1;
			for (int i = 0; i < 7; i++) { //search through all dayslots for day Index
				if(day.equalsIgnoreCase(rootDayIndex[i])){
					pos = i;
					break;
				}
			}
			if(pos == -1){
				return null;
			} else {
				return pos;
			}
		}  

	public Event[] rootDay = new Event[7];
	public Event[] rootTime = new Event[33];

	public Schedule()
	{
		/*You may implement this constructor to suit your needs, or you may add additional constructors.  
		This is the constructor which will be used for marking*/ 
		clearAll();
	}
	
	/*Insertion*/

	public void addEvent(String time, String day, String description)
	{
		/*Insert an event at the given time and day combination. 
		Assume the event is 30 minutes long and insert a single Event node.
		Description should be used to initialise the node.
		
		Duplicate events (events with the same description) are allowed.*/
		addEvent(time, day, description, 30); //call overloaded one with default event time
	}

	public void addEvent(String time, String day, String description, int duration)
	{
		/*Insert an event at the given time and day combination. 
		Description should be used to initialise the necessary nodes.
		Duration should be used to determine how many nodes with the same description have to be inserted.
		Default value of 30 corresponds to a single node. 
		Assume only increments of 30 will be used, eg. 30, 60, 90, 120, etc.
		
		Duplicate events (events with the same description) are allowed.*/
		String placedTime = "";
		String placedDay = "";
		int eventLength = (duration / 30);
		Event[] insertMe = new Event[eventLength];
		for (int i = 0; i < eventLength; i++) {
			insertMe[i] = new Event(time, day, description); //time & day will need to be updated on placing the event
		}
		for (int i = 0; i < eventLength-1; i++) {
			insertMe[i].down = insertMe[i+1];
			insertMe[i+1].up = insertMe[i];
		}
		
		Event dayTransverse = getDayEvent(day);
		Boolean placed = false;
		if(dayTransverse == null){
			rootDay[dayConv(day)] = insertMe[0];
			insertMe[eventLength-1].down = insertMe[0];
			insertMe[0].up = insertMe[eventLength-1];
			placedTime = time;
			placedDay = day;
		} else { 
			if(timeConv(getDayEvent(day).getTime())>timeConv(time)){	//if its the smallest element
				insertMe[eventLength-1].down = rootDay[dayConv(day)];
				insertMe[0].up = rootDay[dayConv(day)].up;
				rootDay[dayConv(day)].up.down = insertMe[0];
				rootDay[dayConv(day)].up = insertMe[eventLength-1];
				rootDay[dayConv(day)] = insertMe[0];
				placedTime = time;
				placedDay = day;
				
			} else {
			while(dayTransverse.down != getDayEvent(day)){ //not yet at tail
				if((timeConv(dayTransverse.getTime()) < timeConv(time)) && (timeConv(dayTransverse.down.getTime()) > timeConv(time))){ 
					if(timeConv(dayTransverse.down.getTime())-timeConv(dayTransverse.getTime()) > eventLength){ //large enough gap
						dayTransverse.down.up = insertMe[eventLength-1];
						insertMe[eventLength-1].down = dayTransverse.down;
						dayTransverse.down = insertMe[0];
						insertMe[0].up = dayTransverse;
						placedTime = rootTimeIndex[timeConv(dayTransverse.getTime())+1];
						placedDay = dayTransverse.getDay();
						placed = true;
					}
				}
				dayTransverse = dayTransverse.down;
			}
			if(!placed){ // place at tail
				if(getDayEvent(day).up.getTime() == time){ //tail == time of new node
					if((33-(timeConv(getDayEvent(day).up.getTime())+1) >= eventLength)){ // day has enough space
					
						Event head = getDayEvent(day);
						Event tail = getDayEvent(day).up;
						head.up = insertMe[eventLength-1];
						tail.down = insertMe[0];
						insertMe[0].up = tail;
						insertMe[eventLength-1].down = head;
						placedTime = rootTimeIndex[timeConv(getDayEvent(day).up.getTime())+1];
						placedDay = day;
						for (int i = 0; i < eventLength; i++) { //set new times in node
							insertMe[i].nodeTime = rootTimeIndex[(timeConv(placedTime)+i)];
						}
					} else {// current day does not have enough space
						int newDay;
						if(dayConv(day)+1 < 7) {
							newDay = dayConv(day)+1;
						} else {
							newDay = 0;
						}
							addEvent("06:00", rootDayIndex[newDay], description, duration);
							return;
					}
				} else if((33-(timeConv(getDayEvent(day).up.getTime())+1) >= eventLength)){ // day has enough space
				//	System.out.println(timeConv(getDayEvent(day).up.getTime()));
					Event head = getDayEvent(day);
					Event tail = getDayEvent(day).up;
					placedTime = time;
					placedDay = day;
					head.up = insertMe[eventLength-1];
					tail.down = insertMe[0];
					insertMe[0].up = tail;
					insertMe[eventLength-1].down = head;
					for (int i = 0; i < eventLength; i++) { //set new times in node
						insertMe[i].nodeTime = rootTimeIndex[(timeConv(placedTime)+i)];
					}
				
				} else {// current day does not have enough space
					
					int newDay;
					if(dayConv(day)+1 < 7) {
						newDay = dayConv(day)+1;
					} else {
					    newDay = 0;
					}
						addEvent("06:00", rootDayIndex[newDay], description, duration);
						return;
				}
			}
		
		}
	}
		Boolean timePlaced = false;
		Event timeTransverse;
		
		for (int i = timeConv(placedTime); i < timeConv(placedTime)+eventLength; i++) {
			
			timeTransverse = rootTime[i];
			if(timeTransverse == null){ //node is only node on timeslot
				rootTime[i] = insertMe[0];
			} else {
				while(timeTransverse.right != null){ //node is not the first node
					if(dayConv(timeTransverse.getDay()) < dayConv(placedDay)){
						insertMe[i-timeConv(placedTime)].right = timeTransverse.right;
						timeTransverse.right = insertMe[i-timeConv(placedTime)];
						timePlaced = true;
					}
					
					timeTransverse = timeTransverse.right;
				}
				if(!timePlaced){ //new node is Last node in timeslot
					timeTransverse.right = insertMe[i-timeConv(placedTime)];
				}

			}
		}
		

	
}
	
	/*Deletion methods*/
	public String deleteEvent(String time, String day)
	{
		/*Delete the event at the given time and day combination and return the description of the 
		deleted event. Note: all adjacent (up and down) events with the same description must also be deleted.
		
		If no such event exists, return null.*/
		if(getEvent(time, day) != null){
			Event timeTransverse = getTimeEvent(time);
			if(timeTransverse.getDay().equalsIgnoreCase(day)){ //test first element
				timeTransverse.up.down = timeTransverse.down;
				timeTransverse.down.up = timeTransverse.up;
				rootTime[timeConv(time)] = timeTransverse.right;
				
			} else {
			while(timeTransverse.right != null){
				if(timeTransverse.right.getDay().equalsIgnoreCase(day)){
					timeTransverse.right.up.down = timeTransverse.right.down;
					timeTransverse.right.down.up = timeTransverse.right.up;
					timeTransverse.right = timeTransverse.right.right;
					break;
				}
				timeTransverse = timeTransverse.right;
			}
		}
		} else {
			return null;
		}

				
		return null;
	}
	
	public void deleteEvent(String description)
	{
		/*Delete all events that match the given description.*/

		Event transverse;
		for (int i = 0; i < 33; i++) {
			transverse = rootTime[i];
			if(transverse != null){
			
			while(transverse.right != null){				
				if(transverse.right.getDescription().equalsIgnoreCase(description)){
					transverse.right.up.down = transverse.right.down;
					transverse.right.down.up = transverse.right.up;
					transverse.right = transverse.right.right;
				}
				transverse = transverse.right;
				if(transverse == null){break;}
			}
		}
		}
				
	}
	
	/*Clearing Methods*/
	public void clearByDay(String day)
	{
		/*All events for the given day should be deleted.
		If the day has no events, simply do nothing.*/
		Event transverse = getDayEvent(day);
		Event timeTransverse;
		Event deleteMe;
		while(transverse.down != getDayEvent(day)){
			deleteMe = transverse;
			timeTransverse = getTimeEvent(transverse.getTime());
			while(timeTransverse != null){
				if(timeTransverse.right == deleteMe){
					timeTransverse.right = deleteMe.right;
				}
				timeTransverse = timeTransverse.right;
			}
			transverse = transverse.down;
		}
		deleteMe = transverse;
			timeTransverse = getTimeEvent(transverse.getTime());
			while(timeTransverse != null){
				if(timeTransverse.right == deleteMe){
					timeTransverse.right = deleteMe.right;
				}
				timeTransverse = timeTransverse.right;
			}
			rootDay[dayConv(day)] = null;
	}

	public void clearByTime(String time)
	{
		/*All events for the given time should be deleted.
		If the time has no events, simply do nothing.*/
		if(getTimeEvent(time) == null){
		} else {
			Event transverse = getTimeEvent(time);
			do {
				transverse.up.down = transverse.down;
				transverse.down.up = transverse.up;
				transverse = transverse.right;
			} while (transverse != null);
		}
	}
	
	public void clearAll()
	{
		/*Delete all events from the schedule.*/
		for (int i = 0; i < 7; i++) {
			rootDay[i] = null;
		}
		for (int i = 0; i < 33; i++) {
			rootTime[i] = null;
		}
	}
	
	
	/*Query methods*/
	public Event getEvent(String time, String day)
	{
		/*Return the first event for the given time and day combination.  
		If no such event exists, return null*/
		Event timeTransverse = getTimeEvent(time);
		while(timeTransverse != null){
			if(timeTransverse.getDay().equalsIgnoreCase(day)){
				return timeTransverse;
			}
			timeTransverse = timeTransverse.right;
		}
		
		return null;
	}

	public Event findEvent(String description)
	{
		/*Return the first event that matches the given description.  
		If no such event exists, return null*/
		Event transverse;
		for (int i = 0; i < 7; i++) {
			transverse = rootDay[i];
			while(transverse.down != rootDay[i]){
				if(transverse.getDescription() == description){
					return transverse;
				}
				transverse = transverse.down;
			}
			if(transverse.getDescription() == description){
				return transverse;
			}
		}
		return null;
	}
	
	public Event getTimeEvent(String time)
	{
		/*Return the head event for the time passed as a parameter.
		If no such event exists, return null*/
		int pos = -1;
		for (int i = 0; i < 33; i++) { //search through all timeslots for time Index
			if(time == rootTimeIndex[i]){
				pos = i;
				break;
			}
		}
		if(pos == -1){
			return null;
		} else {
			return rootTime[pos];
		}
	}
	
	public Event getDayEvent(String day)
	{
		/*Return the head event for the day passed as a parameter.
		If no such event exists, return null*/
		int pos = -1;
			for (int i = 0; i < 7; i++) { //search through all dayslots for day Index
				if(day.equalsIgnoreCase(rootDayIndex[i])){
					pos = i;
					break;
				}
			}
			if(pos == -1){
				return null;
			} else {
				return rootDay[pos];
			}
	}
		
	
}