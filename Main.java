import java.util.Random;

public class Main
{
	/*Use this class to test your implementation.  This file will be overwritten for marking purposes.*/
	
	private static void printSchedule(Schedule mySchedule){
		String Line = "\t";
		for (int i = 0; i < 7; i++) {
			Line = Line+"\t"+mySchedule.rootDayIndex[i];
		}
		System.out.println(Line);
		Event printMe;
		for (int i = 0; i < 33; i++) {
			Line = "("+i+")\t" +mySchedule.rootTimeIndex[i];
			for (int j = 0; j < 7; j++) {
				printMe = mySchedule.getEvent(mySchedule.rootTimeIndex[i], mySchedule.rootDayIndex[j]);
				if(printMe == null){
					Line = Line + "\t" + "*";
				} else {
					Line = Line + "\t" + printMe.getDescription();
				}
				
			}
			System.out.println(Line);
		}

	}

	public static void main(String[] args)
	{
		
		//Write code to test your implementation here.
		Schedule mySchedule = new Schedule();

		/* MANUALLY ADDING EVENTS
		mySchedule.addEvent("06:00", "MON", "Hockey");
		
		mySchedule.addEvent("06:30", "TUE", "Tennis");
		mySchedule.addEvent("09:30", "TUE", "Sport");

		mySchedule.addEvent("07:00", "TUE", "Rugby");
		mySchedule.addEvent("06:30", "SUN", "Rugby");
		mySchedule.addEvent("20:30", "SUN", "Tennis");
		
		mySchedule.addEvent("06:00", "tue", "hockey");
		mySchedule.addEvent("21:00", "SUN", "Ball",300); 
		mySchedule.addEvent("21:00", "SUN", "Lol",300);
		mySchedule.addEvent("21:00", "SUN", "Lol",300);
		mySchedule.addEvent("21:00", "SUN", "Lol",300);
		 mySchedule.addEvent("21:30", "tue", "aadsa",990);
		 mySchedule.addEvent("21:30", "tue", "bbb",990);
		 mySchedule.addEvent("06:00", "thu", "hockey");

		mySchedule.clearByTime("22:00");
		*/

		int number = new Random().nextInt(5 - 1 + 1) + 1;
		int day = new Random().nextInt(6 - 0 + 1) + 0;
		int time = new Random().nextInt(32 - 0 + 1) + 0;
		number = number*30;
		Integer counter = 0;
			for(int j = 0; j < 15; j++){
				number = new Random().nextInt(5 - 1 + 1) + 1;
				number = number*30;
				day = new Random().nextInt(6 - 0 + 1) + 0;
				time = new Random().nextInt(32 - 0 + 1) + 0;
				mySchedule.addEvent(mySchedule.rootTimeIndex[time], mySchedule.rootDayIndex[day], counter.toString(),number);
				counter++;
			}
		
		

		String queryDay = "mon";
		System.out.println();
		System.out.println("\t Day Printout: "+queryDay);
		Event Transverse = mySchedule.rootDay[mySchedule.dayConv(queryDay)];
		while(Transverse.down != mySchedule.rootDay[mySchedule.dayConv(queryDay)]) {
			System.out.println(Transverse.getDescription() + "\t" + Transverse.getDay() + "\t" + Transverse.getTime());
			Transverse = Transverse.down;
		}
		System.out.println(Transverse.getDescription() + "\t" + Transverse.getDay() + "\t" + Transverse.getTime());

		

		String queryTime = "06:00";
		System.out.println();
		System.out.println("\t Time Printout: "+queryTime);
		Event timeTransverse = mySchedule.rootTime[mySchedule.timeConv(queryTime)];
		while(timeTransverse != null){
			System.out.println(timeTransverse.getDescription() + "\t" + timeTransverse.getDay() + "\t" + timeTransverse.getTime());
			timeTransverse = timeTransverse.right;
		}

		System.out.println();

     	printSchedule(mySchedule);
	}

	
	
}
