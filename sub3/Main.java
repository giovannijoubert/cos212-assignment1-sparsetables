import java.util.Random;

public class Main
{
	/*Use this class to test your implementation.  This file will be overwritten for marking purposes.*/
	
	private static void printSchedule(Schedule mySchedule){
		String Line = "Output";
		for (int i = 0; i < 7; i++) {
			Line = Line+","+mySchedule.rootDayIndex[i];
			if (mySchedule.rootDay[i] == null){
				Line = Line + " (null)";  
			} else {
				Line = Line + " (" + mySchedule.rootDay[i].getDescription() +")";
			}
		}
		System.out.println(Line);
		Event printMe, R, D, U;
		for (int i = 0; i < 33; i++) {
			Line = mySchedule.rootTimeIndex[i];
			if (mySchedule.rootTime[i] == null){
				Line = Line + " (null)";  
			} else {
				Line = Line + " (" + mySchedule.rootTime[i].getDescription() +")";
			}
			Line = Line + ",";
			for (int j = 0; j < 7; j++) {
				printMe = mySchedule.getEvent(mySchedule.rootTimeIndex[i], mySchedule.rootDayIndex[j]);
				if(printMe == null){
					Line = Line + ",";
				} else {
					D = printMe.down; 
					R = printMe.right;
					U = printMe.up;
					Line = Line + printMe.getDescription(); 
					if (D == null){
						Line = Line + " (null)";  
					} else {
						Line = Line + " (" + D.getDescription() +")";
					}
					if (U == null){
						Line = Line + "(null)";  
					} else {
						Line = Line + "(" + U.getDescription() +")";
					}
					if (R == null){
						Line = Line + "(null)";  
					} else {
						Line = Line + "(" + R.getDescription() +")";
					}
					Line = Line + ",";
				}
			
				
			}
			System.out.println(Line);
		}

	}

	public static void main(String[] args)
	{
		
		//Write code to test your implementation here.
		Schedule mySchedule = new Schedule();

		// MANUALLY ADDING EVENTS
		System.out.println();
		
		mySchedule.addEvent("06:30", "mon", "A");
		mySchedule.addEvent("07:00", "tue", "B");
		mySchedule.addEvent("21:30", "tue", "C");
		mySchedule.addEvent("07:00", "sun", "D");
		mySchedule.addEvent("21:30", "sun", "E");
		mySchedule.addEvent("22:00", "sun", "F");
		mySchedule.addEvent("08:00", "fri", "G");
		mySchedule.addEvent("06:00", "tue", "H",90);
		mySchedule.addEvent("06:00", "tue", "I",60);
		mySchedule.addEvent("20:00", "mon", "I",60);
		mySchedule.addEvent("06:00", "fri", "3",90);
		System.out.println(mySchedule.findEvent("I").getTime());

		System.out.println();

		
     	printSchedule(mySchedule);
	}

	
	
}
