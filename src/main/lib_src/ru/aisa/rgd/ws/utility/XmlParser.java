/**
 * XmlParser.java (c) AIS@ 2010 - 2010
 */
package ru.aisa.rgd.ws.utility;

import java.util.List;

/**
 * @author maloi
 *
 */
public interface XmlParser {

	/**
	 * @param tagName
	 * @return
	 */
	List<String> getAllValues(String tagName);

	/**
	 * @param tagName
	 * @return
	 */
	String getLastValue(String tagName);

	/**
	 * @param tagName
	 * @return
	 */
	String getValue(String tagName);

}
