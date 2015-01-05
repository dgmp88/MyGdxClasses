package com.boondog.imports.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class DataWriter implements Runnable {
	DataManager data;
	int writePause = 500; // Allow final animations to finish smoothly
	
	public DataWriter(DataManager data) {
		this.data = data;
		Thread thisThread = new Thread(this);
		thisThread.start();
	}
	
	@Override
	public void run() {
		try {
			Thread.sleep(writePause);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		writeData();
		return;
	}
	
	
	public void writeData() {
		 // Write an output CSV data file
		 String fullFile = "data/" + data.name;
		 FileHandle fh = Gdx.files.local(fullFile);
		 
		 
		 // Write everything (including headers) to a string buffer
		 String buffer = "";
		 for (String header : data.headers) {
			 buffer = buffer + header + "\t";
		 }
		 buffer = buffer + "\n";
		 data.resetReadLine();
		 while(data.hasNext()) {
			 for (float f : data.readLine()) {
				 try {
					 buffer += f + "\t";
				 } catch (IndexOutOfBoundsException ioobe) {
					 buffer += "NaN\t";
				 }
			 }
			 buffer += "\n";
		 } 
		 
		 // Write to file (overwrite if anything exists)
		 fh.writeString(buffer, false);
	}


}
