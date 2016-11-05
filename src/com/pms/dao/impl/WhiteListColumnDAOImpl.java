package com.pms.dao.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.pms.dao.WhiteListColumnDAO;
import com.pms.model.WhiteListColumn;
import com.pms.model.HibernateUtil;

public class WhiteListColumnDAOImpl implements WhiteListColumnDAO {

	@Override
	public WhiteListColumn WhiteListColumnAdd(WhiteListColumn column) throws Exception {
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		String sqlString = "select * from task where columnkey = :columnkey ";
		WhiteListColumn rs = null;
		try
		{
			Query q = session.createSQLQuery(sqlString).addEntity(WhiteListColumn.class);
			q.setString("columnkey", column.getColumnKey());

			rs = (WhiteListColumn)q.uniqueResult();
			if(rs == null) {
				session.save(column);
			}
			tx.commit();
		}
		catch(ConstraintViolationException cne){
			tx.rollback();
			System.out.println(cne.getSQLException().getMessage());
			throw new Exception("存在重名白名单列。");
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
		return column;
	}

	@Override
	public WhiteListColumn GetWhiteListColumnByKey(String columnKey) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		WhiteListColumn rs = null;
		String sqlString = "select * from whitelist_column where columnkey = :columnkey ";
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(WhiteListColumn.class);
			q.setString( "columnkey", columnKey );
			rs = (WhiteListColumn)q.uniqueResult();
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
