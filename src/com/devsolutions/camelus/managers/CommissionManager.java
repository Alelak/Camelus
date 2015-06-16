package com.devsolutions.camelus.managers;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.devsolutions.camelus.entities.Commission;
import com.devsolutions.camelus.mappers.CommissionMapper;
import com.devsolutions.camelus.services.DBConnection;

public class CommissionManager {

	public static void add(Commission commission) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		session.getMapper(CommissionMapper.class).add(commission);
		session.commit();
		session.close();
	}

	public static List<Commission> getAll() {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		List<Commission> commissions = session
				.getMapper(CommissionMapper.class).getAll();
		session.close();
		return commissions;
	}
	
	public static Commission getById(int id) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		Commission commission = session
				.getMapper(CommissionMapper.class).getById(id);
		session.close();
		return commission;
	}

}
