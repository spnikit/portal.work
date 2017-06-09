package sheff.rjd.ws.OCO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



@SuppressWarnings("unchecked")
public class TORLists {

	public static final String SF = "Счет-фактура";
	public static final String CorrSF = "Корректировочный счет-фактура";
	public static final String Package = "Пакет документов";
	public static final String FPu26 = "ФПУ-26";
	public static final String MH3 = "МХ-3";
	public static final String TORG12 = "ТОРГ-12";
	public static final String MH1 = "МХ-1";
	public static final String ACTzud = "Акт ЗУД";
	public static final String ACTpp = "Акт приема передачи ТМЦ";
	public static final String ACTbrak = "Акт браковки";
	public static final String VU22 = "ВУ-22";
	public static final String VU23 = "ВУ-23_О";
	public static final String VU36 = "ВУ-36М_О";
	public static final String VU41 = "ВУ-41 ЭТД";
	public static final String RDV = "РДВ";
	public static final String Card = "Карточка документ";
	public static final String Spravka = "Справка 2612";
	public static final String RSH = "Расчет стоимости услуг по хранению";
	public static final String RSV = "Расчет стоимости услуг по погрузке/выгрузке";
	
	public static final String GU45 = "ГУ-45";
	public static final String GU38a = "ГУ-38а";
	public static final String GU38b = "ГУ-38б";
	public static final String GU23 = "ГУ-23";
	public static final String perech = "Перечень вагонов";
	
	public static final String CardRZDS = "Карточка документ РЖДС";
	public static final String SFS = "Счет-фактура РЖДС на услугу";
	public static final String SFG = "Счет-фактура РЖДС на товар";
	public static final String TORG12RZDS = "ТОРГ-12 РЖДС";
	public static final String Actrzds = "Акт РЖДС";
	
	public static final String FPU26ASR = "ФПУ-26 АСР";
	public static final String Schet = "Счет";
	public static final String Rashifr = "Расшифровка";
	public static final String PackageCSS = "Пакет документов ЦСС";
	public static final String SFCSS = "Счет-фактура ЦСС";
	
	public static final String ActRTK = "Акт РТК";
	public static final String CorrActRTK = "Корректировочный акт РТК";
	public static final String PackageRTK = "Пакет документов РТК";
	public static final String SFRTK = "Счет-фактура РТК";
	public static final String CorrSFRTK = "Корректировочный счет-фактура РТК";
	public static final String PRETENSION = "Претензия";
	
	public static List packlist = new ArrayList();
	static {
		packlist.add(ACTzud);
		packlist.add(VU22);
		packlist.add(VU23);
		packlist.add(VU36);
		packlist.add(VU41);
		packlist.add(Card);
		packlist.add(RDV);
		packlist.add(ACTbrak);
		packlist.add(MH3);
		packlist.add(MH1);
		packlist.add(ACTpp);
		packlist.add(Spravka);
		packlist.add(RSH);
		packlist.add(RSV);
		packlist.add(Rashifr);
		packlist.add(Schet);
		packlist.add(FPU26ASR);
	}
	
	public static List autosign = new ArrayList();
	static {
		autosign.add(ACTzud);
		autosign.add(VU22);
		autosign.add(VU23);
		autosign.add(VU36);
		autosign.add(VU41);
		autosign.add(Card);
		autosign.add(ACTbrak);
		autosign.add(Spravka);
		autosign.add(GU23);
		autosign.add(RSH);
		autosign.add(RSV);
	}
	
	public static List acceptlist = new ArrayList();
	static {
		acceptlist.add(MH1);
		acceptlist.add(MH3);
		acceptlist.add(ACTpp);
		acceptlist.add(FPu26);
		acceptlist.add(FPU26ASR);
	}
	
	public static List asulist = new ArrayList();
	static {
		asulist.add(MH1);
		asulist.add(MH3);
		asulist.add(ACTpp);
		asulist.add(FPu26);
		asulist.add(RDV);
	}
	
