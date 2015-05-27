package com.pms.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.pms.dao.ResDatasetDAO;
import com.pms.model.HibernateUtil;
import com.pms.model.ResDataSet;

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
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
					Locale.SIMPLIFIED_CHINESE);
			String timenow = sdf.format(new Date());
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

}
