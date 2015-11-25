package com.pms.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
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
import com.pms.dao.ResClassifyRelationDAO;
import com.pms.dao.ResColumnClassifyDAO;
import com.pms.dao.ResColumnClassifyRelationDAO;
import com.pms.dao.ResColumnDAO;
import com.pms.dao.ResColumnRelationDAO;
import com.pms.dao.ResDataDAO;
import com.pms.dao.ResDatasetDAO;
import com.pms.dao.ResDatasetSensitiveDAO;
import com.pms.dao.ResRowRelationDAO;
import com.pms.dao.ResValueDAO;
import com.pms.dao.ResValueSensitiveDAO;
import com.pms.dao.ResourceDAO;
import com.pms.dao.impl.AttributeDAOImpl;
import com.pms.dao.impl.ResClassifyRelationDAOImpl;
import com.pms.dao.impl.ResColumnClassifyDAOImpl;
import com.pms.dao.impl.ResColumnClassifyRelationDAOImpl;
import com.pms.dao.impl.ResColumnDAOImpl;
import com.pms.dao.impl.ResColumnRelationDAOImpl;
import com.pms.dao.impl.ResDataDAOImpl;
import com.pms.dao.impl.ResDatasetDAOImpl;
import com.pms.dao.impl.ResDatasetSensitiveDAOImpl;
import com.pms.dao.impl.ResRowRelationDAOImpl;
import com.pms.dao.impl.ResValueDAOImpl;
import com.pms.dao.impl.ResValueSensitiveDAOImpl;
import com.pms.dao.impl.ResourceDAOImpl;
import com.pms.model.AttrDefinition;
import com.pms.model.AttrDictionary;
import com.pms.model.ResColumn;
import com.pms.model.ResColumnClassify;
import com.pms.model.ResData;
import com.pms.model.ResDataSet;
import com.pms.model.ResDataSetSensitive;
import com.pms.model.ResDataTemplate;
import com.pms.model.ResFeature;
import com.pms.model.ResRelationClassify;
import com.pms.model.ResRelationColumn;
import com.pms.model.ResRelationColumnClassify;
import com.pms.model.ResRelationRow;
import com.pms.model.ResRole;
import com.pms.model.ResRoleResource;
import com.pms.model.ResRoleResourceImport;
import com.pms.model.ResValue;
import com.pms.model.ResValueSensitive;

public class ResourceUploadService {

	private static Log logger = LogFactory.getLog(ResourceUploadService.class);
	
	private final String SHEET_DATASET_SENSITIVE = "数据集敏感度字典";
	private final String SHEET_DATASET = "数据集定义";
	private final String SHEET_COLUMN_CLASSIFY = "字段分类定义";
	private final String SHEET_COLUMN = "字段";
	private final String SHEET_VALUE_SENSITIVE = "字段值敏感度字典";
	private final String SHEET_VALUE = "字段值字典";
	private final String SHEET_COLUMN_ClASSIFY_REALTION = "字段分类关系";
	private final String SHEET_ROW_RELATION = "数据集-字段-字段值";
	private final String SHEET_COLUMN_RELATION = "数据集-字段分类-字段";
	private final String SHEET_CLASSIFY_RELATION = "数据集-字段分类关系";
	
	private final String SHEET_DATASET_SENSITIVE_COL_DATASET_SENSITIVE_LEVEL = "敏感度编码";
	private final String SHEET_DATASET_SENSITIVE_COL_CLUE_SRC_SYS = "所属节点编码";
	private final String SHEET_DATASET_SENSITIVE_COL_DATASET_SENSITIVE_NAME = "敏感度名称";
	
	private final String SHEET_DATASET_COL_DATA_SET = "数据集编码";
	private final String SHEET_DATASET_COL_CLUE_SRC_SYS = "所属节点编码";
	private final String SHEET_DATASET_COL_DATASET_NAME = "数据集名称";
	private final String SHEET_DATASET_COL_DATASET_SENSITIVE_LEVEL = "数据集敏感度";
	
	private final String SHEET_COLUMN_CLASSIFY_COL_SECTION_CLASS = "字段分类编码";
	private final String SHEET_COLUMN_CLASSIFY_COL_CLUE_SRC_SYS = "所属节点编码";
	private final String SHEET_COLUMN_CLASSIFY_COL_CLASSIFY_NAME = "字段分类名称";
	
	private final String SHEET_COLUMN_COL_ELEMENT = "字段编码";
	private final String SHEET_COLUMN_COL_CLUE_SRC_SYS = "所属节点编码";
	private final String SHEET_COLUMN_COL_DATA_SET = "数据集编码";
	private final String SHEET_COLUMN_COL_COLUMU_CN = "字段中文名称";
	private final String SHEET_COLUMN_COL_COLUMN_NAME = "字段英文名称";
	private final String SHEET_COLUMN_COL_RMK = "字段描述";
	
	private final String SHEET_VALUE_SENSITIVE_COL_VALUE_SENSITIVE_ID = "敏感度编码";
	private final String SHEET_VALUE_SENSITIVE_COL_CLUE_SRC_SYS = "所属节点编码";
	private final String SHEET_VALUE_SENSITIVE_COL_VALUE_SENSITIVE_NAME = "敏感度名称";
	
	private final String SHEET_VALUE_COL_ELEMENT_VALUE = "字段值";
	private final String SHEET_VALUE_COL_CLUE_SRC_SYS = "所属节点编码";
	private final String SHEET_VALUE_COL_VALUE_NAME = "备注";
	private final String SHEET_VALUE_COL_VALUE_SENSITIVE_ID = "字段值敏感度编码";
	private final String SHEET_VALUE_COL_ELEMENT = "字段值所属的字段编码";
	
	private final String SHEET_COLUMN_ClASSIFY_REALTION_COL_SECTION_RELATIOIN_CLASS = "字段分类关系代码";
	private final String SHEET_COLUMN_ClASSIFY_REALTION_COL_CLUE_SRC_SYS = "所属节点编码";
	private final String SHEET_COLUMN_ClASSIFY_REALTION_COL_SRC_CLASS_CODE = "源字段分类编码";
	private final String SHEET_COLUMN_ClASSIFY_REALTION_COL_DST_CLASS_CODE = "目标字段分类编码";
	
