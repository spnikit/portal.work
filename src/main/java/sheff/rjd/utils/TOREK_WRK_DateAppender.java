package sheff.rjd.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TOREK_WRK_DateAppender {

public String fromtorek (String indate) throws ParseException{
	SimpleDateFormat sdf = new SimpleDateFormat();
	sdf.applyPattern("yyyyMMddHHmmss");
	Date date = sdf.parse(indate);
	sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
	return sdf.format(date);
	
}	
public String totorek (String indate) throws ParseException{
	SimpleDateFormat sdf = new SimpleDateFormat();
	sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
	Date date = sdf.parse(indate);
	sdf.applyPattern("yyyyMMddHHmmss.sssssss");
	return sdf.format(date);
}

public String tovrk (String indate) throws ParseException{
	SimpleDateFormat sdf = new SimpleDateFormat();
	sdf.applyPattern("yyyy-MM-dd HH.mm.ss");
	Date date = sdf.parse(indate);
	sdf.applyPattern("yyyy-MM-dd HH:mm");
	return sdf.format(date);
}


}
