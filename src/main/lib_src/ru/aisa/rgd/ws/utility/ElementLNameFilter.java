package ru.aisa.rgd.ws.utility;

import org.w3c.dom.Element;


public class ElementLNameFilter implements NodeFilter<Element>
	{
	private String	lNane;
	
	public ElementLNameFilter(String lNane)
		{
		if (lNane == null)
			throw new NullPointerException("lNane is null");
		
		this.lNane = lNane;
		}
	
	public boolean accept(Element el)
		{
		if (lNane.equals(el.getLocalName()))
			return true;
		return false;
		}
	}
