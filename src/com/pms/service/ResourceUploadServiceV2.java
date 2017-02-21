package com.pms.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.pms.dao.AttributeDAO;
import com.pms.dao.AuditLogDAO;
import com.pms.dao.AuditLogDescribeDao;
import com.pms.dao.ImportLogDAO;
import com.pms.dao.PrivilegeDAO;
import com.pms.dao.ResClassifyRelationResourceDAO;
import com.pms.dao.ResColumnClassifyDAO;
import com.pms.dao.ResColumnClassifyRelationDAO;
import com.pms.dao.ResColumnResourceDAO;
import com.pms.dao.ResClassifyResourceDAO;
import com.pms.dao.ResDatasetDAO;
import com.pms.dao.ResDatasetSensitiveDAO;
import com.pms.dao.ResRowResourceDAO;
import com.pms.dao.ResValueDAO;
import com.pms.dao.ResValueSensitiveDAO;
import com.pms.dao.ResourceDAO;
import com.pms.dao.ResourceUploadDAO;
import com.pms.dao.UserDAO;
import com.pms.dao.impl.AttributeDAOImpl;
import com.pms.dao.impl.AuditLogDAOImpl;
import com.pms.dao.impl.AuditLogDescribeDAOImpl;
import com.pms.dao.impl.ImportLogDAOImpl;
import com.pms.dao.impl.PrivilegeDAOImpl;
import com.pms.dao.impl.ResClassifyRelationResourceDAOImpl;
import com.pms.dao.impl.ResColumnClassifyDAOImpl;
import com.pms.dao.impl.ResColumnClassifyRelationDAOImpl;
import com.pms.dao.impl.ResColumnResourceDAOImpl;
import com.pms.dao.impl.ResClassifyResourceDAOImpl;
import com.pms.dao.impl.ResDatasetDAOImpl;
import com.pms.dao.impl.ResDatasetSensitiveDAOImpl;
import com.pms.dao.impl.ResRowResourceDAOImpl;
import com.pms.dao.impl.ResValueDAOImpl;
import com.pms.dao.impl.ResValueSensitiveDAOImpl;
import com.pms.dao.impl.ResourceDAOImpl;
import com.pms.dao.impl.ResourceUploadDAOImpl;
import com.pms.dao.impl.UserDAOImpl;
import com.pms.model.AttrDefinition;
import com.pms.model.AttrDictionary;
import com.pms.model.AuditResLog;
import com.pms.model.AuditResLogDescribe;
import com.pms.model.AuditRoleLog;
import com.pms.model.AuditRoleLogDescribe;
import com.pms.model.ImportLog;
import com.pms.model.Privilege;
import com.pms.model.ResColumn;
import com.pms.model.ResColumnClassify;
import com.pms.model.ResColumnPrivate;
import com.pms.model.ResData;
import com.pms.model.ResDataSet;
import com.pms.model.ResDataSetPrivate;
import com.pms.model.ResDataSetSensitive;
import com.pms.model.ResClassifyRelationResource;
import com.pms.model.ResClassifyResource;
import com.pms.model.ResFeature;
import com.pms.model.ResRelationColumnClassify;
import com.pms.model.ResRoleResourceImport;
import com.pms.model.ResRowResource;
import com.pms.model.ResRowResourcePrivate;
import com.pms.model.ResRole;
import com.pms.model.ResRoleResource;
import com.pms.model.ResValue;
import com.pms.model.ResValuePrivate;
import com.pms.model.ResValueSensitive;
import com.pms.model.RowResourceColumn;
import com.pms.model.User;
import com.pms.util.ConfigHelper;
import com.pms.util.DateTimeUtil;

public class ResourceUploadServiceV2 {

	private static Log logger = LogFactory.getLog(ResourceUploadServiceV2.class);
	
	private final String SHEET_DATASET_SENSITIVE = "数据集敏感度字典";
	private final String SHEET_DATASET = "数据集定义";
	private final String SHEET_COLUMN_CLASSIFY = "字段分类定义";
	private final String SHEET_COLUMN = "字段";
	private final String SHEET_VALUE_SENSITIVE = "字段值敏感度字典";
	private final String SHEET_VALUE = "字段值字典";
	private final String SHEET_COLUMN_ClASSIFY_REALTION = "字段分类关系";
	private final String SHEET_ROW_RESOURCE = "数据集-字段-字段值";
	private final String SHEET_CLASSIFY_RESOURCE = "数据集-字段分类-字段";
	private final String SHEET_CLASSIFY_RELATION_RESOURCE = "数据集-字段分类关系";
	
	private final String SHEET_DATASET_SENSITIVE_COL_DATASET_SENSITIVE_LEVEL = "敏感度编码";
	private final String SHEET_DATASET_SENSITIVE_COL_CLUE_SRC_SYS = "所属节点编码";
	private final String SHEET_DATASET_SENSITIVE_COL_DATASET_SENSITIVE_NAME = "敏感度名称";
	
	private final String SHEET_DATASET_COL_DATA_SET = "数据集编码";
	private final String SHEET_DATASET_COL_CLUE_SRC_SYS = "所属节点编码";
	private final String SHEET_DATASET_COL_DATASET_NAME = "数据集名称";
	private final String SHEET_DATASET_COL_DATASET_SENSITIVE_LEVEL = "数据集敏感度";
	private final String SHEET_DATASET_COL_DATASET_ISPRIVATE = "是否私有";
	
	private final String SHEET_COLUMN_CLASSIFY_COL_SECTION_CLASS = "字段分类编码";
	private final String SHEET_COLUMN_CLASSIFY_COL_CLUE_SRC_SYS = "所属节点编码";
	private final String SHEET_COLUMN_CLASSIFY_COL_CLASSIFY_NAME = "字段分类名称";
	
	private final String SHEET_COLUMN_COL_ELEMENT = "字段编码";
	private final String SHEET_COLUMN_COL_CLUE_SRC_SYS = "所属节点编码";
	private final String SHEET_COLUMN_COL_DATA_SET = "数据集编码";
	private final String SHEET_COLUMN_COL_COLUMU_CN = "字段中文名称";
	private final String SHEET_COLUMN_COL_COLUMN_NAME = "字段英文名称";
	private final String SHEET_COLUMN_COL_RMK = "字段描述";
	private final String SHEET_COLUMN_COL_ISPRIVATE = "是否私有";
	
	private final String SHEET_VALUE_SENSITIVE_COL_VALUE_SENSITIVE_ID = "敏感度编码";
	private final String SHEET_VALUE_SENSITIVE_COL_CLUE_SRC_SYS = "所属节点编码";
	private final String SHEET_VALUE_SENSITIVE_COL_VALUE_SENSITIVE_NAME = "敏感度名称";
	
	private final String SHEET_VALUE_COL_ELEMENT_VALUE = "字段值";
	private final String SHEET_VALUE_COL_CLUE_SRC_SYS = "所属节点编码";
	private final String SHEET_VALUE_COL_VALUE_NAME = "备注";
	private final String SHEET_VALUE_COL_VALUE_SENSITIVE_ID = "字段值敏感度编码";
	private final String SHEET_VALUE_COL_ELEMENT = "字段值所属的字段编码";
	private final String SHEET_VALUE_COL_ISPRIVATE = "是否私有";
	
	private final String SHEET_COLUMN_ClASSIFY_REALTION_COL_SECTION_RELATIOIN_CLASS = "字段分类关系代码";
	private final String SHEET_COLUMN_ClASSIFY_REALTION_COL_CLUE_SRC_SYS = "所属节点编码";
	private final String SHEET_COLUMN_ClASSIFY_REALTION_COL_SRC_CLASS_CODE = "源字段分类编码";
	private final String SHEET_COLUMN_ClASSIFY_REALTION_COL_DST_CLASS_CODE = "目标字段分类编码";
	
	private final String SHEET_ROW_RESOURCE_COL_ID = "关系的唯一标识";
	private final String SHEET_ROW_RESOURCE_COL_DATA_SET = "数据集编码";
	private final String SHEET_ROW_RESOURCE_COL_ELEMENT = "字段编码";
	private final String SHEET_ROW_RESOURCE_COL_CLUE_SRC_SYS = "所属节点编码";
	private final String SHEET_ROW_RESOURCE_COL_ELEMENT_VALUE = "字段值";
	private final String SHEET_ROW_RESOURCE_COL_RMK = "字段值备注";
	private final String SHEET_ROW_RESOURCE_COL_ISPRIVATE = "是否私有";
	
	private final String SHEET_CLASSIFY_RESOURCE_COL_ID = "关系唯一标识";
	private final String SHEET_CLASSIFY_RESOURCE_COL_DATA_SET = "数据集编码";
	private final String SHEET_CLASSIFY_RESOURCE_COL_SECTION_CLASS = "字段分类编码";
	private final String SHEET_CLASSIFY_RESOURCE_COL_CLUE_SRC_SYS = "所属节点编码";
	private final String SHEET_CLASSIFY_RESOURCE_COL_SECTION_CLASS_NAME = "字段分类";
	private final String SHEET_CLASSIFY_RESOURCE_COL_ELEMENT = "字段";
	private final String SHEET_CLASSIFY_RESOURCE_COL_RMK = "备注";

	private final String SHEET_CLASSIFY_RELATION_RESOURCE_COL_ID = "关系唯一标识";
	private final String SHEET_CLASSIFY_RELATION_RESOURCE_COL_DATA_SET = "数据集编码";
	private final String SHEET_CLASSIFY_RELATION_RESOURCE_COL_SECTION_RELATIOIN_CLASS = "字段分类关系编码";
	private final String SHEET_CLASSIFY_RELATION_RESOURCE_COL_CLUE_SRC_SYS = "所属节点编码";
	private final String SHEET_CLASSIFY_RELATION_RESOURCE_COL_SECTION_RELATIOIN_CLASS_NAME = "字段分类关系";
	
	
	private final String SHEET_ROLE_RESOURCE = "关系对照";
	private final String SHEET_ROLE_RESOURCE_COL_ROLE_TYPE = "角色类型";
	private final String SHEET_ROLE_RESOURCE_COL_ROLE_CODE = "角色代码";
	private final String SHEET_ROLE_RESOURCE_COL_ELEMENT = "数据项";
	private final String SHEET_ROLE_RESOURCE_COL_ELEMENT_VALUE = "数据项值";
	private final String SHEET_ROLE_RESOURCE_COL_DATASET = "协议编码";
	private final String SHEET_ROLE_RESOURCE_COL_SECTION_CLASS = "字段分类编码";
	
//	private final String SHEET_REATURE_RESOURCE = "功能资源";
//	private final String SHEET_REATURE_RESOURCE_COL_SYSTEM_TYPE = "系统类型(J020012)";
//	private final String SHEET_REATURE_RESOURCE_COL_RESOURCE_ID = "资源唯一标识(J030006)";
//	private final String SHEET_REATURE_RESOURCE_COL_APP_ID = "所属业务系ID(J020013)";
//	private final String SHEET_REATURE_RESOURCE_COL_NAME = "名称(J030007)";
//	private final String SHEET_REATURE_RESOURCE_COL_PARENT = "父资源唯一标识(J030008)";
//	private final String SHEET_REATURE_RESOURCE_COL_URL = "URL(G010002)";
//	private final String SHEET_REATURE_RESOURCE_COL_ICON = "图标路径(J030009)";
//	private final String SHEET_REATURE_RESOURCE_COL_RESOURCE_STATUS = "资源状态(J030010)";
//	private final String SHEET_REATURE_RESOURCE_COL_ORDER = "顺序(J030011)";
//	private final String SHEET_REATURE_RESOURCE_COL_DESCRIBE = "资源描述(J030012)";
//	private final String SHEET_REATURE_RESOURCE_COL_REMARK = "备注(J030013)";
//	private final String SHEET_REATURE_RESOURCE_COL_RESOURCE_TYPE = "资源分类(J030035)";
	
	private final String SHEET_WZUSER = "私有用户";
	private final String SHEET_WZUSER_COL_NAME = "真实姓名";
	private final String SHEET_WZUSER_COL_POLICENO = "警员号";
	private final String SHEET_WZUSER_COL_CERTIFICATE_CODE_MD5 = "身份证MD5";
	private final String SHEET_WZUSER_COL_CERTIFICATE = "身份证号";
	
	private final String SHEET_WZROLE = "私有角色";
	private final String SHEET_WZROLE_COL_NAME = "角色名称";
	private final String SHEET_WZROLE_COL_DESC = "角色描述";
	private final String SHEET_WZROLE_COL_CODE = "角色编码";
	
	private final String SHEET_WZROLEUSER = "私有用户角色关系";
	private final String SHEET_WZROLEUSER_COL_CODE = "角色编码";
	private final String SHEET_WZROLEUSER_COL_MD5 = "身份证MD5";
	
