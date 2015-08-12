package com.pms.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.pms.dao.OrganizationDAO;
import com.pms.dao.impl.OrganizationDAOImpl;
import com.pms.model.OrganizationImport;

public class OrgUploadService {
	
	private final String SHEET_ORG = "批量导出";
	
	private final String SHEET_ORG_NAME = "机构名称";
	private final String SHEET_ORG_ID = "机构编码";
	private final String SHEET_ORG_PARENT_ID = "上级机构编码";
	
	public void UploadOrg(File inData) throws Exception {
		InputStream in=new FileInputStream(inData);
        Workbook workbook = WorkbookFactory.create(in);
        
        int sheetCount = workbook.getNumberOfSheets();  //Sheet的数量  
        //deal with seven elements and relationships
        for (int s = 0; s < sheetCount; s++) {
            Sheet sheet = workbook.getSheetAt(s);
            String sheetName = sheet.getSheetName();
            if ( SHEET_ORG.equals(sheetName) ) {
            	updateOrganizationImport(sheet);
            }
        }
        
        in.close();
        
        //update organization;
        updateOrganization();
        
        return;
	}
	
	private void updateOrganization() throws Exception {
		OrganizationDAO dao = new OrganizationDAOImpl();
		List<OrganizationImport> imports = dao.GetOrgImports();
		
		for(int i = 0; i < imports.size(); i++) {
			OrganizationImport oi = imports.get(i);
			dao.OrgImport(oi);
		}
		
		return;
	}

	private void updateOrganizationImport(Sheet sheet) throws Exception {
		int rowCount = sheet.getPhysicalNumberOfRows(); //获取总行数
		Map<String, Integer> idx = new HashMap<String, Integer>();
		OrganizationImport oi = null;
		OrganizationDAO dao = new OrganizationDAOImpl();
		//遍历每一行  
        for (int r = 0; r < rowCount; r++) {
        	Row row = sheet.getRow(r);
        	if(row == null) {
        		continue;
        	}
        	if( r > 2 ) {
        		oi = new OrganizationImport();
        	}
        	int cellCount = row.getPhysicalNumberOfCells(); //获取总列数 
        	//遍历每一列  
            for (int c = 0; c < cellCount; c++) {
            	Cell cell = row.getCell(c);
            	String cellValue = getCellValue(cell);
            	
            	if(r == 2) {
            		if ( SHEET_ORG_NAME.equals(cellValue) ) {
            			idx.put(SHEET_ORG_NAME, c);
            		} else if ( SHEET_ORG_ID.equals(cellValue) ) {
            			idx.put(SHEET_ORG_ID, c);
            		} else if ( SHEET_ORG_PARENT_ID.equals(cellValue) ) {
            			idx.put(SHEET_ORG_PARENT_ID, c);
            		}
            	} else if(r > 2) {
            		if( c == idx.get(SHEET_ORG_NAME) ) {
            			oi.setUNIT(cellValue);
            		} else if ( c == idx.get(SHEET_ORG_ID) ) {
            			oi.setGA_DEPARTMENT(cellValue);
            		} else if ( c== idx.get(SHEET_ORG_PARENT_ID) ) {
            			oi.setPARENT_ORG(cellValue);
            		}
            	}
            }
            
            if( r > 2 ) {
            	if(oi.isValid()) {
		            oi.MakeOrgLevel();
		            dao.OrganizationImportSave(oi);
            	}
            }
        }
		return;
	}
	
	private String getCellValue(Cell cell) {
		if(cell == null) {
			return "";
		}
		int cellType = cell.getCellType();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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
                    if(cellValue.contains(".")) {
                    	cellValue = cellValue.substring(0, cellValue.indexOf('.'));
                    }
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
        return cellValue;
	}
}
