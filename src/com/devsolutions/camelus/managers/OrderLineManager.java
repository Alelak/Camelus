package com.devsolutions.camelus.managers;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.devsolutions.camelus.auditing.Audit;
import com.devsolutions.camelus.auditing.AuditTypes;
import com.devsolutions.camelus.auditing.AuditUtils;
import com.devsolutions.camelus.entities.OrderLine;
import com.devsolutions.camelus.entities.OrderLineTV;
import com.devsolutions.camelus.mappers.OrderLineMapper;
import com.devsolutions.camelus.services.DBConnection;
import com.devsolutions.camelus.services.Session;

public class OrderLineManager {

	public static void add(OrderLine orderLine) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		session.getMapper(OrderLineMapper.class).add(orderLine);
		session.commit();
		session.close();
		AuditUtils.getAuditingService().setAudit(
				new Audit(Session.vendor.getLogin(), AuditTypes.INSERT,
						"a ajouter une ligne de commande id : "
								+ orderLine.getId()));
		AuditUtils.getAuditingService().start();
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
