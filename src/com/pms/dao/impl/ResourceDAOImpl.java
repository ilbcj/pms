package com.pms.dao.impl;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
		String sqlString = "select * from res_feature where 1 = 1 ";
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
		String sqlString = "select count(*) from res_feature where 1 = 1 ";
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
	public List<ResData> GetDatas(List<String> resource_status, ResData criteria, int page, int rows)
			throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<ResData> rs = null;
		String sqlString = "select * from WA_AUTHORITY_RESOURCE where 1 = 1 ";
		if( criteria != null ) {
			if(criteria.getName() != null && criteria.getName().length() > 0) {
				sqlString += " and name like :name ";
			}
			if(criteria.getRESOURCE_ID() != null && criteria.getRESOURCE_ID().length() > 0) {
				sqlString += " and RESOURCE_ID = :RESOURCE_ID ";
			}
			if(resource_status != null) {
				sqlString += " and RESOURCE_STATUS in (:RESOURCE_STATUS) ";
			}
		}
		
		List<String> list=null;
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResData.class);
			if( criteria != null ) {
				if(criteria.getName() != null && criteria.getName().length() > 0) {
					q.setString( "name", "%" + criteria.getName() + "%" );
				}
				if(criteria.getRESOURCE_ID() != null && criteria.getRESOURCE_ID().length() > 0) {
					q.setString( "RESOURCE_ID", criteria.getRESOURCE_ID());
				}
				if(resource_status != null) {
					for (int i = 0; i < resource_status.size(); i++) {
						list =Arrays.asList(resource_status.get(i).split(","));	
					} 
					q.setParameterList("RESOURCE_STATUS", list);
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
		String sqlString = "select count(*) from WA_AUTHORITY_RESOURCE where 1 = 1 ";
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
		String sqlString = "select * from WA_AUTHORITY_RESOURCE ";

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
				" FROM wa_authority_resource a,attrdict b,attrdef c " +
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
		String sqlString = "delete from res_role_resource where roleid = :roleid ";
		
		try {
			Query q = session.createSQLQuery(sqlString);
			q.setInteger("roleid", role.getId());
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
		String sqlString = "select * from WA_AUTHORITY_ROLE where 1 = 1 ";
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
		String sqlString = "select count(*) from WA_AUTHORITY_ROLE where 1 = 1 ";
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
		String sqlString = "select * from WA_AUTHORITY_RESOURCE_ROLE where BUSINESS_ROLE = :BUSINESS_ROLE ";
		
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

	@Override
	public ResFeature GetFeatureById(String id) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		ResFeature rs = null;
		String sqlString = "select * from res_feature where RESOURCE_ID = :RESOURCE_ID ";
		
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

	@Override
	public ResData GetDataById(String id) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		ResData rs = null;
		String sqlString = "select * from WA_AUTHORITY_RESOURCE where RESOURCE_ID = :RESOURCE_ID ";
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResData.class);
			q.setString("RESOURCE_ID", id);
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
}
