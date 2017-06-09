package com.aisa.portal.invoice.ttk.endpoints;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Random;

import org.springframework.oxm.Marshaller;

import com.aisa.portal.invoice.integration.endpoints.objects.DocumentsObj;
import com.aisa.portal.invoice.integration.errors.TTKErrors;
import com.aisa.portal.invoice.integration.facade.PortalSFFacade;
import com.aisa.portal.invoice.ttk.SellerSignedInvoceToTTK;
import com.aisa.portal.invoice.ttk.endpoints.abstracts.PortalTTKAbstractEndpoint;
 

import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.documents.StandartResponseWrapper;
import ru.aisa.rgd.ws.endpoint.ETDAbstractEndpoint;
import ru.aisa.rgd.ws.endpoint.ResponseAdapter;
import ru.aisa.rgd.ws.exeption.ServiceError;
 
import ru.iit.portal8888.portal.SNoticeInvoiceRequestDocument;
import ru.iit.portal8888.portal.SNoticeInvoiceRequestDocument.SNoticeInvoiceRequest;
import ru.iit.portal8888.portal.SNoticeInvoiceResponseDocument;
import ru.iit.portal8888.portal.SNoticeInvoiceResponseDocument.SNoticeInvoiceResponse;

public class TestEndpoint extends PortalTTKAbstractEndpoint<StandartResponseWrapper>{
	SellerSignedInvoceToTTK obj;
	public SellerSignedInvoceToTTK getObj() {
		return obj;
	}

	public void setObj(SellerSignedInvoceToTTK obj) {
		this.obj = obj;
	}

	protected TestEndpoint(Marshaller marshaller) {
		super(marshaller);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ResponseAdapter<StandartResponseWrapper> processRequest(Object arg, PortalSFFacade facade)
			throws Exception {
		
		
		SNoticeInvoiceRequestDocument docreq=(SNoticeInvoiceRequestDocument)arg;
		SNoticeInvoiceRequest request = docreq.getSNoticeInvoiceRequest();
	//	System.out.println("facade.CheckSignatureXML(request.getXml(), request.getSign()) "+facade.CheckSignatureXML(request.getXml(), request.getSign()));
		FileInputStream in=new  FileInputStream("/home/zpss/work/testSF_out.xml");
		FileChannel fch = in.getChannel();

		ByteBuffer bf=  ByteBuffer.allocate((int) fch.size());
		fch.read(bf);
		fch.close();
		in.close();
		byte[] xml=bf.array();
		byte[] sgn=facade.ConsrtuctSignature(xml);
		 Random randomGenerator = new Random();
		 int randomInt = randomGenerator.nextInt(1000);
		  System.out.println(randomInt);
 	  obj.processSF("ADEE7F6B-7D99-42CE-9454-713111A7B58C", "F265D303-2F1B-4259-B0F5-D17097C7C6A9",  randomInt , xml, sgn, false);
		    	StandartResponseWrapper wrapper = new StandartResponseWrapper();  
				ResponseAdapter<StandartResponseWrapper> adapter = new ResponseAdapter<StandartResponseWrapper>(false , wrapper, TTKErrors.ERR_OK);
				return adapter;
		  
	}

	@Override
	protected Object composeResponce(ResponseAdapter<StandartResponseWrapper> adapter) {
		SNoticeInvoiceResponseDocument respdoc=SNoticeInvoiceResponseDocument.Factory.newInstance();
		SNoticeInvoiceResponse response = respdoc.addNewSNoticeInvoiceResponse();
		if (adapter.isStatus())
		{
			response.setCode(adapter.getResponse().getCode());
		 
		}
		else
		{
			response.setCode(adapter.getError().getCode());
		 
		}
		return respdoc;
	}

	@Override
	protected ResponseAdapter<StandartResponseWrapper> processRequest(
			Object arg, ServiceFacade facade) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ArrayList<DocumentsObj> createDocs(Object arg, String guid)
			throws Exception {
		ArrayList<DocumentsObj> docs=new ArrayList<DocumentsObj> ();

		return docs;
	}

}
