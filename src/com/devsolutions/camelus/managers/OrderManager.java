package com.devsolutions.camelus.managers;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.devsolutions.camelus.entities.Order;
import com.devsolutions.camelus.mappers.OrderMapper;
import com.devsolutions.camelus.services.DBConnection;

public class OrderManager {

	public static void add(Order order) {

		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		session.getMapper(OrderMapper.class).add(order);
		session.commit();
		session.close();

	}

	public static List<Order> getAll() {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		List<Order> orders = session.getMapper(OrderMapper.class).getAll();
		session.close();
		return orders;
	}

	public static List<Order> getByVendorId(int vendor_id) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		List<Order> orders = session.getMapper(OrderMapper.class)
				.getByVendorId(vendor_id);
		session.close();
		return orders;

	}

	public static List<Order> getByClientId(long client_id) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		List<Order> orders = session.getMapper(OrderMapper.class)
				.getByClientId(client_id);
		session.close();
		return orders;

	}

	public static Order getById(long id) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		Order order = session.getMapper(OrderMapper.class).getById(id);
		session.close();
		return order;

	}
}
