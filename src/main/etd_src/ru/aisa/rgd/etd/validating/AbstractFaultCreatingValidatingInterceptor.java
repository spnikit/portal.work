package ru.aisa.rgd.etd.validating;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.transform.TransformerException;

import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.endpoint.interceptor.AbstractValidatingInterceptor;
import org.springframework.ws.soap.SoapBody;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.SoapFaultDetail;
import org.springframework.ws.soap.SoapFaultDetailElement;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.xml.namespace.QNameUtils;
import org.xml.sax.SAXParseException;
  
  /**
   * Subclass of <code>AbstractValidatingInterceptor</code> that creates a SOAP Fault whenever the request message cannot
   * be validated. The contents of the SOAP Fault can be specified by setting the <code>addValidationErrorDetail</code>,
   * <code>faultStringOrReason</code>, or  <code>detailElementName</code> properties. Further customizing can be
   * accomplished by overriding <code>handleRequestValidationErrors</code>.
   *
   * @author Arjen Poutsma
   * @see #setAddValidationErrorDetail(boolean)
   * @see #setFaultStringOrReason(String)
   * @see #DEFAULT_FAULTSTRING_OR_REASON
   * @see #setDetailElementName(javax.xml.namespace.QName)
   * @see #DEFAULT_DETAIL_ELEMENT_NAME
   * @see #handleResponseValidationErrors(org.springframework.ws.context.MessageContext,org.xml.sax.SAXParseException[])
   * @since 1.0.0
   */
  public abstract class AbstractFaultCreatingValidatingInterceptor extends AbstractValidatingInterceptor {
  
      /**
       * Default SOAP Fault Detail name used when a validation errors occur on the request.
       *
       * @see #setDetailElementName(javax.xml.namespace.QName)
       */
      public static final QName DEFAULT_DETAIL_ELEMENT_NAME =
              QNameUtils.createQName("http://springframework.org/spring-ws", "ValidationError", "spring-ws");
  
      /**
       * Default SOAP Fault string used when a validation errors occur on the request.
       *
       * @see #setFaultStringOrReason(String)
       */
      public static final String DEFAULT_FAULTSTRING_OR_REASON = "Ошибка валидации";
  
      private boolean addValidationErrorDetail = true;
  
      private QName detailElementName = DEFAULT_DETAIL_ELEMENT_NAME;
  
      private String faultStringOrReason = DEFAULT_FAULTSTRING_OR_REASON;
  
      private Locale faultStringOrReasonLocale = Locale.ENGLISH;
    //    private Locale faultStringOrReasonLocale = new Locale("ru");
  
      /**
       * Returns whether a SOAP Fault detail element should be created when a validation error occurs. This detail element
       * will contain the exact validation errors. It is only added when the underlying message is a
       * <code>SoapMessage</code>. Defaults to <code>true</code>.
       *
       * @see org.springframework.ws.soap.SoapFault#addFaultDetail()
       */
      public boolean getAddValidationErrorDetail() {
          return addValidationErrorDetail;
      }
  
      /**
       * Indicates whether a SOAP Fault detail element should be created when a validation error occurs. This detail
       * element will contain the exact validation errors. It is only added when the underlying message is a
       * <code>SoapMessage</code>. Defaults to <code>true</code>.
       *
       * @see org.springframework.ws.soap.SoapFault#addFaultDetail()
       */
      public void setAddValidationErrorDetail(boolean addValidationErrorDetail) {
          this.addValidationErrorDetail = addValidationErrorDetail;
      }
  
      /** Returns the fault detail element name when validation errors occur on the request. */
      public QName getDetailElementName() {
          return detailElementName;
      }
  
     /**
      * Sets the fault detail element name when validation errors occur on the request. Defaults to
      * <code>DEFAULT_DETAIL_ELEMENT_NAME</code>.
      *
      * @see #DEFAULT_DETAIL_ELEMENT_NAME
      */
     public void setDetailElementName(QName detailElementName) {
         this.detailElementName = detailElementName;
     }
 
     /** Sets the SOAP <code>faultstring</code> or <code>Reason</code> used when validation errors occur on the request. */
     public String getFaultStringOrReason() {
         return faultStringOrReason;
     }
 
     /**
      * Sets the SOAP <code>faultstring</code> or <code>Reason</code> used when validation errors occur on the request.
      * It is only added when the underlying message is a <code>SoapMessage</code>. Defaults to
      * <code>DEFAULT_FAULTSTRING_OR_REASON</code>.
      *
      * @see #DEFAULT_FAULTSTRING_OR_REASON
      */
     public void setFaultStringOrReason(String faultStringOrReason) {
         this.faultStringOrReason = faultStringOrReason;
     }
 
     /** Returns the SOAP fault reason locale used when validation errors occur on the request. */
     public Locale getFaultStringOrReasonLocale() {
         return faultStringOrReasonLocale;
     }
 
     /**
      * Sets the SOAP fault reason locale used when validation errors occur on the request.  It is only added when the
      * underlying message is a <code>SoapMessage</code>. Defaults to English.
      *
      * @see java.util.Locale#ENGLISH
      */
     public void setFaultStringOrReasonLocale(Locale faultStringOrReasonLocale) {
         this.faultStringOrReasonLocale = faultStringOrReasonLocale;
     }
 
     /**
      * Template method that is called when the request message contains validation errors. This implementation logs all
      * errors, returns <code>false</code>, and creates a {@link SoapBody#addClientOrSenderFault(String,Locale) client or
      * sender} {@link SoapFault}, adding a {@link SoapFaultDetail} with all errors if the
      * <code>addValidationErrorDetail</code> property is <code>true</code>.
      *
      * @param messageContext the message context
      * @param errors         the validation errors
      * @return <code>true</code> to continue processing the request, <code>false</code> (the default) otherwise
      */
     protected boolean handleRequestValidationErrors(MessageContext messageContext, SAXParseException[] errors)
             throws TransformerException {
    	 String errorMessage = "";
    	 String localeError = "";
         for (int i = 0; i < errors.length; i++) {
             logger.warn("XML validation error on request: " + errors[i].getMessage());
         }
         if (messageContext.getResponse() instanceof SoapMessage) {
             SoapMessage response = (SoapMessage) messageContext.getResponse();
             SoapBody body = response.getSoapBody();
             SoapFault fault = body.addClientOrSenderFault(getFaultStringOrReason(), getFaultStringOrReasonLocale());
             if (getAddValidationErrorDetail()) {
                 SoapFaultDetail detail = fault.addFaultDetail();
                 for (int i = 0; i < errors.length; i++) {
                     SoapFaultDetailElement detailElement = detail.addFaultDetailElement(getDetailElementName());
                     errorMessage = errors[i].getMessage();
                     localeError = localeErrorMessage(errorMessage);
                     if (!localeError.equalsIgnoreCase("NULL")) detailElement.addText(localeError);
                     else detailElement.addText(errorMessage);
                     logger.debug("Localize message: original=\""+errorMessage+"\"; replaced=\""+localeError+"\"");
                     //detailElement.addText(errors[i].getMessage());
                     //System.out.println("details="+localeErrorMessage(errors[i].getMessage()));
                 }
             }
         }
         return false;
     }
     
     //Метод пока что для грубой замены текста
     protected String localeErrorMessage(String errorMsg){
    	 String result = errorMsg;
    	 String [] tokens = errorMsg.split(" ");
    	 result = mkLocaledString(tokens);
    	 return result;
     }
     
     protected class replaceBean{
    	 private List<Integer> indexs;
    	 private List<String> keys;
    	 private int items;
    	 private String replaceStr;
    	 
    	 replaceBean(){
    		 replaceStr = "";
    		 items=0;
    		 indexs = new ArrayList<Integer>();
    		 keys = new ArrayList<String>();
    	 }
    	 
    	 public void setReplaceStr(String replaceStr){
    		 this.replaceStr = replaceStr;
    	 }
    	 
    	 public String getReplaceStr(){
    		 return replaceStr;
    	 }
    	 
    	 public int getItems(){
    		 return items;
    	 }
    	 
    	 public void addKeys(String key, int index){
    		 keys.add(key);
    		 indexs.add(index);
    		 items+=1;
    	 }
    	 
    	 public String getKey(int index){
    		 if(index<items && index>-1) return keys.get(index);
    		 else return null;
    	 }
    	 
    	 public int getIndex(int index){
    		 if(index<items && index>-1) return indexs.get(index);
    		 else return -1;    		 
    	 }
     }
     
     protected String mkLocaledString(String [] tokens){
    	 String result = "NULL";
    	 
    	 //Мапа с параметрами "перевода" ошибки
    	 Map <String, replaceBean> mp = new HashMap<String, replaceBean>();
    	 replaceBean rb;
    	 
    	 //----------------definition zone -----------------
    	 //cvc-pattern-valid: Value '' is not facet-valid with respect to pattern '(-)?\d{1,6}' for type 'int6'.
    	 rb = new replaceBean();
    	 rb.setReplaceStr("Ошибка формата. Значение <value> не является верным для типа <type> Шаблон: <pattern>");
    	 rb.addKeys("<value>", 2);
    	 rb.addKeys("<type>", 13);
    	 rb.addKeys("<pattern>", 10);
    	 mp.put("cvc-pattern-valid:", rb);
    	 
    	 //cvc-type.3.1.3: The value '' of element 'kod_depo' is not valid.
    	 rb = new replaceBean();
    	 rb.setReplaceStr("Ошибка значения типа. Значение <value> элемента <element> не верно.");
    	 rb.addKeys("<value>", 3);
    	 rb.addKeys("<element>", 6);
    	 mp.put("cvc-type.3.1.3:", rb);
    	 
    	 //cvc-complex-type.2.4.a: Invalid content was found starting with element 'tip_pred'. One of '{kod_depo}' is expected.
    	 rb = new replaceBean();
    	 rb.setReplaceStr("Ошибка последовательности. Пропущен элемент <prev_element> перед элементом <element>");
    	 rb.addKeys("<prev_element>", 11);
    	 rb.addKeys("<element>", 8);
    	 mp.put("cvc-complex-type.2.4.a:", rb);
    	 
    	 //cvc-complex-type.2.4.d: Invalid content was found starting with element 'row'. No child element is expected at this point.
    	 rb = new replaceBean();
    	 rb.setReplaceStr("Ошибка границ. Возможно количество элементов <element_name> превышает максимально возможное.");
    	 rb.addKeys("<element_name>", 8);
    	 mp.put("cvc-complex-type.2.4.d:", rb);
    	 
    	 //TODO Добавить остальные описания:
    	 //--------------- End of definition zone ----------------
    	 
    	 rb = new replaceBean();
    	 rb = mp.get(tokens[0]);
    	 if(rb!=null){
    		 result = rb.getReplaceStr();
    		 for(int i=0; i<rb.getItems(); i++){
    			 result = result.replace(rb.getKey(i), tokens[rb.getIndex(i)]);
    		 }
    	 }
    	 
    	 return result;
     }
}
