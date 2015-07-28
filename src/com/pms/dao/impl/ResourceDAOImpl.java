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
import com.pms.model.AttrDictionary;
import com.pms.model.HibernateUtil;
import com.pms.model.ResData;
import com.pms.model.ResFeature;
import com.pms.model.ResRole;
import com.pms.model.ResRoleResource;


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
			feature.setResource_id(new Integer(feature.getId()).toString());
			feature = (ResFeature) session.merge(feature);
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
	public List<ResFeature> GetFeatures(ResFeature criteria, int page, int rows)
			throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<ResFeature> rs = null;
		String sqlString = "select * from WA_AUTHORITY_FUNC_RESOURCE where 1 = 1 and delete_status =:delete_status";
		if( criteria != null ) {
			if(criteria.getName() != null && criteria.getName().length() > 0) {
				sqlString += " and name like :name ";
			}
			if(criteria.getResource_id() != null && criteria.getResource_id().length() > 0) {
				sqlString += " and resource_id = :resource_id ";
			}
		}
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResFeature.class);
			q.setInteger("delete_status", ResFeature.DELSTATUSNO);
			if( criteria != null ) {
				if(criteria.getName() != null && criteria.getName().length() > 0) {
					q.setString( "name", "%" + criteria.getName() + "%" );
				}
				if(criteria.getResource_id() != null && criteria.getResource_id().length() > 0) {
					q.setString( "resource_id",  criteria.getResource_id());
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
		String sqlString = "select count(*) from WA_AUTHORITY_FUNC_RESOURCE where 1 = 1 and delete_status =:delete_status";
		if( criteria != null ) {
			if(criteria.getName() != null && criteria.getName().length() > 0) {
				sqlString += " and name like :name ";
			}
			if(criteria.getResource_id() != null && criteria.getResource_id().length() > 0) {
				sqlString += " and resource_id = :resource_id ";
			}
		}
		
		try {
			Query q = session.createSQLQuery(sqlString);
			q.setInteger("delete_status", ResFeature.DELSTATUSNO);
			if( criteria != null ) {
				if(criteria.getName() != null && criteria.getName().length() > 0) {
					q.setString( "name", "%" + criteria.getName() + "%" );
				}
				if(criteria.getResource_id() != null && criteria.getResource_id().length() > 0) {
					q.setString( "resource_id", criteria.getResource_id());
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
	public List<ResData> GetDatas( List<String> resource_status, List<String> delete_status, List<String> resource_type,
			List<String> dataset_sensitive_level, List<String> data_set, List<String> section_class, 
			List<String> element, List<String> section_relatioin_class, 
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
			if(criteria.getRESOURCE_REMARK() != null && criteria.getRESOURCE_REMARK().length() > 0) {
				sqlString += " and RESOURCE_REMARK like :RESOURCE_REMARK ";
			}
			if(resource_status != null) {
				for (int i = 0; i < resource_status.size(); i++) {
					list =Arrays.asList(resource_status.get(i).split(","));	
				}
				if(list.get(0) != "" && ! list.get(0).equals("")){
					sqlString += " and RESOURCE_STATUS in (:RESOURCE_STATUS) ";
				}
			}
			if(delete_status != null) {
				for (int i = 0; i < delete_status.size(); i++) {
					list =Arrays.asList(delete_status.get(i).split(","));	
				}
				if(list.get(0) != "" && ! list.get(0).equals("")){
					sqlString += " and DELETE_STATUS in (:DELETE_STATUS) ";
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
			q.setInteger("DELETE_STATUS", ResData.DELSTATUSNO);
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
				if(criteria.getRESOURCE_REMARK() != null && criteria.getRESOURCE_REMARK().length() > 0) {
					q.setString( "RESOURCE_REMARK", "%" + criteria.getRESOURCE_REMARK() + "%" );
				}
				if(resource_status != null) {
					for (int i = 0; i < resource_status.size(); i++) {
						list =Arrays.asList(resource_status.get(i).split(","));	
					}
					if(list.get(0) != "" && ! list.get(0).equals("")){
						q.setParameterList("RESOURCE_STATUS", list);
					}
				}	
				if(delete_status != null) {
					for (int i = 0; i < delete_status.size(); i++) {
						list =Arrays.asList(delete_status.get(i).split(","));	
					}
					if(list.get(0) != "" && ! list.get(0).equals("")){
						q.setParameterList("DELETE_STATUS", list);
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
			q.setInteger("DELETE_STATUS", ResData.DELSTATUSNO);
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
		String sqlString = "select * from WA_AUTHORITY_DATA_RESOURCE ";

		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResData.class);
			
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
	public List<AttrDictionary> GetDatasDictionarys(int id) throws Exception
	{
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<AttrDictionary> rs = null;
		String sqlString = "SELECT b.* " +
				" FROM WA_AUTHORITY_DATA_RESOURCE a,attrdict b,attrdef c " +
				" WHERE a.DELETE_STATUS=b.code AND b.attrid=c.id and a.id=:id ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(AttrDictionary.class);
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
			q.setInteger("DELETE_STATUS", ResRole.DELSTATUSNO);
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
			q.setInteger("DELETE_STATUS", ResRole.DELSTATUSNO);
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
	
	@Override
	public ResRoleResource ResRoleResourceAdd(ResRoleResource resRoleResource) throws Exception {
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		try
		{
			resRoleResource = (ResRoleResource) session.merge(resRoleResource);
			tx.commit();
		}
		catch(ConstraintViolationException cne){
			tx.rollback();
			System.out.println(cne.getSQLException().getMessage());
			throw new Exception("存在重名用户。");
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
		String sqlString = "delete from WA_AUTHORITY_RESOURCE_ROLE where BUSINESS_ROLE = :BUSINESS_ROLE and restype = :restype ";
		
		try {
			Query q = session.createSQLQuery(sqlString);
			q.setString("BUSINESS_ROLE", roleId);
			q.setInteger("restype", ResRoleResource.RESTYPEFEATURE);
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
					rr.setRestype(ResRoleResource.RESTYPEFEATURE);
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
		String sqlString = "delete from WA_AUTHORITY_RESOURCE_ROLE where BUSINESS_ROLE = :BUSINESS_ROLE and restype = :restype ";
		
		try {
			Query q = session.createSQLQuery(sqlString);
			q.setString("BUSINESS_ROLE", roleId);
			q.setInteger("restype", ResRoleResource.RESTYPEDATA);
			q.executeUpdate();
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
					Locale.SIMPLIFIED_CHINESE);
			String timenow = sdf.format(new Date());
//			List<ResRoleResource> roleResNodes=GetRoleResourcesByRoleid(roleId);
//			for (int j = 0; j < roleResNodes.size(); j++) {
//				ResRoleResource resRole=new ResRoleResource();
//				resRole.setRESOURCE_ID(roleResNodes.get(j).getRESOURCE_ID());
//				resRole.setBUSINESS_ROLE(roleResNodes.get(j).getBUSINESS_ROLE());
//				resRole.setRestype(roleResNodes.get(j).getRestype());
//				resRole.setDELETE_STATUS(ResRoleResource.DELSTATUSYES);
//				resRole.setDATA_VERSION(roleResNodes.get(j).getDATA_VERSION());
//				resRole.setLATEST_MOD_TIME(timenow);
//				
//				resRole = ResRoleResourceAdd(resRole);
//			}
			ResRoleResource rr;
			if( dataIds != null) {
				for(int i = 0; i<dataIds.size(); i++) {
					rr = new ResRoleResource();
					rr.setBUSINESS_ROLE(roleId);
					rr.setRESOURCE_ID(dataIds.get(i));
					rr.setLATEST_MOD_TIME(timenow);
					rr.setDATA_VERSION(1);
					rr.setRestype(ResRoleResource.RESTYPEDATA);
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

	@SuppressWarnings("unchecked")
	@Override
	public List<ResRoleResource> GetRoleResourcesByRoleid(String id)
			throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<ResRoleResource> rs = null;
		String sqlString = "select * from WA_AUTHORITY_RESOURCE_ROLE where BUSINESS_ROLE = :BUSINESS_ROLE and DELETE_STATUS =:DELETE_STATUS";
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResRoleResource.class);
			q.setString("BUSINESS_ROLE", id);
			q.setInteger("DELETE_STATUS", ResRoleResource.DELSTATUSNO);
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
	public List<ResRole> GetRoleById(int id) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<ResRole> rs = null;
		String sqlString = "select * from WA_AUTHORITY_ROLE where id = :id ";
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResRole.class);
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

	@Override
	public ResData GetDataByResId(String resId) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		ResData rs = null;
		String sqlString = "select * from WA_AUTHORITY_DATA_RESOURCE where RESOURCE_ID = :RESOURCE_ID ";
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResData.class);
			q.setString("RESOURCE_ID", resId);
			rs = (ResData) q.uniqueResult();
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
		String sqlString = "select * from WA_AUTHORITY_DATA_RESOURCE where id = :id and ";
		
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
		String sqlString = "select * from WA_AUTHORITY_DATA_RESOURCE where RESOURCE_ID in (SELECT resource_id FROM wa_authority_resource_role where business_role = :business_role and restype = :restype);";

		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResData.class);
			q.setString("business_role", roleId);
			q.setInteger("restype", ResRoleResource.RESTYPEDATA);
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
}
