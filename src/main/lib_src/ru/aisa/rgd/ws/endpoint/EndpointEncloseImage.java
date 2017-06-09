package ru.aisa.rgd.ws.endpoint;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.oxm.Marshaller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.xml.sax.ContentHandler;

import ru.aisa.edt.ImageDocDocument;
import ru.aisa.edt.ImageDocDocument.ImageDoc;
import ru.aisa.edt.Signature;
import ru.aisa.rgd.etd.dao.ETDFacade;
import ru.aisa.rgd.etd.ws.ETDUpdateBdSecurityEndpoint;
import sheff.rjd.extend.SNMPSender;
import sheff.rjd.utils.BigDocHandler;



public class EndpointEncloseImage extends ETDUpdateBdSecurityEndpoint<ContentHandler, ImageDocDocument>{protected EndpointEncloseImage(Marshaller marshaller) {
		super(marshaller);
		// TODO Auto-generated constructor stub
	}

//extends AbstractSaxPayloadEndpoint{
	/**
	 * @uml.property  name="npjt"
	 * @uml.associationEnd  
	 */
	private NamedParameterJdbcTemplate npjt;
	/**
	 * @uml.property  name="securityManager"
	 * @uml.associationEnd  
	 */
	
	private DataSource ds;
	/**
	 * @uml.property  name="sjt"
	 * @uml.associationEnd  
	 */
	private SimpleJdbcTemplate sjt;
	/**
	 * @uml.property  name="transTemplate"
	 * @uml.associationEnd  
	 */
	private TransactionTemplate transTemplate;

	private static Logger	log	= Logger.getLogger(EndpointEncloseImage.class);

	/**
	 * @return
	 * @uml.property  name="transTemplate"
	 */
	public TransactionTemplate getTransTemplate() {
		return transTemplate;
	}


	/**
	 * @param transTemplate
	 * @uml.property  name="transTemplate"
	 */
	public void setTransTemplate(TransactionTemplate transTemplate) {
		this.transTemplate = transTemplate;
	}
	public SimpleJdbcTemplate getJt() {
		return sjt;
	}


	public void setJt(SimpleJdbcTemplate sjt) {
		this.sjt = sjt;
	}

	/**
	 * @return
	 * @uml.property  name="ds"
	 */
	public DataSource getDs() {
		return ds;
	}

	/**
	 * @param ds
	 * @uml.property  name="ds"
	 */
	public void setDs(DataSource ds) {
		this.ds = ds;
	}

	/**
	 * @return
	 * @uml.property  name="securityManager"
	 */
	
