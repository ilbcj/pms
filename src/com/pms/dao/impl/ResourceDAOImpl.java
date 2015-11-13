package com.pms.dao.impl;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.pms.dao.ResourceDAO;
import com.pms.model.HibernateUtil;
import com.pms.model.ResData;
import com.pms.model.ResDataOrg;
import com.pms.model.ResDataTemplate;
import com.pms.model.ResFeature;
import com.pms.model.ResRole;
import com.pms.model.ResRoleOrg;
import com.pms.model.ResRoleResource;
import com.pms.model.ResRoleResourceImport;


public class ResourceDAOImpl implements ResourceDAO {
	@Override
	public ResFeature FeatureAdd(ResFeature feature) throws Exception {
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		try
		{
			feature = (ResFeature) session.merge(feature);
			if(feature.getRESOURCE_ID() == null || feature.getRESOURCE_ID().isEmpty()) {
				feature.setRESOURCE_ID(new Integer(feature.getId()).toString());
				feature = (ResFeature) session.merge(feature);
			}
			tx.commit();
		}
		catch(ConstraintViolationException cne){
			tx.rollback();
			System.out.println(cne.getSQLException().getMessage());
			throw new Exception("存在重名功能资源。");
		}
		catch(org.hibernate.exception.SQLGrammarException e)
		{
			tx.rollback();
			System.out.println(e.getSQLException().getMessage());
			throw e.getSQLException();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return feature;
	}
	
	@Override
	public ResRoleOrg ResRoleOrgAdd(ResRoleOrg resRoleOrg) throws Exception {
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		try
		{
			resRoleOrg = (ResRoleOrg) session.merge(resRoleOrg);
			tx.commit();
		}
		catch(ConstraintViolationException cne){
			tx.rollback();
			System.out.println(cne.getSQLException().getMessage());
			throw new Exception("存在重名数据资源。");
		}
		catch(org.hibernate.exception.SQLGrammarException e)
		{
			tx.rollback();
			System.out.println(e.getSQLException().getMessage());
			throw e.getSQLException();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return resRoleOrg;
	}
	
	@Override
	public ResDataOrg ResDataOrgAdd(ResDataOrg resDataOrg) throws Exception {
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		try
		{
			resDataOrg = (ResDataOrg) session.merge(resDataOrg);
			tx.commit();
		}
		catch(ConstraintViolationException cne){
			tx.rollback();
			System.out.println(cne.getSQLException().getMessage());
			throw new Exception("存在重名数据资源。");
		}
		catch(org.hibernate.exception.SQLGrammarException e)
		{
			tx.rollback();
			System.out.println(e.getSQLException().getMessage());
			throw e.getSQLException();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return resDataOrg;
	}
	
	@Override
	public void FeatureDel(ResFeature res) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		try
		{
			session.delete(res);
			tx.commit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ResFeature> GetFeatureNodeByParentId(String pid, ResFeature criteria) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<ResFeature> rs = null;
		String sqlString = "select * from WA_AUTHORITY_FUNC_RESOURCE where PARENT_RESOURCE = :PARENT_RESOURCE ";
		if( criteria != null ) {
			if(criteria.getRESOUCE_NAME() != null && criteria.getRESOUCE_NAME().length() > 0) {
				sqlString += " and RESOUCE_NAME like :RESOUCE_NAME ";
			}
			if(criteria.getRESOURCE_ID() != null && criteria.getRESOURCE_ID().length() > 0) {
				sqlString += " and RESOURCE_ID = :RESOURCE_ID ";
			}
			if(criteria.getSYSTEM_TYPE() != null && criteria.getSYSTEM_TYPE().length() > 0) {
				sqlString += " and SYSTEM_TYPE like :SYSTEM_TYPE ";
			}
			if(criteria.getAPP_ID() != null && criteria.getAPP_ID().length() > 0) {
				sqlString += " and APP_ID = :APP_ID ";
			}
		}
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResFeature.class);
			q.setString("PARENT_RESOURCE", pid);
			if( criteria != null ) {
				if(criteria.getRESOUCE_NAME() != null && criteria.getRESOUCE_NAME().length() > 0) {
					q.setString( "RESOUCE_NAME", "%" + criteria.getRESOUCE_NAME() + "%" );
				}
				if(criteria.getRESOURCE_ID() != null && criteria.getRESOURCE_ID().length() > 0) {
					q.setString( "RESOURCE_ID",  criteria.getRESOURCE_ID());
				}
				if(criteria.getSYSTEM_TYPE() != null && criteria.getSYSTEM_TYPE().length() > 0) {
					q.setString( "SYSTEM_TYPE",  criteria.getSYSTEM_TYPE());
				}
				if(criteria.getAPP_ID() != null && criteria.getAPP_ID().length() > 0) {
					q.setString( "APP_ID",  criteria.getAPP_ID());
				}
			}
			rs = q.list();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return rs;
	}
	
	@Override
	public boolean FeatureHasChild(String pid) throws Exception {
		int rs = GetFeatureNodeCountByParentId(pid);
		return rs > 0;
	}
	
	@Override
	public int GetFeatureNodeCountByParentId(String pid) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		int rs;
		String sqlString = "select count(*) from WA_AUTHORITY_FUNC_RESOURCE where PARENT_RESOURCE = :PARENT_RESOURCE ";
		try {
			Query q = session.createSQLQuery(sqlString);
			q.setString("PARENT_RESOURCE", pid);
			rs = ((BigInteger)q.uniqueResult()).intValue();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return rs;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ResFeature> GetAllFeatures(ResFeature criteria) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<ResFeature> rs = null;
		String sqlString = "select * from WA_AUTHORITY_FUNC_RESOURCE where DELETE_STATUS =:DELETE_STATUS";
		if( criteria != null ) {
			if(criteria.getRESOUCE_NAME() != null && criteria.getRESOUCE_NAME().length() > 0) {
				sqlString += " and RESOUCE_NAME like :RESOUCE_NAME ";
			}
			if(criteria.getRESOURCE_ID() != null && criteria.getRESOURCE_ID().length() > 0) {
				sqlString += " and RESOURCE_ID = :RESOURCE_ID ";
			}
			if(criteria.getSYSTEM_TYPE() != null && criteria.getSYSTEM_TYPE().length() > 0) {
				sqlString += " and SYSTEM_TYPE like :SYSTEM_TYPE ";
			}
			if(criteria.getAPP_ID() != null && criteria.getAPP_ID().length() > 0) {
				sqlString += " and APP_ID = :APP_ID ";
			}
			
		}
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResFeature.class);
			q.setInteger("DELETE_STATUS", criteria.getDELETE_STATUS());
			if( criteria != null ) {
				if(criteria.getRESOUCE_NAME() != null && criteria.getRESOUCE_NAME().length() > 0) {
					q.setString( "RESOUCE_NAME", "%" + criteria.getRESOUCE_NAME() + "%" );
				}
				if(criteria.getRESOURCE_ID() != null && criteria.getRESOURCE_ID().length() > 0) {
					q.setString( "RESOURCE_ID",  criteria.getRESOURCE_ID());
				}
				if(criteria.getSYSTEM_TYPE() != null && criteria.getSYSTEM_TYPE().length() > 0) {
					q.setString( "SYSTEM_TYPE",  criteria.getSYSTEM_TYPE());
				}
				if(criteria.getAPP_ID() != null && criteria.getAPP_ID().length() > 0) {
					q.setString( "APP_ID",  criteria.getAPP_ID());
				}
			}
			rs = q.list();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return rs;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ResFeature> GetFeatures(String pid, ResFeature criteria, int page, int rows)
			throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<ResFeature> rs = null;
		String sqlString = "select * from WA_AUTHORITY_FUNC_RESOURCE where 1 = 1 and DELETE_STATUS =:DELETE_STATUS and PARENT_RESOURCE =:PARENT_RESOURCE";
		if( criteria != null ) {
			if(criteria.getRESOUCE_NAME() != null && criteria.getRESOUCE_NAME().length() > 0) {
				sqlString += " and RESOUCE_NAME like :RESOUCE_NAME ";
			}
			if(criteria.getRESOURCE_ID() != null && criteria.getRESOURCE_ID().length() > 0) {
				sqlString += " and RESOURCE_ID = :RESOURCE_ID ";
			}
			if(criteria.getSYSTEM_TYPE() != null && criteria.getSYSTEM_TYPE().length() > 0) {
				sqlString += " and SYSTEM_TYPE like :SYSTEM_TYPE ";
			}
			if(criteria.getAPP_ID() != null && criteria.getAPP_ID().length() > 0) {
				sqlString += " and APP_ID = :APP_ID ";
			}
		}
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResFeature.class);
			q.setInteger("DELETE_STATUS", criteria.getDELETE_STATUS());
			q.setString("PARENT_RESOURCE", pid);
			if( criteria != null ) {
				if(criteria.getRESOUCE_NAME() != null && criteria.getRESOUCE_NAME().length() > 0) {
					q.setString( "RESOUCE_NAME", "%" + criteria.getRESOUCE_NAME() + "%" );
				}
				if(criteria.getRESOURCE_ID() != null && criteria.getRESOURCE_ID().length() > 0) {
					q.setString( "RESOURCE_ID",  criteria.getRESOURCE_ID());
				}
				if(criteria.getSYSTEM_TYPE() != null && criteria.getSYSTEM_TYPE().length() > 0) {
					q.setString( "SYSTEM_TYPE",  criteria.getSYSTEM_TYPE());
				}
				if(criteria.getAPP_ID() != null && criteria.getAPP_ID().length() > 0) {
					q.setString( "APP_ID",  criteria.getAPP_ID());
				}
			}
			if( page > 0 && rows > 0) {
				q.setFirstResult((page-1) * rows);   
				q.setMaxResults(rows);
			}
			rs = q.list();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return rs;
	}

