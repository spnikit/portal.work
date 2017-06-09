package sheff.rjd.services.transoil;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

import ru.aisa.rgd.etd.dao.ETDSyncServiceFacade;

public class SendObject {
	private static Logger log = Logger.getLogger(SendObject.class); 
	private ETDSyncServiceFacade etdsyncfacade;
	private TransService trans;
	
	public ETDSyncServiceFacade getEtdsyncfacade() {
		return etdsyncfacade;
	}

	public void setEtdsyncfacade(ETDSyncServiceFacade etdsyncfacade) {
		this.etdsyncfacade = etdsyncfacade;
	}

	public TransService getTrans() {
		return trans;
	}

	public void setTrans(TransService trans) {
		this.trans = trans;
	}

	public void Send(String etdlist){
		
			try{
//System.out.println(etdlist);
		TransDocForSendObj doc = etdsyncfacade.getTransDocument(etdlist);
		trans.SendtoTransoil(doc.getEtdid(), doc.getForname(), doc.getXml(), doc.getBlob());


			}catch (Exception e){
				StringWriter outError = new StringWriter();
				PrintWriter errorWriter = new PrintWriter(outError);
//				e.printStackTrace(errorWriter);
				log.error("Не удалось отправить документ с etdid = "+etdlist+ "  "+outError.toString());
//				e.printStackTrace();
			}
			
		
		
	}
	
	
}
