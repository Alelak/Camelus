package com.devsolutions.camelus.managers;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.devsolutions.camelus.auditing.Audit;
import com.devsolutions.camelus.auditing.AuditTypes;
import com.devsolutions.camelus.auditing.AuditUtils;
import com.devsolutions.camelus.entities.Product;
import com.devsolutions.camelus.entities.ProductTableView;
import com.devsolutions.camelus.mappers.ProductMapper;
import com.devsolutions.camelus.services.DBConnection;
import com.devsolutions.camelus.services.Session;

public class ProductManager {
	public static List<Product> getAll() {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		List<Product> products = session.getMapper(ProductMapper.class)
				.getAll();
		session.close();
		return products;
	}

	public static List<ProductTableView> getAllProductTableView() {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		List<ProductTableView> products = session
				.getMapper(ProductMapper.class).getAllProductTableView();
		session.close();
		return products;
	}

	public static Product getById(long id) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		Product product = session.getMapper(ProductMapper.class).getById(id);
		session.close();
		return product;
	}

	public static void add(Product product) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		session.getMapper(ProductMapper.class).add(product);
		session.commit();
		session.close();
		AuditUtils.getAuditingService().setAudit(
				new Audit(Session.admin.getLogin(), AuditTypes.INSERT,
						"a ajouter un produit id : " + product.getId()));
		AuditUtils.getAuditingService().start();
	}

	public static void update(Product product) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		session.getMapper(ProductMapper.class).update(product);
		session.commit();
		session.close();
		AuditUtils.getAuditingService().setAudit(
				new Audit(Session.admin.getLogin(), AuditTypes.UPDATE,
						"a modifier un produit id : " + product.getId()));
		AuditUtils.getAuditingService().start();
	}

	public static void delete(long id) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		session.getMapper(ProductMapper.class).delete(id);
		session.commit();
		session.close();
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