	private final String SHEET_ROW_RELATION_COL_ID = "关系的唯一标识";
	private final String SHEET_ROW_RELATION_COL_DATA_SET = "数据集编码";
	private final String SHEET_ROW_RELATION_COL_ELEMENT = "字段编码";
	private final String SHEET_ROW_RELATION_COL_CLUE_SRC_SYS = "所属节点编码";
	private final String SHEET_ROW_RELATION_COL_ELEMENT_VALUE = "字段值";
	
	private final String SHEET_COLUMN_RELATION_COL_ID = "关系唯一标识";
	private final String SHEET_COLUMN_RELATION_COL_DATA_SET = "数据集编码";
	private final String SHEET_COLUMN_RELATION_COL_SECTION_CLASS = "字段分类编码";
	private final String SHEET_COLUMN_RELATION_COL_CLUE_SRC_SYS = "所属节点编码";
	private final String SHEET_COLUMN_RELATION_COL_ELEMENT = "字段";

	private final String SHEET_CLASSIFY_RELATION_COL_ID = "关系唯一标识";
	private final String SHEET_CLASSIFY_RELATION_COL_DATA_SET = "数据集编码";
	private final String SHEET_CLASSIFY_RELATION_COL_SECTION_RELATIOIN_CLASS = "字段分类关系编码";
	private final String SHEET_CLASSIFY_RELATION_COL_CLUE_SRC_SYS = "所属节点编码";
	private final String SHEET_CLASSIFY_RELATION_COL_SECTION_RELATIOIN_CLASS_NAME = "字段分类关系";
	
	
	private final String SHEET_ROLE_RESOURCE = "关系对照";
	private final String SHEET_ROLE_RESOURCE_COL_ROLE_TYPE = "角色类型";
	private final String SHEET_ROLE_RESOURCE_COL_ROLE_CODE = "角色代码";
	private final String SHEET_ROLE_RESOURCE_COL_ELEMENT_B050016 = "数据来源";
	private final String SHEET_ROLE_RESOURCE_COL_ELEMENT_B050016_VALUE = "数据来源编码";
	private final String SHEET_ROLE_RESOURCE_COL_DATASET = "协议编码";
	private final String SHEET_ROLE_RESOURCE_COL_SECTION_CLASS = "字段分类编码";
	
	private final String SHEET_REATURE_RESOURCE = "功能资源";
	private final String SHEET_REATURE_RESOURCE_COL_SYSTEM_TYPE = "系统类型(J020012)";
	private final String SHEET_REATURE_RESOURCE_COL_RESOURCE_ID = "资源唯一标识(J030006)";
	private final String SHEET_REATURE_RESOURCE_COL_APP_ID = "所属业务系ID(J020013)";
	private final String SHEET_REATURE_RESOURCE_COL_NAME = "名称(J030007)";
	private final String SHEET_REATURE_RESOURCE_COL_PARENT = "父资源唯一标识(J030008)";
	private final String SHEET_REATURE_RESOURCE_COL_URL = "URL(G010002)";
	private final String SHEET_REATURE_RESOURCE_COL_ICON = "图标路径(J030009)";
	private final String SHEET_REATURE_RESOURCE_COL_RESOURCE_STATUS = "资源状态(J030010)";
	private final String SHEET_REATURE_RESOURCE_COL_ORDER = "顺序(J030011)";
	private final String SHEET_REATURE_RESOURCE_COL_DESCRIBE = "资源描述(J030012)";
	private final String SHEET_REATURE_RESOURCE_COL_REMARK = "备注(J030013)";
	private final String SHEET_REATURE_RESOURCE_COL_RESOURCE_TYPE = "资源分类(J030035)";
	
	public void UploadResourceFeature(File inData) throws Exception {
		InputStream in=new FileInputStream(inData);
        Workbook workbook = WorkbookFactory.create(in);
        
        int sheetCount = workbook.getNumberOfSheets();  //Sheet的数量  
        for (int s = 0; s < sheetCount; s++) {
        	Sheet sheet = workbook.getSheetAt(s);
            String sheetName = sheet.getSheetName();
			if ( SHEET_REATURE_RESOURCE.equals(sheetName) ) {
	        	updateFeatureResource(sheet);
	        }
        }
        
        in.close();
        return;
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
	}
	
	public void UploadResourceData(File inData) throws Exception {
		InputStream in=new FileInputStream(inData);
        Workbook workbook = WorkbookFactory.create(in);
        
        int sheetCount = workbook.getNumberOfSheets();  //Sheet的数量  
        //deal with seven elements and relationships
        for (int s = 0; s < sheetCount; s++) {
            Sheet sheet = workbook.getSheetAt(s);
            String sheetName = sheet.getSheetName();
            if ( SHEET_DATASET_SENSITIVE.equals(sheetName) ) {
//            	updateDatasetSensitive(sheet);
            } else if ( SHEET_DATASET.equals(sheetName) ) {
//            	updateDataset(sheet);
            } else if ( SHEET_COLUMN_CLASSIFY.equals(sheetName) ) {
//            	updateColumnClassify(sheet);
            } else if ( SHEET_COLUMN.equals(sheetName) ) {
//            	updateColumn(sheet);
            } else if ( SHEET_VALUE_SENSITIVE.equals(sheetName) ) {
//            	updateValueSensitive(sheet);
            } else if ( SHEET_VALUE.equals(sheetName) ) {
//            	updateValue(sheet);
            } else if ( SHEET_COLUMN_ClASSIFY_REALTION.equals(sheetName) ) {
//            	updateColumnClassifyRelation(sheet);
            } else if ( SHEET_ROW_RELATION.equals(sheetName) ) {
//            	updateRowRelation(sheet);
            } else if ( SHEET_COLUMN_RELATION.equals(sheetName) ) {
//            	updateColumnRelation(sheet);
            } else if ( SHEET_CLASSIFY_RELATION.equals(sheetName) ) {
            	updateClassifyRelation(sheet);
            }
        }
        
        in.close();
        
        //update resource;
        updateResourceData();
        
        return;
	}
	
