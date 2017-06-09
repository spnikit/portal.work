package ru.aisa.rgd.ws.utility;

import org.w3c.dom.DOMException;

import ru.aisa.rgd.ws.exeption.InternalException;
import ru.aisa.rgd.ws.utility.DataBinder;

public interface RowHandler<T>
	{
	public static final String	lastBulidDate	= "21.10.2013 12:42:39";
	
	/**
	 * Обработчик для элементов-строк таблицы
	 * 
	 * @param b
	 *          - dataBinder, где rootEl - текущий обрабатываемый элемент
	 * @param rowNum
	 *          - порядковый номер обрабатываемого элемента (нумерация с 0)
	 * @param obj
	 *          - дополнительный объект
	 * @throws InternalException 
	 * @throws DOMException 
	 * @throws Exception
	 */
	void handleRow(DataBinder b, int rowNum, T obj) throws DOMException, InternalException;
	}
