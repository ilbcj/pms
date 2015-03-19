package com.pms.dao.impl;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.pms.dao.OrganizationDAO;
import com.pms.model.HibernateUtil;
import com.pms.model.Organization;

public class OrganizationDAOImpl implements OrganizationDAO {

	@Override
	public Organization OrgNodeAdd(Organization node) throws Exception {
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		try
		{
			node = (Organization) session.merge(node);
			tx.commit();
		}
		catch(ConstraintViolationException cne){
			tx.rollback();
			System.out.println(cne.getSQLException().getMessage());
			throw new Exception("存在重名机构。");
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
		return node;
	}

	@Override
	public void OrgNodeMod(Organization node) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		try
		{
			session.update(node);
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

	@Override
	public void OrgNodeDel(Organization node) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		try
		{
			session.delete(node);
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
	public List<Organization> GetOrgNodeByParentId(int pid) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<Organization> rs = null;
		String sqlString = "select * from organization where parent_id = :parent_id ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(Organization.class);
			q.setInteger("parent_id", pid);
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
	public boolean OrgHasChild(int pid) throws Exception {
		int rs = GetOrgNodeCountByParentId(pid);
		return rs > 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Organization> GetOrgNodeByParentId(int pid, int page, int rows)
			throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<Organization> rs = null;
		String sqlString = "select * from organization where parent_id = :parent_id ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(Organization.class);
			q.setInteger("parent_id", pid);
			q.setFirstResult((page-1) * rows);   
			q.setMaxResults(rows);   
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
	public List<Organization> GetOrgNodeByParentId(int pid, Organization condition)
			throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<Organization> rs = null;
		String sqlString = "select * from organization where parent_id = :parent_id ";
		if( condition != null ) {
			if(condition.getName() != null && condition.getName().length() > 0) {
				sqlString += " and name like :name ";
			}
			if(condition.getUid() != null && condition.getUid().length() > 0) {
				sqlString += " and uid = :uid ";
			}
			if(condition.getOrg_level() != null && condition.getOrg_level().length() > 0) {
				sqlString += " and org_level = :org_level ";
			}
		}
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(Organization.class);
			q.setInteger("parent_id", pid);
			if( condition != null ) {
				if(condition.getName() != null && condition.getName().length() > 0) {
					q.setString( "name", "%" + condition.getName() + "%" );
				}
				if(condition.getUid() != null && condition.getUid().length() > 0) {
					q.setString( "uid", condition.getUid() );
				}
				if(condition.getOrg_level() != null && condition.getOrg_level().length() > 0) {
					q.setString( "org_level", condition.getOrg_level() );
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
	public int GetOrgNodeCountByParentId(int pid) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		int rs;
		String sqlString = "select count(*) from organization where parent_id = :parent_id ";
		try {
			Query q = session.createSQLQuery(sqlString);
			q.setInteger("parent_id", pid);
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
	public Organization GetOrgNodeById(int id) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		Organization rs = null;
		String sqlString = "select * from organization where id = :id ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(Organization.class);
			q.setInteger("id", id);
			rs = (Organization) q.uniqueResult();
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
