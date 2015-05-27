package com.pms.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.pms.dao.ResColumnDAO;
import com.pms.model.HibernateUtil;
import com.pms.model.ResColumn;

public class ResColumnDAOImpl implements ResColumnDAO {

	@Override
	public ResColumn ResColumnSave(ResColumn c)
			throws Exception {
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		
		ResColumn rs = null;
		String sqlString = "select * from WA_COLUMN where COLUMN_ID = :COLUMN_ID ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResColumn.class);
			q.setString("COLUMN_ID", c.getCOLUMN_ID());
			rs = (ResColumn) q.uniqueResult();
			
			if(rs != null) {
				c.setId(rs.getId());
				c.setDATA_VERSION(rs.getDATA_VERSION() + 1);
			} else {
				c.setDATA_VERSION( 1 );
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
					Locale.SIMPLIFIED_CHINESE);
			String timenow = sdf.format(new Date());
			c.setLATEST_MOD_TIME(timenow);
			
			c = (ResColumn) session.merge(c);
			tx.commit();
		} catch(ConstraintViolationException cne){
			tx.rollback();
			System.out.println(cne.getSQLException().getMessage());
			throw new Exception("存在重名字段。");
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
		return c;
	}

}
