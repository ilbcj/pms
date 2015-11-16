package com.pms.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.pms.dao.ResRowRelationDAO;
import com.pms.model.HibernateUtil;
import com.pms.model.ResRelationRow;

public class ResRowRelationDAOImpl implements ResRowRelationDAO {

	@Override
	public ResRelationRow ResRelationRowSave(ResRelationRow rr)
			throws Exception {
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		
		ResRelationRow rs = null;
		String sqlString = "select * from WA_ROW_RELATION where ID = :ID ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResRelationRow.class);
			q.setInteger("ID", rr.getId());
			rs = (ResRelationRow) q.uniqueResult();
			
			if(rs != null) {
				rr.setId(rs.getId());
				rr.setDATA_VERSION(rs.getDATA_VERSION() + 1);
			} else {
				rr.setDATA_VERSION( 1 );
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
					Locale.SIMPLIFIED_CHINESE);
			String timenow = sdf.format(new Date());
			rr.setLATEST_MOD_TIME(timenow);
			
			rr = (ResRelationRow) session.merge(rr);
			tx.commit();
		} catch(ConstraintViolationException cne){
			tx.rollback();
			System.out.println(cne.getSQLException().getMessage());
			throw new Exception("存在重名数据集-字段-字段值。");
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
		return rr;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ResRelationRow> QueryAllResRelationRow() throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		
		List<ResRelationRow> rs = null;
		String sqlString = "select * from WA_ROW_RELATION ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResRelationRow.class);
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
	public int ResRowRelationImportClear() throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		
		int rs = 0;
		String sqlString = "delete from WA_ROW_RELATION ";
		try {
			Query q = session.createSQLQuery(sqlString);
			rs = q.executeUpdate();
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
