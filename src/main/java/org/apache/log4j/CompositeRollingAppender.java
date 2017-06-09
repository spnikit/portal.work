package org.apache.log4j;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.helpers.CountingQuietWriter;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.spi.LoggingEvent;

public class CompositeRollingAppender extends org.apache.log4j.FileAppender {

	private final int TOP_OF_DAY = 3;
	private final String endDayTime = "23.59.59";
	private final String timePattern = "HH.mm.ss", logTimePattern = "HH:mm:ss";
	private Logger log = Logger.getLogger(CompositeRollingAppender.class);
	private String datePattern = "yyyy-MM-dd";
	private Date now = new Date(), realTime = new Date(), preRealTime = new Date();
	private long nextCheck = System.currentTimeMillis () - 1;
	private SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
	private SimpleDateFormat timeSdf = new SimpleDateFormat(timePattern);
	private RollingCalendar rc = new RollingCalendar();

	protected long maxFileSize = 10*1024*1024;
	protected String baseFileName;

	public CompositeRollingAppender() {
		preRealTime.setTime(System.currentTimeMillis());
		now.setTime(System.currentTimeMillis());
    }

//	public CompositeRollingAppender (Layout layout, String filename,
//				   String datePattern) throws IOException {
//	    this(layout, filename, datePattern, true);
//	}
//
//	public CompositeRollingAppender(Layout layout, String filename, boolean append)
//									  throws IOException {
//	    super(layout, filename, append);
//	}
//
//	public CompositeRollingAppender (Layout layout, String filename,
//				   String datePattern, boolean append) throws IOException {
//	    super(layout, filename, append);
//	    this.datePattern = datePattern;
//		activateOptions();
//	}
//
//	public CompositeRollingAppender(Layout layout, String filename) throws IOException {
//	    super(layout, filename);
//	}
//
//	public void setDatePattern(String pattern) {
//	    datePattern = pattern;
//	}
//
//	public String getDatePattern() {
//	    return datePattern;
//	}

	public long getMaximumFileSize() {
		return maxFileSize;
	}

	public void setMaxFileSize(long maxFileSize) {
	   this.maxFileSize = maxFileSize;
	}

	public void setMaximumFileSize(long maxFileSize) {
		this.maxFileSize = maxFileSize;
	}

	public void setMaxFileSize(String value) {
	    maxFileSize = OptionConverter.toFileSize(value, maxFileSize + 1);
	}

	protected void setQWForFiles(Writer writer) {
	    qw = new CountingQuietWriter(writer, errorHandler);
	}

	protected void subAppend(LoggingEvent event) {
		checker();
		super.subAppend(event);
	}

	protected void checker() {
		checker(false);
	}
	protected void checker(boolean isFirstCheck) {
		long n = System.currentTimeMillis();
		boolean timeCheck = (n >= nextCheck);
		if (timeCheck ||
				(isFirstCheck ? false : ((fileName != null) && (((CountingQuietWriter) qw).getCount() >= maxFileSize)))) {
			rollOverCondition(timeCheck);
			now.setTime(n);
			nextCheck = rc.getNextCheckMillis(now);
		}
	}

	public void setFile(String file){
		baseFileName = file.trim();
		fileName = file.trim();
	}

	public synchronized void setNewFile(String fileName) throws IOException {
		fileName = fileName.trim();
		reset();
        this.setQWForFiles(createWriter(new FileOutputStream(fileName)));
        this.fileName = fileName;
        this.fileAppend = false;
        writeHeader();
    }

	protected void existingInit() {
		File old = new File(baseFileName);
		if (old.exists()) {			
			BufferedReader br;
			try {
				br = new BufferedReader(new InputStreamReader(new FileInputStream(old)));
				String line;
				if ((line = br.readLine()) != null) {
					line = line.substring(0,line.indexOf(","));
					String dateLine = line.substring(0, line.indexOf(" "));
					String timeLine = line.substring(line.indexOf(" ") + 1);
					br.close();
					try {
						now.setTime(sdf.parse(dateLine).getTime());
						preRealTime.setTime((new SimpleDateFormat(logTimePattern)).parse(timeLine).getTime());
						nextCheck = rc.getNextCheckMillis(now);
						this.setQWForFiles(createWriter(new FileOutputStream(old, true)));
						((CountingQuietWriter) qw).setCount(old.length());
						checker();//(true);

					} catch (ParseException e) {
						StringWriter outError = new StringWriter();
						PrintWriter errorWriter = new PrintWriter( outError );
						e.printStackTrace(errorWriter);
					//	log.trace(outError.toString());
					}
				}
			} catch (IOException e) {
				StringWriter outError = new StringWriter();
				PrintWriter errorWriter = new PrintWriter( outError );
				e.printStackTrace(errorWriter);
		//		log.trace(outError.toString());
			}
		}
	}

