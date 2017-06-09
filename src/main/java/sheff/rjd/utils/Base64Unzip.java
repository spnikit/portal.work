package sheff.rjd.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class Base64Unzip {

	public static final String filename = "/home/vovako/Загрузки/1463476244761_14542313_14.xfdl";
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		FileOutputStream fos = new FileOutputStream(new File("/home/vovako/Загрузки/decoded/dec.xfdl"));
		fos.write(Base64.decode(readFile(filename).substring(52)));
		fos.close();
		fos.flush();
		
	}

	public static String readFile(String filename) throws IOException
	{
	    String content = null;
	    File file = new File(filename); //for ex foo.txt
	    FileReader reader = null;
	    try {
	        reader = new FileReader(file);
	        char[] chars = new char[(int) file.length()];
	        reader.read(chars);
	        content = new String(chars);
	        reader.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        if(reader !=null){reader.close();}
	    }
	    return content;
	}	
}
