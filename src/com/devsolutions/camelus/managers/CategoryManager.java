package com.devsolutions.camelus.managers;

import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;

import com.devsolutions.camelus.auditing.Audit;
import com.devsolutions.camelus.auditing.AuditTypes;
import com.devsolutions.camelus.auditing.AuditUtils;
import com.devsolutions.camelus.entities.Category;
import com.devsolutions.camelus.mappers.CategoryMapper;
import com.devsolutions.camelus.services.DBConnection;
import com.devsolutions.camelus.services.Session;
import com.devsolutions.camelus.utils.FXUtils;

public class CategoryManager {

	public static List<Category> getAll() {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		List<Category> categories = null;
		try {
			categories = session.getMapper(CategoryMapper.class).getAll();
		} catch (PersistenceException e) {
			FXUtils.openDBErrorDialog();
		} finally {
			session.close();
		}

		return categories;

	}

	public static Category getById(int id) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		Category category = null;
		try {
			category = session.getMapper(CategoryMapper.class).getById(id);
		} catch (PersistenceException e) {
			FXUtils.openDBErrorDialog();
		} finally {
			session.close();
		}

		return category;

	}

	public static void add(Category category) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		session.getMapper(CategoryMapper.class).add(category);
		try {
			session.commit();
		} catch (PersistenceException e) {
			FXUtils.openDBErrorDialog();
		} finally {
			session.close();
		}
		AuditUtils.getAuditingService().setAudit(
				new Audit(Session.admin.getLogin(), AuditTypes.INSERT,
						"a ajouter une categorie id : " + category.getId()));
		AuditUtils.getAuditingService().start();
	}

	public static void delete(int id) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		session.getMapper(CategoryMapper.class).delete(id);
		try {
			session.commit();
		} catch (PersistenceException e) {
			FXUtils.openDBErrorDialog();
		} finally {
			session.close();
		}
		AuditUtils.getAuditingService().setAudit(
				new Audit(Session.admin.getLogin(), AuditTypes.DELETE,
						"a supprimer une categorie id : " + id));
		AuditUtils.getAuditingService().start();
	}

	public static void update(Category category) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		session.getMapper(CategoryMapper.class).update(category);
		try {
			session.commit();
		} catch (PersistenceException e) {
			FXUtils.openDBErrorDialog();
		} finally {
			session.close();
		}
		AuditUtils.getAuditingService().setAudit(
				new Audit(Session.admin.getLogin(), AuditTypes.UPDATE,
						"a modifier une categorie id : " + category.getId()));
		AuditUtils.getAuditingService().start();
	}
}
