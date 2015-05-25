package com.pms.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ResourceUploadService {
	private final String SHEET_DATASET_SENSITIVE = "数据集敏感度字典";
	private final String SHEET_DATASET = "数据集定义";
	private final String SHEET_COLUMN_CLASSIFY = "字段分类定义";
	private final String SHEET_COLUMN = "字段";
	private final String SHEET_VALUE_SENSITIVE = "字段值敏感度字典";
	private final String SHEET_VALUE = "字段值字典";
	private final String SHEET_COLUMN_ClASSIFY_REALTION = "字段分类关系";
	private final String SHEET_ROW_RELATION = "数据集-字段-字段值";
	private final String SHEET_COLUMN_RELATION = "数据集-字段分类-字段值";
	private final String SHEET_CLASSIFY_RELATION = "数据集-字段分类关系";
//	private final String SHEET_DATASET_SENSITIVE = "数据集敏感度字典";
//	private final String SHEET_DATASET_SENSITIVE = "数据集敏感度字典";
//	private final String SHEET_DATASET_SENSITIVE = "数据集敏感度字典";
	
	public void UploadResource(File inData) throws InvalidFormatException, IOException {
		InputStream in=new FileInputStream(inData);
        Workbook workbook = WorkbookFactory.create(in);
        
        int sheetCount = workbook.getNumberOfSheets();  //Sheet的数量  
        //deal with seven elements
        for (int s = 0; s < sheetCount; s++) {
            Sheet sheet = workbook.getSheetAt(s);
            String sheetName = sheet.getSheetName();
            if ( SHEET_DATASET_SENSITIVE.equals(sheetName) ) {
            	updateDatasetSensitive(sheet);
            } else if ( SHEET_DATASET.equals(sheetName) ) {
            	updateDataset(sheet);
            } else if ( SHEET_COLUMN_CLASSIFY.equals(sheetName) ) {
            	updateColumnClassify(sheet);
            } else if ( SHEET_COLUMN.equals(sheetName) ) {
            	updateColumn(sheet);
            } else if ( SHEET_VALUE_SENSITIVE.equals(sheetName) ) {
            	updateVlaueSensitive(sheet);
            } else if ( SHEET_VALUE.equals(sheetName) ) {
            	updateValue(sheet);
            } else if ( SHEET_COLUMN_ClASSIFY_REALTION.equals(sheetName) ) {
            	updateColumnClassifyRelation(sheet);
            }
        }
        
        //deal with relationships
        for (int s = 0; s < sheetCount; s++) {
        	Sheet sheet = workbook.getSheetAt(s);
            String sheetName = sheet.getSheetName();
            if ( SHEET_ROW_RELATION.equals(sheetName) ) {
            	updateRowRelation(sheet);
            } else if ( SHEET_COLUMN_RELATION.equals(sheetName) ) {
            	updateColumnRelation(sheet);
            } else if ( SHEET_CLASSIFY_RELATION.equals(sheetName) ) {
            	updateClassifyRelation(sheet);
            }
        }
        in.close();
        return;
	}
	
	private void updateDatasetSensitive(Sheet sheet) {
		
	}
	
	private void updateDataset(Sheet sheet) {
		
	}

	private void updateColumnClassify(Sheet sheet) {
		
	}
	
	private void updateColumn(Sheet sheet) {
		
	}
	
	private void updateVlaueSensitive(Sheet sheet) {
		
	}

	private void updateValue(Sheet sheet) {
		
	}

	private void updateColumnClassifyRelation(Sheet sheet) {
		
	}

	private void updateRowRelation(Sheet sheet) {
		
	}
	
	private void updateColumnRelation(Sheet sheet) {
		
	}
	
	private void updateClassifyRelation(Sheet sheet) {
		
	}
	private void sheetProcess(Sheet sheet){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		int rowCount = sheet.getPhysicalNumberOfRows(); //获取总行数  
        //遍历每一行  
        for (int r = 0; r < rowCount; r++) {  
            Row row = sheet.getRow(r);  
            int cellCount = row.getPhysicalNumberOfCells(); //获取总列数  
            //遍历每一列  
            for (int c = 0; c < cellCount; c++) {  
                Cell cell = row.getCell(c);  
                int cellType = cell.getCellType();  
                String cellValue = null;  
                switch(cellType) {  
                    case Cell.CELL_TYPE_STRING: //文本  
                        cellValue = cell.getStringCellValue();  
                        break;  
                    case Cell.CELL_TYPE_NUMERIC: //数字、日期  
                        if(DateUtil.isCellDateFormatted(cell)) {  
                            cellValue = sdf.format(cell.getDateCellValue()); //日期型  
                        }  
                        else {  
                            cellValue = String.valueOf(cell.getNumericCellValue()); //数字  
                        }  
                        break;  
                    case Cell.CELL_TYPE_BOOLEAN: //布尔型  
                        cellValue = String.valueOf(cell.getBooleanCellValue());  
                        break;  
                    case Cell.CELL_TYPE_BLANK: //空白  
                        cellValue = cell.getStringCellValue();  
                        break;  
                    case Cell.CELL_TYPE_ERROR: //错误  
                        cellValue = "错误";  
                        break;  
                    case Cell.CELL_TYPE_FORMULA: //公式  
                        cellValue = "错误";  
                        break;  
                    default:  
                        cellValue = "错误";  
                }  
                System.out.print(cellValue + "\t");
                    
            }  
            System.out.println(); 
        }
	}
	
}
