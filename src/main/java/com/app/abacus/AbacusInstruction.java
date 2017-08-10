package com.app.abacus;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Class is responsible to read textToSpeech and Instruction from Excel Sheet
 * and create create a action list 
 * 
 * @author prashant.joshi (198joshi@gmail.com)
 *
 */
public class AbacusInstruction {
	
	private String fileName;
	LinkedHashMap<String, HashMap<String, String>> instructions = new LinkedHashMap<String, HashMap<String,String>>();
	List<String> listOfInsAndMap = new ArrayList<String>();

	private static XSSFRow row;
	
	/**
	 * Method is responsible to read an excel and get textToSpeech and Instructions
	 */
	public void readInstructionFile() throws IOException {
		
		// Create excel sheet handler
		FileInputStream ExcelFileToRead = new FileInputStream(getFileName());
		XSSFWorkbook workbook = new XSSFWorkbook(ExcelFileToRead);
		XSSFSheet spreadsheet = workbook.getSheetAt(0);
		Iterator<Row> rowIterator = spreadsheet.iterator();
		int counter = 1;
		if(rowIterator.hasNext()) {
			row = (XSSFRow) rowIterator.next();
		}
		
		// Iterate each row of an excel sheet 
		while (rowIterator.hasNext()) {
			String key = "step" + counter++;
			String textToSpeech = "";
			String instruction = "";
			row = (XSSFRow) rowIterator.next();
			Iterator<Cell> cellIterator = row.cellIterator();
			if (cellIterator.hasNext()) {
				//Text to Speech
				Cell cell = cellIterator.next();
				if(cell != null) {
					switch (cell.getCellType()) {
						case Cell.CELL_TYPE_STRING:
							textToSpeech = cell.getStringCellValue();
							break;
					}
				}
			}
			if (cellIterator.hasNext()) {	
				//Instruction
				Cell cell = cellIterator.next();
				if(cell != null) {
					switch (cell.getCellType()) {
						case Cell.CELL_TYPE_STRING:
							instruction = cell.getStringCellValue();
							break;
					}
				}
			}
			HashMap<String, String> textAction = new HashMap<String, String>();
			textAction.put(textToSpeech, instruction);
			instructions.put(key, textAction);
		}
		workbook.close();
		ExcelFileToRead.close();
	}
	
	/**
	 * Method is responsible to map textToSpeech and  Action in a List
	 */
	public void mapInstructionsAndActionsInList() throws IOException {
		FileInputStream ExcelFileToRead = new FileInputStream(getFileName());
		XSSFWorkbook workbook = new XSSFWorkbook(ExcelFileToRead);
		XSSFSheet spreadsheet = workbook.getSheet("Positive");
		Iterator<Row> rowIterator = spreadsheet.iterator();
		if(rowIterator.hasNext()) {
			row = (XSSFRow) rowIterator.next();
		}
		while (rowIterator.hasNext()) {
			String textToSpeech = "";
			String instruction = "";
			row = (XSSFRow) rowIterator.next();
			Iterator<Cell> cellIterator = row.cellIterator();
			if (cellIterator.hasNext()) {
				//Text to Speech
				Cell cell = cellIterator.next();
				if(cell != null) {
					switch (cell.getCellType()) {
						case Cell.CELL_TYPE_STRING:
							textToSpeech = cell.getStringCellValue();
							break;
					}
				}
			}
			if (cellIterator.hasNext()) {	
				//Instruction
				Cell cell = cellIterator.next();
				if(cell != null) {
					switch (cell.getCellType()) {
						case Cell.CELL_TYPE_STRING:
							instruction = cell.getStringCellValue();
							break;
					}
				}
			}
			listOfInsAndMap.add(textToSpeech);
			if(!instruction.trim().equalsIgnoreCase("")) {
				instruction = "<action>" + instruction + "</action>";
				listOfInsAndMap.add(instruction);
			}
			
		}
		
		workbook.close();
		ExcelFileToRead.close();
	}

	/**
	 * @return the instructions
	 */
	public LinkedHashMap<String, HashMap<String, String>> getInstructions() {
		return instructions;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the listOfInsAndMap
	 */
	public List<String> getListOfInsAndMap() {
		return listOfInsAndMap;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AbacusInstruction instruction = new AbacusInstruction();
		try {
			instruction.setFileName("/Users/prashant.joshi/Desktop/Instruction/AppInstruction.xlsx");
			instruction.mapInstructionsAndActionsInList();
		} catch (IOException e) {/** Eating Exceptions */}

	}

}
