package com.pms.dao.impl;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.pms.dao.ResDatasetDAO;
import com.pms.model.HibernateUtil;
import com.pms.model.ResDataSet;
import com.pms.model.ResDataSetPrivate;
import com.pms.util.DateTimeUtil;

public class ResDatasetDAOImpl implements ResDatasetDAO {

	@Override
	public ResDataSet ResDataSetSave(ResDataSet ds)
			throws Exception {
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		
		ResDataSet rs = null;
		String sqlString = "select * from WA_DATASET where DATA_SET = :DATA_SET ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResDataSet.class);
			q.setString("DATA_SET", ds.getDATA_SET());
			rs = (ResDataSet) q.uniqueResult();
			
			if(rs != null) {
				ds.setId(rs.getId());
				ds.setDATA_VERSION(rs.getDATA_VERSION() + 1);
			} else {
				ds.setDATA_VERSION( 1 );
			}
			
			String timenow = DateTimeUtil.GetCurrentTime();
			ds.setLATEST_MOD_TIME(timenow);
			
			ds = (ResDataSet) session.merge(ds);
			tx.commit();
		} catch(ConstraintViolationException cne){
			tx.rollback();
			System.out.println(cne.getSQLException().getMessage());
			throw new Exception("存在重名数据集。");
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
		return ds;
	}
	
	@Override
	public ResDataSetPrivate ResDataSetPrivateSave(ResDataSetPrivate dsp)
			throws Exception {
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		
		ResDataSetPrivate rs = null;
		String sqlString = "select * from WA_DATASET_PRIVATE where DATA_SET = :DATA_SET ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResDataSetPrivate.class);
			q.setString("DATA_SET", dsp.getDATA_SET());
			rs = (ResDataSetPrivate) q.uniqueResult();
			
			if(rs != null) {
				dsp.setId(rs.getId());
				dsp.setDATA_VERSION(rs.getDATA_VERSION() + 1);
			} else {
				dsp.setDATA_VERSION( 1 );
			}
			
			String timenow = DateTimeUtil.GetCurrentTime();
			dsp.setLATEST_MOD_TIME(timenow);
			
			dsp = (ResDataSetPrivate) session.merge(dsp);
			tx.commit();
		} catch(ConstraintViolationException cne){
			tx.rollback();
			System.out.println(cne.getSQLException().getMessage());
			throw new Exception("存在重名私有数据集。");
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
		return dsp;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ResDataSet> QueryAllDataSet() throws Exception {
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		
		List<ResDataSet> rs = null;
		String sqlString = "select * from WA_DATASET ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResDataSet.class);
			rs = q.list();
			tx.commit();
		} catch(Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return rs;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ResDataSetPrivate> QueryAllDataSetPrivate() throws Exception {
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		
		List<ResDataSetPrivate> rs = null;
		String sqlString = "select * from WA_DATASET_PRIVATE ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResDataSetPrivate.class);
			rs = q.list();
			tx.commit();
		} catch(Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return rs;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ResDataSet> QueryRowDataSet() throws Exception {
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		
		List<ResDataSet> rs = null;
		String sqlString = "SELECT * FROM WA_DATASET WHERE DATA_SET IN (SELECT DISTINCT DATA_SET FROM WA_ROW_RELATION) ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResDataSet.class);
			rs = q.list();
			tx.commit();
		} catch(Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return rs;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ResDataSet> QueryColumnDataSet() throws Exception {
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		
		List<ResDataSet> rs = null;
		String sqlString = "SELECT * FROM WA_DATASET WHERE DATA_SET IN (SELECT DISTINCT DATA_SET FROM WA_COLUMN_RELATION) ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResDataSet.class);
			rs = q.list();
			tx.commit();
		} catch(Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return rs;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ResDataSet> QueryClassifyDataSet() throws Exception {
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		
		List<ResDataSet> rs = null;
		String sqlString = "SELECT * FROM WA_DATASET WHERE DATA_SET IN (SELECT DISTINCT DATA_SET FROM WA_CLASSIFY_RELATION) ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResDataSet.class);
			rs = q.list();
			tx.commit();
		} catch(Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return rs;
	}

}