	/**
	 * @return
	 * @uml.property  name="npjt"
	 */
	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}


	/**
	 * @param npjt
	 * @uml.property  name="npjt"
	 */
	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}
	@Override
	protected ContentHandler createContentHandler() throws Exception {
		return new BigDocHandler();
	}
	
	
	
	
	@Override
	protected ImageDocDocument composeInvalidSecurityResponce(Signature signature) 
	{
	ImageDocDocument responseDocument = ImageDocDocument.Factory.newInstance();
	ImageDoc response = responseDocument.addNewImageDoc();
	response.addNewSecurity().setLogonstatus("false");
	//InputStream in=new ByteArrayInputStream(responseDocument.toString().getBytes());
	//StreamSource source=new StreamSource(in);
	return responseDocument;//source;
	}

	@Override
	protected ContentHandler convertRequest(Object obj) {		
		BigDocHandler requestDocument = (BigDocHandler) obj;
		return requestDocument;
	}
	
	@Override
	protected Signature getSecurity(ContentHandler requestDocument) {
		Signature s = Signature.Factory.newInstance();
		s.setSign(((BigDocHandler)requestDocument).sign);//requestDocument.getDocumentsTableRequest().getSecurity();
		return s;
		
	}
	
	

	@Override
	protected ImageDocDocument processRequest(ContentHandler requestDocument, Signature signature, ETDFacade facade) throws Exception{ //getResponse(ContentHandler arg0) throws Exception {
		StreamSource source;
		final BigDocHandler input=(BigDocHandler)requestDocument;
		final ImageDocDocument out=ImageDocDocument.Factory.newInstance();
		out.addNewImageDoc(); 
		out.getImageDoc().setSecurity(signature);
		final Map pp = new HashMap();
		pp.put("DOCID",Integer.parseInt(input.DocId));
		final int docid=Integer.parseInt(input.DocId);
		if (input.request.equals("clear")){
		
			
				getTransTemplate().execute(new TransactionCallbackWithoutResult() {

					protected void doInTransactionWithoutResult(TransactionStatus status) {
						try{
							try{
								getNpjt().update("update SNT.images set updt=0 where DOCID=:DOCID and updt<>0",pp);
							}catch(Exception e){System.out.println("exc1"); e.printStackTrace();}
							String idstodel=input.IdsForDel;


							if (!input.BaseDocId.equals("-1")){
								final List <Integer> result=new ArrayList<Integer>();
								try{
									pp.put("BASEDOCID", Integer.parseInt(input.BaseDocId));
									getNpjt().query("select imageid from SNT.images where DOCID=:BASEDOCID order by imageid", pp, new ParameterizedRowMapper<Object>()	{

										public Object mapRow(ResultSet rs, int numrow) {
											try{


												result.add(rs.getInt("imageid"));

											}
											catch (Exception e) {
												StringWriter outError = new StringWriter();
												PrintWriter errorWriter = new PrintWriter( outError );
												e.printStackTrace(errorWriter);
												log.error(outError.toString());
											}
											return null;
										}

									});
								}catch(Exception e){
									
								}
								//System.out.println("Hello from EncloseImage");
								try{	
									for (int i=0; i<result.size();i++){
										pp.put("IMGID",result.get(i));
										getNpjt().update("insert into SNT.images (DOCID,IMAGEID,template,updt, TSCREATE) "+
												" values ( :DOCID, :IMGID,(select template from SNT.images where docid=:BASEDOCID and imageid=:IMGID ),0,current timestamp ) ", pp);
									}

								}catch(Exception e){
								    System.out
									    .println("save1");
								    out.getImageDoc().setResponse("save_error");
								}


							} else {
								try{
									getNpjt().update("update SNT.images set updt=0 where DOCID=:DOCID and updt<>0",pp);
								}catch(Exception e){e.printStackTrace();}
							}


							if (idstodel.indexOf("scan")>-1){

								ArrayList<Object[]> aa = new ArrayList<Object[]>();

								String[] ids=idstodel.split("IDscan");
								try{
									for (int i=1; i<ids.length; i++){
										aa.add(new Object[]{Integer.parseInt(ids[i].split("ID")[0]),docid});

									}
									ids=null;
									getJt().batchUpdate("update SNT.images set updt=-1 where imageid=? and DOCID=?", aa);
								}catch(Exception e){
									
								}
							}


							out.getImageDoc().setResponse("ok");
						}
						catch(Exception ex){System.out
						    .println("save2");
							StringWriter outError = new StringWriter();
							PrintWriter errorWriter = new PrintWriter( outError );
							ex.printStackTrace(errorWriter);
							log.error(outError.toString());
							status.setRollbackOnly();
							out.getImageDoc().setResponse("save_error");
							SNMPSender.sendMessage(ex);
						}
					}});
		
			InputStream in=new ByteArrayInputStream(out.toString().getBytes());
			source=new StreamSource(in);

			//sortImageIds(input.ImgIds,  input.DocId);
			return out;//source;
		}


		if (input.request.equals("save")){
		//	byte[] signature = input.sign;
			
				getTransTemplate().execute(new TransactionCallbackWithoutResult() {
					protected void doInTransactionWithoutResult(TransactionStatus status) {
						try{

							final List <String> result=new ArrayList<String>();
							List <String> idList = new ArrayList<String>();
							HashMap idsMap = new HashMap();
							try{
								int iter=0;
								while (iter<input.ImageId.size()){   
									idList.add(input.ImageId.get(iter));

									iter++;
								}

								idsMap.put("IMAGEID", idList);
								idsMap.put("DOCID",docid);
							}catch(Exception e){}
							//System.out.println("ImageID "+idsMap.get("IMAGEID"));
							//System.out.println("DocID "+idsMap.get("DOCID"));
							/*			for(int i =1; i<imgids.split("IDscan").length; i++)
			{

				result.add(imgids.split("IDscan")[i].substring(0, 1));
			}*/
							try{
								getNpjt().query("select imageid from SNT.images where DOCID=:DOCID and IMAGEID in (:IMAGEID)", idsMap, new ParameterizedRowMapper<Object>()	{


									public Object mapRow(ResultSet rs, int numrow) {
										try{

											result.add(rs.getString("imageid"));

										}
										catch (Exception e) {
											StringWriter outError = new StringWriter();
											PrintWriter errorWriter = new PrintWriter( outError );
											e.printStackTrace(errorWriter);
											log.error(outError.toString());
										}
										return null;
									}

								});

							}catch(Exception e){
							    System.out
							    .println("save3");
							    out.getImageDoc().setResponse("save_error");
							}
							

							ArrayList<Object[]> aa = new ArrayList<Object[]>();
							try{
								for (int i=0; i<result.size();i++){

									for (int test=0; test<input.ImageId.size();test++)
									{	
										if( input.ImageId.get(test).equals(result.get(i))){

											aa.add(new Object[]{input.ImageBl.get(test).getBytes("UTF-8"),result.get(i), docid});
											input.ImageId.remove(test);
											input.ImageBl.remove(test);
											break;
										} 
									}	
								}
								if (result.size()>0)getJt().batchUpdate("update SNT.images set bcp_template=?, updt=1 where IMAGEID=? and DOCID=?", aa);

							}catch(Exception e){System.out
							    .println("save4");
							    out.getImageDoc().setResponse("save_error");
							}
							try{
								aa= new ArrayList<Object[]>();
								for (int i=0; i<input.ImageId.size();){

									aa.add(new Object[]{docid,input.ImageId.get(i),input.ImageBl.get(i).getBytes("UTF-8"),input.ImageBl.get(i).getBytes("UTF-8")});

									input.ImageId.remove(i);
									input.ImageBl.remove(i);
								}
								if (aa.size()>0)
									getJt().batchUpdate("insert into SNT.images (DOCID,IMAGEID,template,bcp_template,updt, TSCREATE) "+
											" values ( ?, ?,?, ?,0,current timestamp )",aa  );
							
							
								out.getImageDoc().setResponse("ok");
							}catch(Exception e){
							    System.out
							    .println("save5");
								out.getImageDoc().setResponse("save_error");
								}
						
						}
						catch(Exception ex){
							StringWriter outError = new StringWriter();
							PrintWriter errorWriter = new PrintWriter( outError );
							ex.printStackTrace(errorWriter);
							log.error(outError.toString());							
							status.setRollbackOnly();
							System.out
							    .println("save6");
							out.getImageDoc().setResponse("save_error");
							
							SNMPSender.sendMessage(ex);
							try{

								final List <String> result=new ArrayList<String>();
								List <String> idList = new ArrayList<String>();
								HashMap idsMap = new HashMap();
								try{
									int iter=0;
									while (iter<input.ImageId.size()){   
										idList.add(input.ImageId.get(iter));

										iter++;
									}

									idsMap.put("IMAGEID", idList);
									idsMap.put("DOCID",docid);
								}catch(Exception e){}
								//System.out.println("ImageID "+idsMap.get("IMAGEID"));
								//System.out.println("DocID "+idsMap.get("DOCID"));
								/*			for(int i =1; i<imgids.split("IDscan").length; i++)
				{

					result.add(imgids.split("IDscan")[i].substring(0, 1));
				}*/
								try{
									getNpjt().query("select imageid from SNT.images where DOCID=:DOCID and IMAGEID in (:IMAGEID)", idsMap, new ParameterizedRowMapper<Object>()	{


										public Object mapRow(ResultSet rs, int numrow) {
											try{

												result.add(rs.getString("imageid"));

											}
											catch (Exception e) {
												StringWriter outError = new StringWriter();
												PrintWriter errorWriter = new PrintWriter( outError );
												e.printStackTrace(errorWriter);
												log.error(outError.toString());
											}
											return null;
										}

									});

								}catch(Exception e){
								    System.out
								    .println("save7");
								    out.getImageDoc().setResponse("save_error");
								}
								
								ArrayList<Object[]> aa = new ArrayList<Object[]>();
								try{
									for (int i=0; i<result.size();i++){

										for (int test=0; test<input.ImageId.size();test++)
										{	
											if( input.ImageId.get(test).equals(result.get(i))){

												aa.add(new Object[]{input.ImageBl.get(test).getBytes("UTF-8"),result.get(i), docid});
												input.ImageId.remove(test);
												input.ImageBl.remove(test);
												break;
											} 
										}	
									}
									if (result.size()>0)getJt().batchUpdate("update SNT.images set bcp_template=?, updt=1 where IMAGEID=? and DOCID=?", aa);

								}catch(Exception e){
								    System.out
								    .println("save8");
								    out.getImageDoc().setResponse("save_error");
								}
								try{
									aa= new ArrayList<Object[]>();
									for (int i=0; i<input.ImageId.size();){

										aa.add(new Object[]{docid,input.ImageId.get(i),input.ImageBl.get(i).getBytes("UTF-8"),input.ImageBl.get(i).getBytes("UTF-8")});

										input.ImageId.remove(i);
										input.ImageBl.remove(i);
									}
									if (aa.size()>0)
										getJt().batchUpdate("insert into SNT.images (DOCID,IMAGEID,template,bcp_template,updt, TSCREATE) "+
												" values ( ?, ?,?, ?,0,current timestamp )",aa  );
								}catch(Exception e){
								}
								out.getImageDoc().setResponse("ok");
							}
							catch(Exception ex1){
								StringWriter outError1 = new StringWriter();
								PrintWriter errorWriter1 = new PrintWriter( outError1 );
								ex.printStackTrace(errorWriter1);
								log.error(outError1.toString());							
								status.setRollbackOnly();
								System.out
								    .println("save9");
								out.getImageDoc().setResponse("save_error");
							//	SNMPSender.sendMessage(ex);
							}
						}
					}});



			InputStream in=new ByteArrayInputStream(out.toString().getBytes());
			source=new StreamSource(in);
			//sortImageIds(input.ImgIds,  input.DocId);

			return out;//source;

		}
		return null;
	}


}