package com.devsolutions.camelus.managers;

import java.util.Collections;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;

import com.devsolutions.camelus.entities.OrderLine;
import com.devsolutions.camelus.entities.OrderLineTV;
import com.devsolutions.camelus.mappers.OrderLineMapper;
import com.devsolutions.camelus.services.DBConnection;
import com.devsolutions.camelus.utils.FXUtils;

public class OrderLineManager {

	public static void add(OrderLine orderLine) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		session.getMapper(OrderLineMapper.class).add(orderLine);
		session.commit();
		session.close();

	}

	public static List<OrderLineTV> getAll() {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		List<OrderLineTV> orderLinesTV = Collections.emptyList();
		try {
			orderLinesTV = session.getMapper(OrderLineMapper.class).getAll();
		} catch (PersistenceException e) {
			FXUtils.openDBErrorDialog();
		} finally {
			session.close();
		}

		return orderLinesTV;
	}

	public static List<OrderLineTV> getByOrderId(long order_id) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		List<OrderLineTV> orderLinesTV = Collections.emptyList();
		try {
			orderLinesTV = session.getMapper(OrderLineMapper.class)
					.getByOrderId(order_id);
		} catch (PersistenceException e) {
			FXUtils.openDBErrorDialog();
		} finally {
			session.close();
		}
		return orderLinesTV;
	}

	public static List<OrderLine> getByProductId(long product_id) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		List<OrderLine> orderLines = Collections.emptyList();
		try {
			orderLines = session.getMapper(OrderLineMapper.class)
					.getByProductId(product_id);
		} catch (PersistenceException e) {
			FXUtils.openDBErrorDialog();
		} finally {
			session.close();
		}
		return orderLines;
	}
}
