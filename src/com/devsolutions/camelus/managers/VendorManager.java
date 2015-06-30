package com.devsolutions.camelus.managers;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.devsolutions.camelus.entities.Vendor;
import com.devsolutions.camelus.mappers.VendorMapper;
import com.devsolutions.camelus.services.DBConnection;

public class VendorManager {
	public static List<Vendor> getAll() {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		List<Vendor> vendors = session.getMapper(VendorMapper.class).getAll();
		session.close();
		return vendors;
	}

	public static Vendor getByUserName(String login) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		Vendor vendor = session.getMapper(VendorMapper.class).getByUserName(
				login);
		session.close();
		return vendor;
	}

	public static Vendor getBySin(String sin) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		Vendor vendor = session.getMapper(VendorMapper.class).getBySin(sin);
		session.close();
		return vendor;
	}

	public static void add(Vendor vendor) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		session.getMapper(VendorMapper.class).add(vendor);
		session.commit();
		session.close();
	}

	public static void update(Vendor vendor) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		session.getMapper(VendorMapper.class).update(vendor);
		session.commit();
		session.close();
	}

	public static void delete(int id) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		session.getMapper(VendorMapper.class).delete(id);
		session.commit();
		session.close();
	}
}
