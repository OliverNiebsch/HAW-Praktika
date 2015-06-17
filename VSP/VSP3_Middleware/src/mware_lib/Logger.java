package mware_lib;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {
	public boolean log = false;

	private BufferedWriter logWriter;
	private boolean logOnConsole;

	public Logger() {
		this(null, true);
	}

	public Logger(File logfile, boolean logOnConsole) {
		this.logOnConsole = logOnConsole;
		logWriter = null;

		if (logfile != null) {
			try {
				logWriter = new BufferedWriter(new FileWriter(logfile));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Loggt die angegebene Nachricht. Sollte diese nicht mit einem \n enden,
	 * wird das Newline automatisch hinzugefügt.
	 * 
	 * @param msg Die zu loggende Nachricht
	 */
	public void print(String msg) {
		if (!log)
			return;
		
		if (!msg.endsWith("\n"))
			msg += "\n";
		
		if (logWriter != null) {
			try {
				logWriter.write(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if (logOnConsole) {
			System.out.print(msg);
		}
	}
	
	/**
	 * Loggt die angegebene Nachricht. Sollte diese nicht mit einem \n enden,
	 * wird das Newline automatisch hinzugefügt.
	 * 
	 * @param format Ein formatierter String
	 * @param args Die Argumente für den formatierten String
	 */
	public void print(String format, Object... args) {
		if (!log)
			return;
		
		String msg = String.format(format, args);
		if (!msg.endsWith("\n"))
			msg += "\n";
		
		if (logWriter != null) {
			try {
				logWriter.write(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if (logOnConsole) {
			System.out.print(msg);
		}
	}
}