	private void updateFeatureResource(Sheet sheet) throws Exception {
		int rowCount = sheet.getPhysicalNumberOfRows(); //获取总行数
		Map<String, Integer> idx = new HashMap<String, Integer>();
		ResFeature feature = null;
		ResourceDAO dao = new ResourceDAOImpl();
		//遍历每一行  
        for (int r = 0; r < rowCount; r++) {
        	Row row = sheet.getRow(r);
        	if(row == null) {
        		continue;
        	}
        	if( r > 0 ) {
        		feature = new ResFeature();
        	}
        	int cellCount = row.getPhysicalNumberOfCells(); //获取总列数 
        	//遍历每一列  
            for (int c = 0; c < cellCount; c++) {
            	Cell cell = row.getCell(c);
            	String cellValue = getCellValue(cell);

            	if(r == 0) {
            		if ( SHEET_REATURE_RESOURCE_COL_SYSTEM_TYPE.equals(cellValue) ) {
            			idx.put(SHEET_REATURE_RESOURCE_COL_SYSTEM_TYPE, c);
            		} else if ( SHEET_REATURE_RESOURCE_COL_RESOURCE_ID.equals(cellValue) ) {
            			idx.put(SHEET_REATURE_RESOURCE_COL_RESOURCE_ID, c);
            		} else if ( SHEET_REATURE_RESOURCE_COL_APP_ID.equals(cellValue) ) {
            			idx.put(SHEET_REATURE_RESOURCE_COL_APP_ID, c);
            		} else if ( SHEET_REATURE_RESOURCE_COL_NAME.equals(cellValue) ) {
            			idx.put(SHEET_REATURE_RESOURCE_COL_NAME, c);
            		} else if ( SHEET_REATURE_RESOURCE_COL_PARENT.equals(cellValue) ) {
            			idx.put(SHEET_REATURE_RESOURCE_COL_PARENT, c);
            		} else if ( SHEET_REATURE_RESOURCE_COL_URL.equals(cellValue) ) {
            			idx.put(SHEET_REATURE_RESOURCE_COL_URL, c);
            		} else if ( SHEET_REATURE_RESOURCE_COL_ICON.equals(cellValue) ) {
            			idx.put(SHEET_REATURE_RESOURCE_COL_ICON, c);
            		} else if ( SHEET_REATURE_RESOURCE_COL_RESOURCE_STATUS.equals(cellValue) ) {
            			idx.put(SHEET_REATURE_RESOURCE_COL_RESOURCE_STATUS, c);
            		} else if ( SHEET_REATURE_RESOURCE_COL_ORDER.equals(cellValue) ) {
            			idx.put(SHEET_REATURE_RESOURCE_COL_ORDER, c);
            		} else if ( SHEET_REATURE_RESOURCE_COL_DESCRIBE.equals(cellValue) ) {
            			idx.put(SHEET_REATURE_RESOURCE_COL_DESCRIBE, c);
            		} else if ( SHEET_REATURE_RESOURCE_COL_REMARK.equals(cellValue) ) {
            			idx.put(SHEET_REATURE_RESOURCE_COL_REMARK, c);
            		} else if ( SHEET_REATURE_RESOURCE_COL_RESOURCE_TYPE.equals(cellValue) ) {
            			idx.put(SHEET_REATURE_RESOURCE_COL_RESOURCE_TYPE, c);
            		}
            	} else {
            		if(idx.size() == 0) {
            			String warnMsg = "[IRF]导入数据文件格式不正确!";
            			logger.warn(warnMsg);
            			throw new Exception(warnMsg);
            		}
            		if( c == idx.get(SHEET_REATURE_RESOURCE_COL_SYSTEM_TYPE) ) {
            			feature.setSYSTEM_TYPE(cellValue);
            		} else if ( c == idx.get(SHEET_REATURE_RESOURCE_COL_RESOURCE_ID) ) {
            			feature.setRESOURCE_ID(cellValue);
            		} else if ( c== idx.get(SHEET_REATURE_RESOURCE_COL_APP_ID) ) {
            			feature.setAPP_ID(cellValue);
            		} else if ( c== idx.get(SHEET_REATURE_RESOURCE_COL_NAME) ) {
            			feature.setRESOUCE_NAME(cellValue);
            		} else if ( c== idx.get(SHEET_REATURE_RESOURCE_COL_PARENT) ) {
            			feature.setPARENT_RESOURCE(cellValue);
            		} else if ( c== idx.get(SHEET_REATURE_RESOURCE_COL_URL) ) {
            			feature.setURL(cellValue);
            		} else if ( c== idx.get(SHEET_REATURE_RESOURCE_COL_ICON) ) {
            			feature.setRESOURCE_ICON_PATH(cellValue);
            		} else if ( c== idx.get(SHEET_REATURE_RESOURCE_COL_RESOURCE_STATUS) ) {
            			feature.setRESOURCE_STATUS(Integer.parseInt(cellValue));
            		} else if ( c== idx.get(SHEET_REATURE_RESOURCE_COL_ORDER) ) {
            			feature.setRESOURCE_ORDER(cellValue);
            		} else if ( c== idx.get(SHEET_REATURE_RESOURCE_COL_DESCRIBE) ) {
            			feature.setRESOURCE_DESCRIBE(cellValue);
            		} else if ( c== idx.get(SHEET_REATURE_RESOURCE_COL_REMARK) ) {
            			feature.setRMK(cellValue);
            		} else if ( c== idx.get(SHEET_REATURE_RESOURCE_COL_RESOURCE_TYPE) ) {
            			feature.setFUN_RESOURCE_TYPE(Integer.parseInt(cellValue));
            		}
            	}
            }
            
            if( r > 0 ) {
            	if(feature.isValid()) {
		            feature.setDELETE_STATUS(ResDataSetSensitive.DELSTATUSNO);
		            dao.FeatureAdd(feature);
            	}
            }
        }
		
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
        	int cellCount = row.getPhysicalNumberOfCells(); //获取总列数 
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
        	Row row = sheet.getRow(r);
        	if(row == null) {
        		continue;
        	}
        	if( r > 0 ) {
        		ds = new ResDataSet();
        	}
        	int cellCount = row.getPhysicalNumberOfCells(); //获取总列数 
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
            		}
            		
            	}
            }
            
            if( r > 0 ) {
            	if( ds.isValid() ) {
		            ds.setDELETE_STATUS(ResDataSet.DELSTATUSNO);
		            dao.ResDataSetSave(ds);
		            
		            attrDef = adao.GetAttrDefinitionByCode(AttrDefinition.ATTR_RESOURCEDATA_DATA_SET_CODE);
					if(attrDef != null) {
						AttrDictionary attrDict = new AttrDictionary();
						attrDict.setAttrid(attrDef.getId());
						attrDict.setValue(ds.getDATASET_NAME());
						attrDict.setCode(ds.getDATA_SET());
						adao.AttrDictionaryAdd(attrDict);
					}
            	}
            }
        }
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
        	int cellCount = row.getPhysicalNumberOfCells(); //获取总列数 
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
            }
        }
	}
	
	private void updateColumn(Sheet sheet) throws Exception {
		int rowCount = sheet.getPhysicalNumberOfRows(); //获取总行数
		Map<String, Integer> idx = new HashMap<String, Integer>();
		ResColumn col = null;
		ResColumnDAO dao = new ResColumnDAOImpl();
		
		AttributeDAO adao = new AttributeDAOImpl();
		AttrDefinition attrDef = null;
		
		//遍历每一行  
        for (int r = 0; r < rowCount; r++) {
        	Row row = sheet.getRow(r);
        	if(row == null) {
        		continue;
        	}
        	if( r > 0 ) {
        		col = new ResColumn();
        	}
        	int cellCount = row.getPhysicalNumberOfCells(); //获取总列数 
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
            		}             		
            	}
            }
            
            if( r > 0 ) {
            	if( col.isValid() ) {
		            col.setDELETE_STATUS(ResColumn.DELSTATUSNO);
		            dao.ResColumnSave(col);
		            
		            attrDef = adao.GetAttrDefinitionByCode(AttrDefinition.ATTR_RESOURCEDATA_ELEMENT_CODE);
					if(attrDef != null) {
						AttrDictionary attrDict = new AttrDictionary();
						attrDict.setAttrid(attrDef.getId());
						attrDict.setValue(col.getCOLUMU_CN());
						attrDict.setCode(col.getELEMENT());
						adao.AttrDictionaryAdd(attrDict);
					}
            	}
            }
        }
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
        	int cellCount = row.getPhysicalNumberOfCells(); //获取总列数 
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
        	Row row = sheet.getRow(r);
        	if(row == null) {
        		continue;
        	}
        	if( r > 0 ) {
        		val = new ResValue();
        	}
        	int cellCount = row.getPhysicalNumberOfCells(); //获取总列数 
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
            		}
            	} else {
            		if(idx.size() == 0) {
            			throw new Exception("导入数据文件格式不正确!");
            		}
            		if( c == idx.get(SHEET_VALUE_COL_ELEMENT_VALUE) ) {
            			val.setELEMENT_VALUE(cellValue);
            		} else if ( c == idx.get(SHEET_VALUE_COL_CLUE_SRC_SYS) ) {
            			val.setCLUE_SRC_SYS(cellValue);
            		} else if ( c== idx.get(SHEET_VALUE_COL_VALUE_NAME) ) {
            			val.setVALUE_NAME(cellValue);
            		} else if ( c== idx.get(SHEET_VALUE_COL_VALUE_SENSITIVE_ID) ) {
            			val.setVALUE_SENSITIVE_ID(cellValue);
            		} else if ( c== idx.get(SHEET_VALUE_COL_ELEMENT) ) {
            			val.setELEMENT(cellValue);
            		}           		
            	}
            }
            
            if( r > 0 ) {
            	if( val.isValid() ) {
		            val.setDELETE_STATUS(ResValue.DELSTATUSNO);
		            dao.ResValueSave(val);
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
        	int cellCount = row.getPhysicalNumberOfCells(); //获取总列数 
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

	private void updateRowRelation(Sheet sheet) throws Exception {
		int rowCount = sheet.getPhysicalNumberOfRows(); //获取总行数
		Map<String, Integer> idx = new HashMap<String, Integer>();
		ResRelationRow rr = null;
		ResRowRelationDAO dao = new ResRowRelationDAOImpl();
		dao.ResRowRelationImportClear();
		
		//遍历每一行  
        for (int r = 0; r < rowCount; r++) {
        	Row row = sheet.getRow(r);
        	if(row == null) {
        		continue;
        	}
        	if( r > 0 ) {
        		rr = new ResRelationRow();
        	}
        	int cellCount = row.getPhysicalNumberOfCells(); //获取总列数 
        	//遍历每一列  
            for (int c = 0; c < cellCount; c++) {
            	Cell cell = row.getCell(c);
            	String cellValue = getCellValue(cell);
            	
            	if(r == 0) {
            		if ( SHEET_ROW_RELATION_COL_ID.equals(cellValue) ) {
            			idx.put(SHEET_ROW_RELATION_COL_ID, c);
            		} else if ( SHEET_ROW_RELATION_COL_DATA_SET.equals(cellValue) ) {
            			idx.put(SHEET_ROW_RELATION_COL_DATA_SET, c);
            		} else if ( SHEET_ROW_RELATION_COL_ELEMENT.equals(cellValue) ) {
            			idx.put(SHEET_ROW_RELATION_COL_ELEMENT, c);
            		} else if ( SHEET_ROW_RELATION_COL_CLUE_SRC_SYS.equals(cellValue) ) {
            			idx.put(SHEET_ROW_RELATION_COL_CLUE_SRC_SYS, c);
            		} else if ( SHEET_ROW_RELATION_COL_ELEMENT_VALUE.equals(cellValue) ) {
            			idx.put(SHEET_ROW_RELATION_COL_ELEMENT_VALUE, c);
            		}
            	} else {
            		if(idx.size() == 0) {
            			throw new Exception("导入数据文件格式不正确!");
            		}
            		if( c == idx.get(SHEET_ROW_RELATION_COL_ID) ) {
            			//rr.setId(cellValue);
            		} else if ( c == idx.get(SHEET_ROW_RELATION_COL_DATA_SET) ) {
            			rr.setDATA_SET(cellValue);
            		} else if ( c== idx.get(SHEET_ROW_RELATION_COL_ELEMENT) ) {
            			rr.setELEMENT(cellValue);
            		} else if ( c== idx.get(SHEET_ROW_RELATION_COL_CLUE_SRC_SYS) ) {
            			rr.setCLUE_SRC_SYS(cellValue);
            		} else if ( c== idx.get(SHEET_ROW_RELATION_COL_ELEMENT_VALUE) ) {
            			rr.setELEMENT_VALUE(cellValue);
            		}        		
            	}
            }
            
            if( r > 0 ) {
            	if( rr.isValid() ) {
		            rr.setDELETE_STATUS(ResRelationRow.DELSTATUSNO);
		            dao.ResRelationRowSave(rr);
            	}
            }
        }
	}
	
	private void updateColumnRelation(Sheet sheet) throws Exception {
		int rowCount = sheet.getPhysicalNumberOfRows(); //获取总行数
		Map<String, Integer> idx = new HashMap<String, Integer>();
		ResRelationColumn rc = null;
		ResColumnRelationDAO dao = new ResColumnRelationDAOImpl();
		dao.ResColumnRelationImportClear();
		//遍历每一行  
        for (int r = 0; r < rowCount; r++) {
        	Row row = sheet.getRow(r);
        	if(row == null) {
        		continue;
        	}
        	if( r > 0 ) {
        		rc = new ResRelationColumn();
        	}
        	int cellCount = row.getPhysicalNumberOfCells(); //获取总列数 
        	//遍历每一列  
            for (int c = 0; c < cellCount; c++) {
            	Cell cell = row.getCell(c);
            	String cellValue = getCellValue(cell);
            	
            	if(r == 0) {
            		if ( SHEET_COLUMN_RELATION_COL_ID.equals(cellValue) ) {
            			idx.put(SHEET_COLUMN_RELATION_COL_ID, c);
            		} else if ( SHEET_COLUMN_RELATION_COL_DATA_SET.equals(cellValue) ) {
            			idx.put(SHEET_COLUMN_RELATION_COL_DATA_SET, c);
            		} else if ( SHEET_COLUMN_RELATION_COL_SECTION_CLASS.equals(cellValue) ) {
            			idx.put(SHEET_COLUMN_RELATION_COL_SECTION_CLASS, c);
            		} else if ( SHEET_COLUMN_RELATION_COL_CLUE_SRC_SYS.equals(cellValue) ) {
            			idx.put(SHEET_COLUMN_RELATION_COL_CLUE_SRC_SYS, c);
            		} else if ( SHEET_COLUMN_RELATION_COL_ELEMENT.equals(cellValue) ) {
            			idx.put(SHEET_COLUMN_RELATION_COL_ELEMENT, c);
            		}
            	} else {
            		if(idx.size() == 0) {
            			throw new Exception("导入数据文件格式不正确!");
            		}
            		if( c == idx.get(SHEET_COLUMN_RELATION_COL_ID) ) {
            			//rr.setId(cellValue);
            		} else if ( c == idx.get(SHEET_COLUMN_RELATION_COL_DATA_SET) ) {
            			rc.setDATA_SET(cellValue);
            		} else if ( c== idx.get(SHEET_COLUMN_RELATION_COL_SECTION_CLASS) ) {
            			rc.setSECTION_CLASS(cellValue);
            		} else if ( c== idx.get(SHEET_COLUMN_RELATION_COL_CLUE_SRC_SYS) ) {
            			rc.setCLUE_SRC_SYS(cellValue);
            		} else if ( c== idx.get(SHEET_COLUMN_RELATION_COL_ELEMENT) ) {
            			rc.setELEMENT(cellValue);
            		}        		
            	}
            }
            
            if( r > 0 ) {
            	if( rc.isValid() ) {
		            rc.setDELETE_STATUS(ResRelationColumn.DELSTATUSNO);
		            dao.ResRelationColumnSave(rc);
            	}
            }
        }
	}
	
	private void updateClassifyRelation(Sheet sheet) throws Exception {
		int rowCount = sheet.getPhysicalNumberOfRows(); //获取总行数
		Map<String, Integer> idx = new HashMap<String, Integer>();
		ResRelationClassify rc = null;
		ResClassifyRelationDAO dao = new ResClassifyRelationDAOImpl();
		dao.ResClassifyRelationImportClear();
		
		//遍历每一行  
        for (int r = 0; r < rowCount; r++) {
        	Row row = sheet.getRow(r);
        	if(row == null) {
        		continue;
        	}
        	if( r > 0 ) {
        		rc = new ResRelationClassify();
        	}
        	int cellCount = row.getPhysicalNumberOfCells(); //获取总列数 
        	//遍历每一列  
            for (int c = 0; c < cellCount; c++) {
            	Cell cell = row.getCell(c);
            	String cellValue = getCellValue(cell);
            	if(r == 0) {
            		if ( SHEET_CLASSIFY_RELATION_COL_ID.equals(cellValue) ) {
            			idx.put(SHEET_CLASSIFY_RELATION_COL_ID, c);
            		} else if ( SHEET_CLASSIFY_RELATION_COL_DATA_SET.equals(cellValue) ) {
            			idx.put(SHEET_CLASSIFY_RELATION_COL_DATA_SET, c);
            		} else if ( SHEET_CLASSIFY_RELATION_COL_SECTION_RELATIOIN_CLASS.equals(cellValue) ) {
            			idx.put(SHEET_CLASSIFY_RELATION_COL_SECTION_RELATIOIN_CLASS, c);
            		} else if ( SHEET_CLASSIFY_RELATION_COL_CLUE_SRC_SYS.equals(cellValue) ) {
            			idx.put(SHEET_CLASSIFY_RELATION_COL_CLUE_SRC_SYS, c);
            		} else if ( SHEET_CLASSIFY_RELATION_COL_SECTION_RELATIOIN_CLASS_NAME.equals(cellValue) ) {
            			idx.put(SHEET_CLASSIFY_RELATION_COL_SECTION_RELATIOIN_CLASS_NAME, c);
            		}
            	} else {
            		if(idx.size() == 0) {
            			throw new Exception("导入数据文件格式不正确!");
            		}
            		if( c == idx.get(SHEET_CLASSIFY_RELATION_COL_ID) ) {
            			//rr.setId(cellValue);
            		} else if ( c == idx.get(SHEET_CLASSIFY_RELATION_COL_DATA_SET) ) {
            			rc.setDATA_SET(cellValue);
            		} else if ( c == idx.get(SHEET_CLASSIFY_RELATION_COL_SECTION_RELATIOIN_CLASS) ) {
            			rc.setSECTION_RELATIOIN_CLASS(cellValue);
            		} else if ( c == idx.get(SHEET_CLASSIFY_RELATION_COL_CLUE_SRC_SYS) ) {
            			rc.setCLUE_SRC_SYS(cellValue);
            		} else if ( c == idx.get(SHEET_CLASSIFY_RELATION_COL_SECTION_RELATIOIN_CLASS_NAME) ) {
            			rc.setSECTION_RELATIOIN_CLASS_NAME(cellValue);
            		}
            	}
            }
            
            if( r > 0 ) {
            	if( rc.isValid() ) {
		            rc.setDELETE_STATUS(ResRelationClassify.DELSTATUSNO);
		            dao.ResRelationClassifySave(rc);
            	}
            }
        }
	}
	
	private void updateRoleResource(Sheet sheet) throws Exception {
		int rowCount = sheet.getPhysicalNumberOfRows(); //获取总行数
		Map<String, Integer> idx = new HashMap<String, Integer>();

		ResRoleResourceImport rrri = null;
		ResourceDAO dao = new ResourceDAOImpl();
		dao.ResRoleResourceImportClear();
		
		String roleName = "";
		String roleId = "";
		String element = "B050016";
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
        	
        	int cellCount = row.getPhysicalNumberOfCells(); //获取总列数 
        	//遍历每一列  
            for (int c = 0; c < cellCount; c++) {
            	Cell cell = row.getCell(c);
            	String cellValue = getCellValue(cell);

//            	if(cellValue.equals("111053000004")) {
//            		System.out.print(cellValue);
//            	}
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
            		} else if ( SHEET_ROLE_RESOURCE_COL_ELEMENT_B050016.equals(cellValue) ) {
            			idx.put(SHEET_ROLE_RESOURCE_COL_ELEMENT_B050016, c);
            		} else if ( SHEET_ROLE_RESOURCE_COL_ELEMENT_B050016_VALUE.equals(cellValue) ) {
            			idx.put(SHEET_ROLE_RESOURCE_COL_ELEMENT_B050016_VALUE, c);
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
            		} else if ( c== idx.get(SHEET_ROLE_RESOURCE_COL_ELEMENT_B050016) ) {
            			if( cellValue != null && cellValue.length() > 0 ) {
            				//save element name, so nothing to do
            			}
            		} else if ( c== idx.get(SHEET_ROLE_RESOURCE_COL_ELEMENT_B050016_VALUE) ) {
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
					}
            	}
            }
        }
        return;
	}
	
	private void updateRoleAndResourceRelatioinship() throws Exception {
		ResourceDAO dao = new ResourceDAOImpl();
		// clear old resource and role relationship
		dao.ClearPublicRoleAndRelationship();
		
		int start = 300000;
		int rows = 10000;
		List<ResRoleResourceImport> rrris = null;
		rrris= dao.GetResRoleResourceImport(start, rows);
		while(rrris != null && rrris.size() > 0) {
			for(int i = 0; i < rrris.size(); i++) {
				ResRoleResourceImport rrri = rrris.get(i);
				
				// update role
				createOrUpdateRole(rrri.getRoleName(), rrri.getRoleId());
				
				// update role and resource relationship
				//updateRoleAndResourceRelationshipOfColumn(rrri.getRoleId(), rrri.getDataSet(), rrri.getElement());
				
				updateRoleAndResourceRelationshipOfRelationRow(rrri.getRoleId(), rrri.getDataSet(), rrri.getElement(), rrri.getElemnetValue());
				
				updateRoleAndResourceRelationshipOfRelationColumn(rrri.getRoleId(), rrri.getDataSet(), rrri.getSectionClass(), rrri.getElement());
			}
			start += rrris.size();
			rrris= dao.GetResRoleResourceImport(start, rows);
		};
		return;
	}
	
	private void updateRoleAndResourceRelationshipOfRelationColumn(
			String roleId, String dataSet, String sectionClass, String element) throws Exception {
		ResourceDAO dao = new ResourceDAOImpl();
		ResData resource = dao.GetDataByRelationColumn(dataSet, sectionClass, element);
		
		if(resource == null) {
			ResDataTemplate resTemp = dao.GetDataTemplateByRelationColumn(dataSet, sectionClass, element);
			if(resTemp == null) {
				logger.warn("[IRRARD]no record found when search data resource template of column relation by condition of dataset:'" + dataSet + "', element:'" + element + "', sectionClass:'" + sectionClass + "'");
				return;
			}
			
			resource = SaveResDataTemplate2ResData(resTemp);
		}
		
		addPublicRoleAndDataResourceRelationship(roleId, resource.getRESOURCE_ID());
		return;
	}

	private void updateRoleAndResourceRelationshipOfRelationRow(String roleId,
			String dataSet, String element, String elemnetValue) throws Exception {
		ResourceDAO dao = new ResourceDAOImpl();
		ResData resource = dao.GetDataByRelationRow(dataSet, element, elemnetValue);
		
		if(resource == null) {
			ResDataTemplate resTemp = dao.GetDataTemplateByRelationRow(dataSet, element, elemnetValue);
			if(resTemp == null) {
				logger.warn("[IRRARD]no record found when search data resource template of row relation by condition of dataset:'" + dataSet + "', element:'" + element + "', elementValue:'" + elemnetValue + "'");
				return;
			}
			
			resource = SaveResDataTemplate2ResData(resTemp);
		}
		
		addPublicRoleAndDataResourceRelationship(roleId, resource.getRESOURCE_ID());
		
		return;
	}

	private ResData SaveResDataTemplate2ResData(ResDataTemplate resTemp) throws Exception {
		ResourceDAO dao = new ResourceDAOImpl();
		ResData res = new ResData();
		res.setDATA_SET(resTemp.getDATA_SET());
		res.setDATA_VERSION(1);
		res.setDATASET_SENSITIVE_LEVEL(resTemp.getDATASET_SENSITIVE_LEVEL());
		res.setDELETE_STATUS(ResData.DELSTATUSNO);
		res.setELEMENT(resTemp.getELEMENT());
		res.setELEMENT_VALUE(resTemp.getELEMENT_VALUE());
		res.setName(resTemp.getName());
		res.setOPERATE_SYMBOL(resTemp.getOPERATE_SYMBOL());
		res.setRESOURCE_DESCRIBE(resTemp.getRESOURCE_DESCRIBE());
		res.setRESOURCE_ID(resTemp.getRESOURCE_ID());
		res.setRESOURCE_STATUS(resTemp.getRESOURCE_STATUS());
		res.setResource_type(resTemp.getResource_type());
		res.setRMK(res.getRMK());
		res.setSECTION_CLASS(resTemp.getSECTION_CLASS());
		res.setSECTION_RELATIOIN_CLASS(resTemp.getSECTION_RELATIOIN_CLASS());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.SIMPLIFIED_CHINESE);
		String timenow = sdf.format(new Date());
		res.setLATEST_MOD_TIME(timenow);
		res = dao.DataAdd(res);
		return res;
	}

