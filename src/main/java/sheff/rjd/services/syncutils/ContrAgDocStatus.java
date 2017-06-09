package sheff.rjd.services.syncutils;

import ru.aisa.rgd.etd.objects.ETDDocument;

public class ContrAgDocStatus {

	public static int getstatus(ETDDocument obj) {

		if (obj.isDrop()&&!obj.getName().equals("Счет-фактура"))
			return 4;
		else if (obj.isDrop()&&obj.getName().equals("Счет-фактура"))
			return 7;
		
		else if (obj.getName().equals("Пакет документов")) {
			if (obj.getCount() == -1)
				return 1;
			else if (obj.getCount() == 0)
				return 2;
			else if (obj.getCount() == 1)
				return 3;
		}

		else if (obj.getName().equals("РДВ")) {
			if (obj.getSigned() == 0)
				return 1;
			else if (obj.getSigned() == 1)
				return 3;

		}

		else if (obj.getName().equals("Счет-фактура")){
			if (obj.getSigned() == 0&&obj.getSfSign()==0)
				return 5;
			else if (obj.getSigned() == 0&&obj.getSfSign()==1)
				return 6;
			else if (obj.getSigned() == 1&&obj.getSfSign()==1)
				return 8;
		}
		
		
		else {
			if (obj.getSigned() == 0 && obj.getVisible() != 2)
				return 1;
			else if (obj.getSigned() == 0 && obj.getVisible() == 2)
				return 2;
			else if (obj.getSigned() == 1)
				return 3;

		}

		return -1;
	}
	
	
}
