package com.pms.dao.impl;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.pms.dao.ResDatasetSensitiveDAO;
import com.pms.model.HibernateUtil;
import com.pms.model.ResDataSetSensitive;
import com.pms.util.DateTimeUtil;

public class ResDatasetSensitiveDAOImpl implements ResDatasetSensitiveDAO {

	@Override
	public ResDataSetSensitive ResDataSetSensitiveSave(ResDataSetSensitive dss)
			throws Exception {
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		
		ResDataSetSensitive rs = null;
		String sqlString = "select * from WA_DATASET_SENSITIVE where DATASET_SENSITIVE_LEVEL = :DATASET_SENSITIVE_LEVEL ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResDataSetSensitive.class);
			q.setString("DATASET_SENSITIVE_LEVEL", dss.getDATASET_SENSITIVE_LEVEL());
			rs = (ResDataSetSensitive) q.uniqueResult();
			
			if(rs != null) {
				dss.setId(rs.getId());
				dss.setDATA_VERSION(rs.getDATA_VERSION() + 1);
			} else {
				dss.setDATA_VERSION( 1 );
			}
			
			String timenow = DateTimeUtil.GetCurrentTime();
			dss.setLATEST_MOD_TIME(timenow);
			
			dss = (ResDataSetSensitive) session.merge(dss);
			tx.commit();
		} catch(ConstraintViolationException cne){
			tx.rollback();
			System.out.println(cne.getSQLException().getMessage());
			throw new Exception("存在重名数据集敏感度。");
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
		return dss;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ResDataSetSensitive> QueryAllDataSetSensitive()
			throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		
		List<ResDataSetSensitive> rs = null;
		String sqlString = "select * from WA_DATASET_SENSITIVE ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResDataSetSensitive.class);
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
	public List<ResDataSetSensitive> QueryDataSetSensitive(String dataSet)
			throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		
		List<ResDataSetSensitive> rs = null;
		String sqlString = "SELECT * FROM WA_DATASET_SENSITIVE WHERE DATASET_SENSITIVE_LEVEL IN (SELECT DATASET_SENSITIVE_LEVEL FROM WA_DATASET WHERE DATA_SET = :DATA_SET ) ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResDataSetSensitive.class);
			q.setString("DATA_SET", dataSet);
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
