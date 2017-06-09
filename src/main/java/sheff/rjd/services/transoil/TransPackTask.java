package sheff.rjd.services.transoil;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.log4j.Logger;

import ru.aisa.rgd.etd.dao.ETDSyncServiceFacade;


public class TransPackTask implements Runnable{
	
	private static Logger log = Logger.getLogger(TransPackTask.class); 
	String etdlist;
	TransService trans;
	ETDSyncServiceFacade etdsyncfacade;
	public TransPackTask(String etdlist, TransService trans, ETDSyncServiceFacade etdsyncfacade){
		this.etdlist = etdlist;
		this.trans = trans;
		this.etdsyncfacade = etdsyncfacade;
	}
	
	public void run() {

//			for (int i=0;i<20;i++){
				try{					
					int i=0;
//					while (!etdsyncfacade.getCountPortalDoc(etdlist)||i<20){
//						System.out.println("wait "+i);
//					log.debug("thread sleep for docid = "+etdlist);
//					Thread.sleep(15000);
//					i++;
//					}
				
					
					if (!etdsyncfacade.getCountPortalDoc(etdlist)){
						while (i<20){
							if (!etdsyncfacade.getCountPortalDoc(etdlist)){
//								System.out.println("wait");
								Thread.sleep(15000);
							}
							else {
								TransDocForSendObj doc = etdsyncfacade.getTransDocument(etdlist);
								trans.SendtoTransoil(doc.getEtdid(), doc.getForname(), doc.getXml(), doc.getBlob());
								break;
							}
							i++;
						}
//						System.out.println("stop");
						
					}
					
					else {
			TransDocForSendObj doc = etdsyncfacade.getTransDocument(etdlist);
			trans.SendtoTransoil(doc.getEtdid(), doc.getForname(), doc.getXml(), doc.getBlob());
					}
				}catch (Exception e){
					StringWriter outError = new StringWriter();
					PrintWriter errorWriter = new PrintWriter(outError);
					e.printStackTrace(errorWriter);
					log.error("Не удалось отправить документ с etdid = "+etdlist+ "  "+outError.toString());
					e.printStackTrace();
				}
//			}
			
		
	}

}
