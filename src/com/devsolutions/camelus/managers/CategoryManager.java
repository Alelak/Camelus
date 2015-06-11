package com.devsolutions.camelus.managers;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.devsolutions.camelus.entities.Category;
import com.devsolutions.camelus.mappers.CategoryMapper;
import com.devsolutions.camelus.services.DBConnection;

public class CategoryManager {

	public static List<Category> getAll() {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		List<Category> categories = session.getMapper(CategoryMapper.class)
				.getAll();
		session.close();
		return categories;

	}

	public static Category getById(int id) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		Category category = session.getMapper(CategoryMapper.class).getById(id);
		session.close();
		return category;

	}

	public static void add(String description) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		session.getMapper(CategoryMapper.class).add(description);
		session.commit();
		session.close();
	}
}
