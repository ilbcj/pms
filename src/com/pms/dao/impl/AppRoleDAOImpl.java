package com.pms.dao.impl;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.pms.dao.AppRoleDAO;
import com.pms.model.AppRole;
import com.pms.model.HibernateUtil;

public class AppRoleDAOImpl implements AppRoleDAO {

	@Override
	public AppRole AppRoleAdd(AppRole ar) throws Exception {
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		try
		{
			ar = (AppRole) session.merge(ar);
			tx.commit();
		}
		catch(ConstraintViolationException cne){
			tx.rollback();
			System.out.println(cne.getSQLException().getMessage());
			throw new Exception("存在重名应用角色。");
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
		return ar;
	}

	@Override
	public void AppRoleMod(AppRole ar) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		try
		{
			session.update(ar);
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
	public void AppRoleDel(AppRole ar) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		try
		{
			session.delete(ar);
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
	public List<AppRole> GetAppRoles(AppRole criteria, int page,
			int rows) throws Exception{
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<AppRole> rs = null;
		String sqlString = "select * from application where 1 = 1 ";
		if( criteria != null ) {
			if(criteria.getName() != null && criteria.getName().length() > 0) {
				sqlString += " and name like :name ";
			}
			if(criteria.getCode() != null && criteria.getCode().length() > 0) {
				sqlString += " and code like :code ";
			}
		}
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(AppRole.class);
			if( criteria != null ) {
				if(criteria.getName() != null && criteria.getName().length() > 0) {
					q.setString( "name", "%" + criteria.getName() + "%" );
				}
				if(criteria.getCode() != null && criteria.getCode().length() > 0) {
					q.setString( "code", "%" + criteria.getCode() + "%" );
				}
			}
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

//	@Override
//	public int GetApplicationsCount(Application criteria) throws Exception {
//		Session session = HibernateUtil.currentSession();
//		Transaction tx = session.beginTransaction();
//		int rs;
//		String sqlString = "select count(*) from application where 1 = 1 ";
//		if( criteria != null ) {
//			if( criteria != null ) {
//				if(criteria.getName() != null && criteria.getName().length() > 0) {
//					sqlString += " and name like :name ";
//				}
//				if(criteria.getFlag() != null && criteria.getFlag().length() > 0) {
//					sqlString += " and flag like :flag ";
//				}
//			}
//		}
//		
//		try {
//			Query q = session.createSQLQuery(sqlString);
//			if( criteria != null ) {
//				if( criteria != null ) {
//					if(criteria.getName() != null && criteria.getName().length() > 0) {
//						q.setString( "name", "%" + criteria.getName() + "%" );
//					}
//					if(criteria.getFlag() != null && criteria.getFlag().length() > 0) {
//						q.setString( "flag", "%" + criteria.getFlag() + "%" );
//					}
//				}
//			}
//			rs = ((BigInteger)q.uniqueResult()).intValue();
//			tx.commit();
//		} catch (Exception e) {
//			e.printStackTrace();
//			tx.rollback();
//			System.out.println(e.getMessage());
//			throw e;
//		} finally {
//			HibernateUtil.closeSession();
//		}
//		return rs;
//	}
//	
//	@SuppressWarnings("unchecked")
//	public List<Application> GetApplicationsWithNopage() throws Exception
//	{
//		Session session = HibernateUtil.currentSession();
//		Transaction tx = session.beginTransaction();
//		List<Application> rs = null;
//		String sqlString = "select * from application ";
//				
//		try {
//			Query q = session.createSQLQuery(sqlString).addEntity(Application.class);
//			rs = q.list();
//			tx.commit();
//		} catch (Exception e) {
//			e.printStackTrace();
//			tx.rollback();
//			System.out.println(e.getMessage());
//			throw e;
//		} finally {
//			HibernateUtil.closeSession();
//		}
//		return rs;
//	}


}