	@Override
	public int GetFeaturesCount(ResFeature criteria) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		int rs;
		String sqlString = "select count(*) from WA_AUTHORITY_FUNC_RESOURCE where 1 = 1 and DELETE_STATUS =:DELETE_STATUS";
		if( criteria != null ) {
			if(criteria.getRESOUCE_NAME() != null && criteria.getRESOUCE_NAME().length() > 0) {
				sqlString += " and RESOUCE_NAME like :RESOUCE_NAME ";
			}
			if(criteria.getRESOURCE_ID() != null && criteria.getRESOURCE_ID().length() > 0) {
				sqlString += " and RESOURCE_ID = :RESOURCE_ID ";
			}
		}
		
		try {
			Query q = session.createSQLQuery(sqlString);
			q.setInteger("DELETE_STATUS", criteria.getDELETE_STATUS());
			if( criteria != null ) {
				if(criteria.getRESOUCE_NAME() != null && criteria.getRESOUCE_NAME().length() > 0) {
					q.setString( "RESOUCE_NAME", "%" + criteria.getRESOUCE_NAME() + "%" );
				}
				if(criteria.getRESOURCE_ID() != null && criteria.getRESOURCE_ID().length() > 0) {
					q.setString( "RESOURCE_ID", criteria.getRESOURCE_ID());
				}
			}
			rs = ((BigInteger)q.uniqueResult()).intValue();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return rs;
	}

