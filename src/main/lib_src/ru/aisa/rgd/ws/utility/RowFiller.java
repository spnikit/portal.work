package ru.aisa.rgd.ws.utility;

import org.w3c.dom.DOMException;

import ru.aisa.rgd.ws.exeption.InternalException;
import ru.aisa.rgd.ws.utility.DataBinder;

public interface RowFiller<T1, T2>
{
public static final String	lastBulidDate	= "08.10.2013 14:39:12";

/**
 * 
 * @param b
 *          - биндер, где rootEl - заполняемый столбец
 * @param rowContent
 *          - объект с контентом текущего столбца
 * @param numRow
 *          - номер заполняемой строки (нумерация начинается с нуля)
 * @param options
 *          - объект дополнительных свойств. Будет передан <b>null</b>
 *          дополнительные свойства отсутствуют
 * @throws InternalException 
 * @throws DOMException 
 */
void fillRow(DataBinder b, T1 rowContent, int numRow, T2 options) throws DOMException, InternalException;

}