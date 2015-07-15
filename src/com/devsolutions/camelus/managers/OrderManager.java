package com.devsolutions.camelus.managers;

import java.util.Collections;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;

import com.devsolutions.camelus.auditing.Audit;
import com.devsolutions.camelus.auditing.AuditTypes;
import com.devsolutions.camelus.auditing.AuditUtils;
import com.devsolutions.camelus.entities.Order;
import com.devsolutions.camelus.entities.OrderTV;
import com.devsolutions.camelus.mappers.OrderMapper;
import com.devsolutions.camelus.services.DBConnection;
import com.devsolutions.camelus.services.Session;
import com.devsolutions.camelus.utils.FXUtils;

public class OrderManager {

	public static void add(Order order) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		session.getMapper(OrderMapper.class).add(order);
		try {
			session.commit();
		} catch (PersistenceException e) {
			FXUtils.openDBErrorDialog();
		} finally {
			session.close();
		}
		if (Session.vendor != null) {
			AuditUtils.getAuditingService().setAudit(
					new Audit(Session.vendor.getLogin(), AuditTypes.INSERT,
							"a ajouter une  commande id : " + order.getId()));
		} else {
			AuditUtils.getAuditingService().setAudit(
					new Audit(Session.admin.getLogin(), AuditTypes.INSERT,
							"a ajouter une  commande id : " + order.getId()));
		}
		AuditUtils.getAuditingService().start();
	}

	public static List<Order> getAll() {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		List<Order> orders = Collections.emptyList();
		try {
			orders = session.getMapper(OrderMapper.class).getAll();
		} catch (PersistenceException e) {
			FXUtils.openDBErrorDialog();
		} finally {

			session.close();
		}
		return orders;
	}

	public static List<OrderTV> getByVendorId(int vendor_id) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		List<OrderTV> orders = Collections.emptyList();
		try {
			orders = session.getMapper(OrderMapper.class).getByVendorId(
					vendor_id);
		} catch (PersistenceException e) {
			FXUtils.openDBErrorDialog();
		} finally {

			session.close();
		}
		return orders;
	}

	public static List<Order> getByMonthAndYear(int vendor_id, int year,
			int month) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		List<Order> orders = Collections.emptyList();
		try {
			orders = session.getMapper(OrderMapper.class).getByMonthAndYear(
					vendor_id, year, month);
		} catch (PersistenceException e) {
			FXUtils.openDBErrorDialog();
		} finally {

			session.close();
		}
		return orders;
	}

	public static List<Order> getByYear(int vendor_id, int year) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		List<Order> orders = Collections.emptyList();
		try {
			orders = session.getMapper(OrderMapper.class).getByYear(vendor_id,
					year);
		} catch (PersistenceException e) {
			FXUtils.openDBErrorDialog();
		} finally {

			session.close();
		}
		return orders;
	}

	public static List<OrderTV> getAllTV() {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		List<OrderTV> orders = Collections.emptyList();
		try {
			orders = session.getMapper(OrderMapper.class).getAllTV();
		} catch (PersistenceException e) {
			FXUtils.openDBErrorDialog();
		} finally {

			session.close();
		}
		return orders;
	}

	public static List<Order> getByClientId(long client_id) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		List<Order> orders = Collections.emptyList();
		try {
			orders = session.getMapper(OrderMapper.class).getByClientId(
					client_id);
		} catch (PersistenceException e) {
			FXUtils.openDBErrorDialog();
		} finally {

			session.close();
		}
		return orders;
	}

	public static Order getById(long id) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		Order order = null;
		try {
			order = session.getMapper(OrderMapper.class).getById(id);
		} catch (PersistenceException e) {
			FXUtils.openDBErrorDialog();
		}

		session.close();
		return order;
	}

	public static void cancel(long id) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		session.getMapper(OrderMapper.class).cancel(id);
		try {
			session.commit();
		} catch (PersistenceException e) {
			FXUtils.openDBErrorDialog();
		} finally {
			session.close();
		}
		if (Session.vendor != null) {
			AuditUtils.getAuditingService().setAudit(
					new Audit(Session.vendor.getLogin(), AuditTypes.UPDATE,
							"a annulé une commande id : " + id));
		} else {
			AuditUtils.getAuditingService().setAudit(
					new Audit(Session.admin.getLogin(), AuditTypes.UPDATE,
							"a annulé une commande id : " + id));
		}
		AuditUtils.getAuditingService().start();
	}
}