	@Override
	public ResData DataAdd(ResData data) throws Exception {
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		try
		{
			data = (ResData) session.merge(data);
			tx.commit();
		}
		catch(ConstraintViolationException cne){
			tx.rollback();
			System.out.println(cne.getSQLException().getMessage());
			throw new Exception("存在重名数据资源。");
		}
		catch(org.hibernate.exception.SQLGrammarException e)
		{
			tx.rollback();
			System.out.println(e.getSQLException().getMessage());
			throw e.getSQLException();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return data;
	}

	@Override
	public void DataDel(ResData res) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		try
		{
			session.delete(res);
			tx.commit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ResData> GetDatas( List<String> resource_status, List<String> resource_type, List<String> dataset_sensitive_level,
			List<String> data_set, List<String> section_class, List<String> element, List<String> section_relatioin_class, 
			ResData criteria, int page, int rows)
					throws Exception {
		
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<ResData> rs = null;
		List<String> list=null;
		String sqlString = "select * from WA_AUTHORITY_DATA_RESOURCE where 1 = 1 and DELETE_STATUS =:DELETE_STATUS ";
		if( criteria != null ) {
			if(criteria.getName() != null && criteria.getName().length() > 0) {
				sqlString += " and name like :name ";
			}
			if(criteria.getRESOURCE_ID() != null && criteria.getRESOURCE_ID().length() > 0) {
				sqlString += " and RESOURCE_ID = :RESOURCE_ID ";
			}
			if(criteria.getRESOURCE_DESCRIBE() != null && criteria.getRESOURCE_DESCRIBE().length() > 0) {
				sqlString += " and RESOURCE_DESCRIBE like :RESOURCE_DESCRIBE ";
			}
			if(criteria.getRMK() != null && criteria.getRMK().length() > 0) {
				sqlString += " and RMK like :RMK ";
			}
			if(resource_status != null) {
				for (int i = 0; i < resource_status.size(); i++) {
					list =Arrays.asList(resource_status.get(i).split(","));	
				}
				if(list.get(0) != "" && ! list.get(0).equals("")){
					sqlString += " and RESOURCE_STATUS in (:RESOURCE_STATUS) ";
				}
			}
			if(resource_type != null) {
				for (int i = 0; i < resource_type.size(); i++) {
					list =Arrays.asList(resource_type.get(i).split(","));	
				}
				if(list.get(0) != "" && ! list.get(0).equals("")){
					sqlString += " and resource_type in (:resource_type) ";
				}
			}
			if(dataset_sensitive_level != null) {
				for (int i = 0; i < dataset_sensitive_level.size(); i++) {
					list =Arrays.asList(dataset_sensitive_level.get(i).split(","));	
				}
				if(list.get(0) != "" && ! list.get(0).equals("")){
					sqlString += " and DATASET_SENSITIVE_LEVEL in (:DATASET_SENSITIVE_LEVEL) ";
				}
			}
			if(data_set != null) {
				for (int i = 0; i < data_set.size(); i++) {
					list =Arrays.asList(data_set.get(i).split(","));	
				}
				if(list.get(0) != "" && ! list.get(0).equals("")){
					sqlString += " and DATA_SET in (:DATA_SET) ";
				}
			}
			if(section_class != null) {
				for (int i = 0; i < section_class.size(); i++) {
					list =Arrays.asList(section_class.get(i).split(","));	
				}
				if(list.get(0) != "" && ! list.get(0).equals("")){
					sqlString += " and SECTION_CLASS in (:SECTION_CLASS) ";
				}
			}
			if(element != null) {
				for (int i = 0; i < element.size(); i++) {
					list =Arrays.asList(element.get(i).split(","));	
				}
				if(list.get(0) != "" && ! list.get(0).equals("")){
					sqlString += " and ELEMENT in (:ELEMENT) ";
				}
			}
			if(section_relatioin_class != null) {
				for (int i = 0; i < section_relatioin_class.size(); i++) {
					list =Arrays.asList(section_relatioin_class.get(i).split(","));	
				}
				if(list.get(0) != "" && ! list.get(0).equals("")){
					sqlString += " and SECTION_RELATIOIN_CLASS in (:SECTION_RELATIOIN_CLASS) ";
				}
			}
		}
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResData.class);
			q.setInteger("DELETE_STATUS", criteria.getDELETE_STATUS());
			if( criteria != null ) {
				if(criteria.getName() != null && criteria.getName().length() > 0) {
					q.setString( "name", "%" + criteria.getName() + "%" );
				}
				if(criteria.getRESOURCE_ID() != null && criteria.getRESOURCE_ID().length() > 0) {
					q.setString( "RESOURCE_ID", criteria.getRESOURCE_ID());
				}
				if(criteria.getRESOURCE_DESCRIBE() != null && criteria.getRESOURCE_DESCRIBE().length() > 0) {
					q.setString( "RESOURCE_DESCRIBE", "%" + criteria.getRESOURCE_DESCRIBE() + "%" );
				}
				if(criteria.getRMK() != null && criteria.getRMK().length() > 0) {
					q.setString( "RMK", "%" + criteria.getRMK() + "%" );
				}
				if(resource_status != null) {
					for (int i = 0; i < resource_status.size(); i++) {
						list =Arrays.asList(resource_status.get(i).split(","));	
					}
					if(list.get(0) != "" && ! list.get(0).equals("")){
						q.setParameterList("RESOURCE_STATUS", list);
					}
				}	
				if(resource_type != null) {
					for (int i = 0; i < resource_type.size(); i++) {
						list =Arrays.asList(resource_type.get(i).split(","));	
					}
					if(list.get(0) != "" && ! list.get(0).equals("")){
						q.setParameterList("resource_type", list);
					}
				}
				if(dataset_sensitive_level != null) {
					for (int i = 0; i < dataset_sensitive_level.size(); i++) {
						list =Arrays.asList(dataset_sensitive_level.get(i).split(","));	
					}
					if(list.get(0) != "" && ! list.get(0).equals("")){
						q.setParameterList("DATASET_SENSITIVE_LEVEL", list);
					}
				}
				if(data_set != null) {
					for (int i = 0; i < data_set.size(); i++) {
						list =Arrays.asList(data_set.get(i).split(","));	
					}
					if(list.get(0) != "" && ! list.get(0).equals("")){
						q.setParameterList("DATA_SET", list);
					}
				}
				if(section_class != null) {
					for (int i = 0; i < section_class.size(); i++) {
						list =Arrays.asList(section_class.get(i).split(","));	
					}
					if(list.get(0) != "" && ! list.get(0).equals("")){
						q.setParameterList("SECTION_CLASS", list);
					}
				}
				if(element != null) {
					for (int i = 0; i < element.size(); i++) {
						list =Arrays.asList(element.get(i).split(","));	
					}
					if(list.get(0) != "" && ! list.get(0).equals("")){
						q.setParameterList("ELEMENT", list);
					}
				}
				if(section_relatioin_class != null) {
					for (int i = 0; i < section_relatioin_class.size(); i++) {
						list =Arrays.asList(section_relatioin_class.get(i).split(","));	
					}
					if(list.get(0) != "" && ! list.get(0).equals("")){
						q.setParameterList("SECTION_RELATIOIN_CLASS", list);
					}
					
				}
				

			}
			if( page > 0 && rows > 0) {
				q.setFirstResult((page-1) * rows);   
				q.setMaxResults(rows);
			}
			rs = q.list();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return rs;
	}

	@Override
	public int GetDatasCount(ResData criteria) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		int rs;
		String sqlString = "select count(*) from WA_AUTHORITY_DATA_RESOURCE where 1 = 1 and DELETE_STATUS =:DELETE_STATUS ";
		if( criteria != null ) {
			if( criteria != null ) {
				if(criteria.getName() != null && criteria.getName().length() > 0) {
					sqlString += " and name like :name ";
				}
				if(criteria.getRESOURCE_ID() != null && criteria.getRESOURCE_ID().length() > 0) {
					sqlString += " and RESOURCE_ID = :RESOURCE_ID ";
				}
			}
		}
		
		try {
			Query q = session.createSQLQuery(sqlString);
			q.setInteger("DELETE_STATUS", criteria.getDELETE_STATUS());
			if( criteria != null ) {
				if( criteria != null ) {
					if(criteria.getName() != null && criteria.getName().length() > 0) {
						q.setString( "name", "%" + criteria.getName() + "%" );
					}
					if(criteria.getRESOURCE_ID() != null && criteria.getRESOURCE_ID().length() > 0) {
						q.setString( "RESOURCE_ID", criteria.getRESOURCE_ID());
					}
				}
			}
			rs = ((BigInteger)q.uniqueResult()).intValue();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return rs;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ResDataTemplate> GetDataTemplates( List<String> resource_status, List<String> resource_type, List<String> dataset_sensitive_level,
			List<String> data_set, List<String> section_class, List<String> element, List<String> section_relatioin_class, 
			ResDataTemplate criteria, int page, int rows)
					throws Exception {
		
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<ResDataTemplate> rs = null;
		List<String> list=null;
		String sqlString = "select * from WA_AUTHORITY_DATA_RESOURCE_Template where 1 = 1 and DELETE_STATUS =:DELETE_STATUS ";
		if( criteria != null ) {
			if(criteria.getName() != null && criteria.getName().length() > 0) {
				sqlString += " and name like :name ";
			}
			if(criteria.getRESOURCE_ID() != null && criteria.getRESOURCE_ID().length() > 0) {
				sqlString += " and RESOURCE_ID = :RESOURCE_ID ";
			}
			if(criteria.getRESOURCE_DESCRIBE() != null && criteria.getRESOURCE_DESCRIBE().length() > 0) {
				sqlString += " and RESOURCE_DESCRIBE like :RESOURCE_DESCRIBE ";
			}
			if(criteria.getRMK() != null && criteria.getRMK().length() > 0) {
				sqlString += " and RMK like :RMK ";
			}
			if(resource_status != null) {
				for (int i = 0; i < resource_status.size(); i++) {
					list =Arrays.asList(resource_status.get(i).split(","));	
				}
				if(list.get(0) != "" && ! list.get(0).equals("")){
					sqlString += " and RESOURCE_STATUS in (:RESOURCE_STATUS) ";
				}
			}
			if(resource_type != null) {
				for (int i = 0; i < resource_type.size(); i++) {
					list =Arrays.asList(resource_type.get(i).split(","));	
				}
				if(list.get(0) != "" && ! list.get(0).equals("")){
					sqlString += " and resource_type in (:resource_type) ";
				}
			}
			if(dataset_sensitive_level != null) {
				for (int i = 0; i < dataset_sensitive_level.size(); i++) {
					list =Arrays.asList(dataset_sensitive_level.get(i).split(","));	
				}
				if(list.get(0) != "" && ! list.get(0).equals("")){
					sqlString += " and DATASET_SENSITIVE_LEVEL in (:DATASET_SENSITIVE_LEVEL) ";
				}
			}
			if(data_set != null) {
				for (int i = 0; i < data_set.size(); i++) {
					list =Arrays.asList(data_set.get(i).split(","));	
				}
				if(list.get(0) != "" && ! list.get(0).equals("")){
					sqlString += " and DATA_SET in (:DATA_SET) ";
				}
			}
			if(section_class != null) {
				for (int i = 0; i < section_class.size(); i++) {
					list =Arrays.asList(section_class.get(i).split(","));	
				}
				if(list.get(0) != "" && ! list.get(0).equals("")){
					sqlString += " and SECTION_CLASS in (:SECTION_CLASS) ";
				}
			}
			if(element != null) {
				for (int i = 0; i < element.size(); i++) {
					list =Arrays.asList(element.get(i).split(","));	
				}
				if(list.get(0) != "" && ! list.get(0).equals("")){
					sqlString += " and ELEMENT in (:ELEMENT) ";
				}
			}
			if(section_relatioin_class != null) {
				for (int i = 0; i < section_relatioin_class.size(); i++) {
					list =Arrays.asList(section_relatioin_class.get(i).split(","));	
				}
				if(list.get(0) != "" && ! list.get(0).equals("")){
					sqlString += " and SECTION_RELATIOIN_CLASS in (:SECTION_RELATIOIN_CLASS) ";
				}
			}
		}
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResDataTemplate.class);
			q.setInteger("DELETE_STATUS", criteria.getDELETE_STATUS());
			if( criteria != null ) {
				if(criteria.getName() != null && criteria.getName().length() > 0) {
					q.setString( "name", "%" + criteria.getName() + "%" );
				}
				if(criteria.getRESOURCE_ID() != null && criteria.getRESOURCE_ID().length() > 0) {
					q.setString( "RESOURCE_ID", criteria.getRESOURCE_ID());
				}
				if(criteria.getRESOURCE_DESCRIBE() != null && criteria.getRESOURCE_DESCRIBE().length() > 0) {
					q.setString( "RESOURCE_DESCRIBE", "%" + criteria.getRESOURCE_DESCRIBE() + "%" );
				}
				if(criteria.getRMK() != null && criteria.getRMK().length() > 0) {
					q.setString( "RMK", "%" + criteria.getRMK() + "%" );
				}
				if(resource_status != null) {
					for (int i = 0; i < resource_status.size(); i++) {
						list =Arrays.asList(resource_status.get(i).split(","));	
					}
					if(list.get(0) != "" && ! list.get(0).equals("")){
						q.setParameterList("RESOURCE_STATUS", list);
					}
				}	
				if(resource_type != null) {
					for (int i = 0; i < resource_type.size(); i++) {
						list =Arrays.asList(resource_type.get(i).split(","));	
					}
					if(list.get(0) != "" && ! list.get(0).equals("")){
						q.setParameterList("resource_type", list);
					}
				}
				if(dataset_sensitive_level != null) {
					for (int i = 0; i < dataset_sensitive_level.size(); i++) {
						list =Arrays.asList(dataset_sensitive_level.get(i).split(","));	
					}
					if(list.get(0) != "" && ! list.get(0).equals("")){
						q.setParameterList("DATASET_SENSITIVE_LEVEL", list);
					}
				}
				if(data_set != null) {
					for (int i = 0; i < data_set.size(); i++) {
						list =Arrays.asList(data_set.get(i).split(","));	
					}
					if(list.get(0) != "" && ! list.get(0).equals("")){
						q.setParameterList("DATA_SET", list);
					}
				}
				if(section_class != null) {
					for (int i = 0; i < section_class.size(); i++) {
						list =Arrays.asList(section_class.get(i).split(","));	
					}
					if(list.get(0) != "" && ! list.get(0).equals("")){
						q.setParameterList("SECTION_CLASS", list);
					}
				}
				if(element != null) {
					for (int i = 0; i < element.size(); i++) {
						list =Arrays.asList(element.get(i).split(","));	
					}
					if(list.get(0) != "" && ! list.get(0).equals("")){
						q.setParameterList("ELEMENT", list);
					}
				}
				if(section_relatioin_class != null) {
					for (int i = 0; i < section_relatioin_class.size(); i++) {
						list =Arrays.asList(section_relatioin_class.get(i).split(","));	
					}
					if(list.get(0) != "" && ! list.get(0).equals("")){
						q.setParameterList("SECTION_RELATIOIN_CLASS", list);
					}
					
				}
				

			}
			if( page > 0 && rows > 0) {
				q.setFirstResult((page-1) * rows);   
				q.setMaxResults(rows);
			}
			rs = q.list();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return rs;
	}

	@Override
	public int GetDataTemplatesCount(ResDataTemplate criteria) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		int rs;
		String sqlString = "select count(*) from WA_AUTHORITY_DATA_RESOURCE_Template where 1 = 1 and DELETE_STATUS =:DELETE_STATUS ";
		if( criteria != null ) {
			if( criteria != null ) {
				if(criteria.getName() != null && criteria.getName().length() > 0) {
					sqlString += " and name like :name ";
				}
				if(criteria.getRESOURCE_ID() != null && criteria.getRESOURCE_ID().length() > 0) {
					sqlString += " and RESOURCE_ID = :RESOURCE_ID ";
				}
			}
		}
		
		try {
			Query q = session.createSQLQuery(sqlString);
			q.setInteger("DELETE_STATUS", criteria.getDELETE_STATUS());
			if( criteria != null ) {
				if( criteria != null ) {
					if(criteria.getName() != null && criteria.getName().length() > 0) {
						q.setString( "name", "%" + criteria.getName() + "%" );
					}
					if(criteria.getRESOURCE_ID() != null && criteria.getRESOURCE_ID().length() > 0) {
						q.setString( "RESOURCE_ID", criteria.getRESOURCE_ID());
					}
				}
			}
			rs = ((BigInteger)q.uniqueResult()).intValue();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return rs;
	}
	
	@SuppressWarnings("unchecked")
	public List<ResData> GetAllDatas() throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<ResData> rs = null;
		String sqlString = "select * from WA_AUTHORITY_DATA_RESOURCE where 1=1 and resource_type =:resource_type ";

		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResData.class);
			q.setInteger("resource_type", ResData.RESTYPEPUBLIC);
			rs = q.list();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return rs;
	}
	
	@SuppressWarnings("unchecked")
	public ResData GetDataByColumn(String dataSet, String element) throws Exception
	{
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<ResData> rs = null;
		ResData result = null;
		String sqlString = "select * from WA_AUTHORITY_DATA_RESOURCE where data_set =:data_set and element = :element and element_value is null";

		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResData.class);
			q.setString("data_set", dataSet);
			q.setString("element", element);
			rs = q.list();
			tx.commit();
			if(rs.size() == 0) {
				result = null;
			}
			else if(rs.size() == 1) {
				result = rs.get(0);
			}
			else {
				throw new Exception("字段资源不唯一[dataset:" + dataSet + ", element:" + element + "]");
			}
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public ResData GetDataByRelationRow(String dataSet, String element, String elemnetValue) throws Exception
	{
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<ResData> rs = null;
		ResData result = null;
		String sqlString = "select * from WA_AUTHORITY_DATA_RESOURCE where data_set =:data_set and element = :element and element_value = :element_value";

		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResData.class);
			q.setString("data_set", dataSet);
			q.setString("element", element);
			q.setString("element_value", elemnetValue);
			rs = q.list();
			tx.commit();
			if(rs.size() == 0) {
				result = null;
			}
			else if(rs.size() == 1) {
				result = rs.get(0);
			}
			else {
				throw new Exception("数据集-字段-字段值数据资源不唯一[dataset:" + dataSet + ", element:" + element + ", elementValue:" + elemnetValue + "]");
			}
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public ResData GetDataByRelationColumn(String dataSet, String sectionClass, String element) throws Exception
	{
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<ResData> rs = null;
		ResData result = null;
		String sqlString = "select * from WA_AUTHORITY_DATA_RESOURCE where data_set =:data_set and element = :element and section_class = :section_class";

		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResData.class);
			q.setString("data_set", dataSet);
			q.setString("element", element);
			q.setString("section_class", sectionClass);
			rs = q.list();
			tx.commit();
			if(rs.size() == 0) {
				result = null;
			}
			else if(rs.size() == 1) {
				result = rs.get(0);
			}
			else {
				throw new Exception("字段资源不唯一[dataset:" + dataSet + ", element:" + element + "]");
			}
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		
		return result;
	}
	

	
	@Override
	public ResRole RoleAdd(ResRole role) throws Exception {
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		try
		{
			role = (ResRole) session.merge(role);
			role.setBUSINESS_ROLE(new Integer(role.getId()).toString());
			role = (ResRole) session.merge(role);
			tx.commit();
		}
		catch(ConstraintViolationException cne){
			tx.rollback();
			System.out.println(cne.getSQLException().getMessage());
			throw new Exception("存在重名角色。");
		}
		catch(org.hibernate.exception.SQLGrammarException e)
		{
			tx.rollback();
			System.out.println(e.getSQLException().getMessage());
			throw e.getSQLException();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return role;
	}
	
	@SuppressWarnings("unchecked")
	public ResRole RoleImport(ResRole role) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		
		String sqlString = "SELECT * FROM wa_authority_role WHERE business_role =:business_role ";
		List<ResRole> rs = null;
		try
		{
			Query q = session.createSQLQuery(sqlString).addEntity(ResRole.class);
			q.setString("business_role", role.getBUSINESS_ROLE());
			rs = q.list();
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
					Locale.SIMPLIFIED_CHINESE);
			String timenow = sdf.format(new Date());
			role.setLATEST_MOD_TIME(timenow);
			if( rs.size() == 0 ) {
				role = (ResRole) session.merge(role);
			}
			else if ( rs.size() == 1 ) {
				if(!rs.get(0).getBUSINESS_ROLE_NAME().equals(role.getBUSINESS_ROLE_NAME())
						|| rs.get(0).getDELETE_STATUS() != ResRole.DELSTATUSNO) {
					role.setId(rs.get(0).getId());
					role.setDATA_VERSION(rs.get(0).getDATA_VERSION() + 1);
					role.setDELETE_STATUS(ResRole.DELSTATUSNO);
					role = (ResRole) session.merge(role);
				}
			}
			else if ( rs.size() > 1 ) {
				throw new Exception("存在" + rs.size() + "条记录的角色代码为" + role.getBUSINESS_ROLE() );
			}
			
			tx.commit();
		}
		catch(ConstraintViolationException cne){
			tx.rollback();
			System.out.println(cne.getSQLException().getMessage());
			throw new Exception("存在重名角色。");
		}
		catch(org.hibernate.exception.SQLGrammarException e)
		{
			tx.rollback();
			System.out.println(e.getSQLException().getMessage());
			throw e.getSQLException();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return role;
	}

	@Override
	public void RoleDel(ResRole role) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		String sqlString = "delete from WA_AUTHORITY_RESOURCE_ROLE where  BUSINESS_ROLE = :BUSINESS_ROLE ";
		
		try {
			Query q = session.createSQLQuery(sqlString);
			q.setInteger("BUSINESS_ROLE", role.getId());
			q.executeUpdate();
		
			//TODO: delete role&resource first
			session.delete(role);
			tx.commit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return;
	}
	
	@SuppressWarnings("unchecked")
	public List<ResRole> GetAllRoles() throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<ResRole> rs = null;
		String sqlString = "SELECT * FROM wa_authority_role WHERE 1=1 AND business_role_type =:business_role_type ";

		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResRole.class);
			q.setInteger("business_role_type", ResRole.RESROLETYPEPUBLIC);
			rs = q.list();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return rs;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ResRole> GetRolesByTime(String time) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<ResRole> rs = null;
		String sqlString = "SELECT * FROM wa_authority_role WHERE 1=1 AND business_role_type =:business_role_type and LATEST_MOD_TIME > :LATEST_MOD_TIME";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResRole.class);
			q.setInteger("business_role_type", ResRole.RESROLETYPEPUBLIC);
			q.setString( "LATEST_MOD_TIME", time);
			rs = q.list();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return rs;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ResRole> GetRoles(ResRole criteria, int page, int rows)
			throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<ResRole> rs = null;
		String sqlString = "select * from WA_AUTHORITY_ROLE where 1 = 1 and DELETE_STATUS =:DELETE_STATUS";
		if( criteria != null ) {
			if(criteria.getBUSINESS_ROLE_NAME() != null && criteria.getBUSINESS_ROLE_NAME().length() > 0) {
				sqlString += " and BUSINESS_ROLE_NAME like :BUSINESS_ROLE_NAME ";
			}
			if(criteria.getBUSINESS_ROLE() != null && criteria.getBUSINESS_ROLE().length() > 0) {
				sqlString += " and BUSINESS_ROLE = :BUSINESS_ROLE ";
			}
		}
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResRole.class);
			q.setInteger("DELETE_STATUS", criteria.getDELETE_STATUS());
			if( criteria != null ) {
				if(criteria.getBUSINESS_ROLE_NAME() != null && criteria.getBUSINESS_ROLE_NAME().length() > 0) {
					q.setString( "BUSINESS_ROLE_NAME", "%" + criteria.getBUSINESS_ROLE_NAME() + "%" );
				}
				if(criteria.getBUSINESS_ROLE() != null && criteria.getBUSINESS_ROLE().length() > 0) {
					q.setString( "BUSINESS_ROLE", criteria.getBUSINESS_ROLE());
				}
			}
			if( page > 0 && rows > 0) {
				q.setFirstResult((page-1) * rows);   
				q.setMaxResults(rows);
			}
			rs = q.list();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return rs;
	}

	@Override
	public int GetRolesCount(ResRole criteria) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		int rs;
		String sqlString = "select count(*) from WA_AUTHORITY_ROLE where 1 = 1 and DELETE_STATUS =:DELETE_STATUS";
		if( criteria != null ) {
			if( criteria != null ) {
				if(criteria.getBUSINESS_ROLE_NAME() != null && criteria.getBUSINESS_ROLE_NAME().length() > 0) {
					sqlString += " and BUSINESS_ROLE_NAME like :BUSINESS_ROLE_NAME ";
				}
				if(criteria.getBUSINESS_ROLE() != null && criteria.getBUSINESS_ROLE().length() > 0) {
					sqlString += " and BUSINESS_ROLE = :BUSINESS_ROLE ";
				}
			}
		}
		
		try {
			Query q = session.createSQLQuery(sqlString);
			q.setInteger("DELETE_STATUS", criteria.getDELETE_STATUS());
			if( criteria != null ) {
				if( criteria != null ) {
					if(criteria.getBUSINESS_ROLE_NAME() != null && criteria.getBUSINESS_ROLE_NAME().length() > 0) {
						q.setString( "BUSINESS_ROLE_NAME", "%" + criteria.getBUSINESS_ROLE_NAME() + "%" );
					}
					if(criteria.getBUSINESS_ROLE() != null && criteria.getBUSINESS_ROLE().length() > 0) {
						q.setString( "BUSINESS_ROLE", criteria.getBUSINESS_ROLE());
					}
				}
			}
			rs = ((BigInteger)q.uniqueResult()).intValue();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return rs;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ResRoleResource ResRoleResourceAdd(ResRoleResource resRoleResource) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.SIMPLIFIED_CHINESE);
		String timenow = sdf.format(new Date());
		
		List<ResRoleResource> rs = null;
		String sqlString = "SELECT * FROM wa_authority_resource_role WHERE business_role=:business_role AND resource_id =:resource_id ";

		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResRoleResource.class);
			q.setString("business_role", resRoleResource.getBUSINESS_ROLE());
			q.setString("resource_id", resRoleResource.getRESOURCE_ID());
			rs = q.list();

			resRoleResource.setLATEST_MOD_TIME(timenow);
			if(rs.size() == 0) {
				resRoleResource = (ResRoleResource) session.merge(resRoleResource);
			}
			else if(rs.size() == 1) {
				if(rs.get(0).getDELETE_STATUS() != ResRoleResource.DELSTATUSNO) {
					resRoleResource.setId(rs.get(0).getId());
					resRoleResource.setDATA_VERSION(rs.get(0).getDATA_VERSION() + 1);
				}
			}
			else {
				throw new Exception("存在" + rs.size() + "条相同的角色资源对应关系记录[resId:" + resRoleResource.getRESOURCE_ID() + ", roleId:" + resRoleResource.getBUSINESS_ROLE() + "]" );
			}
			tx.commit();
		}
		catch(ConstraintViolationException cne){
			tx.rollback();
			System.out.println(cne.getSQLException().getMessage());
			throw new Exception("存在重名的角色资源对应关系。");
		}
		catch(org.hibernate.exception.SQLGrammarException e)
		{
			tx.rollback();
			System.out.println(e.getSQLException().getMessage());
			throw e.getSQLException();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return resRoleResource;
	}
	
	@Override
	public void UpdateFeatureRoleResource(String roleId, List<String> featureIds)
			throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		String sqlString = "delete from WA_AUTHORITY_RESOURCE_ROLE where BUSINESS_ROLE = :BUSINESS_ROLE and RESOURCE_CLASS = :RESOURCE_CLASS ";
		
		try {
			Query q = session.createSQLQuery(sqlString);
			q.setString("BUSINESS_ROLE", roleId);
			q.setInteger("RESOURCE_CLASS", ResRoleResource.RESCLASSFEATURE);
			q.executeUpdate();
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
					Locale.SIMPLIFIED_CHINESE);
			String timenow = sdf.format(new Date());
			
			ResRoleResource rr;
			if(featureIds != null) {
				for(int i = 0; i<featureIds.size(); i++) {
					rr = new ResRoleResource();
					rr.setBUSINESS_ROLE(roleId);
					rr.setRESOURCE_ID(featureIds.get(i));
					rr.setLATEST_MOD_TIME(timenow);
					rr.setDATA_VERSION(1);
					rr.setRESOURCE_CLASS(ResRoleResource.RESCLASSFEATURE);
					session.merge(rr);
				}
			}
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return;
	}

	@Override
	public void UpdateDataRoleResource(String roleId, List<String> dataIds)
			throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		String sqlString = "delete from WA_AUTHORITY_RESOURCE_ROLE where BUSINESS_ROLE = :BUSINESS_ROLE and RESOURCE_CLASS = :RESOURCE_CLASS ";
		
		try {
			Query q = session.createSQLQuery(sqlString);
			q.setString("BUSINESS_ROLE", roleId);
			q.setInteger("RESOURCE_CLASS", ResRoleResource.RESCLASSDATA);
			q.executeUpdate();
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
					Locale.SIMPLIFIED_CHINESE);
			String timenow = sdf.format(new Date());

			ResRoleResource rr;
			if( dataIds != null) {
				for(int i = 0; i<dataIds.size(); i++) {
					rr = new ResRoleResource();
					rr.setBUSINESS_ROLE(roleId);
					rr.setRESOURCE_ID(dataIds.get(i));
					rr.setLATEST_MOD_TIME(timenow);
					rr.setDATA_VERSION(1);
					rr.setRESOURCE_CLASS(ResRoleResource.RESCLASSDATA);
					session.merge(rr);
				}
			}
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return;		
	}
	
	@Override
	public void UpdateDataResource(List<String> dataIds) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		String sqlString = "REPLACE INTO wa_authority_data_resource SELECT * FROM wa_authority_data_resource_Template WHERE RESOURCE_ID in (:RESOURCE_ID) ";
		
		try {
			Query q = session.createSQLQuery(sqlString);
			if(dataIds != null) {
				q.setParameterList("RESOURCE_ID", dataIds);
			}
			q.executeUpdate();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return;		
	}
	
	@SuppressWarnings("unchecked")
	public List<ResRoleResource> GetAllResRoles() throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<ResRoleResource> rs = null;
		String sqlString = "SELECT a.* FROM wa_authority_resource_role a, wa_authority_role b WHERE a.business_role=b.business_role AND b.business_role_type =:business_role_type ";

		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResRoleResource.class);
			q.setInteger("business_role_type", ResRole.RESROLETYPEPUBLIC);
			rs = q.list();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return rs;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ResRoleResource> GetResRolesByTime(String time) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<ResRoleResource> rs = null;
		String sqlString = "SELECT a.* FROM wa_authority_resource_role a, wa_authority_role b WHERE a.business_role=b.business_role AND b.business_role_type =:business_role_type and a.LATEST_MOD_TIME > :LATEST_MOD_TIME";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResRoleResource.class);
			q.setInteger("business_role_type", ResRole.RESROLETYPEPUBLIC);
			q.setString( "LATEST_MOD_TIME", time);
			rs = q.list();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return rs;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ResRoleResource> GetRoleResourcesByRoleid(String id)
			throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<ResRoleResource> rs = null;
		String sqlString = "select * from WA_AUTHORITY_RESOURCE_ROLE where BUSINESS_ROLE = :BUSINESS_ROLE";
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResRoleResource.class);
			q.setString("BUSINESS_ROLE", id);
			rs = q.list();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return rs;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ResRole> GetRoleById(String id) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<ResRole> rs = null;
		String sqlString = "select * from WA_AUTHORITY_ROLE WHERE business_role =:business_role ";
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResRole.class);
			q.setString("business_role", id);
			rs = q.list();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return rs;
	}
	
	@Override
	public ResFeature GetFeatureByResId(String id) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		ResFeature rs = null;
		String sqlString = "select * from WA_AUTHORITY_FUNC_RESOURCE where RESOURCE_ID = :RESOURCE_ID ";
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResFeature.class);
			q.setString("RESOURCE_ID", id);
			rs = (ResFeature) q.uniqueResult();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return rs;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ResFeature> GetFeatureById(int id) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<ResFeature> rs = null;
		String sqlString = "select * from WA_AUTHORITY_FUNC_RESOURCE where id = :id ";
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResFeature.class);
			q.setInteger("id", id);
			rs = q.list();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return rs;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ResData> GetDataByResId(String resId) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<ResData> rs = null;
		String sqlString = "select * from WA_AUTHORITY_DATA_RESOURCE where RESOURCE_ID = :RESOURCE_ID ";
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResData.class);
			q.setString("RESOURCE_ID", resId);
			rs = q.list();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return rs;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ResDataTemplate> GetDataTemplateByResId(String resId) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<ResDataTemplate> rs = null;
		String sqlString = "select * from WA_AUTHORITY_DATA_RESOURCE_Template where RESOURCE_ID = :RESOURCE_ID ";
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResDataTemplate.class);
			q.setString("RESOURCE_ID", resId);
			rs = q.list();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return rs;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ResData> GetDataById(int id) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<ResData> rs = null;
		String sqlString = "select * from WA_AUTHORITY_DATA_RESOURCE where id = :id";
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResData.class);
			q.setInteger("id", id);
			rs = q.list();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return rs;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ResData> GetDatasByRole(String roleId) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<ResData> rs = null;
		String sqlString = "select * from WA_AUTHORITY_DATA_RESOURCE where RESOURCE_ID in (SELECT resource_id FROM wa_authority_resource_role where business_role = :business_role and RESOURCE_CLASS = :RESOURCE_CLASS);";

		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResData.class);
			q.setString("business_role", roleId);
			q.setInteger("RESOURCE_CLASS", ResRoleResource.RESCLASSDATA);
			rs = q.list();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return rs;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ResData> GetDatasByTime(String time) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<ResData> rs = null;
		String sqlString = "select * from WA_AUTHORITY_DATA_RESOURCE  where resource_type =:resource_type and LATEST_MOD_TIME > :LATEST_MOD_TIME";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResData.class);
			q.setInteger("resource_type", ResData.RESTYPEPUBLIC);
			q.setString( "LATEST_MOD_TIME", time);
			rs = q.list();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return rs;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ResData> GetColumnDatasByDataSet(String dataSet)
			throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<ResData> rs = null;
		String sqlString = "select * from WA_AUTHORITY_DATA_RESOURCE where DATA_SET = :DATA_SET and ELEMENT is not null and ELEMENT_VALUE is null";

		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResData.class);
			q.setString("DATA_SET", dataSet);
			rs = q.list();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return rs;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ResRoleOrg> GetResRoleOrgByRoleid(String id)
			throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<ResRoleOrg> rs = null;
		String sqlString = "select * from WA_AUTHORITY_BILATERAL_ROLE_ORG where BUSINESS_ROLE = :BUSINESS_ROLE";
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResRoleOrg.class);
			q.setString("BUSINESS_ROLE", id);
			rs = q.list();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return rs;
	}

	@Override
	public ResRoleResourceImport ResRoleResourceImportAdd(
			ResRoleResourceImport rrri) throws Exception {
		Session session = HibernateUtil.currentSession();
//		System.out.println("session:" + session);
		Transaction tx = session.beginTransaction();
//		System.out.println("tx:" + tx);
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
					Locale.SIMPLIFIED_CHINESE);
			String timenow = sdf.format(new Date());
