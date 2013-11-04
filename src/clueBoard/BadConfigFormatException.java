package clueBoard;
/*
 * NAMES: David Grisham and Leah Moldauer
 * ClueBoard Part 2 (BoardCell class)
 */

import java.io.FileNotFoundException;
import java.io.PrintWriter;


public class BadConfigFormatException extends Exception {

	private static final long serialVersionUID = -7635349599395779904L;
	String outputFile = "errorLog.txt";
	
	public BadConfigFormatException() { }
	
	public BadConfigFormatException(String message) throws FileNotFoundException {
		super(message);
		//message will be specific to the issue, so just a general constructor will work for this
		
		PrintWriter out = new PrintWriter(outputFile);
		out.print(message);
		out.close();
	}
}
