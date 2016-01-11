/**   
 * @ClassName:     ${OrgUploadService}   
 * @Description:   ${机构数据上传管理功能}   
 * 
 * @ProductName:   ${中盈集中用户平台}
 * @author         ${北京中盈网信科技有限公司}  
 * @version        V1.0     
 * @Date           ${2014.9.4} 
*/
package com.pms.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.pms.dao.AuditLogDAO;
import com.pms.dao.AuditLogDescribeDao;
import com.pms.dao.OrganizationDAO;
import com.pms.dao.impl.AuditLogDAOImpl;
import com.pms.dao.impl.AuditLogDescribeDAOImpl;
import com.pms.dao.impl.OrganizationDAOImpl;
import com.pms.model.AuditOrgLog;
import com.pms.model.AuditOrgLogDescribe;
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
        AddOrgImportLog();
        
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
            		if(idx.size() == 0) {
            			throw new Exception("导入数据文件格式不正确!");
            		}
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
	
	private void AddOrgImportLog() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.SIMPLIFIED_CHINESE);
		String timenow = sdf.format(new Date());
		
		AuditOrgLog auditOrgLog = new AuditOrgLog();
		AuditLogDAO logdao = new AuditLogDAOImpl();
		AuditLogService als = new AuditLogService();
		
		auditOrgLog.setAdminId(als.adminLogin());
		auditOrgLog.setIpAddr("");
		auditOrgLog.setFlag(AuditOrgLog.LOGFLAGIMPORT);
		auditOrgLog.setResult(AuditOrgLog.LOGRESULTSUCCESS);
		auditOrgLog.setLATEST_MOD_TIME(timenow);
		auditOrgLog = logdao.AuditOrgLogAdd(auditOrgLog);
		
		AuditOrgLogDescribe auditOrgLogDescribe = new AuditOrgLogDescribe();
		AuditLogDescribeDao logDescdao = new AuditLogDescribeDAOImpl();
		
		auditOrgLogDescribe.setLogid(auditOrgLog.getId());
		
		String str="";
		str += "机构导入";
		
		auditOrgLogDescribe.setDescrib(str);
		
		auditOrgLogDescribe.setLATEST_MOD_TIME(timenow);
		auditOrgLogDescribe = logDescdao.AuditOrgLogDescribeAdd(auditOrgLogDescribe);
	}
}
