package sheff.rjd.services.syncutils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
public class db2DateGenerator {

	
	public String Date(String indate){
		
		SimpleDateFormat myDateFormat = new SimpleDateFormat("dd.MM.yyyy");
		
		try{
		    Date date = myDateFormat.parse(indate);
		    myDateFormat.applyPattern("yyyy-MM-dd");
		    String strDate = myDateFormat.format(date);
		    return strDate;
		} catch(Exception e){
		    e.printStackTrace();
		    return null;
		    
		}
		
	}
	
	public Date shiftDate(String indate, boolean plus) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		Date date = sdf.parse(indate);
		 Calendar c = Calendar.getInstance();
		    c.setTime(date);
		    
		    if (plus)
		    c.add(Calendar.DATE, 1);
		    else if (!plus)
		    	 c.add(Calendar.DATE, -1);
		    
		    date.setTime(c.getTimeInMillis());
		
		return date;
	}
	
	
}
