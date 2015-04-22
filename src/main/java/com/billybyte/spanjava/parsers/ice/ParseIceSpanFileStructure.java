package com.billybyte.spanjava.parsers.ice;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.Calendar;

import com.billybyte.commonstaticmethods.Utils;

public class ParseIceSpanFileStructure {

	private final BufferedReader br;
	private final FileWriter fw;
		
	public ParseIceSpanFileStructure(Reader reader, String fileName) throws IOException {
		this.br = new BufferedReader(reader);
		this.fw = new FileWriter(fileName);
	}
	
	public static void main(String[] args) {
		
		String spanFileName = "ice.20130128.pa5";
		String outputFileName = "iceStructure.txt";
		
		try {
			
			Calendar beforeTime = Calendar.getInstance();
			ParseIceSpanFileStructure spanParser = new ParseIceSpanFileStructure(new FileReader(spanFileName), outputFileName);
			
			spanParser.processSpan();
			
			Calendar afterTime = Calendar.getInstance();

			Utils.prt("took "+(afterTime.getTimeInMillis()-beforeTime.getTimeInMillis())+" ms");
			
			
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void processSpan() throws IOException {
				
//		int lineNum = 0;
		
		String prevRecType = "!";
		int recCount = 0;
		
//		while(br.ready()&&(lineNum<1000)) {
		while(br.ready()) {
			
			String line = br.readLine();
//			Utils.prt(lineNum+" ("+line.length()+"): "+line);

			String recIdString = line.substring(0, 2);
			
			if(recIdString.compareTo(prevRecType)==0) {
				// if it is same type as before...
				recCount = recCount+1;
			} else {
				String writeString = prevRecType+","+recCount+"\n";
				fw.write(writeString);
				prevRecType = recIdString;
				recCount = 1;
			}
			
//			lineNum++;
		}
		
		fw.flush();
		fw.close();

	}
	

}
