package com.devsolutions.camelus.managers;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.devsolutions.camelus.auditing.Audit;
import com.devsolutions.camelus.auditing.AuditTypes;
import com.devsolutions.camelus.auditing.AuditUtils;
import com.devsolutions.camelus.entities.Order;
import com.devsolutions.camelus.entities.OrderTV;
import com.devsolutions.camelus.mappers.OrderMapper;
import com.devsolutions.camelus.services.DBConnection;
import com.devsolutions.camelus.services.Session;

public class OrderManager {

	public static void add(Order order) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		session.getMapper(OrderMapper.class).add(order);
		session.commit();
		session.close();
		if (Session.vendor != null) {
			AuditUtils.getAuditingService().setAudit(
					new Audit(Session.vendor.getLogin(), AuditTypes.INSERT,
							"a ajouter une  commande id : " + order.getId()));
		}
		else
		{
			AuditUtils.getAuditingService().setAudit(
					new Audit(Session.admin.getLogin(), AuditTypes.INSERT,
							"a ajouter une  commande id : " + order.getId()));
		}
		AuditUtils.getAuditingService().start();
	}

	public static List<Order> getAll() {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		List<Order> orders = session.getMapper(OrderMapper.class).getAll();
		session.close();
		return orders;
	}

	public static List<OrderTV> getByVendorId(int vendor_id) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		List<OrderTV> orders = session.getMapper(OrderMapper.class)
				.getByVendorId(vendor_id);
		session.close();
		return orders;
	}

	public static List<OrderTV> getAllTV() {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		List<OrderTV> orders = session.getMapper(OrderMapper.class).getAllTV();
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

	public static void cancel(long id) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		session.getMapper(OrderMapper.class).cancel(id);
		session.commit();
		session.close();
		if (Session.vendor != null) {
		AuditUtils.getAuditingService().setAudit(
				new Audit(Session.vendor.getLogin(), AuditTypes.UPDATE,
						"a annulé une commande id : " + id));
		}else
		{
			AuditUtils.getAuditingService().setAudit(
					new Audit(Session.admin.getLogin(), AuditTypes.UPDATE,
							"a annulé une commande id : " + id));
		}
		AuditUtils.getAuditingService().start();
	}
}
