package com.devsolutions.camelus.managers;

import java.util.List;
import org.apache.ibatis.session.SqlSession;
import com.devsolutions.camelus.entities.Admin;
import com.devsolutions.camelus.mappers.AdminMapper;
import com.devsolutions.camelus.services.DBConnection;

public class AdminManager {
	public static List<Admin> getAll() {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		List<Admin> admins = session.getMapper(AdminMapper.class).getAll();
		session.close();
		return admins;
	}

	public static Admin getByUserName(String login) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		Admin admin = session.getMapper(AdminMapper.class).getByUserName(login);
		session.close();
		return admin;
	}

	public static Admin getBySin(String sin) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		Admin admin = session.getMapper(AdminMapper.class).getBySin(sin);
		session.close();
		return admin;
	}

	public static void add(Admin admin) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		session.getMapper(AdminMapper.class).add(admin);
		session.commit();
		session.close();
	}

	public static void update(Admin admin) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		session.getMapper(AdminMapper.class).update(admin);
		session.commit();
		session.close();
	}

	public static void delete(int id) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		session.getMapper(AdminMapper.class).delete(id);
		session.commit();
		session.close();
	}
}