	private final String SHEET_WZROLEFUNC = "私有角色与功能资源关系";
	private final String SHEET_WZROLEFUNC_COL_RESID = "资源ID";
	private final String SHEET_WZROLEFUNC_COL_ROLEID = "角色编码";
	
	public void UploadResourceDataV2(File inData) throws Exception {
		InputStream in=new FileInputStream(inData);
        Workbook workbook = WorkbookFactory.create(in);
        
        int sheetCount = workbook.getNumberOfSheets();  //Sheet的数量  
        //deal with seven elements and relationships
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
            	updateValueSensitive(sheet);
            } else if ( SHEET_VALUE.equals(sheetName) ) {
            	updateValue(sheet);
            } else if ( SHEET_COLUMN_ClASSIFY_REALTION.equals(sheetName) ) {
            	updateColumnClassifyRelation(sheet);
            } else if ( SHEET_ROW_RESOURCE.equals(sheetName) ) {
            	updateRowResource(sheet);
            } else if ( SHEET_CLASSIFY_RESOURCE.equals(sheetName) ) {
            	updateClassifyResource(sheet);
            } else if ( SHEET_CLASSIFY_RELATION_RESOURCE.equals(sheetName) ) {
            	updateClassifyRelationResource(sheet);
            }
        }
        
        in.close();
        
        //update resource;
        logger.info("数据资源导入V2完成");
        String importType = "数据资源导入;";
        AddResImportLog(importType);
        
        return;
	}
	
	private void updateDatasetSensitive(Sheet sheet) throws Exception {
		int rowCount = sheet.getPhysicalNumberOfRows(); //获取总行数
		Map<String, Integer> idx = new HashMap<String, Integer>();
		ResDataSetSensitive dss = null;
		ResDatasetSensitiveDAO dao = new ResDatasetSensitiveDAOImpl();
		
		AttributeDAO adao = new AttributeDAOImpl();
		AttrDefinition attrDef = null;
		
		//遍历每一行  
        for (int r = 0; r < rowCount; r++) {
        	Row row = sheet.getRow(r);
        	if(row == null) {
        		continue;
        	}
        	if( r > 0 ) {
        		dss = new ResDataSetSensitive();
        	}
        	int cellCount = row.getLastCellNum(); //获取总列数 
        	//遍历每一列  
            for (int c = 0; c < cellCount; c++) {
            	Cell cell = row.getCell(c);
            	String cellValue = getCellValue(cell);

            	if(r == 0) {
            		if ( SHEET_DATASET_SENSITIVE_COL_DATASET_SENSITIVE_LEVEL.equals(cellValue) ) {
            			idx.put(SHEET_DATASET_SENSITIVE_COL_DATASET_SENSITIVE_LEVEL, c);
            		} else if ( SHEET_DATASET_SENSITIVE_COL_CLUE_SRC_SYS.equals(cellValue) ) {
            			idx.put(SHEET_DATASET_SENSITIVE_COL_CLUE_SRC_SYS, c);
            		} else if ( SHEET_DATASET_SENSITIVE_COL_DATASET_SENSITIVE_NAME.equals(cellValue) ) {
            			idx.put(SHEET_DATASET_SENSITIVE_COL_DATASET_SENSITIVE_NAME, c);
            		}
            	} else {
            		if(idx.size() == 0) {
            			throw new Exception("导入数据文件格式不正确!");
            		}
            		if( c == idx.get(SHEET_DATASET_SENSITIVE_COL_DATASET_SENSITIVE_LEVEL) ) {
            			dss.setDATASET_SENSITIVE_LEVEL(cellValue);
            		} else if ( c == idx.get(SHEET_DATASET_SENSITIVE_COL_CLUE_SRC_SYS) ) {
            			dss.setCLUE_SRC_SYS(cellValue);
            		} else if ( c== idx.get(SHEET_DATASET_SENSITIVE_COL_DATASET_SENSITIVE_NAME) ) {
            			dss.setDATASET_SENSITIVE_NAME(cellValue);
            		}
            	}
            }
            
            if( r > 0 ) {
            	if(dss.isValid()) {
		            dss.setDELETE_STATUS(ResDataSetSensitive.DELSTATUSNO);
		            dao.ResDataSetSensitiveSave(dss);
		            
		            attrDef = adao.GetAttrDefinitionByCode(AttrDefinition.ATTR_RESOURCEDATA_DATASET_SENSITIVE_LEVEL_CODE);
					if(attrDef != null) {
						AttrDictionary attrDict = new AttrDictionary();
						attrDict.setAttrid(attrDef.getId());
						attrDict.setValue(dss.getDATASET_SENSITIVE_NAME());
						attrDict.setCode(dss.getDATASET_SENSITIVE_LEVEL());
						adao.AttrDictionaryAdd(attrDict);
					}
            	}
            	else {
            		logger.info("[IRD] import data of DatasetSensitive format error[row number: " + r + "]");
            	}
            }
        }
	}
	
	private void updateDataset(Sheet sheet) throws Exception {
		int rowCount = sheet.getPhysicalNumberOfRows(); //获取总行数
		Map<String, Integer> idx = new HashMap<String, Integer>();
		ResDataSet ds = null;
		ResDatasetDAO dao = new ResDatasetDAOImpl();
		
		AttributeDAO adao = new AttributeDAOImpl();
		AttrDefinition attrDef = null;
		
		//遍历每一行  
        for (int r = 0; r < rowCount; r++) {
        	boolean isLocal = false;
        	Row row = sheet.getRow(r);
        	if(row == null) {
        		continue;
        	}
        	if( r > 0 ) {
        		ds = new ResDataSet();
        	}
        	int cellCount = row.getLastCellNum(); //获取总列数 
        	//遍历每一列  
            for (int c = 0; c < cellCount; c++) {
            	Cell cell = row.getCell(c);
            	String cellValue = getCellValue(cell);
            	
            	if(r == 0) {
            		if ( SHEET_DATASET_COL_DATA_SET.equals(cellValue) ) {
            			idx.put(SHEET_DATASET_COL_DATA_SET, c);
            		} else if ( SHEET_DATASET_COL_CLUE_SRC_SYS.equals(cellValue) ) {
            			idx.put(SHEET_DATASET_COL_CLUE_SRC_SYS, c);
            		} else if ( SHEET_DATASET_COL_DATASET_NAME.equals(cellValue) ) {
            			idx.put(SHEET_DATASET_COL_DATASET_NAME, c);
            		} else if ( SHEET_DATASET_COL_DATASET_SENSITIVE_LEVEL.equals(cellValue) ) {
            			idx.put(SHEET_DATASET_COL_DATASET_SENSITIVE_LEVEL, c);
            		} else if ( SHEET_DATASET_COL_DATASET_ISPRIVATE.equals(cellValue) ) {
            			idx.put(SHEET_DATASET_COL_DATASET_ISPRIVATE, c);
            		}  
            		
            	} else {
            		if(idx.size() == 0) {
            			throw new Exception("导入数据文件格式不正确!");
            		}
            		if( c == idx.get(SHEET_DATASET_COL_DATA_SET) ) {
            			ds.setDATA_SET(cellValue);
            		} else if ( c == idx.get(SHEET_DATASET_COL_CLUE_SRC_SYS) ) {
            			ds.setCLUE_SRC_SYS(cellValue);
            		} else if ( c== idx.get(SHEET_DATASET_COL_DATASET_NAME) ) {
            			ds.setDATASET_NAME(cellValue);
            		} else if ( c== idx.get(SHEET_DATASET_COL_DATASET_SENSITIVE_LEVEL) ) {
            			ds.setDATASET_SENSITIVE_LEVEL(cellValue);
            		} else if ( c== idx.get(SHEET_DATASET_COL_DATASET_ISPRIVATE) ) {
            			if( "1".equals(cellValue) ) {
            				isLocal = true;
            			}
            		}
            	}
            }
            
            if( r > 0 ) {
            	if( ds.isValid() ) {
            		ds.setDELETE_STATUS(ResDataSet.DELSTATUSNO);
		            if( isLocal ) {
		            	ResDataSetPrivate dsp = new ResDataSetPrivate(ds);
		            	dao.ResDataSetPrivateSave(dsp);
		            }
		            else {
		            	dao.ResDataSetSave(ds);
		            }
		            
		            updateDefaultRoleOfDataSet(ds, isLocal);
		            
		            attrDef = adao.GetAttrDefinitionByCode(AttrDefinition.ATTR_RESOURCEDATA_DATA_SET_CODE);
					if(attrDef != null) {
						AttrDictionary attrDict = new AttrDictionary();
						attrDict.setAttrid(attrDef.getId());
						attrDict.setValue(ds.getDATASET_NAME());
						attrDict.setCode(ds.getDATA_SET());
						adao.AttrDictionaryAdd(attrDict);
					}
					
					
            	}
            	else {
            		logger.info("[IRD] import data of Dataset format error[row number: " + r + "]");
            	}
            }
        }
	}

	private void updateDefaultRoleOfDataSet(ResDataSet ds, boolean isLocal) {
		// if isLocal is true, then create local role only, else create both public and local role
		ResourceUploadDAO dao = new ResourceUploadDAOImpl();
		try{
			ResRole role = new ResRole();
			role.setBUSINESS_ROLE_NAME("默认公有角色[" + ds.getDATASET_NAME() + "]");
			role.setBUSINESS_ROLE_TYPE(ResRole.RESROLETYPEPUBLIC);
			role.setCLUE_SRC_SYS(ds.getCLUE_SRC_SYS());
			role.setROLE_DESC(ds.getDATA_SET());
			role.setSYSTEM_TYPE("");
			role.setDELETE_STATUS(ResRole.DELSTATUSNO);
			if( !isLocal ) {
				dao.DefaultRoleOfDataSetAdd(role);
			}
			
			role.setBUSINESS_ROLE_NAME("默认本地角色[" + ds.getDATASET_NAME() + "]");
			role.setBUSINESS_ROLE_TYPE(ResRole.RESROLETYPELOCAL);
			dao.DefaultRoleOfDataSetAdd(role);
		}
		catch(Exception e) {
			logger.info("[IRD]create default role failed.[" + e.getMessage() + "]");
		}
		return;
	}

	private void updateColumnClassify(Sheet sheet) throws Exception {
		int rowCount = sheet.getPhysicalNumberOfRows(); //获取总行数
		Map<String, Integer> idx = new HashMap<String, Integer>();
		ResColumnClassify cc = null;
		ResColumnClassifyDAO dao = new ResColumnClassifyDAOImpl();
		
		AttributeDAO adao = new AttributeDAOImpl();
		AttrDefinition attrDef = null;
		
		//遍历每一行  
        for (int r = 0; r < rowCount; r++) {
        	Row row = sheet.getRow(r);
        	if(row == null) {
        		continue;
        	}
        	if( r > 0 ) {
        		cc = new ResColumnClassify();
        	}
        	int cellCount = row.getLastCellNum(); //获取总列数 
        	//遍历每一列  
            for (int c = 0; c < cellCount; c++) {
            	Cell cell = row.getCell(c);
            	String cellValue = getCellValue(cell);
            	
            	if(r == 0) {
            		if ( SHEET_COLUMN_CLASSIFY_COL_SECTION_CLASS.equals(cellValue) ) {
            			idx.put(SHEET_COLUMN_CLASSIFY_COL_SECTION_CLASS, c);
            		} else if ( SHEET_COLUMN_CLASSIFY_COL_CLUE_SRC_SYS.equals(cellValue) ) {
            			idx.put(SHEET_COLUMN_CLASSIFY_COL_CLUE_SRC_SYS, c);
            		} else if ( SHEET_COLUMN_CLASSIFY_COL_CLASSIFY_NAME.equals(cellValue) ) {
            			idx.put(SHEET_COLUMN_CLASSIFY_COL_CLASSIFY_NAME, c);
            		}             		
            	} else {
            		if(idx.size() == 0) {
            			throw new Exception("导入数据文件格式不正确!");
            		}
            		if( c == idx.get(SHEET_COLUMN_CLASSIFY_COL_SECTION_CLASS) ) {
            			cc.setSECTION_CLASS(cellValue);
            		} else if ( c == idx.get(SHEET_COLUMN_CLASSIFY_COL_CLUE_SRC_SYS) ) {
            			cc.setCLUE_SRC_SYS(cellValue);
            		} else if ( c== idx.get(SHEET_COLUMN_CLASSIFY_COL_CLASSIFY_NAME) ) {
            			cc.setCLASSIFY_NAME(cellValue);
            		}             		
            	}
            }
            
            if( r > 0 ) {
            	if( cc.isValid() ) {
		            cc.setDELETE_STATUS(ResColumnClassify.DELSTATUSNO);
		            dao.ResColumnClassifySave(cc);
		            
		            attrDef = adao.GetAttrDefinitionByCode(AttrDefinition.ATTR_RESOURCEDATA_SECTION_CLASS_CODE);
					if(attrDef != null) {
						AttrDictionary attrDict = new AttrDictionary();
						attrDict.setAttrid(attrDef.getId());
						attrDict.setValue(cc.getCLASSIFY_NAME());
						attrDict.setCode(cc.getSECTION_CLASS());
						adao.AttrDictionaryAdd(attrDict);
					}
            	}
            	else {
            		logger.info("[IRD] import data of ColumnClassify format error[row number: " + r + "]");
            	}
            }
        }
	}
	
	private void updateColumn(Sheet sheet) throws Exception {
		int rowCount = sheet.getPhysicalNumberOfRows(); //获取总行数
		Map<String, Integer> idx = new HashMap<String, Integer>();
		ResColumn col = null;
		ResColumnResourceDAO dao = new ResColumnResourceDAOImpl();
		
		AttributeDAO adao = new AttributeDAOImpl();
		AttrDefinition attrDef = null;
		
		//catch all dataset for update resource record
		ResDatasetDAO rdsdao = new ResDatasetDAOImpl();
		List<ResDataSet> rdss = rdsdao.QueryAllDataSet();
		Map<String, ResDataSet> rdssMap = new HashMap<String, ResDataSet>();
		for(ResDataSet rds : rdss) {
			rdssMap.put(rds.getDATA_SET(), rds);
		}
		List<ResDataSetPrivate> rdsps = rdsdao.QueryAllDataSetPrivate();
		Map<String, ResDataSetPrivate> rdspsMap = new HashMap<String, ResDataSetPrivate>();
		for(ResDataSetPrivate rdsp : rdsps) {
			rdspsMap.put(rdsp.getDATA_SET(), rdsp);
		}
		
		//遍历每一行  
        for (int r = 0; r < rowCount; r++) {
        	boolean isLocal = false;
        	Row row = sheet.getRow(r);
        	if(row == null) {
        		continue;
        	}
        	if( r > 0 ) {
        		col = new ResColumn();
        	}
        	int cellCount = row.getLastCellNum(); //获取总列数 
        	//遍历每一列  
            for (int c = 0; c < cellCount; c++) {
            	Cell cell = row.getCell(c);
            	String cellValue = getCellValue(cell);
            	if(r == 0) {
            		if ( SHEET_COLUMN_COL_ELEMENT.equals(cellValue) ) {
            			idx.put(SHEET_COLUMN_COL_ELEMENT, c);
            		} else if ( SHEET_COLUMN_COL_CLUE_SRC_SYS.equals(cellValue) ) {
            			idx.put(SHEET_COLUMN_COL_CLUE_SRC_SYS, c);
            		} else if ( SHEET_COLUMN_COL_DATA_SET.equals(cellValue) ) {
            			idx.put(SHEET_COLUMN_COL_DATA_SET, c);
            		} else if ( SHEET_COLUMN_COL_COLUMU_CN.equals(cellValue) ) {
            			idx.put(SHEET_COLUMN_COL_COLUMU_CN, c);
            		} else if ( SHEET_COLUMN_COL_COLUMN_NAME.equals(cellValue) ) {
            			idx.put(SHEET_COLUMN_COL_COLUMN_NAME, c);
            		} else if ( SHEET_COLUMN_COL_RMK.equals(cellValue) ) {
            			idx.put(SHEET_COLUMN_COL_RMK, c);
            		} else if ( SHEET_COLUMN_COL_ISPRIVATE.equals(cellValue) ) {
            			idx.put(SHEET_COLUMN_COL_ISPRIVATE, c);
            		}
            	} else {
            		if(idx.size() == 0) {
            			throw new Exception("导入数据文件格式不正确!");
            		}
            		if( c == idx.get(SHEET_COLUMN_COL_ELEMENT) ) {
            			col.setELEMENT(cellValue);
            		} else if ( c == idx.get(SHEET_COLUMN_COL_CLUE_SRC_SYS) ) {
            			col.setCLUE_SRC_SYS(cellValue);
            		} else if ( c== idx.get(SHEET_COLUMN_COL_DATA_SET) ) {
            			col.setDATA_SET(cellValue);
            		} else if ( c== idx.get(SHEET_COLUMN_COL_COLUMU_CN) ) {
            			col.setCOLUMU_CN(cellValue);
            		} else if ( c== idx.get(SHEET_COLUMN_COL_COLUMN_NAME) ) {
            			col.setCOLUMN_NAME(cellValue);
            		} else if ( c== idx.get(SHEET_COLUMN_COL_RMK) ) {
            			col.setRMK(cellValue);
            		} else if ( c== idx.get(SHEET_COLUMN_COL_ISPRIVATE) ) {
            			if("1".equals(cellValue) ) {
            				isLocal = true;
            			}
            		}
            	}
            }
            
            if( r > 0 ) {
            	if( col.isValid() ) {
		            col.setDELETE_STATUS(ResColumn.DELSTATUSNO);
		            if( isLocal ) {
		            	ResColumnPrivate rcp = new ResColumnPrivate(col);
		            	dao.ResColumnPrivateSave(rcp);
		            }
		            else {
		            	dao.ResColumnSave(col);
		            }
		            
		            updateColumnResourceInDefaultRoleOfDataSet(col, isLocal, rdssMap, rdspsMap);
		            
		            attrDef = adao.GetAttrDefinitionByCode(AttrDefinition.ATTR_RESOURCEDATA_ELEMENT_CODE);
					if(attrDef != null) {
						AttrDictionary attrDict = new AttrDictionary();
						attrDict.setAttrid(attrDef.getId());
						attrDict.setValue(col.getCOLUMU_CN());
						attrDict.setCode(col.getELEMENT());
						adao.AttrDictionaryAdd(attrDict);
					}
            	}
            	else {
            		logger.info("[IRD] import data of Column format error[row number: " + r + "]");
            	}
            }
        }
	}
	
	private void updateColumnResourceInDefaultRoleOfDataSet(ResColumn col,
			boolean isLocal, Map<String, ResDataSet>rdssMap, Map<String, ResDataSetPrivate> rdspsMap) {
		// if isLocal is true, then put resource into local role, else put resource into public role
		ResourceUploadDAO dao = new ResourceUploadDAOImpl();
		try{
			//create column resource record
			ResData data = new ResData();
			data.setDATA_SET(col.getDATA_SET());
			
			if( isLocal ) {
				ResDataSetPrivate rdsp = rdspsMap.get(col.getDATA_SET());
				String sensitiveLevel = null;
				if(rdsp == null) {
					ResDataSet rds = rdssMap.get(col.getDATA_SET());
					if(rds == null) {
						throw new Exception("columns' data_set is unknown:" + col.getDATA_SET());
					}
					sensitiveLevel = rds.getDATASET_SENSITIVE_LEVEL();
				}
				else {
					sensitiveLevel = rdsp.getDATASET_SENSITIVE_LEVEL();
				}
				data.setDATASET_SENSITIVE_LEVEL(sensitiveLevel);
				data.setResource_type(ResData.RESTYPELOCAL);
			}
			else {
				ResDataSet rds = rdssMap.get(col.getDATA_SET());
				if(rds == null) {
					throw new Exception("columns' data_set is unknown:" + col.getDATA_SET());
				}
				data.setDATASET_SENSITIVE_LEVEL(rds.getDATASET_SENSITIVE_LEVEL());
				data.setResource_type(ResData.RESTYPEPUBLIC);
			}
			
			data.setDELETE_STATUS(ResData.DELSTATUSNO);
			data.setELEMENT(col.getELEMENT());
			data.setName("字段资源-" + col.getELEMENT() + "[" + col.getCOLUMU_CN() + "]");
			data.setRESOURCE_DESCRIBE(col.getRMK());
			data.setRESOURCE_STATUS(ResData.RESSTATUSENABLE);
			
			data = dao.ImportResDataOfColumn(data);
			
			//create resource and role relationship record
			int roleType = isLocal ? ResRole.RESROLETYPELOCAL : ResRole.RESROLETYPEPUBLIC;
			ResRole role = dao.GetDefaultRoleByDataSet(data.getDATA_SET(), roleType);
			if(role == null) {
				throw new Exception("default role isn't exist(dataSet: " + data.getDATA_SET() + "; roleType: " + roleType + ")");
			}
			ResRoleResource rrr = new ResRoleResource();
			rrr.setBUSINESS_ROLE(role.getBUSINESS_ROLE());
			rrr.setDELETE_STATUS(ResRoleResource.DELSTATUSNO);
			rrr.setRESOURCE_ID(data.getRESOURCE_ID());
			rrr.setRESOURCE_CLASS(ResRoleResource.RESCLASSDATA);
			dao.ResRoleResourceAdd(rrr);
		}
		catch(Exception e) {
			logger.info("[IRD]update column resource in the default role failed.[" + e.getMessage() + "]");
		}
		return;		
	}

	private void updateValueSensitive(Sheet sheet) throws Exception {
		int rowCount = sheet.getPhysicalNumberOfRows(); //获取总行数
		Map<String, Integer> idx = new HashMap<String, Integer>();
		ResValueSensitive vs = null;
		ResValueSensitiveDAO dao = new ResValueSensitiveDAOImpl();
		//遍历每一行  
        for (int r = 0; r < rowCount; r++) {
        	Row row = sheet.getRow(r);
        	if(row == null) {
        		continue;
        	}
        	if( r > 0 ) {
        		vs = new ResValueSensitive();
        	}
        	int cellCount = row.getLastCellNum(); //获取总列数 
        	//遍历每一列  
            for (int c = 0; c < cellCount; c++) {
            	Cell cell = row.getCell(c);
            	String cellValue = getCellValue(cell);
            	
            	if(r == 0) {
            		if ( SHEET_VALUE_SENSITIVE_COL_VALUE_SENSITIVE_ID.equals(cellValue) ) {
            			idx.put(SHEET_VALUE_SENSITIVE_COL_VALUE_SENSITIVE_ID, c);
            		} else if ( SHEET_VALUE_SENSITIVE_COL_CLUE_SRC_SYS.equals(cellValue) ) {
            			idx.put(SHEET_VALUE_SENSITIVE_COL_CLUE_SRC_SYS, c);
            		} else if ( SHEET_VALUE_SENSITIVE_COL_VALUE_SENSITIVE_NAME.equals(cellValue) ) {
            			idx.put(SHEET_VALUE_SENSITIVE_COL_VALUE_SENSITIVE_NAME, c);
            		}
            	} else {
            		if(idx.size() == 0) {
            			throw new Exception("导入数据文件格式不正确!");
            		}
            		if( c == idx.get(SHEET_VALUE_SENSITIVE_COL_VALUE_SENSITIVE_ID) ) {
            			vs.setVALUE_SENSITIVE_ID(cellValue);
            		} else if ( c == idx.get(SHEET_VALUE_SENSITIVE_COL_CLUE_SRC_SYS) ) {
            			vs.setCLUE_SRC_SYS(cellValue);
            		} else if ( c== idx.get(SHEET_VALUE_SENSITIVE_COL_VALUE_SENSITIVE_NAME) ) {
            			vs.setVALUE_SENSITIVE_NAME(cellValue);
            		}
            	}
            }
            
            if( r > 0 ) {
            	if( vs.isValid() ) {
		            vs.setDELETE_STATUS(ResValueSensitive.DELSTATUSNO);
		            dao.ResValueSensitiveSave(vs);
            	}
            	else {
            		logger.info("[IRD] import data of ValueSensitive format error[row number: " + r + "]");
            	}
            }
        }
	}

	private void updateValue(Sheet sheet) throws Exception {
		int rowCount = sheet.getPhysicalNumberOfRows(); //获取总行数
		Map<String, Integer> idx = new HashMap<String, Integer>();
		ResValue val = null;
		ResValueDAO dao = new ResValueDAOImpl();
		//遍历每一行
        for (int r = 0; r < rowCount; r++) {
        	boolean isLocal = false;
        	Row row = sheet.getRow(r);
        	if(row == null) {
        		continue;
        	}
        	if( r > 0 ) {
        		val = new ResValue();
        	}
        	int cellCount = row.getLastCellNum(); //获取总列数 
        	//遍历每一列  
            for (int c = 0; c < cellCount; c++) {
            	Cell cell = row.getCell(c);
            	String cellValue = getCellValue(cell);
            	
            	if(r == 0) {
            		if ( SHEET_VALUE_COL_ELEMENT_VALUE.equals(cellValue) ) {
            			idx.put(SHEET_VALUE_COL_ELEMENT_VALUE, c);
            		} else if ( SHEET_VALUE_COL_CLUE_SRC_SYS.equals(cellValue) ) {
            			idx.put(SHEET_VALUE_COL_CLUE_SRC_SYS, c);
            		} else if ( SHEET_VALUE_COL_VALUE_NAME.equals(cellValue) ) {
            			idx.put(SHEET_VALUE_COL_VALUE_NAME, c);
            		} else if ( SHEET_VALUE_COL_VALUE_SENSITIVE_ID.equals(cellValue) ) {
            			idx.put(SHEET_VALUE_COL_VALUE_SENSITIVE_ID, c);
            		} else if ( SHEET_VALUE_COL_ELEMENT.equals(cellValue) ) {
            			idx.put(SHEET_VALUE_COL_ELEMENT, c);
            		} else if ( SHEET_VALUE_COL_ISPRIVATE.equals(cellValue) ) {
            			idx.put(SHEET_VALUE_COL_ISPRIVATE, c);
            		}
            	} else {
            		if(idx.size() == 0) {
            			throw new Exception("导入数据文件格式不正确!");
            		}
            		if( c == idx.get(SHEET_VALUE_COL_ELEMENT_VALUE) ) {
            			val.setELEMENT_VALUE(cellValue);
            		} else if ( c == idx.get(SHEET_VALUE_COL_CLUE_SRC_SYS) ) {
            			val.setCLUE_SRC_SYS(cellValue);
            		} else if ( c == idx.get(SHEET_VALUE_COL_VALUE_NAME) ) {
            			val.setVALUE_NAME(cellValue);
            		} else if ( c == idx.get(SHEET_VALUE_COL_VALUE_SENSITIVE_ID) ) {
            			val.setVALUE_SENSITIVE_ID(cellValue);
            		} else if ( c == idx.get(SHEET_VALUE_COL_ELEMENT) ) {
            			val.setELEMENT(cellValue);
            		} else if ( c == idx.get(SHEET_VALUE_COL_ISPRIVATE) ) {
            			if( "1".equals(cellValue) ) {
            				isLocal = true;
            			}
            		}
            	}
            }
            
            if( r > 0 ) {
            	if( val.isValid() ) {
		            val.setDELETE_STATUS(ResValue.DELSTATUSNO);
		            if(isLocal) {
		            	ResValuePrivate rvp = new ResValuePrivate(val);
		            	dao.ResValuePrivateSave(rvp);
		            } else {
		            	dao.ResValueSave(val);
		            }
		            
		            // update column of row resource list
		            ResourceUploadDAO rudao = new ResourceUploadDAOImpl();
		            RowResourceColumn col = new RowResourceColumn();
		            col.setElement(val.getELEMENT());
		            rudao.RowResourceColumnAdd(col);		            
            	}
            	else {
            		logger.info("[IRD] import data of Value format error[row number: " + r + "]");
            	}
            }
        }
	}

	private void updateColumnClassifyRelation(Sheet sheet) throws Exception {
		int rowCount = sheet.getPhysicalNumberOfRows(); //获取总行数
		Map<String, Integer> idx = new HashMap<String, Integer>();
		ResRelationColumnClassify rcc = null;
		ResColumnClassifyRelationDAO dao = new ResColumnClassifyRelationDAOImpl();
		
		AttributeDAO adao = new AttributeDAOImpl();
		AttrDefinition attrDef = null;
		Map<String, String> columnClassifyMap = queryAllNameAndCodeOfColumnClassify();
		
		//遍历每一行  
        for (int r = 0; r < rowCount; r++) {
        	Row row = sheet.getRow(r);
        	if(row == null) {
        		continue;
        	}
        	if( r > 0 ) {
        		rcc = new ResRelationColumnClassify();
        	}
        	int cellCount = row.getLastCellNum(); //获取总列数 
        	//遍历每一列  
            for (int c = 0; c < cellCount; c++) {
            	Cell cell = row.getCell(c);
            	String cellValue = getCellValue(cell);
            	
            	if(r == 0) {
            		if ( SHEET_COLUMN_ClASSIFY_REALTION_COL_SECTION_RELATIOIN_CLASS.equals(cellValue) ) {
            			idx.put(SHEET_COLUMN_ClASSIFY_REALTION_COL_SECTION_RELATIOIN_CLASS, c);
            		} else if ( SHEET_COLUMN_ClASSIFY_REALTION_COL_CLUE_SRC_SYS.equals(cellValue) ) {
            			idx.put(SHEET_COLUMN_ClASSIFY_REALTION_COL_CLUE_SRC_SYS, c);
            		} else if ( SHEET_COLUMN_ClASSIFY_REALTION_COL_SRC_CLASS_CODE.equals(cellValue) ) {
            			idx.put(SHEET_COLUMN_ClASSIFY_REALTION_COL_SRC_CLASS_CODE, c);
            		} else if ( SHEET_COLUMN_ClASSIFY_REALTION_COL_DST_CLASS_CODE.equals(cellValue) ) {
            			idx.put(SHEET_COLUMN_ClASSIFY_REALTION_COL_DST_CLASS_CODE, c);
            		}
            	} else {
            		if(idx.size() == 0) {
            			throw new Exception("导入数据文件格式不正确!");
            		}
            		if( c == idx.get(SHEET_COLUMN_ClASSIFY_REALTION_COL_SECTION_RELATIOIN_CLASS) ) {
            			rcc.setSECTION_RELATIOIN_CLASS(cellValue);
            		} else if ( c == idx.get(SHEET_COLUMN_ClASSIFY_REALTION_COL_CLUE_SRC_SYS) ) {
            			rcc.setCLUE_SRC_SYS(cellValue);
            		} else if ( c== idx.get(SHEET_COLUMN_ClASSIFY_REALTION_COL_SRC_CLASS_CODE) ) {
            			rcc.setSRC_CLASS_CODE(cellValue);
            		} else if ( c== idx.get(SHEET_COLUMN_ClASSIFY_REALTION_COL_DST_CLASS_CODE) ) {
            			rcc.setDST_CLASS_CODE(cellValue);
            		}          		
            	}
            }
            
            if( r > 0 ) {
            	if( rcc.isValid() ) {
		            rcc.setDELETE_STATUS(ResRelationColumnClassify.DELSTATUSNO);
		            dao.ResRelationColumnClassifySave(rcc);
		            
		            attrDef = adao.GetAttrDefinitionByCode(AttrDefinition.ATTR_RESOURCEDATA_SECTION_RELATION_CLASS_CODE);
					if(attrDef != null) {
						AttrDictionary attrDict = new AttrDictionary();
						attrDict.setAttrid(attrDef.getId());
						attrDict.setValue(columnClassifyMap.get(rcc.getSRC_CLASS_CODE()) + "->" + columnClassifyMap.get(rcc.getDST_CLASS_CODE()));
						attrDict.setCode(rcc.getSECTION_RELATIOIN_CLASS());
						adao.AttrDictionaryAdd(attrDict);
					}
            	}
            	else {
            		logger.info("[IRD] import data of ColumnClassifyRelation format error[row number: " + r + "]");
            	}
            }
        }
	}

	private Map<String, String> queryAllNameAndCodeOfColumnClassify() throws Exception {
		Map<String, String> result = new HashMap<String, String>();
		ResColumnClassifyDAO rccdao = new ResColumnClassifyDAOImpl();
		List<ResColumnClassify> rccs = rccdao.QueryAllColumnClassify();
		if( rccs != null ) {
			for( ResColumnClassify rcc : rccs ) {
				result.put(rcc.getSECTION_CLASS(), rcc.getCLASSIFY_NAME());
			}
		}
		return result;
	}

	private void updateRowResource(Sheet sheet) throws Exception {
		int rowCount = sheet.getPhysicalNumberOfRows(); //获取总行数
		Map<String, Integer> idx = new HashMap<String, Integer>();
		ResRowResource rr = null;
		ResRowResourceDAO dao = new ResRowResourceDAOImpl();
		
		//catch all dataset for update resource record
		ResDatasetDAO rdsdao = new ResDatasetDAOImpl();
		List<ResDataSet> rdss = rdsdao.QueryAllDataSet();
		Map<String, ResDataSet> rdssMap = new HashMap<String, ResDataSet>();
		for(ResDataSet rds : rdss) {
			rdssMap.put(rds.getDATA_SET(), rds);
		}
		List<ResDataSetPrivate> rdsps = rdsdao.QueryAllDataSetPrivate();
		Map<String, ResDataSetPrivate> rdspsMap = new HashMap<String, ResDataSetPrivate>();
		for(ResDataSetPrivate rdsp : rdsps) {
			rdspsMap.put(rdsp.getDATA_SET(), rdsp);
		}
		
		//遍历每一行  
        for (int r = 0; r < rowCount; r++) {
        	boolean isLocal = false;
        	Row row = sheet.getRow(r);
        	if(row == null) {
        		continue;
        	}
        	if( r > 0 ) {
        		rr = new ResRowResource();
        	}
        	int cellCount = row.getLastCellNum(); //获取总列数 
        	//遍历每一列  
            for (int c = 0; c < cellCount; c++) {
            	Cell cell = row.getCell(c);
            	String cellValue = getCellValue(cell);
            	
            	if(r == 0) {
            		if ( SHEET_ROW_RESOURCE_COL_ID.equals(cellValue) ) {
            			idx.put(SHEET_ROW_RESOURCE_COL_ID, c);
            		} else if ( SHEET_ROW_RESOURCE_COL_DATA_SET.equals(cellValue) ) {
            			idx.put(SHEET_ROW_RESOURCE_COL_DATA_SET, c);
            		} else if ( SHEET_ROW_RESOURCE_COL_ELEMENT.equals(cellValue) ) {
            			idx.put(SHEET_ROW_RESOURCE_COL_ELEMENT, c);
            		} else if ( SHEET_ROW_RESOURCE_COL_CLUE_SRC_SYS.equals(cellValue) ) {
            			idx.put(SHEET_ROW_RESOURCE_COL_CLUE_SRC_SYS, c);
            		} else if ( SHEET_ROW_RESOURCE_COL_ELEMENT_VALUE.equals(cellValue) ) {
            			idx.put(SHEET_ROW_RESOURCE_COL_ELEMENT_VALUE, c);
            		} else if ( SHEET_ROW_RESOURCE_COL_RMK.equals(cellValue) ) {
            			idx.put(SHEET_ROW_RESOURCE_COL_RMK, c);
            		} else if ( SHEET_ROW_RESOURCE_COL_ISPRIVATE.equals(cellValue) ) {
            			idx.put(SHEET_ROW_RESOURCE_COL_ISPRIVATE, c);
            		}
            	} else {
            		if(idx.size() == 0) {
            			throw new Exception("导入数据文件格式不正确!");
            		}
            		if( c == idx.get(SHEET_ROW_RESOURCE_COL_ID) ) {
            			//rr.setId(cellValue);
            		} else if ( c == idx.get(SHEET_ROW_RESOURCE_COL_DATA_SET) ) {
            			rr.setDATA_SET(cellValue);
            		} else if ( c == idx.get(SHEET_ROW_RESOURCE_COL_ELEMENT) ) {
            			rr.setELEMENT(cellValue);
            		} else if ( c == idx.get(SHEET_ROW_RESOURCE_COL_CLUE_SRC_SYS) ) {
            			rr.setCLUE_SRC_SYS(cellValue);
            		} else if ( c == idx.get(SHEET_ROW_RESOURCE_COL_ELEMENT_VALUE) ) {
            			rr.setELEMENT_VALUE(cellValue);
            		} else if ( c == idx.get(SHEET_ROW_RESOURCE_COL_RMK) ) {
            			rr.setRmk(cellValue);
            		} else if ( c == idx.get(SHEET_ROW_RESOURCE_COL_ISPRIVATE) ) {
            			if( "1".equals(cellValue) ) {
            				isLocal = true;
            			}
            		}
            	}
            }
            
            if( r > 0 ) {
            	if( rr.isValid() ) {
		            rr.setDELETE_STATUS(ResRowResource.DELSTATUSNO);
		            if(isLocal) {
		            	ResRowResourcePrivate rrrp = new ResRowResourcePrivate(rr);
		            	dao.ResRelationRowPrivateSave(rrrp);
		            } else {
		            	dao.ResRelationRowSave(rr);
		            }
		            
		            updateRowResourceInDefaultRoleOfDataSet(rr, isLocal, rdssMap, rdspsMap);
            	}
            	else {
            		logger.info("[IRD] import data of RowResource format error[row number: " + r + "]");
            	}
            }
        }
	}
	
	private void updateRowResourceInDefaultRoleOfDataSet(ResRowResource rr,
			boolean isLocal, Map<String, ResDataSet>rdssMap, Map<String, ResDataSetPrivate> rdspsMap) {
		// if isLocal is true, then put resource into local role, else put resource into public role
		ResourceUploadDAO dao = new ResourceUploadDAOImpl();
		try{
			//create column resource record
			ResData data = new ResData();
			data.setDATA_SET(rr.getDATA_SET());
			
			if( isLocal ) {
				ResDataSetPrivate rdsp = rdspsMap.get(rr.getDATA_SET());
				String sensitiveLevel = null;
				if(rdsp == null) {
					ResDataSet rds = rdssMap.get(rr.getDATA_SET());
					if(rds == null) {
						throw new Exception("rowResource's data_set is unknown:" + rr.getDATA_SET());
					}
					sensitiveLevel = rds.getDATASET_SENSITIVE_LEVEL();
				}
				else {
					sensitiveLevel = rdsp.getDATASET_SENSITIVE_LEVEL();
				}
				data.setDATASET_SENSITIVE_LEVEL(sensitiveLevel);
				data.setResource_type(ResData.RESTYPELOCAL);
			}
			else {
				ResDataSet rds = rdssMap.get(rr.getDATA_SET());
				if(rds == null) {
					throw new Exception("rowResource's data_set is unknown:" + rr.getDATA_SET());
				}
				data.setDATASET_SENSITIVE_LEVEL(rds.getDATASET_SENSITIVE_LEVEL());
				data.setResource_type(ResData.RESTYPEPUBLIC);
			}
			
			data.setDELETE_STATUS(ResData.DELSTATUSNO);
			data.setELEMENT(rr.getELEMENT());
			data.setELEMENT_VALUE(rr.getELEMENT_VALUE());
			data.setName("字段值资源-" + rr.getELEMENT() + "[" + rr.getELEMENT_VALUE() + "]");
			data.setRESOURCE_DESCRIBE(rr.getRmk());
			data.setOPERATE_SYMBOL(ResData.RES_OPERATE_SYMBOL_EQUAL);
			data.setRESOURCE_STATUS(ResData.RESSTATUSENABLE);
			
			data = dao.ImportResDataOfRow(data);
			
			//create resource and role relationship record
			int roleType = isLocal ? ResRole.RESROLETYPELOCAL : ResRole.RESROLETYPEPUBLIC;
			ResRole role = dao.GetDefaultRoleByDataSet(data.getDATA_SET(), roleType);
			if(role == null) {
				throw new Exception("default role isn't exist(dataSet: " + data.getDATA_SET() + "; roleType: " + roleType + ")");
			}
			ResRoleResource rrr = new ResRoleResource();
			rrr.setBUSINESS_ROLE(role.getBUSINESS_ROLE());
			rrr.setDELETE_STATUS(ResRoleResource.DELSTATUSNO);
			rrr.setRESOURCE_ID(data.getRESOURCE_ID());
			rrr.setRESOURCE_CLASS(ResRoleResource.RESCLASSDATA);
			dao.ResRoleResourceAdd(rrr);
		}
		catch(Exception e) {
			logger.info("[IRD]update row resource in the default role failed.[" + e.getMessage() + "]");
		}
		return;
	}
	
	private void updateClassifyResource(Sheet sheet) throws Exception {
		int rowCount = sheet.getPhysicalNumberOfRows(); //获取总行数
		Map<String, Integer> idx = new HashMap<String, Integer>();
		ResClassifyResource rc = null;
		ResClassifyResourceDAO dao = new ResClassifyResourceDAOImpl();
		
		//catch all dataset for update resource record
		ResDatasetDAO rdsdao = new ResDatasetDAOImpl();
		List<ResDataSet> rdss = rdsdao.QueryAllDataSet();
		Map<String, ResDataSet> rdssMap = new HashMap<String, ResDataSet>();
		for(ResDataSet rds : rdss) {
			rdssMap.put(rds.getDATA_SET(), rds);
		}

		//遍历每一行  
        for (int r = 0; r < rowCount; r++) {
        	Row row = sheet.getRow(r);
        	if(row == null) {
        		continue;
        	}
        	if( r > 0 ) {
        		rc = new ResClassifyResource();
        	}
        	int cellCount = row.getLastCellNum(); //获取总列数 
        	//遍历每一列  
            for (int c = 0; c < cellCount; c++) {
            	Cell cell = row.getCell(c);
            	String cellValue = getCellValue(cell);
            	
            	if(r == 0) {
            		if ( SHEET_CLASSIFY_RESOURCE_COL_ID.equals(cellValue) ) {
            			idx.put(SHEET_CLASSIFY_RESOURCE_COL_ID, c);
            		} else if ( SHEET_CLASSIFY_RESOURCE_COL_DATA_SET.equals(cellValue) ) {
            			idx.put(SHEET_CLASSIFY_RESOURCE_COL_DATA_SET, c);
            		} else if ( SHEET_CLASSIFY_RESOURCE_COL_SECTION_CLASS.equals(cellValue) ) {
            			idx.put(SHEET_CLASSIFY_RESOURCE_COL_SECTION_CLASS, c);
            		} else if ( SHEET_CLASSIFY_RESOURCE_COL_CLUE_SRC_SYS.equals(cellValue) ) {
            			idx.put(SHEET_CLASSIFY_RESOURCE_COL_CLUE_SRC_SYS, c);
            		} else if ( SHEET_CLASSIFY_RESOURCE_COL_SECTION_CLASS_NAME.equals(cellValue) ) {
            			idx.put(SHEET_CLASSIFY_RESOURCE_COL_SECTION_CLASS_NAME, c);
            		} else if ( SHEET_CLASSIFY_RESOURCE_COL_ELEMENT.equals(cellValue) ) {
            			idx.put(SHEET_CLASSIFY_RESOURCE_COL_ELEMENT, c);
            		} else if ( SHEET_CLASSIFY_RESOURCE_COL_RMK.equals(cellValue) ) {
            			idx.put(SHEET_CLASSIFY_RESOURCE_COL_RMK, c);
            		}
            	} else {
            		if(idx.size() == 0) {
            			throw new Exception("导入数据文件格式不正确!");
            		}
            		if( c == idx.get(SHEET_CLASSIFY_RESOURCE_COL_ID) ) {
            			//rr.setId(cellValue);
            		} else if ( c == idx.get(SHEET_CLASSIFY_RESOURCE_COL_DATA_SET) ) {
            			rc.setDATA_SET(cellValue);
            		} else if ( c== idx.get(SHEET_CLASSIFY_RESOURCE_COL_SECTION_CLASS) ) {
            			rc.setSECTION_CLASS(cellValue);
            		} else if ( c== idx.get(SHEET_CLASSIFY_RESOURCE_COL_CLUE_SRC_SYS) ) {
            			rc.setCLUE_SRC_SYS(cellValue);
            		} else if ( c== idx.get(SHEET_CLASSIFY_RESOURCE_COL_SECTION_CLASS_NAME) ) {
            			rc.setSectionClassName(cellValue);
            		} else if ( c== idx.get(SHEET_CLASSIFY_RESOURCE_COL_ELEMENT) ) {
            			rc.setELEMENT(cellValue);
            		} else if ( c== idx.get(SHEET_CLASSIFY_RESOURCE_COL_RMK) ) {
            			rc.setRmk(cellValue);
            		}
            	}
            }
            
            if( r > 0 ) {
            	if( rc.isValid() ) {
		            rc.setDELETE_STATUS(ResClassifyResource.DELSTATUSNO);
		            dao.ResClassifyResourceSave(rc);
		            
		            updateClassifyResourceInDefaultRoleOfDataSet(rc, rdssMap);
            	}
            	else {
            		logger.info("[IRD] import data of ClassifyResource format error[row number: " + r + "]");
            	}
            }
        }
	}
	
	private void updateClassifyResourceInDefaultRoleOfDataSet(ResClassifyResource rc, Map<String, ResDataSet> rdssMap) {
		// no local resource, put all resources into public role
		ResourceUploadDAO dao = new ResourceUploadDAOImpl();
		try{
			//create column resource record
			ResData data = new ResData();
			data.setDATA_SET(rc.getDATA_SET());
			
			ResDataSet rds = rdssMap.get(rc.getDATA_SET());
			if(rds == null) {
				throw new Exception("classifyResource's data_set is unknown:" + rc.getDATA_SET());
			}
			data.setDATASET_SENSITIVE_LEVEL(rds.getDATASET_SENSITIVE_LEVEL());
			data.setResource_type(ResData.RESTYPEPUBLIC);
			
			data.setDELETE_STATUS(ResData.DELSTATUSNO);
			data.setELEMENT(rc.getELEMENT());
			data.setSECTION_CLASS(rc.getSECTION_CLASS());
			data.setName("字段分类资源-" + rc.getSECTION_CLASS() + "[" + rc.getELEMENT() + "]");
			data.setRESOURCE_DESCRIBE(rc.getSectionClassName() + "---" + rc.getRmk());
			data.setRESOURCE_STATUS(ResData.RESSTATUSENABLE);
			
			data = dao.ImportResDataOfClassify(data);
			
			//create resource and role relationship record
			int roleType = ResRole.RESROLETYPEPUBLIC;
			ResRole role = dao.GetDefaultRoleByDataSet(data.getDATA_SET(), roleType);
			if(role == null) {
				throw new Exception("default role isn't exist(dataSet: " + data.getDATA_SET() + "; roleType: " + roleType + ")");
			}
			ResRoleResource rrr = new ResRoleResource();
			rrr.setBUSINESS_ROLE(role.getBUSINESS_ROLE());
			rrr.setDELETE_STATUS(ResRoleResource.DELSTATUSNO);
			rrr.setRESOURCE_ID(data.getRESOURCE_ID());
			rrr.setRESOURCE_CLASS(ResRoleResource.RESCLASSDATA);
			dao.ResRoleResourceAdd(rrr);
		}
		catch(Exception e) {
			logger.info("[IRD]update classify resource in the default role failed.[" + e.getMessage() + "]");
		}
		return;		
	}

	private void updateClassifyRelationResource(Sheet sheet) throws Exception {
		int rowCount = sheet.getPhysicalNumberOfRows(); //获取总行数
		Map<String, Integer> idx = new HashMap<String, Integer>();
		ResClassifyRelationResource rcr = null;
		ResClassifyRelationResourceDAO dao = new ResClassifyRelationResourceDAOImpl();

		//catch all dataset for update resource record
		ResDatasetDAO rdsdao = new ResDatasetDAOImpl();
		List<ResDataSet> rdss = rdsdao.QueryAllDataSet();
		Map<String, ResDataSet> rdssMap = new HashMap<String, ResDataSet>();
		for(ResDataSet rds : rdss) {
			rdssMap.put(rds.getDATA_SET(), rds);
		}
		
		//遍历每一行  
        for (int r = 0; r < rowCount; r++) {
        	Row row = sheet.getRow(r);
        	if(row == null) {
        		continue;
        	}
        	if( r > 0 ) {
        		rcr = new ResClassifyRelationResource();
        	}
        	int cellCount = row.getLastCellNum(); //获取总列数 
        	//遍历每一列  
            for (int c = 0; c < cellCount; c++) {
            	Cell cell = row.getCell(c);
            	String cellValue = getCellValue(cell);
            	if(r == 0) {
            		if ( SHEET_CLASSIFY_RELATION_RESOURCE_COL_ID.equals(cellValue) ) {
            			idx.put(SHEET_CLASSIFY_RELATION_RESOURCE_COL_ID, c);
            		} else if ( SHEET_CLASSIFY_RELATION_RESOURCE_COL_DATA_SET.equals(cellValue) ) {
            			idx.put(SHEET_CLASSIFY_RELATION_RESOURCE_COL_DATA_SET, c);
            		} else if ( SHEET_CLASSIFY_RELATION_RESOURCE_COL_SECTION_RELATIOIN_CLASS.equals(cellValue) ) {
            			idx.put(SHEET_CLASSIFY_RELATION_RESOURCE_COL_SECTION_RELATIOIN_CLASS, c);
            		} else if ( SHEET_CLASSIFY_RELATION_RESOURCE_COL_CLUE_SRC_SYS.equals(cellValue) ) {
            			idx.put(SHEET_CLASSIFY_RELATION_RESOURCE_COL_CLUE_SRC_SYS, c);
            		} else if ( SHEET_CLASSIFY_RELATION_RESOURCE_COL_SECTION_RELATIOIN_CLASS_NAME.equals(cellValue) ) {
            			idx.put(SHEET_CLASSIFY_RELATION_RESOURCE_COL_SECTION_RELATIOIN_CLASS_NAME, c);
            		}
            	} else {
            		if(idx.size() == 0) {
            			throw new Exception("导入数据文件格式不正确!");
            		}
            		if( c == idx.get(SHEET_CLASSIFY_RELATION_RESOURCE_COL_ID) ) {
            			//rr.setId(cellValue);
            		} else if ( c == idx.get(SHEET_CLASSIFY_RELATION_RESOURCE_COL_DATA_SET) ) {
            			rcr.setDATA_SET(cellValue);
            		} else if ( c == idx.get(SHEET_CLASSIFY_RELATION_RESOURCE_COL_SECTION_RELATIOIN_CLASS) ) {
            			rcr.setSECTION_RELATIOIN_CLASS(cellValue);
            		} else if ( c == idx.get(SHEET_CLASSIFY_RELATION_RESOURCE_COL_CLUE_SRC_SYS) ) {
            			rcr.setCLUE_SRC_SYS(cellValue);
            		} else if ( c == idx.get(SHEET_CLASSIFY_RELATION_RESOURCE_COL_SECTION_RELATIOIN_CLASS_NAME) ) {
            			rcr.setSECTION_RELATIOIN_CLASS_NAME(cellValue);
            		}
            	}
            }
            
            if( r > 0 ) {
            	if( rcr.isValid() ) {
		            rcr.setDELETE_STATUS(ResClassifyRelationResource.DELSTATUSNO);
		            dao.ResRelationClassifySave(rcr);
		            
		            updateClassifyResourceInDefaultRoleOfDataSet(rcr, rdssMap);
            	}
            	else {
            		logger.info("[IRD] import data of ClassifyRelationResource format error[row number: " + r + "]");
            	}
            }
        }
	}
	
	private void updateClassifyResourceInDefaultRoleOfDataSet(
			ResClassifyRelationResource rcr, Map<String, ResDataSet> rdssMap) {
		// no local resource, put all resources into public role
		ResourceUploadDAO dao = new ResourceUploadDAOImpl();
		try{
			//create column resource record
			ResData data = new ResData();
			data.setDATA_SET(rcr.getDATA_SET());
			
			ResDataSet rds = rdssMap.get(rcr.getDATA_SET());
			if(rds == null) {
				throw new Exception("classifyResource's data_set is unknown:" + rcr.getDATA_SET());
			}
			data.setDATASET_SENSITIVE_LEVEL(rds.getDATASET_SENSITIVE_LEVEL());
			data.setResource_type(ResData.RESTYPEPUBLIC);
			
			data.setDELETE_STATUS(ResData.DELSTATUSNO);
			data.setSECTION_RELATIOIN_CLASS(rcr.getSECTION_RELATIOIN_CLASS());
			data.setName("字段分类关系资源-" + rcr.getSECTION_RELATIOIN_CLASS() + "[" + rcr.getDATA_SET() + "]");
			data.setRESOURCE_DESCRIBE(rcr.getSECTION_RELATIOIN_CLASS_NAME());
			data.setRESOURCE_STATUS(ResData.RESSTATUSENABLE);
			
			data = dao.ImportResDataOfClassifyRelation(data);
			
			//create resource and role relationship record
			int roleType = ResRole.RESROLETYPEPUBLIC;
			ResRole role = dao.GetDefaultRoleByDataSet(data.getDATA_SET(), roleType);
			if(role == null) {
				throw new Exception("default role isn't exist(dataSet: " + data.getDATA_SET() + "; roleType: " + roleType + ")");
			}
			ResRoleResource rrr = new ResRoleResource();
			rrr.setBUSINESS_ROLE(role.getBUSINESS_ROLE());
			rrr.setDELETE_STATUS(ResRoleResource.DELSTATUSNO);
			rrr.setRESOURCE_ID(data.getRESOURCE_ID());
			rrr.setRESOURCE_CLASS(ResRoleResource.RESCLASSDATA);
			dao.ResRoleResourceAdd(rrr);
		}
		catch(Exception e) {
			logger.info("[IRD]update classify relation resource in the default role failed.[" + e.getMessage() + "]");
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
        cellValue = cellValue.trim();
        return cellValue;
	}
	
	private void AddResImportLog(String importType) throws Exception {
		String timenow = DateTimeUtil.GetCurrentTime();
		
		AuditResLog auditResLog = new AuditResLog();
		AuditLogDAO logdao = new AuditLogDAOImpl();
		AuditLogService als = new AuditLogService();
		
		auditResLog.setAdminId(als.adminLogin());
		auditResLog.setIpAddr("");
		auditResLog.setFlag(AuditResLog.LOGFLAGIMPORT);
		auditResLog.setResult(AuditResLog.LOGRESULTSUCCESS);
		auditResLog.setLATEST_MOD_TIME(timenow);
		auditResLog = logdao.AuditResLogAdd(auditResLog);
		
		String str="";
		AuditResLogDescribe auditResLogDescribe = new AuditResLogDescribe();
		AuditLogDescribeDao logDescdao = new AuditLogDescribeDAOImpl();
		auditResLogDescribe.setLogid(auditResLog.getId());
		str += importType;
		auditResLogDescribe.setDescrib(str);
		auditResLogDescribe.setLATEST_MOD_TIME(timenow);
		auditResLogDescribe = logDescdao.AuditResLogDescribeAdd(auditResLogDescribe);
	}
	
	public void UploadResourceRole(File inData) throws Exception {
		InputStream in=new FileInputStream(inData);
        Workbook workbook = WorkbookFactory.create(in);
        
        int sheetCount = workbook.getNumberOfSheets();  //Sheet的数量  
        for (int s = 0; s < sheetCount; s++) {
        	Sheet sheet = workbook.getSheetAt(s);
            String sheetName = sheet.getSheetName();
			if ( SHEET_ROLE_RESOURCE.equals(sheetName) ) {
	        	updateRoleResource(sheet);
	        }
        }
        
        in.close();
        
        updateRoleAndResourceRelatioinship();
        String importType = "角色导入;";
        AddRoleImportLog(importType);
	}
	
	private void updateRoleResource(Sheet sheet) throws Exception {
		int rowCount = sheet.getPhysicalNumberOfRows(); //获取总行数
		Map<String, Integer> idx = new HashMap<String, Integer>();

		ResRoleResourceImport rrri = null;
		ResourceDAO dao = new ResourceDAOImpl();
		dao.ResRoleResourceImportClear();
		
		String roleName = "";
		String roleId = "";
		String element = "";
		String elemnetValue = "";
		String dataset = "";
		String sectionClass = "";
		String [] dataset0SectionClasses = null; 
		//遍历每一行  
        for (int r = 0; r < rowCount; r++) {
        	Row row = sheet.getRow(r);
        	if(row == null) {
        		continue;
        	}
        	int cellCount = row.getLastCellNum(); //获取总列数 
        	//遍历每一列  
            for (int c = 0; c < cellCount; c++) {
            	Cell cell = row.getCell(c);
            	String cellValue = getCellValue(cell);

//            	if(cellValue.equals("111053000006")) {
//            		System.out.print(cellValue);
//            	}
//            	if(cellValue.equals("111053000007")) {
//            		System.out.print(cellValue);
//            	}
//            	if(r == 3585) {
//            		System.out.println(r);
//            	}
            	
            	if(r == 0) {
            		if ( SHEET_ROLE_RESOURCE_COL_ROLE_TYPE.equals(cellValue) ) {
            			idx.put(SHEET_ROLE_RESOURCE_COL_ROLE_TYPE, c);
            		} else if ( SHEET_ROLE_RESOURCE_COL_ROLE_CODE.equals(cellValue) ) {
            			idx.put(SHEET_ROLE_RESOURCE_COL_ROLE_CODE, c);
            		} else if ( SHEET_ROLE_RESOURCE_COL_ELEMENT.equals(cellValue) ) {
            			idx.put(SHEET_ROLE_RESOURCE_COL_ELEMENT, c);
            		} else if ( SHEET_ROLE_RESOURCE_COL_ELEMENT_VALUE.equals(cellValue) ) {
            			idx.put(SHEET_ROLE_RESOURCE_COL_ELEMENT_VALUE, c);
            		} else if ( SHEET_ROLE_RESOURCE_COL_DATASET.equals(cellValue) ) {
            			idx.put(SHEET_ROLE_RESOURCE_COL_DATASET, c);
            		} else if ( SHEET_ROLE_RESOURCE_COL_SECTION_CLASS.equals(cellValue) ) {
            			idx.put(SHEET_ROLE_RESOURCE_COL_SECTION_CLASS, c);
            		}            		
            	} else {
            		if(idx.size() == 0) {
            			throw new Exception("导入数据文件格式不正确!");
            		}
            		if( c == idx.get(SHEET_ROLE_RESOURCE_COL_ROLE_TYPE) ) {
            			if( cellValue != null && cellValue.length() > 0 ) {
            				roleName = cellValue;
            			}
            		} else if ( c == idx.get(SHEET_ROLE_RESOURCE_COL_ROLE_CODE) ) {
            			if( cellValue != null && cellValue.length() > 0 ) {
            				roleId = cellValue;
            			}
            		} else if ( c== idx.get(SHEET_ROLE_RESOURCE_COL_ELEMENT) ) {
            			if( cellValue != null && cellValue.length() > 0 ) {
            				element = cellValue;
            			}
            		} else if ( c== idx.get(SHEET_ROLE_RESOURCE_COL_ELEMENT_VALUE) ) {
            			if( cellValue != null && cellValue.length() > 0 ) {
            				elemnetValue = cellValue;
            				dataset0SectionClasses = null;
            			}
            		} else if ( c== idx.get(SHEET_ROLE_RESOURCE_COL_DATASET) ) {
            			if( cellValue != null && cellValue.length() > 0 ) {
                			dataset = cellValue;
            			}
            		} else if ( c== idx.get(SHEET_ROLE_RESOURCE_COL_SECTION_CLASS) ) {
            			if( cellValue != null && cellValue.length() > 0 ) {
                			sectionClass = cellValue;
            			}
            		}
            	}
            }
            
            if( r > 0 ) {
            	if( "WA_SOURCE_0000".equals(dataset) ) {
            		dataset0SectionClasses = sectionClass.split("、");
            		continue;
            	}
            	String [] elemnetValues = elemnetValue.split("、");
            	String [] sectionClasses = sectionClass.split("、");
            	Set<String> sectionClassesSet = new HashSet<String>();
            	for(int i = 0; i < sectionClasses.length; i++) {
            		sectionClassesSet.add(sectionClasses[i]);
            	}
            	if(dataset.startsWith("WA_SOURCE") && !"WA_SOURCE_0000".equals(dataset) && dataset0SectionClasses != null) {
            		for(int i = 0; i < dataset0SectionClasses.length; i++) {
                		sectionClassesSet.add(dataset0SectionClasses[i]);
                	}
            	}
            	
            	for(int i = 0; i < elemnetValues.length; i++) {
            		Iterator<String> it = sectionClassesSet.iterator();
					while(it.hasNext())
					{
						String sectionClassItem = it.next();
						rrri = new ResRoleResourceImport();
						rrri.setRoleName(roleName);
						rrri.setRoleId(roleId);
						rrri.setElement(element);
						rrri.setElemnetValue(elemnetValues[i]);
						rrri.setDataSet(dataset);
						rrri.setSectionClass(sectionClassItem);
		            	if( rrri.isValid() ) {
				            dao.ResRoleResourceImportAdd(rrri);
		            	}
		            	else {
		            		logger.info("[IRRARD] import data of Resource&Role format error[row number: " + r + "; roleName: " + roleName 
		            				+ "; roleId: " + roleId + "; element: " + element + "; element_value: " + elemnetValues[i] + "; dataset: " + dataset + "; classify: " + sectionClassItem + "]");
		            	}
					}
            	}                
            }
            //element = "";
            //elemnetValue = "";
            //dataset0SectionClasses = null;
            dataset = "";
            sectionClass = "";
        }
        return;
	}
	
	private void updateRoleAndResourceRelatioinship() throws Exception {
		ResourceDAO dao = new ResourceDAOImpl();
		
		int start = 0;
		int rows = 10000;
		List<ResRoleResourceImport> rrris = null;
		rrris= dao.GetResRoleResourceImport(start, rows);
		while(rrris != null && rrris.size() > 0) {
			for(ResRoleResourceImport rrri : rrris) {
				
				// update role
				createOrUpdateRole(rrri.getRoleName(), rrri.getRoleId());
				
				// update role and resource relationship
				updateRoleAndResourceRelationShip(rrri);

			}
			start += rrris.size();
			rrris= dao.GetResRoleResourceImport(start, rows);
		};
		return;
	}
	
	private void createOrUpdateRole(String roleName, String roleId) throws Exception {
		ResRole role = new ResRole();
		role.setBUSINESS_ROLE_NAME(roleName);
		role.setBUSINESS_ROLE(roleId);
		role.setBUSINESS_ROLE_TYPE(ResRole.RESROLETYPEPUBLIC);
		role.setDATA_VERSION(1);
		role.setDELETE_STATUS(ResRole.DELSTATUSNO);
		role.setROLE_DESC(roleName);
		ResourceDAO dao = new ResourceDAOImpl();
		dao.RoleImport(role);
		return;
	}
	
	private void updateRoleAndResourceRelationShip(ResRoleResourceImport rrri) {
		ImportLogDAO logdao = new ImportLogDAOImpl();
		ImportLog log = new ImportLog();
		log.setType(ImportLog.TYPE_RESOURCE_ROLE);
		
		//logger.info("start to import res&role_import_record of number: " + rrri.getId());
		ResourceUploadDAO rudao = new ResourceUploadDAOImpl();
		
		try{
			// 1. check section, get all need columns
			//List<ResData> GetDataResourcesOfClassify(String dataSet, String sectionClass, String element, int resourceType ) throws Exception
			List<ResData> classifyResources = rudao.GetDataResourcesOfClassify( rrri.getDataSet(), rrri.getSectionClass(), null, ResData.RESTYPEPUBLIC );
			if( classifyResources == null || classifyResources.size() == 0 ) {
				String message = "[IRRARD] no classify resource records found.[res&role_import_id: " + rrri.getId() +"; dataset: " + rrri.getDataSet() + "; classify: " + rrri.getSectionClass() + "]";
				//logger.info(message);
				log.setMessage(message);
				logdao.ImportLogAdd(log);
			}
			
			// 2. check column resources
			// 2.1 column in the classify query result
			List<ResData> columnResources = new ArrayList<ResData>();
			List<ResData> tempColumnResources = null;
			if( classifyResources != null ) {
				for( ResData res : classifyResources ) {
					tempColumnResources = rudao.GetDataResourcesOfColumn( res.getDATA_SET(), res.getELEMENT(), ResData.RESTYPEPUBLIC );
					if( tempColumnResources == null || tempColumnResources.size() == 0 ) {
						String message = "[IRRARD] column resource is't exist. [res&role_import_id: " + rrri.getId() +"; dataSet:" + res.getDATA_SET() + "; element:" + res.getELEMENT() + "; type: " + ResData.RESTYPEPUBLIC + "]";
						log.setMessage(message);
						logdao.ImportLogAdd(log);
						throw new Exception( message );
					}
					else {
						if( tempColumnResources.size() > 1 ) {
							String message = "[IRRARD] duplicate column resource found. [res&role_import_id: " + rrri.getId() +"; dataSet:" + res.getDATA_SET() + "; element:" + res.getELEMENT() + "; type: " + ResData.RESTYPEPUBLIC + "]";
							log.setMessage(message);
							logdao.ImportLogAdd(log);
							throw new Exception( message );
						}
						columnResources.addAll( tempColumnResources );
					}
				}
			}
			// 2.2 column in the Resource&Role import record
			tempColumnResources = rudao.GetDataResourcesOfColumn( rrri.getDataSet(), rrri.getElement(), ResData.RESTYPEPUBLIC );
			if( tempColumnResources == null || tempColumnResources.size() == 0 ) {
				String message = "[IRRARD] column resource is't exist. [res&role_import_id: " + rrri.getId() +"; dataSet:" + rrri.getDataSet() + "; element:" + rrri.getElement() + "; type: " + ResData.RESTYPEPUBLIC + "]";
				log.setMessage(message);
				logdao.ImportLogAdd(log);
				throw new Exception( message );
			}
			else {
				if( tempColumnResources.size() > 1 ) {
					String message = "[IRRARD] duplicate column resource found. [res&role_import_id: " + rrri.getId() +"; dataSet:" + rrri.getDataSet() + "; element:" + rrri.getElement() + "; type: " + ResData.RESTYPEPUBLIC + "]";
					log.setMessage(message);
					logdao.ImportLogAdd(log);
					throw new Exception( message );
				}
				columnResources.addAll( tempColumnResources );
			}
			
			// 3. check row resource
			ResData rowResource = null;
			List<ResData> rowResources = rudao.GetDataResourcesOfRow( rrri.getDataSet(), rrri.getElement(), rrri.getElemnetValue(), ResData.RESTYPEPUBLIC );
			if( rowResources == null || rowResources.size() == 0 ) {
				String message = "[IRRARD] row resource is't exist. [res&role_import_id: " + rrri.getId() +"; dataSet:" + rrri.getDataSet() + "; element:" + rrri.getElement() + "; value: " + rrri.getElemnetValue() + "; type: " + ResData.RESTYPEPUBLIC + "]";
				//logger.info( message );
				log.setMessage(message);
				logdao.ImportLogAdd(log);
			}
			else {
				if( rowResources.size() > 1 ) {
					String message = "[IRRARD] duplicate row resource found. [res&role_import_id: " + rrri.getId() +"; dataSet:" + rrri.getDataSet() + "; element:" + rrri.getElement() + "; value: " + rrri.getElemnetValue() + "; type: " + ResData.RESTYPEPUBLIC + "]";
					log.setMessage(message);
					logdao.ImportLogAdd(log);
					throw new Exception( message );
				}
				rowResource = rowResources.get( 0 );
			}
			
			// 4. check if row and classify are both non-exist
			if( rowResources == null && (classifyResources == null || classifyResources.size() == 0) ) {
				String message = "[IRRARD] import data of Resource&Role format error for rowResource and classifyResource are both null [res&role_import_id: " + rrri.getId() +"; roleName: " + rrri.getRoleName() 
        				+ "; roleId: " + rrri.getRoleId() + "; element: " + rrri.getElement() + "; element_value: " + rrri.getElemnetValue() + "; dataset: " 
						+ rrri.getDataSet() + "; classify: " + rrri.getSectionClass() + "]";
				log.setMessage(message);
				logdao.ImportLogAdd(log);
				throw new Exception(message);
			}
			
			// 5. import role and resources relation
			// 5.1 create column resources and role relation
			for( ResData colRes : columnResources ) {
				addPublicRoleAndDataResourceRelationship( rrri.getRoleId(), colRes.getRESOURCE_ID() );
			}
			
			// 5.2 create row resources and role relation
			addPublicRoleAndDataResourceRelationship( rrri.getRoleId(), rowResource.getRESOURCE_ID() );
			
			// 5.3 create classify resource and role relation
			if(classifyResources != null) {
				for( ResData classifyRes : classifyResources ) {
					addPublicRoleAndDataResourceRelationship( rrri.getRoleId(), classifyRes.getRESOURCE_ID() );
				}
			}
			
		}catch(Exception e) {
			logger.info( "[IRRARD]updateRoleAndResourceRelationShip failed : " + e.getMessage() + "]" );
		}
		return;
	}
	
	private void addPublicRoleAndDataResourceRelationship(String roleId,
			String resId) throws Exception {
		if(roleId == null || resId == null) {
			throw new Exception( "[IRRARD] resource id or role id cannot be null in create resource&role relation record operate. [roleId: " + roleId + "; resId: " + resId + "]" );
		}
		ResourceUploadDAO dao = new ResourceUploadDAOImpl();
		ResRoleResource rrr = new ResRoleResource();
		rrr.setBUSINESS_ROLE( roleId );
		rrr.setRESOURCE_ID( resId );
		rrr.setRESOURCE_CLASS( ResRoleResource.RESCLASSDATA );
		rrr.setDATA_VERSION( 1 );
		rrr.setDELETE_STATUS( ResRoleResource.DELSTATUSNO );
		dao.ResRoleResourceAdd( rrr );
		return;
	}
	
	private void AddRoleImportLog(String importType) throws Exception {
		String timenow = DateTimeUtil.GetCurrentTime();
		
		AuditRoleLog auditRoleLog = new AuditRoleLog();
		AuditLogDAO logdao = new AuditLogDAOImpl();
		AuditLogService als = new AuditLogService();
		
		auditRoleLog.setAdminId(als.adminLogin());
		auditRoleLog.setIpAddr("");
		auditRoleLog.setFlag(AuditRoleLog.LOGFLAGIMPORT);
		auditRoleLog.setResult(AuditRoleLog.LOGRESULTSUCCESS);
		auditRoleLog.setLATEST_MOD_TIME(timenow);
		auditRoleLog = logdao.AuditRoleLogAdd(auditRoleLog);
		
		String str="";
		AuditLogDescribeDao logDescdao = new AuditLogDescribeDAOImpl();
		
		AuditRoleLogDescribe auditRoleLogDescribe = new AuditRoleLogDescribe();
		auditRoleLogDescribe.setLogid(auditRoleLog.getId());
		str += importType;
		auditRoleLogDescribe.setDescrib(str);
		auditRoleLogDescribe.setLATEST_MOD_TIME(timenow);
		auditRoleLogDescribe = logDescdao.AuditRoleLogDescribeAdd(auditRoleLogDescribe);
	}
	
	public void UploadWZUser(File inData) throws Exception {
		InputStream in=new FileInputStream(inData);
        Workbook workbook = WorkbookFactory.create(in);
        
        int sheetCount = workbook.getNumberOfSheets();  //Sheet的数量  
        for (int s = 0; s < sheetCount; s++) {
        	Sheet sheet = workbook.getSheetAt(s);
            String sheetName = sheet.getSheetName();
			if ( SHEET_WZUSER.equals(sheetName) ) {
	        	updateWZUser(sheet);
	        }
        }
        
        in.close();
        
//        updateRoleAndResourceRelatioinship();
        String importType = "网综用户导入;";
        AddRoleImportLog(importType);
	}

	private void updateWZUser(Sheet sheet) throws Exception {
		int rowCount = sheet.getPhysicalNumberOfRows(); //获取总行数
		Map<String, Integer> idx = new HashMap<String, Integer>();

		//遍历每一行  
        for (int r = 0; r < rowCount; r++) {
        	Row row = sheet.getRow(r);
        	if(row == null) {
        		continue;
        	}
        	int cellCount = row.getLastCellNum(); //获取总列数 
        	String name = null;
        	String policeno = null;
        	String md5 = null;
        	String certificate = null;
        	//遍历每一列  
            for (int c = 0; c < cellCount; c++) {
            	Cell cell = row.getCell(c);
            	String cellValue = getCellValue(cell);

            	
            	if(r == 0) {
            		if ( SHEET_WZUSER_COL_NAME.equals(cellValue) ) {
            			idx.put(SHEET_WZUSER_COL_NAME, c);
            		} else if ( SHEET_WZUSER_COL_POLICENO.equals(cellValue) ) {
            			idx.put(SHEET_WZUSER_COL_POLICENO, c);
            		} else if ( SHEET_WZUSER_COL_CERTIFICATE_CODE_MD5.equals(cellValue) ) {
            			idx.put(SHEET_WZUSER_COL_CERTIFICATE_CODE_MD5, c);
            		} else if ( SHEET_WZUSER_COL_CERTIFICATE.equals(cellValue) ) {
            			idx.put(SHEET_WZUSER_COL_CERTIFICATE, c);
            		}         		
            	} else {
            		if(idx.size() == 0) {
            			throw new Exception("导入的网综用户数据文件格式不正确!");
            		}
            		if( c == idx.get(SHEET_WZUSER_COL_NAME) ) {
            			if( cellValue != null && cellValue.length() > 0 ) {
            				name = cellValue;
            			}
            		} else if ( c == idx.get(SHEET_WZUSER_COL_POLICENO) ) {
            			if( cellValue != null && cellValue.length() > 0 ) {
            				policeno = cellValue;
            			}
            		} else if ( c== idx.get(SHEET_WZUSER_COL_CERTIFICATE_CODE_MD5) ) {
            			if( cellValue != null && cellValue.length() > 0 ) {
            				md5 = cellValue;
            			}
            		} else if ( c== idx.get(SHEET_WZUSER_COL_CERTIFICATE) ) {
            			if( cellValue != null && cellValue.length() > 0 ) {
            				certificate = cellValue;
            			}
            		}
            	}
            }
            
            if( r > 0 ) {
            	UserDAO dao = new UserDAOImpl();
            	User pmsUser = dao.GetUserByCertificateCodeMd5(md5);
            	if( pmsUser == null ) {
            		logger.info("[IWZU]WZUser can not found in pms.[md5: " + md5 + "; certificate: " + certificate + "; name: " + name + "; policeno: " + policeno + "]");
            	}
            	else {
            		if( !pmsUser.getNAME().equals(name) ) {
            			logger.info("[IWZU]name doesn't match.[md5: " + md5 + "; certificate: " + certificate + "; name: " + name + "; policeno: " + policeno + "; pmsUserName: " + pmsUser.getNAME() + "]");
            		}
            		else if(!pmsUser.getCERTIFICATE_CODE_SUFFIX().equals(certificate.substring(certificate.length()-6))) {
            			logger.info("[IWZU]certificate code suffix doesn't match.[md5: " + md5 + "; certificate: " + certificate + "; name: " + name + "; policeno: " + policeno + "; pmsUserSuffix: " + pmsUser.getCERTIFICATE_CODE_SUFFIX() + "]");
            		}
            		else {
            			if(pmsUser.getPOLICE_NO() == null || pmsUser.getPOLICE_NO().length() == 0) {
            				if(policeno != null) {
            					pmsUser.setPOLICE_NO(policeno);
            					pmsUser.setDATA_VERSION(pmsUser.getDATA_VERSION() + 1);
            					String timenow = DateTimeUtil.GetCurrentTime();
            					pmsUser.setLATEST_MOD_TIME(timenow);
            					dao.UserAdd(pmsUser);
            				}
            			}
            			else if( !pmsUser.getPOLICE_NO().equals(policeno) ) {
            				logger.info("[IWZU]police no. doesn't match.[md5: " + md5 + "; certificate: " + certificate + "; name: " + name + "; policeno: " + policeno + "; pmsUserPoliceNo: " + pmsUser.getPOLICE_NO() + "]");
            			}
            		}
            	}
            }
        }
        return;
	}
	
	public void UploadWZRole(File inData) throws Exception {
		InputStream in=new FileInputStream(inData);
        Workbook workbook = WorkbookFactory.create(in);
        
        int sheetCount = workbook.getNumberOfSheets();  //Sheet的数量  
        for (int s = 0; s < sheetCount; s++) {
        	Sheet sheet = workbook.getSheetAt(s);
            String sheetName = sheet.getSheetName();
			if ( SHEET_WZROLE.equals(sheetName) ) {
	        	updateWZRole(sheet);
	        }
        }
        
        in.close();
        
        String importType = "网综角色导入;";
        AddRoleImportLog(importType);
	}
	
	private void updateWZRole(Sheet sheet) throws Exception {
		int rowCount = sheet.getPhysicalNumberOfRows(); //获取总行数
		Map<String, Integer> idx = new HashMap<String, Integer>();

		//遍历每一行  
        for (int r = 0; r < rowCount; r++) {
        	Row row = sheet.getRow(r);
        	if(row == null) {
        		continue;
        	}
        	int cellCount = row.getLastCellNum(); //获取总列数 
        	String name = null;
        	String desc = null;
        	String code = null;
        	//遍历每一列  
            for (int c = 0; c < cellCount; c++) {
            	Cell cell = row.getCell(c);
            	String cellValue = getCellValue(cell);

            	if(r == 0) {
            		if ( SHEET_WZROLE_COL_NAME.equals(cellValue) ) {
            			idx.put(SHEET_WZROLE_COL_NAME, c);
            		} else if ( SHEET_WZROLE_COL_DESC.equals(cellValue) ) {
            			idx.put(SHEET_WZROLE_COL_DESC, c);
            		} else if ( SHEET_WZROLE_COL_CODE.equals(cellValue) ) {
            			idx.put(SHEET_WZROLE_COL_CODE, c);
            		}  		
            	} else {
            		if(idx.size() == 0) {
            			throw new Exception("导入的网综角色数据文件格式不正确!");
            		}
            		if( c == idx.get(SHEET_WZROLE_COL_NAME) ) {
            			if( cellValue != null && cellValue.length() > 0 ) {
            				name = cellValue;
            			}
            		} else if ( c == idx.get(SHEET_WZROLE_COL_DESC) ) {
            			if( cellValue != null && cellValue.length() > 0 ) {
            				desc = cellValue;
            			}
            		} else if ( c== idx.get(SHEET_WZROLE_COL_CODE) ) {
            			if( cellValue != null && cellValue.length() > 0 ) {
            				code = cellValue;
            			}
            		}
            	}
            }
            
            if( r > 0 ) {
            	if(name == null || name.length() == 0 || code == null || code.length() == 0) {
            		logger.info("[IWZR]WZRole data format error.[row id: " + r + "]");
            	}
            	ResourceDAO dao = new ResourceDAOImpl();
            	List<ResRole> pmsRole = dao.GetRoleById(code);
            	if( pmsRole != null && pmsRole.size() > 0) {
            		logger.info("[IWZR]WZRole already exist.[name: " + name + "; desc: " + desc + "; business_role: " + code + "]");
            	}
            	else {
            		ResRole role = new ResRole();
            		role.setBUSINESS_ROLE_NAME(name);
            		role.setBUSINESS_ROLE(code);
            		role.setROLE_DESC(desc);
            		role.setBUSINESS_ROLE_TYPE(ResRole.RESROLETYPELOCAL);
            		role.setDATA_VERSION(1);
            		role.setDELETE_STATUS(ResRole.DELSTATUSNO);
            		role.setCLUE_SRC_SYS(ConfigHelper.getInstance().getRegion());
            		role.setLATEST_MOD_TIME(DateTimeUtil.GetCurrentTime());
            		dao.RoleAdd(role);
            	}
            }
        }
        return;
	}
	
	public void UploadWZRoleUserRelation(File inData) throws Exception {
		InputStream in=new FileInputStream(inData);
        Workbook workbook = WorkbookFactory.create(in);
        
        int sheetCount = workbook.getNumberOfSheets();  //Sheet的数量  
        for (int s = 0; s < sheetCount; s++) {
        	Sheet sheet = workbook.getSheetAt(s);
            String sheetName = sheet.getSheetName();
			if ( SHEET_WZROLEUSER.equals(sheetName) ) {
	        	updateWZRoleUserRelation(sheet);
	        }
        }
        
        in.close();
        
        String importType = "网综角色用户关系导入;";
        AddRoleImportLog(importType);
	}

	private void updateWZRoleUserRelation(Sheet sheet) throws Exception {
		int rowCount = sheet.getPhysicalNumberOfRows(); //获取总行数
		Map<String, Integer> idx = new HashMap<String, Integer>();

		//遍历每一行  
        for (int r = 0; r < rowCount; r++) {
        	Row row = sheet.getRow(r);
        	if(row == null) {
        		continue;
        	}
        	int cellCount = row.getLastCellNum(); //获取总列数 
        	String code = null;
        	String md5 = null;
        	//遍历每一列  
            for (int c = 0; c < cellCount; c++) {
            	Cell cell = row.getCell(c);
            	String cellValue = getCellValue(cell);

            	if(r == 0) {
            		if ( SHEET_WZROLEUSER_COL_CODE.equals(cellValue) ) {
            			idx.put(SHEET_WZROLEUSER_COL_CODE, c);
            		} else if ( SHEET_WZROLEUSER_COL_MD5.equals(cellValue) ) {
            			idx.put(SHEET_WZROLEUSER_COL_MD5, c);
            		}	
            	} else {
            		if(idx.size() == 0) {
            			throw new Exception("导入的网综角色用户关系数据文件格式不正确!");
            		}
            		if( c == idx.get(SHEET_WZROLEUSER_COL_CODE) ) {
            			if( cellValue != null && cellValue.length() > 0 ) {
            				code = cellValue;
            			}
            		} else if ( c == idx.get(SHEET_WZROLEUSER_COL_MD5) ) {
            			if( cellValue != null && cellValue.length() > 0 ) {
            				md5 = cellValue;
            			}
            		}
            	}
            }
            
            if( r > 0 ) {
            	if(code == null || code.length() == 0 || md5 == null || md5.length() == 0) {
            		logger.info("[IWZRUR]WZRoleUserRelation data format error.[row id: " + r + "]");
            	}
            	try{
            		UserDAO userDao = new UserDAOImpl();
        			User user = userDao.GetUserByCertificateCodeMd5(md5);
        			if(user == null) {
        				logger.info("[IWZRUR]User of WZRoleUserRelation doesn't exist.[business_role: " + code + "; certificate_code: " + md5 + "]");
        				continue;
        			}
	            	PrivilegeDAO dao = new PrivilegeDAOImpl();
	            	List<Privilege> pmsUserRole = dao.QueryPrivilegesByOwnerId(md5, Privilege.OWNERTYPEUSER);
	            	boolean isExist = false;
	            	for(Privilege pri : pmsUserRole) {
		            	if( pri.getRole_id() != null && pri.getRole_id().equals(code) ) {
		            		isExist = true;
		            		break;
		            	}
	            	}
	            	
	            	if(isExist) {
	            		logger.info("[IWZRUR]WZRoleUserRelation already exist.[business_role: " + code + "; certificate_code: " + md5 + "]");
	            	}
	            	else {
	            		String ownerIds = md5;
	            		int ownerType = Privilege.OWNERTYPEUSER;
	            		List<String> roleIds = new ArrayList<String>();
	            		roleIds.add(code);
	            		PrivilegeManageService pms = new PrivilegeManageService();
	            		pms.SavePrivilege(ownerIds, ownerType, roleIds);
	            	}
            	}
            	catch(Exception e) {
            		logger.info("[IWZRUR]WZRoleUserRelation save operation failed.[row id: " + r + "; message: " + e.getMessage() + "]");
            	}
            }
        }
        return;
	}
	
	public void UploadWZRoleFuncRelation(File inData) throws Exception {
		InputStream in=new FileInputStream(inData);
        Workbook workbook = WorkbookFactory.create(in);
        
        int sheetCount = workbook.getNumberOfSheets();  //Sheet的数量  
        for (int s = 0; s < sheetCount; s++) {
        	Sheet sheet = workbook.getSheetAt(s);
            String sheetName = sheet.getSheetName();
			if ( SHEET_WZROLEFUNC.equals(sheetName) ) {
	        	updateWZRoleFuncRelation(sheet);
	        }
        }
        
        in.close();
        
        String importType = "网综角色功能资源关系导入;";
        AddRoleImportLog(importType);
	}
	
	private void updateWZRoleFuncRelation(Sheet sheet) throws Exception {
		int rowCount = sheet.getPhysicalNumberOfRows(); //获取总行数
		Map<String, Integer> idx = new HashMap<String, Integer>();

		//遍历每一行  
        for (int r = 0; r < rowCount; r++) {
        	Row row = sheet.getRow(r);
        	if(row == null) {
        		continue;
        	}
        	int cellCount = row.getLastCellNum(); //获取总列数 
        	String resid = null;
        	String roleid = null;
        	//遍历每一列  
            for (int c = 0; c < cellCount; c++) {
            	Cell cell = row.getCell(c);
            	String cellValue = getCellValue(cell);

            	if(r == 0) {
            		if ( SHEET_WZROLEFUNC_COL_RESID.equals(cellValue) ) {
            			idx.put(SHEET_WZROLEFUNC_COL_RESID, c);
            		} else if ( SHEET_WZROLEFUNC_COL_ROLEID.equals(cellValue) ) {
            			idx.put(SHEET_WZROLEFUNC_COL_ROLEID, c);
            		}	
            	} else {
            		if(idx.size() == 0) {
            			throw new Exception("导入的网综角色功能资源关系数据文件格式不正确!");
            		}
            		if( c == idx.get(SHEET_WZROLEFUNC_COL_RESID) ) {
            			if( cellValue != null && cellValue.length() > 0 ) {
            				resid = cellValue;
            			}
            		} else if ( c == idx.get(SHEET_WZROLEFUNC_COL_ROLEID) ) {
            			if( cellValue != null && cellValue.length() > 0 ) {
            				roleid = cellValue;
            			}
            		}
            	}
            }
            
            if( r > 0 ) {
            	if(resid == null || resid.length() == 0 || roleid == null || roleid.length() == 0) {
            		logger.info("[IWZRFR]WZRoleFuncRelation data format error.[row id: " + r + "]");
            	}
            	try{
            		ResourceDAO resDao = new ResourceDAOImpl();
            		ResFeature funcRes = resDao.GetFeatureByResId(resid);
        			if(funcRes == null) {
        				logger.info("[IWZRFR]FeatureRes of WZRoleFuncRelation doesn't exist.[res_id: " + resid + "; business_role: " + roleid + "]");
        				continue;
        			}
        			List<ResRole> pmsRole = resDao.GetRoleById(roleid);
        			if(pmsRole == null || pmsRole.size() == 0) {
        				logger.info("[IWZRFR]Role of WZRoleFuncRelation doesn't exist.[res_id: " + resid + "; business_role: " + roleid + "]");
        				continue;
        			}
	            	ResRoleResource pmsRoleRes = resDao.GetRoleResourceByRoleidAndResid(roleid, resid, ResRoleResource.RESCLASSFEATURE);
	            	if(pmsRoleRes != null) {
	            		logger.info("[IWZRFR]WZRoleFuncRelation already exist.[res_id: " + resid + "; business_role: " + roleid + "]");
	            	}
	            	else {
	            		ResRoleResource roleRes = new ResRoleResource();
	            		roleRes.setBUSINESS_ROLE(roleid);
	            		roleRes.setRESOURCE_ID(resid);
	            		roleRes.setRESOURCE_CLASS(ResRoleResource.RESCLASSFEATURE);
	            		roleRes.setDATA_VERSION(1);
	            		roleRes.setDELETE_STATUS(ResRoleResource.DELSTATUSNO);
	            		roleRes.setLATEST_MOD_TIME(DateTimeUtil.GetCurrentTime());
	            		resDao.ResRoleResourceAdd(roleRes);
	            	}
            	}
            	catch(Exception e) {
            		logger.info("[IWZRFR]WZRoleFuncRelation save operation failed.[row id: " + r + "; message: " + e.getMessage() + "]");
            	}
            }
        }
        return;
	}
}
