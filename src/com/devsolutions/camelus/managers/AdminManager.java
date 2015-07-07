package com.devsolutions.camelus.managers;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.devsolutions.camelus.auditing.Audit;
import com.devsolutions.camelus.auditing.AuditTypes;
import com.devsolutions.camelus.auditing.AuditUtils;
import com.devsolutions.camelus.entities.Admin;
import com.devsolutions.camelus.mappers.AdminMapper;
import com.devsolutions.camelus.services.DBConnection;
import com.devsolutions.camelus.services.Session;

public class AdminManager {
	public static List<Admin> getAll() {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		List<Admin> admins = session.getMapper(AdminMapper.class).getAll();
		session.close();
		return admins;
	}

	public static Admin getByUserName(String login) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		Admin admin = session.getMapper(AdminMapper.class).getByUserName(login);
		session.close();
		return admin;
	}

	public static Admin getBySin(String sin) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		Admin admin = session.getMapper(AdminMapper.class).getBySin(sin);
		session.close();
		return admin;
	}

	public static void add(Admin admin) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		session.getMapper(AdminMapper.class).add(admin);
		session.commit();
		session.close();
		AuditUtils.getAuditingService().setAudit(
				new Audit(Session.admin.getLogin(), AuditTypes.INSERT,
						"a ajouter un admin id : " + admin.getId()));
		AuditUtils.getAuditingService().start();
	}

	public static void update(Admin admin) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		session.getMapper(AdminMapper.class).update(admin);
		session.commit();
		session.close();
		AuditUtils.getAuditingService().setAudit(
				new Audit(Session.admin.getLogin(), AuditTypes.UPDATE,
						"a modifier un admin id : " + admin.getId()));
		AuditUtils.getAuditingService().start();
	}

	public static void delete(int id) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		session.getMapper(AdminMapper.class).delete(id);
		session.commit();
		session.close();
		AuditUtils.getAuditingService().setAudit(
				new Audit(Session.admin.getLogin(), AuditTypes.DELETE,
						"a supprimer un admin id : " + id));
		AuditUtils.getAuditingService().start();
	}
}