//	private void updateRoleAndResourceRelationshipOfColumn(String roleId,
//			String dataSet, String element) throws Exception {
//		ResourceDAO dao = new ResourceDAOImpl();
//		ResData resource = dao.GetDataByColumn(dataSet, element);
//		if(resource != null) {
//			addPublicRoleAndDataResourceRelationship(roleId, resource.getRESOURCE_ID());
//		}
//		return;
//	}

	private void addPublicRoleAndDataResourceRelationship(String roleId,
			String resId) throws Exception {
		ResourceDAO dao = new ResourceDAOImpl();
		ResRoleResource rrr = new ResRoleResource();
		rrr.setBUSINESS_ROLE(roleId);
		rrr.setRESOURCE_ID(resId);
		rrr.setRESOURCE_CLASS(ResRoleResource.RESCLASSDATA);
		rrr.setDATA_VERSION(1);
		rrr.setDELETE_STATUS(ResRoleResource.DELSTATUSNO);
		dao.ResRoleResourceAdd(rrr);
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
	
	private void updateResourceData() throws Exception {
		//0. perpare dataset 
		ResDatasetDAO rdsdao = new ResDatasetDAOImpl();
		List<ResDataSet> rdss = rdsdao.QueryAllDataSet();

		Map<String, ResDataSet> rdsMap = new HashMap<String, ResDataSet>();
		for(int i = 0; i<rdss.size(); i++) {
			rdsMap.put(rdss.get(i).getDATA_SET(), rdss.get(i));
		}

		//1. switch resourcetemplate status to delete
		ResDataDAO rdd = new ResDataDAOImpl();
		rdd.UpdateResDataTemplateStatus(ResDataTemplate.DELSTATUSYES);
		
		//2. update resourcetemplate record
		//2.1 updateRowRelation
		ResRowRelationDAO rrrdao = new ResRowRelationDAOImpl();
		List<ResRelationRow> rrrs = rrrdao.QueryAllResRelationRow();
		for(int i = 0; i<rrrs.size(); i++) {
			updateResourceOfRelationRow( rrrs.get(i), rdsMap );
		}
		
		//2.2 updateColumnRelation
		ResColumnRelationDAO rcrdao = new ResColumnRelationDAOImpl();
		List<ResRelationColumn> rrcs = rcrdao.QueryAllResRelationColumn();
		for(int i = 0; i<rrcs.size(); i++) {
			updateResourceOfRelationColumn( rrcs.get(i), rdsMap );
		}
		
		//2.3 updateClassifyRelation
		ResClassifyRelationDAO rclassrdao = new ResClassifyRelationDAOImpl();
		List<ResRelationClassify> rrclasss = rclassrdao.QueryAllResRelationClassify();
		for(int i = 0; i<rrclasss.size(); i++) {
			updateResourceOfRelationClassify( rrclasss.get(i), rdsMap );
		}

		//3 update resource record's status according to deleted records in resourcetemplate
		rdd.DeleteResDataByDeletedRecordsInResDataTemplate();
	}
	
//	private void updateResource() throws Exception {
//		//perpare dataset 
//		ResDatasetDAO rdsdao = new ResDatasetDAOImpl();
//		List<ResDataSet> rdss = rdsdao.QueryAllDataSet();
//		
//		Map<String, ResDataSet> rdsMap = new HashMap<String, ResDataSet>();
//		for(int i = 0; i<rdss.size(); i++) {
//			rdsMap.put(rdss.get(i).getDATA_SET(), rdss.get(i));
//		}
//		
//		//updateColumn
//		ResColumnDAO rcdao = new ResColumnDAOImpl();
//		List<ResColumn> rcs = rcdao.QueryAllColumn();
//		
//		for(int i = 0; i<rcs.size(); i++) {
//			updateResourceOfColumn( rcs.get(i), rdsMap );
//		}
//		
//		//updateColumnRelation
//		ResColumnRelationDAO rcrdao = new ResColumnRelationDAOImpl();
//		List<ResRelationColumn> rrcs = rcrdao.QueryAllResRelationColumn();
//		
//		for(int i = 0; i<rrcs.size(); i++) {
//			updateResourceOfRelationColumn( rrcs.get(i), rdsMap );
//		}
//		
//		//updateRowRelation
//		ResRowRelationDAO rrrdao = new ResRowRelationDAOImpl();
//		List<ResRelationRow> rrrs = rrrdao.QueryAllResRelationRow();
//		
//		for(int i = 0; i<rrrs.size(); i++) {
//			updateResourceOfRelationRow( rrrs.get(i), rdsMap );
//		}
//		
//		//updateClassifyRelation
//		ResClassifyRelationDAO rclassrdao = new ResClassifyRelationDAOImpl();
//		List<ResRelationClassify> rrclasss = rclassrdao.QueryAllResRelationClassify();
//		
//		for(int i = 0; i<rrclasss.size(); i++) {
//			updateResourceOfRelationClassify( rrclasss.get(i), rdsMap );
//		}
//	}
	
//	private void updateResourceOfColumn( ResColumn rc, Map<String, ResDataSet> rdsMap ) throws Exception {
//		ResDataDAO rddao = new ResDataDAOImpl();
//		ResData rd = new ResData();
//		rd.setRESOURCE_STATUS(ResData.RESSTATUSENABLE);
//		rd.setRESOURCE_DESCRIBE("字段数据资源");
//		rd.setDATASET_SENSITIVE_LEVEL( rdsMap.get(rc.getDATA_SET()).getDATASET_SENSITIVE_LEVEL() );
//		rd.setDATA_SET(rc.getDATA_SET());
//		rd.setELEMENT(rc.getELEMENT());
//		rd.setDELETE_STATUS(ResData.DELSTATUSNO);
//		rd.setResource_type(ResData.RESTYPEPUBLIC);
//		rd.setName(rc.getCOLUMU_CN());
//		rddao.ResDataOfColumnSave(rd, rc.getCLUE_SRC_SYS());
//	}
	
	private void updateResourceOfRelationColumn( ResRelationColumn rrc, Map<String, ResDataSet> rdsMap ) throws Exception {
		ResDataDAO rddao = new ResDataDAOImpl();
		ResColumnDAO rcdao = new ResColumnDAOImpl();
		ResColumn rc = rcdao.QueryColumnByElement(rrc.getDATA_SET(), rrc.getELEMENT());
		if( rc == null ) {
			logger.warn("[IRD]column record not found when updata resource of column relation by condition of dataset:'" + rrc.getDATA_SET() + "', element:'" + rrc.getELEMENT() + "' ");
			return;
		}
		ResDataTemplate rdt = new ResDataTemplate();
		rdt.setRESOURCE_STATUS(ResData.RESSTATUSENABLE);
		rdt.setDELETE_STATUS(ResData.DELSTATUSNO);
		rdt.setResource_type(ResData.RESTYPEPUBLIC);
		rdt.setRESOURCE_DESCRIBE("数据集-字段分类-字段数据资源");
		rdt.setDATA_SET(rrc.getDATA_SET());
		rdt.setELEMENT(rrc.getELEMENT());
		rdt.setSECTION_CLASS(rrc.getSECTION_CLASS());
		rdt.setName("列控资源-" + rc.getELEMENT() + "(" + rc.getCOLUMU_CN() + ")");
		
		rddao.ImportResDataOfRelationColumn(rdt);
	}

	private void updateResourceOfRelationRow( ResRelationRow rrr, Map<String, ResDataSet> rdsMap ) throws Exception {
		ResDataDAO rddao = new ResDataDAOImpl();
		ResColumnDAO rcdao = new ResColumnDAOImpl();
		ResColumn rc = rcdao.QueryColumnByElement(rrr.getDATA_SET(), rrr.getELEMENT());
		if( rc == null ) {
			logger.warn("[IRD]column record not found when updata resource of row relation by condition of dataset:'" + rrr.getDATA_SET() + "', element:'" + rrr.getELEMENT() + "' ");
			return;
		}
		ResDataTemplate rdt = new ResDataTemplate();
		rdt.setRESOURCE_STATUS(ResData.RESSTATUSENABLE);
		rdt.setDELETE_STATUS(ResData.DELSTATUSNO);
		rdt.setResource_type(ResData.RESTYPEPUBLIC);
		rdt.setRESOURCE_DESCRIBE("数据集-字段-字段值数据资源");
		rdt.setDATASET_SENSITIVE_LEVEL( rdsMap.get(rrr.getDATA_SET()).getDATASET_SENSITIVE_LEVEL() );
		rdt.setDATA_SET(rrr.getDATA_SET());
		rdt.setELEMENT(rrr.getELEMENT());
		rdt.setELEMENT_VALUE(rrr.getELEMENT_VALUE());
		rdt.setOPERATE_SYMBOL(ResData.RES_OPERATE_SYMBOL_EQUAL);
		rdt.setName("行控资源-" + rc.getELEMENT() + "(" + rc.getCOLUMU_CN() + ":" + rrr.getELEMENT_VALUE() + ")");
		
		rddao.ImportResDataOfRelationRow(rdt);
	}
		
	private void updateResourceOfRelationClassify( ResRelationClassify rrc, Map<String, ResDataSet> rdsMap ) throws Exception {
		ResDataDAO rddao = new ResDataDAOImpl();
		ResDataTemplate rdt = new ResDataTemplate();
		rdt.setRESOURCE_STATUS(ResData.RESSTATUSENABLE);
		rdt.setDELETE_STATUS(ResData.DELSTATUSNO);
		rdt.setResource_type(ResData.RESTYPEPUBLIC);
		rdt.setRESOURCE_DESCRIBE("数据集-字段分类关系数据资源");
		rdt.setDATASET_SENSITIVE_LEVEL( rdsMap.get(rrc.getDATA_SET()).getDATASET_SENSITIVE_LEVEL() );
		rdt.setDATA_SET(rrc.getDATA_SET());
		rdt.setSECTION_RELATIOIN_CLASS(rrc.getSECTION_RELATIOIN_CLASS());
		rdt.setName("列分类关系资源-" + rrc.getSECTION_RELATIOIN_CLASS());
		
		rddao.ImportResDataOfRelationClassify(rdt);
	}
	
//	private void sheetProcess(Sheet sheet){
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		int rowCount = sheet.getPhysicalNumberOfRows(); //获取总行数  
//        //遍历每一行  
//        for (int r = 0; r < rowCount; r++) {  
//            Row row = sheet.getRow(r);
//            int cellCount = row.getPhysicalNumberOfCells(); //获取总列数  
//            //遍历每一列  
//            for (int c = 0; c < cellCount; c++) {  
//                Cell cell = row.getCell(c);  
//                int cellType = cell.getCellType();  
//                String cellValue = null;  
//                switch(cellType) {  
//                    case Cell.CELL_TYPE_STRING: //文本  
//                        cellValue = cell.getStringCellValue();  
//                        break;  
//                    case Cell.CELL_TYPE_NUMERIC: //数字、日期  
//                        if(DateUtil.isCellDateFormatted(cell)) {  
//                            cellValue = sdf.format(cell.getDateCellValue()); //日期型  
//                        }  
//                        else {  
//                            cellValue = String.valueOf(cell.getNumericCellValue()); //数字  
//                        }  
//                        break;  
//                    case Cell.CELL_TYPE_BOOLEAN: //布尔型  
//                        cellValue = String.valueOf(cell.getBooleanCellValue());  
//                        break;  
//                    case Cell.CELL_TYPE_BLANK: //空白  
//                        cellValue = cell.getStringCellValue();  
//                        break;  
//                    case Cell.CELL_TYPE_ERROR: //错误  
//                        cellValue = "错误";  
//                        break;  
//                    case Cell.CELL_TYPE_FORMULA: //公式  
//                        cellValue = "错误";  
//                        break;  
//                    default:  
//                        cellValue = "错误";  
//                }  
//                System.out.print(cellValue + "\t");
//                    
//            }  
//            System.out.println(); 
//        }
//	}
	
}