	public static List mrm = new ArrayList();
	static {
		mrm.add(GU45);
		mrm.add(GU38a);
		mrm.add(GU38b);
		mrm.add(GU23);
		mrm.add(perech);
	}
	public static HashMap<String ,Object> exportList = new HashMap<String , Object>();
	static {
		exportList.put(FPu26, "FPU-26");
		exportList.put(SF, "Schet-factura");
		exportList.put(MH3, "MX-3");
		exportList.put(MH1, "MX-1");
		exportList.put(ACTzud, "Act ZUD");
		exportList.put(ACTpp, "Act priema-peredachi");
		exportList.put(ACTbrak, "Act brakovki");
		exportList.put(VU22, "VU-22");
		exportList.put(VU23, "VU-23O");
		exportList.put(VU36, "VU-36MO");
		exportList.put(VU41, "VU-41");
		exportList.put(RDV, "RDV");
		exportList.put(Card, "Card-document");
		exportList.put(Spravka, "Spravka 2612");
		exportList.put(RSH, "Raschet uslug po hraneniu");
		exportList.put(RSV, "Raschet uslug po pogruzke-vigruzke");
		exportList.put(GU23, "GU-23");
		
		exportList.put(CardRZDS, "Card-document");
		exportList.put(SFS, "Schet-factura na uslugu");
		exportList.put(SFG, "Schet-factura na tovar");
		exportList.put(TORG12RZDS, "TORG-12");
		exportList.put(Actrzds, "Act");
		exportList.put(TORG12, "TORG-12");
		
		exportList.put(FPU26ASR, "FPU26_ASR");
		exportList.put(Schet, "Schet");
		exportList.put(Rashifr, "Rashifrovka");
		
		exportList.put(FPU26ASR, "FPU26_ASR");
		exportList.put(Schet, "Schet");
		exportList.put(Rashifr, "Rashifrovka");
		
		exportList.put(ActRTK, "Act_RTK");
		exportList.put(SFRTK, "SFRTK");
		exportList.put(CorrSFRTK, "CorrSFRTK");
		exportList.put(CorrActRTK, "CorrAct_RTK");
		
		exportList.put(PRETENSION, "Pretension");
	}
	
	public static List adminlistTOR = new ArrayList();
	static {
		adminlistTOR.add(ACTzud);
		adminlistTOR.add(VU22);
		adminlistTOR.add(VU23);
		adminlistTOR.add(VU36);
		adminlistTOR.add(VU41);
		adminlistTOR.add(Card);
		adminlistTOR.add(RDV);
		adminlistTOR.add(ACTbrak);
		adminlistTOR.add(MH3);
		adminlistTOR.add(MH1);
		adminlistTOR.add(ACTpp);
		adminlistTOR.add(Spravka);
		adminlistTOR.add(SF);
		adminlistTOR.add(Package);
		adminlistTOR.add(FPu26);
		adminlistTOR.add(RSH);
		adminlistTOR.add(RSV);
		adminlistTOR.add(GU23);
	}
	
	public static HashMap<String ,Integer> adminlistview = new HashMap<String , Integer>();
	static {
		adminlistview.put(FPu26, 0);
		adminlistview.put(SF, 0);
		adminlistview.put(MH3, 0);
		adminlistview.put(MH1, 0);
		adminlistview.put(ACTzud, 0);
		adminlistview.put(ACTpp, 0);
		adminlistview.put(ACTbrak, 0);
		adminlistview.put(VU22, 0);
		adminlistview.put(VU23, 0);
		adminlistview.put(VU36,0);
		adminlistview.put(VU41,0);
		adminlistview.put(RDV, 0);
		adminlistview.put(Card, 0);
		adminlistview.put(Spravka, 0);
		adminlistview.put(RSH, 0);
		adminlistview.put(RSV, 0);
		adminlistview.put(Package, 0);
		adminlistview.put(GU23,0);
	}
	
	public static HashMap<String ,Integer> adminlistacc = new HashMap<String , Integer>();
	static {
		adminlistacc.put(FPu26, 0);
		adminlistacc.put(MH3, 0);
		adminlistacc.put(MH1, 0);
		adminlistacc.put(ACTzud, 0);
		adminlistacc.put(ACTpp,0);
		adminlistacc.put(ACTbrak, 0);
		adminlistacc.put(VU22, 0);
		adminlistacc.put(VU23, 0);
		adminlistacc.put(VU36, 0);
		adminlistacc.put(VU41, 0);
		adminlistacc.put(Card, 0);
		adminlistacc.put(Spravka, 0);
		adminlistacc.put(RSH, 0);
		adminlistacc.put(RSV, 0);
		adminlistacc.put(Package, 1);
		adminlistacc.put(RDV, 1);
		adminlistacc.put(SF, 1);
	}
	public static HashMap<String ,Integer> adminlissign = new HashMap<String , Integer>();
	static {
		adminlissign.put(FPu26, 1);
		adminlissign.put(SF, 1);
		adminlissign.put(MH3, 1);
		adminlissign.put(MH1, 1);
		adminlissign.put(ACTzud, 1);
		adminlissign.put(ACTpp, 1);
		adminlissign.put(ACTbrak, 1);
		adminlissign.put(VU22, 1);
		adminlissign.put(VU23, 1);
		adminlissign.put(VU36, 1);
		adminlissign.put(VU41, 1);
		adminlissign.put(RDV, 1);
		adminlissign.put(Card, 1);
		adminlissign.put(Spravka, 1);
		adminlissign.put(RSH, 1);
		adminlissign.put(RSV, 1);
		adminlissign.put(Package, 1);
	}
	}
	

