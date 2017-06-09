package ru.aisa.rgd.etd.extend;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;

import ru.aisa.rgd.ws.documents.ETDForm;

public class DocumentSourceBuilder {
	
	static final Logger	logger	= Logger.getLogger(DocumentSourceBuilder.class);
	
	private static final String ENCODING = "UTF-8";
	private static final String TAG_BEGIN = "<xformsmodels>";
	private static final String TAG_END = "</xformsmodels>";
	private static final List<String> GZIP_FORMS;
	
	static
	{
		GZIP_FORMS = new ArrayList<String>();
		GZIP_FORMS.add("АГУ-11ВЦ");
	}
	/**
	 * @type Название шаблона
	 * @param source docData исходного дукумента
	 * @param template шаблон документа
	 * @return 
	 * @throws UnsupportedEncodingException 
	 * @throws TransformerException 
	 */
	public static byte[] createFromSource(String type, byte[] source, byte[] template) throws UnsupportedEncodingException, TransformerException
	{
		logger.debug("Creating documnet from exist for type <"  + type + ">");
		
		//Если формы зазипованы - раззиповываем. Source всегда xml из эндпойнта
		if (GZIP_FORMS.contains(type.trim()))
		{
			logger.debug("Unzip from");
			template = ETDForm.decodeFromArchiv(template);
		}
			
		String src = new String(source, ENCODING);
		String tmpl = new String(template, ENCODING);
		
		int startIndex = src.indexOf(TAG_BEGIN);
		int stopIndex = src.indexOf(TAG_END) + TAG_END.length();
		String srcData = src.substring(startIndex, stopIndex);
		
		//logger.debug("Source : " + srcData);
		
		startIndex = tmpl.indexOf(TAG_BEGIN);
		stopIndex = tmpl.indexOf(TAG_END)  + TAG_END.length();
		String tmplData = tmpl.substring(startIndex, stopIndex);
		
		//logger.debug("Template : " + tmplData);

		String result = tmpl.replace(tmplData , srcData);
		
		//logger.debug("Result : " + result);
		
		byte[] b;
		//	Зазиповываем обратно	 ))
		if (GZIP_FORMS.contains(type.trim()))
		{
			logger.debug("Zzzzip from");
			b = ETDForm.encodeFormToArchiv(result);
		}
		else
		{
			b = result.getBytes(ENCODING);
		}
		
		return b;
	}


}
