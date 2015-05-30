package com.pms.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.pms.dao.ResDatasetSensitiveDAO;
import com.pms.model.HibernateUtil;
import com.pms.model.ResDataSetSensitive;

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
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
					Locale.SIMPLIFIED_CHINESE);
			String timenow = sdf.format(new Date());
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

}
