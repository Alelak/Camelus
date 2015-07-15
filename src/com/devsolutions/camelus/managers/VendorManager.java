package com.devsolutions.camelus.managers;

import java.util.Collections;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;

import com.devsolutions.camelus.auditing.Audit;
import com.devsolutions.camelus.auditing.AuditTypes;
import com.devsolutions.camelus.auditing.AuditUtils;
import com.devsolutions.camelus.entities.Vendor;
import com.devsolutions.camelus.mappers.VendorMapper;
import com.devsolutions.camelus.services.DBConnection;
import com.devsolutions.camelus.services.Session;
import com.devsolutions.camelus.utils.FXUtils;

public class VendorManager {
	public static List<Vendor> getAll() {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		List<Vendor> vendors = Collections.emptyList();
		try {
			vendors = session.getMapper(VendorMapper.class).getAll();
		} catch (PersistenceException e) {
			FXUtils.openDBErrorDialog();
		} finally {
			session.close();
		}
		return vendors;
	}

	public static List<Vendor> getByCommission(int commission_id) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		List<Vendor> vendors = Collections.emptyList();
		try {
			vendors = session.getMapper(VendorMapper.class).getByCommission(
					commission_id);
		} catch (PersistenceException e) {
			FXUtils.openDBErrorDialog();
		} finally {
			session.close();
		}
		return vendors;
	}

	public static Vendor getByUserName(String login) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		Vendor vendor = null;
		try {
			vendor = session.getMapper(VendorMapper.class).getByUserName(login);
		} catch (PersistenceException e) {
			FXUtils.openDBErrorDialog();
		} finally {
			session.close();
		}

		return vendor;
	}

	public static Vendor getById(int id) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		Vendor vendor = null;
		try {
			vendor = session.getMapper(VendorMapper.class).getById(id);
		} catch (PersistenceException e) {
			FXUtils.openDBErrorDialog();
		} finally {
			session.close();
		}

		return vendor;
	}

	public static Vendor getBySin(String sin) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		Vendor vendor = null;
		try {
			vendor = session.getMapper(VendorMapper.class).getBySin(sin);
		} catch (PersistenceException e) {
			FXUtils.openDBErrorDialog();
		} finally {
			session.close();
		}
		return vendor;
	}

	public static void add(Vendor vendor) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		session.getMapper(VendorMapper.class).add(vendor);
		try {
			session.commit();
		} catch (PersistenceException e) {
			FXUtils.openDBErrorDialog();
		} finally {
			session.close();
		}
		AuditUtils.getAuditingService().setAudit(
				new Audit(Session.admin.getLogin(), AuditTypes.INSERT,
						"a ajouter un vendeur id : " + vendor.getId()));
		AuditUtils.getAuditingService().start();
	}

	public static void update(Vendor vendor) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		session.getMapper(VendorMapper.class).update(vendor);
		try {
			session.commit();
		} catch (PersistenceException e) {
			FXUtils.openDBErrorDialog();
		} finally {
			session.close();
		}
		AuditUtils.getAuditingService().setAudit(
				new Audit(Session.admin.getLogin(), AuditTypes.UPDATE,
						"a modifier un vendeur id : " + vendor.getId()));
		AuditUtils.getAuditingService().start();
	}

	public static void updatePassword(int id, String password) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		session.getMapper(VendorMapper.class).updatePassword(id, password);
		try {
			session.commit();
		} catch (PersistenceException e) {
			FXUtils.openDBErrorDialog();
		} finally {
			session.close();
		}
		AuditUtils.getAuditingService().setAudit(
				new Audit(Session.vendor.getLogin(), AuditTypes.UPDATE,
						"a modifier son mot de passe"));
		AuditUtils.getAuditingService().start();
	}

	public static void delete(int id) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		session.getMapper(VendorMapper.class).delete(id);
		try {
			session.commit();
		} catch (PersistenceException e) {
			FXUtils.openDBErrorDialog();
		} finally {
			session.close();
		}
		AuditUtils.getAuditingService().setAudit(
				new Audit(Session.admin.getLogin(), AuditTypes.DELETE,
						"a supprimer un vendeur id : " + id));
		AuditUtils.getAuditingService().start();
	}
}
