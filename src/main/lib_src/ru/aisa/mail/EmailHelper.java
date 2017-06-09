 package ru.aisa.mail;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



public class EmailHelper {
	
	private static Log log = LogFactory.getLog(EmailHelper.class);
	private Session session;
	private String smtpHost;
	private String smtpUser;
	private String smtpPass;
	public  String fromPersonall;
    private String sitename;
	private String[] adresses;
private String sendmailfrom;
    
    public String getSendmailfrom() {
		return sendmailfrom;
	}

	public void setSendmailfrom(String sendmailfrom) {
		this.sendmailfrom = sendmailfrom;
	}

	public String getIssend() {
		return issend;
	}

	public void setIssend(String issend) {
		this.issend = issend;
	}
	private String issend;

	public String getSitename() {
		return sitename;
	}

	public void setSitename(String sitename) {
		this.sitename = sitename;
	}
	
	public String getFromPersonall() {
		return fromPersonall;
	}
	public void setFromPersonall(String fromPersonall) {
		this.fromPersonall = fromPersonall;
	}
	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}
	public String getSmtpHost() {
		return smtpHost;
	}
	public void setSmtpUser(String smtpUser) {
		this.smtpUser = smtpUser;
	}
	public String getSmtpUser() {
		return smtpUser;
	}
	public void setSmtpPass(String smtpPass) {
		this.smtpPass = smtpPass;
	}
	public String getSmtpPass() {
		return smtpPass;
	}
	
	public String[] getAdresses() {
		return adresses;
	}

	public void setAdresses(String[] adresses) {
		this.adresses = adresses;
	}

	public void init(){
		Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", getSmtpHost());
        props.setProperty("mail.user", getSmtpUser());
        props.setProperty("mail.password", getSmtpPass());
        props.setProperty("mail.smtp.debug", "true");
        setSession(Session.getInstance(props));
	}
	private String ENCODE="KOI8-R";
	public void sendEmail(String subj,String body) throws MessagingException, IOException{

		if(adresses.length==0)return;

	    Transport transport = session.getTransport();
	    MimeMessage message = new MimeMessage(getSession());
	    
	    message.setContent(body, "text/html; charset=utf-8");
        message.setSubject(subj);
         message.setSentDate(new Date());
//        message.setHeader("Content-Type", "text/html; charset=\"UTF-8\"");
//        message.setHeader("Content-Transfer-Encoding", "quoted-printable");
        for(String t:adresses){
        	message.addRecipient(Message.RecipientType.TO,
        			new InternetAddress(t));
        }
        
		InternetAddress addrfrom = new InternetAddress(getSmtpUser());
//			addrfrom.setPersonal(fromPersonall,"UTF-8");
		message.setFrom(addrfrom);
		transport.connect(getSmtpHost(),getSmtpUser(),getSmtpPass());
        transport.sendMessage(message,message.getRecipients(Message.RecipientType.TO));
        
        transport.close();
        log.warn("sent email to "+adresses.length+" recipients");
	}
	
	public void sendEmail(String subj, List<String> to, String body, String fileName, byte[] fileContent) throws MessagingException{

		if(to==null||to.size()==0)return;
        Transport transport = getSession().getTransport();
        MimeMessage message = new MimeMessage(getSession());
        message.setSubject(subj,"UTF-8");
        
        MimeBodyPart mbp = new MimeBodyPart();
        mbp.setContent(body,  "text/html; charset=\"UTF-8\"");
        
        MimeBodyPart mbp1 = new MimeBodyPart();
        MyDataSource mds = new MyDataSource();
        mds.setData(fileContent);
        mds.setName(fileName);
        
        mbp1.setDataHandler(new DataHandler(mds));
        mbp1.setFileName(mds.getName());
        
        Multipart mp = new MimeMultipart();
        mp.addBodyPart(mbp);
        mp.addBodyPart(mbp1);
        
        message.setContent(mp);
        
        for(String t:to){
        	message.addRecipient(Message.RecipientType.TO,
        			new InternetAddress(t));
        }
        
		try {
			InternetAddress addrTo = new InternetAddress(getSmtpUser());
			addrTo.setPersonal(getFromPersonall(),"UTF-8");
			message.setFrom(addrTo);
		} catch (UnsupportedEncodingException e) {
			message.setFrom(new InternetAddress(getSmtpUser()));
			log.error("enc", e);
		}
        transport.connect(getSmtpUser(),getSmtpPass());
        transport.sendMessage(message,
        		message.getRecipients(Message.RecipientType.TO));
        transport.close();
        log.warn("sent email to "+to.size()+" recipients");
	}

	private void setSession(Session session) {
		this.session = session;
	}
	private Session getSession() {
		return session;
	}
	public class BasicAuthenticator extends Authenticator
	{
	private String username;
	private String password;
	/**
	* Password authentication, returning username and password specified
	* in the implementation.
	*/
	public BasicAuthenticator(String username, String password){
	this.username = username;
	this.password = password;

	}
	public PasswordAuthentication getPasswordAuthentication()
	{
	return new PasswordAuthentication(username, password);
	}
	}
}