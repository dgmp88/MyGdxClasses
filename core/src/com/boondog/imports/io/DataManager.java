package com.boondog.imports.io;



public class DataManager  {
	String name;
	String[] headers;
	
	// The data. Lets make it a big float[][], as floats are a good size.
	float[][] data;
	
	int writeN, readN; // Current line in the data file.
	int columns;
	int maxSize;
	
	
	public DataManager(String name, String[] headers, int maxSize) {
		this.name = name;
		this.headers = headers;
		this.maxSize = maxSize;
		columns = headers.length;
		data = new float[maxSize][columns];
		writeN = 0;
	}
	
	public void writeLine(float[] line){
		if (line.length != headers.length) {
			System.out.println("Your data line is inconsitent, things may go horribly wrong.");
		}
		
		if (writeN >= maxSize) {
			System.out.println("This is awful. You're loosing data.");
			writeN = maxSize - 1;
		}
		for (int i = 0; i < columns; i++) {
			data[writeN][i] = line[i];
		}
		writeN++;
	}
	
	public float[] readLine() {
		readN++; // Go to next line 
		return data[readN-1]; // Return last line
	}
	
	public void resetReadLine() {
		readN = 0;
	}
	
	public boolean hasNext() {
		return readN<writeN;
	}

}
