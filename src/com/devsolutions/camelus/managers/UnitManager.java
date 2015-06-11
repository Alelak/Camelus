package com.devsolutions.camelus.managers;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.devsolutions.camelus.entities.Unit;
import com.devsolutions.camelus.mappers.UnitMapper;
import com.devsolutions.camelus.services.DBConnection;

public class UnitManager {

	public static List<Unit> getAll() {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		List<Unit> units = session.getMapper(UnitMapper.class).getAll();
		session.close();
		return units;

	}

	public static void add(String description) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		session.getMapper(UnitMapper.class).add(description);
		session.commit();
		session.close();
	}
}
