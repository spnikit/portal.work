package ru.aisa.rgd.ws.utility;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class TypeConverter {
	
	/**
	 * @param  array Source array  
	 * @return      The List view of array
	 * @see         ArrayList
	 */
	public static <T> List<T> arrayToArrayList(T[] array)
	{
		List<T> list = new ArrayList<T>(array.length);
		for (T o : array)
		{
			list.add(o);
		}
		return list;
	}
	
	public static  List<String> arrayToArrayList(int[] array)
	{
		List<String> list = new ArrayList<String>(array.length);
		for (int o : array)
		{
			list.add(String.valueOf(o));
		}
		return list;
	}
	
	public static List<Integer> arrayToList(int[] array)
	{
		List<Integer> list = new ArrayList<Integer>(array.length);
		for (int o : array)
		{
			list.add(new Integer(o));
		}
		return list;
	}
	public static List<Long> arrayToList(long[] array)
	{
		List<Long> list = new ArrayList<Long>(array.length);
		for (long o : array)
		{
			list.add(new Long(o));
		}
		return list;
	}
	
	public static String exceptionToString(Exception e)
	{
		StringWriter outError = new StringWriter();
		PrintWriter errorWriter = new PrintWriter(outError);
		e.printStackTrace(errorWriter);
		return outError.toString();
	}

}
