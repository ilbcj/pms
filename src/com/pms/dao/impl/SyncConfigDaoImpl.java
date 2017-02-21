package com.pms.dao.impl;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.pms.dao.SyncConfigDao;
import com.pms.model.HibernateUtil;
import com.pms.model.SyncConfig;
import com.pms.model.SyncList;
import com.pms.util.DateTimeUtil;

public class SyncConfigDaoImpl implements SyncConfigDao{
	@Override
	public SyncConfig SyncConfigAdd(SyncConfig syncConfig) throws Exception {
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		try
		{
			syncConfig = (SyncConfig) session.merge(syncConfig);
			tx.commit();
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
		return syncConfig;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SyncConfig> GetSyncConfigs( SyncConfig criteria, int page, int rows) throws Exception {		
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<SyncConfig> rs = null;
		String sqlString = "select * from syncconfig where 1 = 1 ";
		if( criteria != null ) {
			if(criteria.getId() != 0) {
				sqlString += " and id = :id ";
			}
			if(criteria.getCLUE_DST_SYS() != null && criteria.getCLUE_DST_SYS().length() > 0) {
				sqlString += " and CLUE_DST_SYS like :CLUE_DST_SYS ";
			}
		}
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(SyncConfig.class);
			if( criteria != null ) {
				if(criteria.getId() != 0) {
					q.setInteger( "id", criteria.getId());
				}
				if(criteria.getCLUE_DST_SYS() != null && criteria.getCLUE_DST_SYS().length() > 0) {
					q.setString( "CLUE_DST_SYS", "%" + criteria.getCLUE_DST_SYS() + "%" );
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
	public int GetSyncConfigsCount(SyncConfig criteria) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		int rs;
		String sqlString = "select count(*) from syncconfig where 1 = 1 ";
//		if( criteria != null ) {
//			if( criteria != null ) {
//				if(criteria.getName() != null && criteria.getName().length() > 0) {
//					sqlString += " and name like :name ";
//				}
//				if(criteria.getRESOURCE_ID() != null && criteria.getRESOURCE_ID().length() > 0) {
//					sqlString += " and RESOURCE_ID = :RESOURCE_ID ";
//				}
//			}
//		}
		
		try {
			Query q = session.createSQLQuery(sqlString);
//			if( criteria != null ) {
//				if( criteria != null ) {
//					if(criteria.getName() != null && criteria.getName().length() > 0) {
//						q.setString( "name", "%" + criteria.getName() + "%" );
//					}
//					if(criteria.getRESOURCE_ID() != null && criteria.getRESOURCE_ID().length() > 0) {
//						q.setString( "RESOURCE_ID", criteria.getRESOURCE_ID());
//					}
//				}
//			}
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
	public List<SyncConfig> GetAllSyncConfigs() throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<SyncConfig> rs = null;
		String sqlString = "select * from syncconfig ";
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(SyncConfig.class);
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
	public SyncList SyncListAdd(SyncList syncList) throws Exception {
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		try
		{
			syncList = (SyncList) session.merge(syncList);
			tx.commit();
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
		return syncList;
	}

	@Override
	public void SyncListMod(SyncList syncList) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		try
		{
			session.update(syncList);
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
	public List<SyncList> GetAllSyncList(int status) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<SyncList> rs = null;
		String sqlString = "select * from synclist where status = :status ";
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(SyncList.class);
			q.setInteger("status", status);
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
	public void UpdateSyncListByUnitAndFilename(String ga_department, String filename, int status) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<SyncList> rs = null;
		String sqlString = "select * from synclist where ga_department = :ga_department and filename = :filename ";
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(SyncList.class);
			q.setString("ga_department", ga_department);
			q.setString("filename", filename);
			rs = q.list();
			
			if(rs != null) {
				for(SyncList sl : rs) {
					sl.setStatus(status);
					sl.setTstamp(DateTimeUtil.GetCurrentTime());
					session.merge(sl);
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
}
