package com.mdsl.utils;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class WriteFile {

	public void bufferedWritter_Overwrite(String filename, String text) throws IOException{
		BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
	    writer.write(text);
	    writer.write("\n");
	    writer.close();
	}
	
	public void bufferedWritter_Append(String filename, String text) throws IOException{
		//BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true));
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename, true),"UTF-8"));
	    writer.append(text);
	    writer.append("\n");
	    writer.close();
	}
	
	
}
