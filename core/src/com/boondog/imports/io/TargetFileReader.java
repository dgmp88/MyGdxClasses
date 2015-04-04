package com.boondog.imports.io;
/**
 * Reads in a target file (basically a CSV)
 * 
 * Target files are stored here as a hashmap.
 *  
 * @author: George Prichard (dgmprichard@gmail.com) 
 * v1.0: February 2014
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class TargetFileReader {
	// Stores the target file
	private Map<String, Array<Float>> tgt = new HashMap<String, Array<Float>>();
	private Array<String> header = new Array<String>(); // HashMaps don't have order, this does

	private FileHandle file;
	private int columns = 0;
	private int rows = 0;
	private String splitBy = "\t";
	
	public TargetFileReader(FileHandle file) {
		this.file = file;
		readTGT();
	}
	
	/** 
	 * 
	 * Read in the target file. Largely copied from 
	 * 
	 * @see:http://www.mkyong.com/java/how-to-read-and-parse-csv-file-in-java/	
	 */
	private void readTGT () { 	
		BufferedReader br = null;
		Reader fileReader = null;
		String line = "";
		int row = 0;
		try {
			fileReader = file.reader();
			// First count the number of lines, so we can initialise the body properly
			br = new BufferedReader(fileReader);
			while ((line = br.readLine()) != null) {
				String[] data = line.split(splitBy); // Split the data! 
				// Read in the header lines
				if (row==0) { 
					columns = data.length;
					for (int i = 0; i<columns; i++) {
						getHeader().add(data[i]);
						tgt.put(data[i],new Array<Float>());
					}
				} else { 			// Put as value for hashmap 
					for (int i = 0; i<columns; i++) {
						tgt.get(getHeader().get(i)).add(Float.parseFloat(data[i]));
					}
				}
				row++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch(GdxRuntimeException e) {
			System.out.println("Target " + file.path() + " not found");
		} finally {
			if (br != null) {
				try {
					br.close();
					fileReader.close();
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(1);
				}  catch (NullPointerException e) {
				}
			}
		}
		rows = row -1;
	}
	
	/**
	 * Note: you can ONLY write to Gdx.files.internal, not to local
	 * 
	 * @param tgt
	 * @param appendTgt
	 * @param append
	 */
	public void writeTGT(TargetFileReader tgt, Map<String, Array<Float>> appendTgt, boolean append) {
		Writer writer = file.writer(append);
		int rows = 0, cols = appendTgt.keySet().size();
		Array<String> headers = new Array<String>();
		for (String h : appendTgt.keySet()) {
			rows = appendTgt.get(h).size;
			headers.add(h);
		}
		try {
			//Write the header at the top if we aren't appending
			if (!append) {
				for (int i = 0; i < cols; i ++) {
					if (i <cols-1) {
						writer.write(headers.get(i) + tgt.splitBy);
					} else {
						writer.write(headers.get(i));
					}
				}
				writer.write("\n");
			}
			
			
			// Write the data
			for (int row = 0; row < rows; row ++) {
				for (int col = 0; col < cols; col ++) {
					if (col <cols-1) { 
						writer.write(appendTgt.get(tgt.header.get(col)).get(row) + tgt.splitBy);
					} else { // Don't have a trailing splitBy
						writer.write(String.valueOf(appendTgt.get(tgt.header.get(col)).get(row)));
					}
				}
				writer.write("\n");
			}

		} catch (IOException e) {
			System.out.println("seriously bad, may have messed up target file");
			e.printStackTrace();
		} finally {
			if (writer!=null) {
				try {
					writer.close();
				} catch (IOException e) {
					System.out.println("Writer failed to close");
					e.printStackTrace();
				}
			}
		}
		
	}
	
	/** 
	 * Just for debugging
	 */
	public void printMap() {
	    Object[] keys = tgt.keySet().toArray();
	    if (keys.length > 0) {
			int rows = tgt.get(keys[0]).size;
			for (int i = 0; i< keys.length; i++) {
				System.out.print("|"+keys[i] + "|\t");
			}
			System.out.println();
			for (int row=0; row<rows; row++) {
				for (int i = 0; i< keys.length; i++) {
					System.out.print(tgt.get(keys[i]).get(row) + "|\t");
				}
				System.out.println();
			}	
	    }
	}
	
	
	public Array<Float> getColumn(String column) {
		return tgt.get(column);
	}
	
	public float[] getRow(String[] heads, int row) {
		float[] r = new float[columns];
		for (int i=0;i<columns;i++) {
			r[i] = tgt.get(heads[i]).get(row);
		}
		return r;
	}
	
	public int[] getRowAsInt(String[] heads,int row) {
		float[] r = getRow(heads,row);
		int[] rI = new int[columns];
		for (int i=0;i<columns;i++) {
			rI[i] = (int) (r[i]);
		}	
		return rI;
	}
	
	public int getRows() {
		return rows;
	}
	
	public float getItemFloat(String column, int row) {
		return tgt.get(column).get(row);
	}
	
	public int getItemInt(String column, int row) {
		return tgt.get(column).get(row).intValue();
	}

	public Array<String> getHeader() {
		return header;
	}

	public void deleteRow(int row) {
		for (String h : header) {
			tgt.get(h).removeIndex(row); 
		}
		writeTGT(this,tgt, false);
	}

	public void append(TargetFileReader tmpBonusList) {
		writeTGT(this,tmpBonusList.tgt,true);
	}

	public Map<String, Array<Float>> getMap() {
		return tgt;
	}
}
