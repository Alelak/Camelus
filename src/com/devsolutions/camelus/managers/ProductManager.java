package com.devsolutions.camelus.managers;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.devsolutions.camelus.entities.Product;
import com.devsolutions.camelus.mappers.ProductMapper;
import com.devsolutions.camelus.services.DBConnection;

public class ProductManager {
	public static List<Product> getAll() {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		List<Product> products = session.getMapper(ProductMapper.class)
				.getAll();
		session.close();
		return products;
	}

	public static Product getById(int id) {
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
	}

	public static void update(Product product) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		session.getMapper(ProductMapper.class).update(product);
		session.commit();
		session.close();
	}

	public static void delete(int id) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		session.getMapper(ProductMapper.class).delete(id);
		session.commit();
		session.close();
	}

}
