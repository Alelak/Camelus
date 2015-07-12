package com.devsolutions.camelus.managers;

import java.util.Collections;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;

import com.devsolutions.camelus.auditing.Audit;
import com.devsolutions.camelus.auditing.AuditTypes;
import com.devsolutions.camelus.auditing.AuditUtils;
import com.devsolutions.camelus.entities.Product;
import com.devsolutions.camelus.entities.ProductTV;
import com.devsolutions.camelus.mappers.ProductMapper;
import com.devsolutions.camelus.services.DBConnection;
import com.devsolutions.camelus.services.Session;
import com.devsolutions.camelus.utils.FXUtils;

public class ProductManager {
	public static List<Product> getAll() {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		List<Product> products = session.getMapper(ProductMapper.class)
				.getAll();
		session.close();
		return products;
	}

	public static List<Product> getByCategory(int category_id) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		List<Product> products = Collections.emptyList();
		try {
			products = session.getMapper(ProductMapper.class).getByCategory(
					category_id);
		} catch (PersistenceException e) {
			FXUtils.openDBErrorDialog();
		} finally {
			session.close();
		}

		return products;
	}

	public static List<Product> getByUnit(int unit_id) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		List<Product> products = Collections.emptyList();
		try {
			products = session.getMapper(ProductMapper.class)
					.getByUnit(unit_id);
		} catch (PersistenceException e) {
			FXUtils.openDBErrorDialog();
		} finally {
			session.close();
		}
		return products;
	}

	public static List<ProductTV> getAllProductTableView() {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();

		List<ProductTV> products = Collections.emptyList();
		try {
			products = session.getMapper(ProductMapper.class)
					.getAllProductTableView();
		} catch (PersistenceException e) {
			FXUtils.openDBErrorDialog();
		} finally {
			session.close();
		}
		return products;
	}

	public static Product getById(long id) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		Product product = null;
		try {

		} catch (PersistenceException e) {
			product = session.getMapper(ProductMapper.class).getById(id);
		}

		finally {
			session.close();
		}
		return product;
	}

	public static void add(Product product) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		session.getMapper(ProductMapper.class).add(product);
		try {
			session.commit();
		} catch (PersistenceException e) {
			FXUtils.openDBErrorDialog();
		} finally {
			session.close();
		}
		AuditUtils.getAuditingService().setAudit(
				new Audit(Session.admin.getLogin(), AuditTypes.INSERT,
						"a ajouter un produit id : " + product.getId()));
		AuditUtils.getAuditingService().start();
	}

	public static void update(Product product) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		session.getMapper(ProductMapper.class).update(product);
		try {
			session.commit();
		} catch (PersistenceException e) {
			FXUtils.openDBErrorDialog();
		} finally {
			session.close();
		}
		AuditUtils.getAuditingService().setAudit(
				new Audit(Session.admin.getLogin(), AuditTypes.UPDATE,
						"a modifier un produit id : " + product.getId()));
		AuditUtils.getAuditingService().start();
	}

	public static void delete(long id) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		session.getMapper(ProductMapper.class).delete(id);
		try {
			session.commit();
		} catch (PersistenceException e) {
			FXUtils.openDBErrorDialog();
		} finally {
			session.close();
		}
		AuditUtils.getAuditingService().setAudit(
				new Audit(Session.admin.getLogin(), AuditTypes.DELETE,
						"a supprimer un produit id : " + id));
		AuditUtils.getAuditingService().start();
	}

	public static void incrementQuantity(int quantity, long id) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		session.getMapper(ProductMapper.class).incrementQuantity(quantity, id);
		session.commit();
		session.close();
	}

	public static void decrementQuantity(int quantity, long id) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		session.getMapper(ProductMapper.class).decrementQuantity(quantity, id);
		session.commit();
		session.close();
	}
}
