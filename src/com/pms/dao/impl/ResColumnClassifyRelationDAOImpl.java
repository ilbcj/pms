package com.pms.dao.impl;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.pms.dao.ResColumnClassifyRelationDAO;
import com.pms.model.HibernateUtil;
import com.pms.model.ResRelationColumnClassify;
import com.pms.util.DateTimeUtil;

public class ResColumnClassifyRelationDAOImpl implements ResColumnClassifyRelationDAO {

	@Override
	public ResRelationColumnClassify ResRelationColumnClassifySave(ResRelationColumnClassify rcc)
			throws Exception {
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		
		ResRelationColumnClassify rs = null;
		String sqlString = "select * from WA_COLUMN_ClASSIFY_REALTION where SECTION_RELATIOIN_CLASS = :SECTION_RELATIOIN_CLASS ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResRelationColumnClassify.class);
			q.setString("SECTION_RELATIOIN_CLASS", rcc.getSECTION_RELATIOIN_CLASS());
			rs = (ResRelationColumnClassify) q.uniqueResult();
			
			if(rs != null) {
				rcc.setId(rs.getId());
				rcc.setDATA_VERSION(rs.getDATA_VERSION() + 1);
			} else {
				rcc.setDATA_VERSION( 1 );
			}
			
			String timenow = DateTimeUtil.GetCurrentTime();
			rcc.setLATEST_MOD_TIME(timenow);
			
			rcc = (ResRelationColumnClassify) session.merge(rcc);
			tx.commit();
		} catch(ConstraintViolationException cne){
			tx.rollback();
			System.out.println(cne.getSQLException().getMessage());
			throw new Exception("存在重名字段分类。");
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
		return rcc;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ResRelationColumnClassify> QueryAllResRelationColumnClassify()
			throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		
		List<ResRelationColumnClassify> rs = null;
		String sqlString = "select * from WA_COLUMN_ClASSIFY_REALTION ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResRelationColumnClassify.class);
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
	public List<ResRelationColumnClassify> QueryResRelationColumnClassify(String dataSet)
			throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		
		List<ResRelationColumnClassify> rs = null;
		String sqlString = " SELECT * FROM WA_COLUMN_CLASSIFY_REALTION WHERE SECTION_RELATIOIN_CLASS IN(SELECT SECTION_RELATIOIN_CLASS FROM WA_CLASSIFY_RELATION WHERE DATA_SET = :DATA_SET ) ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResRelationColumnClassify.class);
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
	
	@Override
	public ResRelationColumnClassify QueryResRelationColumnClassify(String src, String dest) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		
		ResRelationColumnClassify rs = null;
		String sqlString = " SELECT * FROM WA_COLUMN_CLASSIFY_REALTION WHERE SRC_CLASS_CODE = :SRC_CLASS_CODE and DST_CLASS_CODE = :DST_CLASS_CODE ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResRelationColumnClassify.class);
			q.setString("SRC_CLASS_CODE", src);
			q.setString("DST_CLASS_CODE", dest);
			rs = (ResRelationColumnClassify) q.uniqueResult();
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
