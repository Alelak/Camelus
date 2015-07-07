package com.devsolutions.camelus.managers;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.devsolutions.camelus.auditing.Audit;
import com.devsolutions.camelus.auditing.AuditTypes;
import com.devsolutions.camelus.auditing.AuditUtils;
import com.devsolutions.camelus.entities.Category;
import com.devsolutions.camelus.mappers.CategoryMapper;
import com.devsolutions.camelus.services.DBConnection;
import com.devsolutions.camelus.services.Session;

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

	public static void add(Category category) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		session.getMapper(CategoryMapper.class).add(category);
		session.commit();
		session.close();
		AuditUtils.getAuditingService().setAudit(
				new Audit(Session.admin.getLogin(), AuditTypes.INSERT,
						"a ajouter une categorie id : " + category.getId()));
		AuditUtils.getAuditingService().start();
	}

	public static void delete(int id) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		session.getMapper(CategoryMapper.class).delete(id);
		session.commit();
		session.close();
		AuditUtils.getAuditingService().setAudit(
				new Audit(Session.admin.getLogin(), AuditTypes.DELETE,
						"a supprimer une categorie id : " + id));
		AuditUtils.getAuditingService().start();
	}

	public static void update(Category category) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		session.getMapper(CategoryMapper.class).update(category);
		session.commit();
		session.close();
		AuditUtils.getAuditingService().setAudit(
				new Audit(Session.admin.getLogin(), AuditTypes.UPDATE,
						"a modifier une categorie id : " + category.getId()));
		AuditUtils.getAuditingService().start();
	}
}
