package ru.aisa.rgd.ws.utility;

import org.w3c.dom.Node;

public interface NodeFilter<T extends Node>
{
/**
 * 
 * @param node
 * @return <b>true</b> - если элемент допущен фильтром, <b>false</b> - если не
 *         допущен
 */
boolean accept(T node);
}
