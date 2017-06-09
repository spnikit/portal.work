package sheff.rjd.services;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.oxm.Marshaller;
import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;

import com.aisa.portal.invoice.ttk.SellerSignedInvoceToTTK;

import ru.aisa.rgd.etd.dao.ETDSyncServiceFacade;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.TypeConverter;
import rzd8888.gvc.etd.was.etd.synchronizeXfdlDoc.SynchronizeXfdlDocResponse;
import rzd8888.gvc.etd.was.etd.synchronizeXfdlDoc.SynchronizeXfdlDocResponseDocument;
import sheff.rjd.utils.Base64;

@SuppressWarnings("deprecation")
public class SynchronizeXfdlDocResponseEndpoint extends
		AbstractMarshallingPayloadEndpoint {

	private static Log log = LogFactory
			.getLog(SynchronizeXfdlDocResponseEndpoint.class);
	private NamedParameterJdbcTemplate npjt;
	private SellerSignedInvoceToTTK ssiobj;
	 private ETDSyncServiceFacade etdsyncfacade;
	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}

	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}
	
	public SellerSignedInvoceToTTK getSsiobj() {
		return ssiobj;
	}

	public void setSsiobj(SellerSignedInvoceToTTK ssiobj) {
		this.ssiobj = ssiobj;
	}

	public ETDSyncServiceFacade getEtdsyncfacade() {
		return etdsyncfacade;
	}

	public void setEtdsyncfacade(ETDSyncServiceFacade etdsyncfacade) {
		this.etdsyncfacade = etdsyncfacade;
	}

	public SynchronizeXfdlDocResponseEndpoint(Marshaller marshaller) {
		super(marshaller);
	}

	protected Object invokeInternal(Object obj) {
		try {

			SynchronizeXfdlDocResponseDocument responsedoc = (SynchronizeXfdlDocResponseDocument) obj;

			SynchronizeXfdlDocResponse response = responsedoc
					.getSynchronizeXfdlDocResponse();

			if (response.getCode() == 0){
				log.debug("response for etdid = "+response.getEtdDocId()+" successfully got to ETD");
				
				if (response.isSetParams()){
				
				try{
						
			for (int i= 0; i<response.getParams().getParamArray().length;i++){
				
				if (response.getParams().getParamArray(i).getName().equals("_portalId")){
					try{
					Long id = Long.valueOf(response.getParams().getParamArray(i).getStringValue());
					Long etdid = response.getEtdDocId();
					HashMap<String, Object>  pp = new HashMap<String, Object>();
					pp.put("ETDID", etdid);
					pp.put("ID", id);
					npjt.update("update snt.docstore set etdid =:ETDID where id =:ID", pp);
					
					try{
						String formname = (String) npjt.queryForObject("select rtrim(name) from snt.doctype where id = (select typeid from snt.docstore where id =:ID)", pp, String.class);
					if (formname.equals("Пакет документов РТК")){
						
						int flag = 0;
						
						try{
						byte[] blob = npjt.queryForObject("select bldoc from snt.docstore where id = :ID", pp, byte[].class);
						ETDForm form=ETDForm.createFromArchive(blob);
						DataBinder b = form.getBinder();
						flag = b.getInt("flag");
						
						} catch (Exception e){
							log.error(TypeConverter.exceptionToString(e));
						}
						if (flag==0)
						pp.put("status", "Передан Заказчику");
						
						else if (flag==1)
						pp.put("status", "Передан ДИ");
						npjt.update("update snt.docstore set reqnum =:status where id =:ID", pp);
					}
					
					if (formname.contains("чет-фактура")){
						try{
							
							byte[] blob = npjt.queryForObject("select bldoc from snt.docstore where id = :ID", pp, byte[].class);
							int predid = npjt.queryForInt("select predid from snt.docstore where id =:ID", pp);
							
							ETDForm form=ETDForm.createFromArchive(blob);
							DataBinder b = form.getBinder();
							String scabinetid = etdsyncfacade.getCabinetIdByPred(predid);
							String bcabinetid = etdsyncfacade.getCabinetIdByPred(etdsyncfacade.getPredMaker("ОАО «РЖД»"));
							byte[] xml_text = Base64.decode(b.getValue("xml_text"));
							byte[] xml_sign = Base64.decode(b.getValue("xml_sign"));
							if (formname.contains("Счет-фактура")){
							ssiobj.processSF(bcabinetid, scabinetid, id, xml_text, xml_sign, false);
							}else
								{
								ssiobj.processSF(bcabinetid, scabinetid, id, xml_text, xml_sign, true);
								}
							getNpjt().update("update snt.docstore set sf_sign = 0, sf_gfsgn =0  where id =:ID", pp);
							//FIXME for test
//							getNpjt().update("update snt.docstore set sf_sign = 1, sf_gfsgn =1  where id =:ID", pp);
							
						} catch (Exception e){
							log.error(TypeConverter.exceptionToString(e));
						}
					}
					
					if (formname.equals("ГУ-2б")){
						npjt.update("update snt.docstore set signlvl =null where id =:ID", pp);
					}
					
					
					} catch(Exception e){
						log.error("Не удалось определить тип документа. "+TypeConverter.exceptionToString(e));
					}
					
					
					
					}catch (Exception e) {
						StringWriter outError = new StringWriter();
						PrintWriter errorWriter = new PrintWriter(outError);
						e.printStackTrace(errorWriter);
						log.error("Не удалось получить параметр ETDID "+response.getEtdDocId()+outError.toString());
						e.printStackTrace();
					}
					
				}
			}
			}catch (Exception e) {
				StringWriter outError = new StringWriter();
				PrintWriter errorWriter = new PrintWriter(outError);
				e.printStackTrace(errorWriter);
				log.error("Не удалось получить параметры ответа "+response.getEtdDocId()+outError.toString());
				e.printStackTrace();
			}
			}
			}
			else log.error("response etdid = "+response.getEtdDocId()+" didn't get to ETD. Error is: "+response.getDescription()+"");
		} catch (Exception e) {
			StringWriter outError = new StringWriter();
			PrintWriter errorWriter = new PrintWriter(outError);
			e.printStackTrace(errorWriter);
			log.error(outError.toString());
			e.printStackTrace();

			return obj;
		}
		return obj;

	}

}
