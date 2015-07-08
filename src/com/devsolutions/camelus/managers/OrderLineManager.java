package com.devsolutions.camelus.managers;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.devsolutions.camelus.entities.OrderLine;
import com.devsolutions.camelus.entities.OrderLineTV;
import com.devsolutions.camelus.mappers.OrderLineMapper;
import com.devsolutions.camelus.services.DBConnection;

public class OrderLineManager {

	public static void add(OrderLine orderLine) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		session.getMapper(OrderLineMapper.class).add(orderLine);
		session.commit();
		session.close();
	}

	public static List<OrderLineTV> getByOrderId(long order_id) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		List<OrderLineTV> orderLinesTV = session.getMapper(
				OrderLineMapper.class).getByOrderId(order_id);
		session.close();
		return orderLinesTV;
	}

	public static List<OrderLine> getByProductId(long product_id) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		List<OrderLine> orderLines = session.getMapper(OrderLineMapper.class)
				.getByProductId(product_id);
		session.close();
		return orderLines;
	}
}
