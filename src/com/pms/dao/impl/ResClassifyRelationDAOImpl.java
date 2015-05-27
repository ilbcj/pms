package com.pms.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.pms.dao.ResClassifyRelationDAO;
import com.pms.model.HibernateUtil;
import com.pms.model.ResRelationClassify;

public class ResClassifyRelationDAOImpl implements ResClassifyRelationDAO {

	@Override
	public ResRelationClassify ResRelationClassifySave(ResRelationClassify rc)
			throws Exception {
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		
		ResRelationClassify rs = null;
		String sqlString = "select * from WA_CLASSIFY_RELATION where ID = :ID ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResRelationClassify.class);
			q.setInteger("ID", rc.getId());
			rs = (ResRelationClassify) q.uniqueResult();
			
			if(rs != null) {
				rc.setId(rs.getId());
				rc.setDATA_VERSION(rs.getDATA_VERSION() + 1);
			} else {
				rc.setDATA_VERSION( 1 );
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
					Locale.SIMPLIFIED_CHINESE);
			String timenow = sdf.format(new Date());
			rc.setLATEST_MOD_TIME(timenow);
			
			rc = (ResRelationClassify) session.merge(rc);
			tx.commit();
		} catch(ConstraintViolationException cne){
			tx.rollback();
			System.out.println(cne.getSQLException().getMessage());
			throw new Exception("存在重名数据集-字段分类关系。");
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
		return rc;
	}

}
