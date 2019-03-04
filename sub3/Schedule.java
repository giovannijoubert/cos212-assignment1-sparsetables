

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
		if(timeConv(time) == null){
			time = "06:00";
		}

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

		if(dayTransverse != null){
			if(timeConv(dayTransverse.getTime()) > timeConv(time)){ //new node needs to go infront check front size
				if(timeConv(dayTransverse.getTime()) < timeConv(time)+eventLength){
					time = rootTimeIndex[timeConv(dayTransverse.getTime())+1];
				}
			}

		if(dayTransverse.getTime() == time){ //head node time == placetime
			time = rootTimeIndex[timeConv(time)+1];
		} 
		}

		if(dayTransverse == null){
			rootDay[dayConv(day)] = insertMe[0];
			insertMe[eventLength-1].down = insertMe[0];
			insertMe[0].up = insertMe[eventLength-1];
			placedTime = time;
			placedDay = day;
			for (int i = 0; i < eventLength; i++) { //set new times in node
				insertMe[i].nodeTime = rootTimeIndex[(timeConv(placedTime)+i)];
			}
		} else { 
			if((timeConv((getDayEvent(day).getTime()))>timeConv(time)) && (timeConv(getDayEvent(day).getTime()) >= eventLength)){	//if its the smallest element
				insertMe[eventLength-1].down = rootDay[dayConv(day)];
				insertMe[0].up = rootDay[dayConv(day)].up;
				rootDay[dayConv(day)].up.down = insertMe[0];
				rootDay[dayConv(day)].up = insertMe[eventLength-1];
				rootDay[dayConv(day)] = insertMe[0];
				placedTime = time;
				placedDay = day;
				for (int i = 0; i < eventLength; i++) { //set new times in node
					insertMe[i].nodeTime = rootTimeIndex[(timeConv(placedTime)+i)];
				}
			} else {
			while(dayTransverse.down != getDayEvent(day)){ //not yet at tail
				if((timeConv(dayTransverse.getTime()) < timeConv(time)) && (timeConv(dayTransverse.down.getTime()) > timeConv(time))){ 
					if(timeConv(dayTransverse.down.getTime())-timeConv(time) >= eventLength){ //large enough gap
						dayTransverse.down.up = insertMe[eventLength-1];
						insertMe[eventLength-1].down = dayTransverse.down;
						dayTransverse.down = insertMe[0];
						insertMe[0].up = dayTransverse;

						/*placedTime = rootTimeIndex[timeConv(dayTransverse.getTime())+1];*/
						placedTime = time;
						for (int i = 0; i < eventLength; i++) { //set new times in node
							insertMe[i].nodeTime = rootTimeIndex[(timeConv(placedTime)+i)];
						}

						placedDay = dayTransverse.getDay();
						placed = true;
					}
				}
				dayTransverse = dayTransverse.down;
			}
		
			Event testClashFirst;
					for (int i = 0; i < eventLength; i++) { //check for clashing events
						if(timeConv(time)+i < 32){
						testClashFirst = getEvent(rootTimeIndex[timeConv(time)+i],day);
						if (testClashFirst != null){
							addEvent(rootTimeIndex[timeConv(testClashFirst.getTime())+1], day, description, duration);
							return;
						}
					}
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
					if(33-timeConv(time)<eventLength){ //event too big to fit in DESIRED time on day
						int newDay;
					if(dayConv(day)+1 < 7) {
						newDay = dayConv(day)+1;
					} else {
					    newDay = 0;
					}
						addEvent("06:00", rootDayIndex[newDay], description, duration);
						return;
					}
					if(getEvent(time, day) != null){
						placedTime = rootTimeIndex[timeConv(tail.getTime())+1];
					} else {
					placedTime = time;
					}
					placedDay = day;

					Event testClash;
					for (int i = 0; i < eventLength; i++) { //check for clashing events
						testClash = getEvent(rootTimeIndex[timeConv(placedTime)+i],placedDay);
						if (testClash != null){
							addEvent(rootTimeIndex[timeConv(testClash.getTime())+1], placedDay, description, duration);
							return;
						}
					} 

				
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
			timePlaced = false;
			timeTransverse = rootTime[i];
			if(timeTransverse == null){ //node is only node on timeslot
				rootTime[i] = insertMe[i-timeConv(placedTime)];
			} else {
				if(dayConv(rootTime[i].getDay()) > dayConv(placedDay)){//new node is First node in timeslot
					insertMe[i-timeConv(placedTime)].right = rootTime[i];
					rootTime[i] = insertMe[i-timeConv(placedTime)];

				} else {
				if(timeTransverse.right == null){
					if(dayConv(timeTransverse.getDay())<dayConv(placedDay)){
						timeTransverse.right = insertMe[i-timeConv(placedTime)];
						timePlaced = true;
					}
				}
				
				while(timeTransverse.right != null){ //node is not the first node
					if((dayConv(timeTransverse.getDay()) < dayConv(placedDay)) && (dayConv(timeTransverse.right.getDay()) > dayConv(placedDay)) ){
						insertMe[i-timeConv(placedTime)].right = timeTransverse.right;
						timeTransverse.right = insertMe[i-timeConv(placedTime)];
						timePlaced = true;	
					}		
					timeTransverse = timeTransverse.right;	
				}
				if(!timePlaced){ 
					//new node is Last node in timeslot
							timeTransverse.right = insertMe[i-timeConv(placedTime)];
					}
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
		Event deleteMe = getEvent(time, day);
		if(deleteMe != null){ 
			
			Event deleteHead = deleteMe;
			Event deleteTail = deleteMe;

			//Continue going up aslong as description is the same and the time is earlier than selected
			while(deleteHead.up.getDescription().equalsIgnoreCase(deleteMe.getDescription())
			&& timeConv(deleteHead.up.getTime()) < timeConv(deleteMe.getTime())){
				deleteHead = deleteHead.up;
			}

			//Continue going down aslong as description is the same and the time is later than selected
			while(deleteTail.down.getDescription().equalsIgnoreCase(deleteMe.getDescription())
			&& timeConv(deleteTail.down.getTime()) > timeConv(deleteMe.getTime())){
				deleteTail = deleteTail.down;
			}

			if(deleteHead.down == deleteHead){ //deleteHead is only element
				rootDay[dayConv(day)] = null;
			} else if(deleteHead == getDayEvent(day)){ //deleteHead is top element
				if(deleteTail.down == deleteHead){ //event is entire day
					rootDay[dayConv(day)] = null;
				} else {
					rootDay[dayConv(day)] = deleteTail.down;
					rootDay[dayConv(day)].up = deleteHead.up;
				}
				} else {
					deleteHead.up.down = deleteTail.down;
					deleteTail.down.up = deleteHead.up;
				}

			//Now delete for time pointers
			for (int i = timeConv(deleteHead.getTime()); i < timeConv(deleteTail.getTime())+1; i++) {
				Event transverseTime = rootTime[i];

				if(rootTime[i].getDay().equalsIgnoreCase(deleteHead.getDay())){
					rootTime[i] = transverseTime.right;
				}

				if(transverseTime != null){
				while(transverseTime.right != null){
					if(transverseTime.right.getDay().equalsIgnoreCase(deleteHead.getDay())){
						transverseTime.right = transverseTime.right.right;
					}
					transverseTime = transverseTime.right;
					if(transverseTime == null){break;}
				}
				}
			}
			return deleteMe.getDescription();
		} //if no return in statement return null
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
		
		for (int i = 0; i < 7; i++) { //disconnect rootDay
			transverse = rootDay[i];
			if(transverse != null){
			while((transverse.down != rootDay[i])){
				if(transverse.getDescription().equalsIgnoreCase(description)){
				if(transverse == rootDay[i]){
					if(transverse.down == transverse){
						rootDay[i] = null;
					} else {
						rootDay[i] = transverse.down;
						transverse.down.up = transverse.up;
						transverse.up.down = transverse.down;
					}
				} else {
				transverse.up.down = transverse.down;
				transverse.down.up = transverse.up;
				}
				}
				transverse = transverse.down;
			}
		}
		}

		for (int i = 0; i < 33; i++) {
			transverse = rootTime[i];
			if(transverse != null){
			if(transverse.getDescription().equalsIgnoreCase(description)){
				rootTime[i].down.up = rootTime[i].up;
				rootTime[i].up.down = rootTime[i].down;
				rootTime[i] = transverse.right;
			}
		}
		}
		
		if(rootDay[0] != null){
		if(rootDay[0].getDescription().equalsIgnoreCase(description)){
			rootDay[0].down.up = rootDay[0].up;
			rootDay[0] = rootDay[0].down;
		}
	}

				
	}
	
	/*Clearing Methods*/
	public void clearByDay(String day)
	{
		/*All events for the given day should be deleted.
		If the day has no events, simply do nothing.*/
		Event transverse = getDayEvent(day);
		if(transverse == null){
		} else {
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

			for (int i = 0; i < 33; i++) {
				timeTransverse = rootTime[i];
				if(timeTransverse != null){
				while(timeTransverse.right != null){
					if(timeTransverse.right.getDay().equalsIgnoreCase(day)){
						timeTransverse = timeTransverse.right.right;
					}
					timeTransverse = timeTransverse.right;
					
					
				}
			}
			}

			for (int i = 0; i < 33; i++) {
				transverse = rootTime[i];
				if(transverse != null){
				if(transverse.getDay().equalsIgnoreCase(day)){
					rootTime[i].down.up = rootTime[i].up;
					rootTime[i].up.down = rootTime[i].down;
					rootTime[i] = transverse.right;
				}
			}
			}
		}
	}

	public void clearByTime(String time)
	{
		/*All events for the given time should be deleted.
		If the time has no events, simply do nothing.*/
	
		if(getTimeEvent(time) == null){
		} else {
			Event transverse = getTimeEvent(time);
			while(transverse != null){
				if(timeConv(time) == 0){
					rootDay[dayConv(transverse.getDay())] = transverse.down;
				}
				if(rootDay[dayConv(transverse.getDay())] == transverse){
					rootDay[dayConv(transverse.getDay())] = transverse.down;
				}

				transverse.up.down = transverse.down;
				transverse.down.up = transverse.up;
				transverse = transverse.right;
			}
			rootTime[timeConv(time)] = null;
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
			if(transverse != null){
			while(transverse.down != rootDay[i]){
				if(transverse.getDescription().equalsIgnoreCase(description)){
					return transverse;
				}
				transverse = transverse.down;
				if(transverse == null){break;}
			}
		}
		if(transverse != null){
			if(transverse.getDescription().equalsIgnoreCase(description)){
				return transverse;
			}
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