//			System.out.println("timenow:" + timenow);
			rrri.setLATEST_MOD_TIME(timenow);
//			System.out.println("before merge:" + rrri);
			rrri = (ResRoleResourceImport) session.merge(rrri);
//			System.out.println("after merge:" + rrri);
			tx.commit();
//			System.out.println("tx commit:" + tx);
		}
		catch(ConstraintViolationException cne){
			tx.rollback();
			System.out.println(cne.getSQLException().getMessage());
			throw new Exception("存在重名待导入的角色资源对应关系。");
		}
		catch(org.hibernate.exception.SQLGrammarException e)
		{
			tx.rollback();
			System.out.println(e.getSQLException().getMessage());
			throw e.getSQLException();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return rrri;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ResRoleResourceImport> GetResRoleResourceImport()
			throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<ResRoleResourceImport> rs = null;
		String sqlString = "select * from WA_AUTHORITY_RESOURCE_ROLE_IMPORT ";

		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResRoleResourceImport.class);
			rs = q.list();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return rs;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ResDataOrg> GetResDataOrgByResId(String id)
			throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<ResDataOrg> rs = null;
		String sqlString = "select * from WA_AUTHORITY_RESOURCE_ORG where RESOURCE_ID = :RESOURCE_ID";
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResDataOrg.class);
			q.setString("RESOURCE_ID", id);
			rs = q.list();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return rs;
	}

	@Override
	public int ResRoleResourceImportClear() throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		int rs = 0;
		String sqlString = "delete from WA_AUTHORITY_RESOURCE_ROLE_IMPORT ";
		
		try {
			Query q = session.createSQLQuery(sqlString);
			rs = q.executeUpdate();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return rs;
	}

	@Override
	public int ClearPublicRoleAndDataResourceRelationship() throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		int rs = 0;
		//SET SQL_SAFE_UPDATES = 0; 
		String sqlString = "DELETE from WA_AUTHORITY_RESOURCE_ROLE where RESOURCE_CLASS = :RESOURCE_CLASS and BUSINESS_ROLE in (SELECT x.y FROM ( SELECT a.BUSINESS_ROLE as y FROM WA_AUTHORITY_RESOURCE_ROLE a, WA_AUTHORITY_ROLE b where a.BUSINESS_ROLE = b.BUSINESS_ROLE and b.BUSINESS_ROLE_TYPE = 0) x); ";
		try {
			Query q = session.createSQLQuery(sqlString);
			q.setInteger("RESOURCE_CLASS", ResRoleResource.RESCLASSDATA);
			rs = q.executeUpdate();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return rs;
	}
}
