package com.pms.service;

import java.util.List;

import com.pms.dao.ResClassifyRelationDAO;
import com.pms.dao.ResColumnClassifyDAO;
import com.pms.dao.ResColumnDAO;
import com.pms.dao.ResDatasetDAO;
import com.pms.dao.ResDatasetSensitiveDAO;
import com.pms.dao.ResValueDAO;
import com.pms.dao.impl.ResClassifyRelationDAOImpl;
import com.pms.dao.impl.ResColumnClassifyDAOImpl;
import com.pms.dao.impl.ResColumnDAOImpl;
import com.pms.dao.impl.ResDatasetDAOImpl;
import com.pms.dao.impl.ResDatasetSensitiveDAOImpl;
import com.pms.dao.impl.ResValueDAOImpl;
import com.pms.model.ResColumn;
import com.pms.model.ResColumnClassify;
import com.pms.model.ResDataSet;
import com.pms.model.ResDataSetSensitive;
import com.pms.model.ResRelationClassify;
import com.pms.model.ResValue;

public class DataResourceService {
	
	public List<ResDataSet> QueryRowDataSet() throws Exception {
		ResDatasetDAO dao = new ResDatasetDAOImpl();
		List<ResDataSet> res = dao.QueryRowDataSet();
		return res;
	}
	
	public List<ResDataSetSensitive> QueryDataSetSensitive(String dataSet) throws Exception {
		ResDatasetSensitiveDAO dao = new ResDatasetSensitiveDAOImpl();
		List<ResDataSetSensitive> res = dao.QueryDataSetSensitive(dataSet);
		return res;
	}
	
	public List<ResColumn> QueryRowColumn(String dataSet) throws Exception {
		ResColumnDAO dao = new ResColumnDAOImpl();
		List<ResColumn> res = dao.QueryRowColumn(dataSet);
		return res;
	}
	
	public List<ResValue> QueryRowResValue(String dataSet, String element) throws Exception {
		ResValueDAO dao = new ResValueDAOImpl();
		List<ResValue> res = dao.QueryRowResValue(dataSet, element);
		return res;
	}
	
	public List<ResDataSet> QueryColumnDataSet() throws Exception {
		ResDatasetDAO dao = new ResDatasetDAOImpl();
		List<ResDataSet> res = dao.QueryColumnDataSet();
		return res;
	}
	
	public List<ResColumnClassify> QueryColumnClassify(String dataSet) throws Exception {
		ResColumnClassifyDAO dao = new ResColumnClassifyDAOImpl();
		List<ResColumnClassify> res = dao.QueryColumnClassify(dataSet);
		return res;
	}
	
	public List<ResColumn> QueryColumnColumn(String dataSet, String sectionClass) throws Exception {
		ResColumnDAO dao = new ResColumnDAOImpl();
		List<ResColumn> res = dao.QueryColumnColumn(dataSet, sectionClass);
		return res;
	}
	
	public List<ResDataSet> QueryClassifyDataSet() throws Exception {
		ResDatasetDAO dao = new ResDatasetDAOImpl();
		List<ResDataSet> res = dao.QueryClassifyDataSet();
		return res;
	}
	
	public List<ResRelationClassify> QueryResRelationClassify(String dataSet) throws Exception {
		ResClassifyRelationDAO dao = new ResClassifyRelationDAOImpl();
		List<ResRelationClassify> res = dao.QueryResRelationClassify(dataSet);
		return res;
	}
}
