package sheff.rjd.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.aisa.rgd.etd.objects.ETDDocument;

public class DocListDateComparator {
	
	public List<ETDDocument> resp(Date dateafter, Date datebefore, List<ETDDocument> in){
//		System.out.println(dateafter);
//		System.out.println(datebefore);
//	System.out.println("insize: "+in.size());
		List<ETDDocument> resplist = new ArrayList<ETDDocument>();
		
		if (dateafter==null&&datebefore==null)
		return in;
		
		else {
			
			List<ETDDocument> withoutnull= new ArrayList<ETDDocument>();
			
			int z = 0;
			while (z<in.size()){

				
				if (in.get(z).getLastDate()!=null)
					withoutnull.add(in.get(z));
				z++;
			}
			
			
//			System.out.println("withoutnullsize: "+withoutnull.size());
			
			
			
			if (dateafter!=null&&datebefore==null){
				int i = 0;
				
				while (i<withoutnull.size()){
					
				
					if (withoutnull.get(i).getLastDate().after(dateafter)){
						resplist.add(withoutnull.get(i));
						}
					i++;
				}
				
				
			}
			else if (dateafter==null&&datebefore!=null){
				int i = 0;
				
				while (i<withoutnull.size()){
					
					if (withoutnull.get(i).getLastDate().before(datebefore))
						resplist.add(withoutnull.get(i));
					i++;
				}
				
				
			}
			else if (dateafter!=null&&datebefore!=null){
				int i = 0;
				
				while (i<withoutnull.size()){
					
					if (withoutnull.get(i).getLastDate().before(datebefore)&&withoutnull.get(i).getLastDate().after(dateafter))
						resplist.add(withoutnull.get(i));
					i++;
				}
				
				
			}
			
//			System.out.println("response size: "+ resplist.size());
			
			return resplist;
		}
		
		
	}
	
	
}
