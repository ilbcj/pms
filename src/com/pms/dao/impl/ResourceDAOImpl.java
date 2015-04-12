package com.pms.dao.impl;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.pms.dao.ResourceDAO;
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
				sqlString += " and resource_id like :resource_id ";
			}
		}
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResFeature.class);
			if( criteria != null ) {
				if(criteria.getName() != null && criteria.getName().length() > 0) {
					q.setString( "name", "%" + criteria.getName() + "%" );
				}
				if(criteria.getResource_id() != null && criteria.getResource_id().length() > 0) {
					q.setString( "resource_id", "%" + criteria.getResource_id() + "%" );
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
			if( criteria != null ) {
				if(criteria.getName() != null && criteria.getName().length() > 0) {
					sqlString += " and name like :name ";
				}
				if(criteria.getResource_id() != null && criteria.getResource_id().length() > 0) {
					sqlString += " and resource_id like :resource_id ";
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
					if(criteria.getResource_id() != null && criteria.getResource_id().length() > 0) {
						q.setString( "resource_id", "%" + criteria.getResource_id() + "%" );
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
	public List<ResData> GetDatas(ResData criteria, int page, int rows)
			throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<ResData> rs = null;
		String sqlString = "select * from res_data where 1 = 1 ";
		if( criteria != null ) {
			if(criteria.getName() != null && criteria.getName().length() > 0) {
				sqlString += " and name like :name ";
			}
			if(criteria.getResource_id() != null && criteria.getResource_id().length() > 0) {
				sqlString += " and resource_id like :resource_id ";
			}
		}
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResData.class);
			if( criteria != null ) {
				if(criteria.getName() != null && criteria.getName().length() > 0) {
					q.setString( "name", "%" + criteria.getName() + "%" );
				}
				if(criteria.getResource_id() != null && criteria.getResource_id().length() > 0) {
					q.setString( "resource_id", "%" + criteria.getResource_id() + "%" );
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
		String sqlString = "select count(*) from res_data where 1 = 1 ";
		if( criteria != null ) {
			if( criteria != null ) {
				if(criteria.getName() != null && criteria.getName().length() > 0) {
					sqlString += " and name like :name ";
				}
				if(criteria.getResource_id() != null && criteria.getResource_id().length() > 0) {
					sqlString += " and resource_id like :resource_id ";
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
					if(criteria.getResource_id() != null && criteria.getResource_id().length() > 0) {
						q.setString( "resource_id", "%" + criteria.getResource_id() + "%" );
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
	public ResRole RoleAdd(ResRole role) throws Exception {
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		try
		{
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
		String sqlString = "select * from res_role where 1 = 1 ";
		if( criteria != null ) {
			if(criteria.getBusiness_role_name() != null && criteria.getBusiness_role_name().length() > 0) {
				sqlString += " and business_role_name like :business_role_name ";
			}
			if(criteria.getBusiness_role() != null && criteria.getBusiness_role().length() > 0) {
				sqlString += " and business_role like :business_role ";
			}
		}
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResRole.class);
			if( criteria != null ) {
				if(criteria.getBusiness_role_name() != null && criteria.getBusiness_role_name().length() > 0) {
					q.setString( "business_role_name", "%" + criteria.getBusiness_role_name() + "%" );
				}
				if(criteria.getBusiness_role() != null && criteria.getBusiness_role().length() > 0) {
					q.setString( "business_role", "%" + criteria.getBusiness_role() + "%" );
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
		String sqlString = "select count(*) from res_role where 1 = 1 ";
		if( criteria != null ) {
			if( criteria != null ) {
				if(criteria.getBusiness_role_name() != null && criteria.getBusiness_role_name().length() > 0) {
					sqlString += " and business_role_name like :business_role_name ";
				}
				if(criteria.getBusiness_role() != null && criteria.getBusiness_role().length() > 0) {
					sqlString += " and business_role like :business_role ";
				}
			}
		}
		
		try {
			Query q = session.createSQLQuery(sqlString);
			if( criteria != null ) {
				if( criteria != null ) {
					if(criteria.getBusiness_role_name() != null && criteria.getBusiness_role_name().length() > 0) {
						q.setString( "business_role_name", "%" + criteria.getBusiness_role_name() + "%" );
					}
					if(criteria.getBusiness_role() != null && criteria.getBusiness_role().length() > 0) {
						q.setString( "business_role", "%" + criteria.getBusiness_role() + "%" );
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
	public void UpdateFeatureRoleResource(int id, List<Integer> featureIds)
			throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		String sqlString = "delete from res_role_resource where roleid = :roleid and restype = :restype ";
		
		try {
			Query q = session.createSQLQuery(sqlString);
			q.setInteger("roleid", id);
			q.setInteger("restype", ResRoleResource.RESTYPEFEATURE);
			q.executeUpdate();
			
			ResRoleResource rr;
			if(featureIds != null) {
				for(int i = 0; i<featureIds.size(); i++) {
					rr = new ResRoleResource();
					rr.setRoleid(id);
					rr.setResid(featureIds.get(i));
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
	public void UpdateDataRoleResource(int id, List<Integer> dataIds)
			throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		String sqlString = "delete from res_role_resource where roleid = :roleid and restype = :restype ";
		
		try {
			Query q = session.createSQLQuery(sqlString);
			q.setInteger("roleid", id);
			q.setInteger("restype", ResRoleResource.RESTYPEDATA);
			q.executeUpdate();
			
			ResRoleResource rr;
			if( dataIds != null) {
				for(int i = 0; i<dataIds.size(); i++) {
					rr = new ResRoleResource();
					rr.setRoleid(id);
					rr.setResid(dataIds.get(i));
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
	public List<ResRoleResource> GetRoleResourcesByRoleid(int id)
			throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<ResRoleResource> rs = null;
		String sqlString = "select * from res_role_resource where roleid = :roleid ";
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResRoleResource.class);
			q.setInteger("roleid", id);
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
	public ResFeature GetFeatureById(int id) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		ResFeature rs = null;
		String sqlString = "select * from res_feature where id = :id ";
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResFeature.class);
			q.setInteger("id", id);
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
	public ResData GetDataById(int id) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		ResData rs = null;
		String sqlString = "select * from res_data where id = :id ";
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResData.class);
			q.setInteger("id", id);
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