	public void activateOptions() {
		super.activateOptions();
		rc.setType(TOP_OF_DAY);
		nextCheck = rc.getNextCheckMillis(now);
		existingInit();
		
	}

	protected void rollOverCondition(boolean timeCheck) {
		try {
			this.closeFile();
			renameFile(timeCheck);
			zipFile();
			this.setNewFile(baseFileName);
		}
		catch(IOException e) {
			StringWriter outError = new StringWriter();
			PrintWriter errorWriter = new PrintWriter( outError );
			e.printStackTrace(errorWriter);
		//	log.trace(outError.toString());
		}
	}

	protected void renameFile(boolean timeCheck) {
		realTime.setTime(System.currentTimeMillis());
		Date namedRealTime = new Date(realTime.getTime());
		if (timeCheck) {
			try {
				namedRealTime = timeSdf.parse(endDayTime);
			} catch (ParseException e) {
				StringWriter outError = new StringWriter();
				PrintWriter errorWriter = new PrintWriter( outError );
				e.printStackTrace(errorWriter);
			//	log.trace(outError.toString());
			}
		}
		FileInputStream fis;
		try {
			fis = new FileInputStream(fileName);	
//			fileName.replace(oldChar, newChar)
			fileName = fileName.substring(0,fileName.indexOf("Log4j.log")) +  sdf.format(now) + "_" + timeSdf.format(preRealTime) + "-" + timeSdf.format(namedRealTime)+".Log4j.log";
//			File newFile = new File(fileName);		
			FileOutputStream fos;		
			fos = new FileOutputStream(fileName);		
			IOUtils.copy(fis, fos);
			fos.close();
		} catch (IOException e) {
			StringWriter outError = new StringWriter();
			PrintWriter errorWriter = new PrintWriter( outError );
			e.printStackTrace(errorWriter);
		//	log.trace(outError.toString());
		}
		preRealTime.setTime(realTime.getTime());
	}

	public void zipFile() {

		final String zipFileName = fileName;

		new Thread(){
			public void run() {
				FileInputStream fis = null;
				ZipOutputStream zos = null;
				FileChannel fos = null;

				try {
					fis = new FileInputStream(zipFileName);
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					zos = new ZipOutputStream(bos);
					zos.putNextEntry(new ZipEntry(new File(zipFileName).getName()));
					OutputStreamWriter osw = new OutputStreamWriter(zos, "UTF-8");
					IOUtils.copy(fis, osw, getEncoding());
					zos.closeEntry();
					zos.close();

					ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
					ByteBuffer buf = ByteBuffer.allocateDirect(10);

		  	        byte[] bytes = new byte[1024];
		 	        int count = 0;
			        int index = 0;
			        fos = (new FileOutputStream(zipFileName + ".zip")).getChannel();
			        while (count >= 0) {
			            if (index == count) {
			                count = bis.read(bytes);
			                index = 0;
			            }
		 	            while (index < count && buf.hasRemaining()) {
			                buf.put(bytes[index++]);
			            }
			            buf.flip();
		            	fos.write(buf);
			            if (buf.hasRemaining()) {
			                buf.compact();
			            } else {
			                buf.clear();
			            }
			        }
				} catch (IOException e) {
					StringWriter outError = new StringWriter();
					PrintWriter errorWriter = new PrintWriter( outError );
					e.printStackTrace(errorWriter);
				//	log.trace(outError.toString());
				} finally {
					if (fis != null) {
						try {
							fis.close();
					        fos.close();
							new File(zipFileName).delete();
						} catch (IOException e) {
							StringWriter outError = new StringWriter();
							PrintWriter errorWriter = new PrintWriter( outError );
							e.printStackTrace(errorWriter);
				//			log.trace(outError.toString());
					 	}
					}
					if (zos != null) {
						try {
							zos.close();
						} catch (IOException e) {
							StringWriter outError = new StringWriter();
							PrintWriter errorWriter = new PrintWriter( outError );
							e.printStackTrace(errorWriter);
				//			log.trace(outError.toString());
						}
					}
				}
			}
		}.start();
	}
 }