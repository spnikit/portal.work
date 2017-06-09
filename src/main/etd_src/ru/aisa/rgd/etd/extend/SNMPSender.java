/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.aisa.rgd.etd.extend;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.PDUv1;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.IpAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import ru.aisa.rgd.ws.utility.TypeConverter;
import ru.aisa.rjd.log4j.level.RzdLevel;
import sheff.rjd.utils.AppCtxGetterUtils;

import com.ibm.db2.jcc.DB2Diagnosable;

/**
 *
 * @author syndrome
 */
public class SNMPSender
{
	
	//static String snmpIP = "udp:10.200.1.20/162";
	//static String snmpIP = "udp:10.0.0.196/162";	
	 static String messageOID;// = "1.2.0.6.8.5";
	
	 static CommunityTarget target;
	 static CommunityTarget target2;
	 static TransportMapping transport;
	 static Snmp snmp;
	 
	 static final Logger	logger	= Logger.getLogger(SNMPSender.class);
	 static Logger supportLog = Logger.getLogger(SNMPSender.class);
	 	 
	 static
	 {
		 	messageOID = (String)AppCtxGetterUtils.getApplicationContext().getBean("snmpOID");
//		 	System.out.println("OID"+new OID (messageOID));
	        Address targetAddress = GenericAddress.parse((String)AppCtxGetterUtils.getApplicationContext().getBean("snmpip"));
	        target = new CommunityTarget();
	        target.setCommunity(new OctetString("public"));
	        target.setAddress(targetAddress);
	        target.setRetries(1);
	        target.setTimeout(1000);
	        target.setVersion(SnmpConstants.version1);
	        target2 = new CommunityTarget();
	        target2.setCommunity(new OctetString("public"));
	        targetAddress = GenericAddress.parse((String)AppCtxGetterUtils.getApplicationContext().getBean("snmpip2"));
	        target2.setAddress(targetAddress);
	        target2.setRetries(1);
	        target2.setTimeout(1000);
	        target2.setVersion(SnmpConstants.version1);
	        	       	        
	        TransportMapping transport;
			try 
			{
				transport = new DefaultUdpTransportMapping();
				snmp = new Snmp(transport);
			} catch (IOException e) 
			{
				logger.error(TypeConverter.exceptionToString(e));
			}
	        
	 }
	 
	 
		 
	 private static PDUv1 _createMessage(String snmpEvent, String message)
	 {
		GregorianCalendar gc = new GregorianCalendar();
        int h = gc.get(Calendar.HOUR_OF_DAY);
        int m = gc.get(Calendar.MINUTE);
        int s = gc.get(Calendar.SECOND);
        long timeStamp = 100 * (s + (60 * m) + (60*60*h));
        
		PDUv1 pdu = new PDUv1();
        pdu.setType(PDU.V1TRAP);	
        pdu.setAgentAddress(new IpAddress((String)AppCtxGetterUtils.getApplicationContext().getBean("gvcipSNMP")));
        pdu.setGenericTrap(1);
        pdu.setSpecificTrap(1);
        pdu.setTimestamp(timeStamp);
        OctetString val = new OctetString();
        val.setValue(snmpEvent +  message );
 		pdu.add(new VariableBinding(new OID (messageOID), val));
 		
		return pdu;
	 }
	 
	 
	 
	 private static void _sendSNMPMessage(String snmpEvent, String message)
	 {
		 try
		 {
			 PDUv1 pdu = _createMessage(snmpEvent, message);
			 snmp.trap(pdu, target);
			 snmp.trap(pdu, target2);
			// snmp.close();

		 }
		 catch (Exception e)
		 {
			 logger.error(TypeConverter.exceptionToString(e));
		 }
	 }
	 
	
	 
	 
	 public static void sendMessage(String snmpEvent, String msg)
	 {
		_sendSNMPMessage(snmpEvent, msg);
		
	 }
	 
	 public static void sendMessage(String snmpEvent, String prefix, String code, String object,String msg)
	 {
		 msg =" "+code+" Code= "+prefix+" Message:'"+msg+"'";
		 sendMessage(snmpEvent, msg);
		 msg = " Code= "+prefix+" Details: "+code+" Object: "+object+" Message:'"+msg.substring(msg.indexOf(":"))+"'";
		 supportLog.log(RzdLevel.RZD_TRACE, snmpEvent + msg);
	 }
	 
	 public static void sendMessage(Exception e)
	 {
		 sendMessage(e,null);
	 }
	 
	 public static void sendMessage(Exception e,String more){
					 
		 String dbname = (more==null?"AS_ETD":more);    //AS_ETD, ABDPV, CNSI
		 if (e instanceof DataAccessException){
			 DataAccessException dae = (DataAccessException)e;
			 if (dae.getCause() instanceof SQLException){
				 SQLException sqle =  (SQLException)dae.getCause();
				 if (sqle instanceof DB2Diagnosable){
					 DB2Diagnosable db2d =  (DB2Diagnosable)sqle;	
					 if ((db2d.getSqlca().getSqlCode() == -430 || db2d.getSqlca().getSqlCode() == -471) && dbname.equals("AS_ETD")) sendSPsnmp(db2d);
					 else {
					 
					 String prefix = detectprefix(dbname, db2d.getSqlca().getSqlCode());
					 SNMPSender.sendMessage("SQL_ERROR", 
							 					prefix, 	
							 						String.valueOf(db2d.getSqlca().getSqlCode()),
							 							"DB="+dbname, 
							 								db2d.getSqlca().getSqlErrmc());
					 }
				 }
				 else  {
					 //not DB2diag
				 }			 
			}else if(e instanceof IncorrectResultSizeDataAccessException){
				 //incorrect sql 
				 //do nothing
			 }
			 else {
				 //not sqlexception
//				 SNMPSender.sendMessage("SQL_ERROR", 
//		 									"SQL_0006", 	
//		 										"",
//		 											"DB="+dbname, 
//		 												convertException(e));
			 }
		 }
		 else{
			 //not dataaccessexception
//			 SNMPSender.sendMessage("SQL_ERROR", 
//										"SQL_0007", 	
//											"",
//											"DB="+dbname, 
//												convertException(e));
		 }
		 
		 
		 
	 }

	
    private static void sendSPsnmp(DB2Diagnosable db2d) {
		int code = db2d.getSqlca().getSqlCode();
		String prefix = (code==-430?"SP_0001":"SP_0002");
		SNMPSender.sendMessage("SP_ERROR", 
									prefix, 	
										String.valueOf(code),
											"SP", 
												db2d.getSqlca().getSqlErrmc());
		
	}



	private static String detectprefix(String dbname, int sqlCode) {
		if (!dbname.equals("AS_ETD")) return "SQL_0005";
		else if (sqlCode == -551 || sqlCode == -552) 					return "SQL_0001";
		else if (sqlCode == -904 || sqlCode == -911 || sqlCode == -913) return "SQL_0002";
		else if (sqlCode == -923 || sqlCode == -924) 					return "SQL_0003";
		else if (sqlCode == -901 || sqlCode == -905) 					return "SQL_0004";
		else return "SQL_9999";
	}



	/*public static void main(String[] args){
		Exception e=null;
		SNMPSender.sendMessage("SYS_ERROR", "SYS_0001", "0001", "ETD", "Crypto Lib Error");
		SNMPSender.sendMessage(e);
		SNMPSender.sendMessage(e,"ABDPV");
		SNMPSender.sendMessage(e,"CNSI");
		SNMPSender.sendMessage("CA_ERROR", "CA_0001", "0001", "", "OCSP/TSP ip= IP connection problem");
		SNMPSender.sendMessage("CA_ERROR", "CA_0002", "0002", "", "OCSP ip= IP Certificate wrong status. Certificate NUM, status = STATUS_NUM");
		
		SNMPSender.sendMessage("ETD_EVENT", "ETD_0001", "0001", "", "Application AS ETD version = num_ver initialization completed");
    	
    }*/
	
	
	 
	 public static String convertException(Exception e)
	 {
		 String message = e.getMessage();	 
		 if ((message != null) && (message.length() > 200))
			 message = message.substring(0, 200);		 
		 return message;		 
	 }
	

}
