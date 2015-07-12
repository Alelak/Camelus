package com.devsolutions.camelus.managers;

import java.util.Collections;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;

import com.devsolutions.camelus.auditing.Audit;
import com.devsolutions.camelus.auditing.AuditTypes;
import com.devsolutions.camelus.auditing.AuditUtils;
import com.devsolutions.camelus.entities.Client;
import com.devsolutions.camelus.mappers.ClientMapper;
import com.devsolutions.camelus.services.DBConnection;
import com.devsolutions.camelus.services.Session;
import com.devsolutions.camelus.utils.FXUtils;

public class ClientManager {

	public static List<Client> getAll() {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		List<Client> clients = Collections.emptyList();

		try {
			clients = session.getMapper(ClientMapper.class).getAll();
		} catch (PersistenceException e) {
			FXUtils.openDBErrorDialog();
		} finally {
			session.close();
		}

		return clients;
	}

	public static List<Client> getByVendorId(int associated_vendor) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		List<Client> clients = Collections.emptyList();
		try {
			clients = session.getMapper(ClientMapper.class).getByVendorId(
					associated_vendor);
		} catch (PersistenceException e) {
			FXUtils.openDBErrorDialog();
		} finally {
			session.close();
		}

		return clients;
	}

	public static Client getById(long id) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		Client client = null;
		try {
			client = session.getMapper(ClientMapper.class).getById(id);
		} catch (PersistenceException e) {
			FXUtils.openDBErrorDialog();
		} finally {
			session.close();
		}

		return client;
	}

	public static Client getByEntrepriseAndClientName(String enterprise_name,
			String contact_name) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		Client client = null;
		try {
			client = session
					.getMapper(ClientMapper.class)
					.getByEntrepriseAndClientName(enterprise_name, contact_name);
		} catch (PersistenceException e) {
			FXUtils.openDBErrorDialog();
		} finally {
			session.close();
		}

		return client;
	}

	public static void update(Client client) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		session.getMapper(ClientMapper.class).update(client);
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
							"à modifier un client id : " + client.getId()));
		} else {
			AuditUtils.getAuditingService().setAudit(
					new Audit(Session.admin.getLogin(), AuditTypes.UPDATE,
							"à modifier un client id : " + client.getId()));
		}
		AuditUtils.getAuditingService().start();
	}

	public static void add(Client client) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		session.getMapper(ClientMapper.class).add(client);
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
							"à ajouter un client id : " + client.getId()));
		} else {
			AuditUtils.getAuditingService().setAudit(
					new Audit(Session.admin.getLogin(), AuditTypes.INSERT,
							"à ajouter un client id : " + client.getId()));
		}
		AuditUtils.getAuditingService().start();
	}

	public static void delete(long id) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		session.getMapper(ClientMapper.class).delete(id);
		try {
			session.commit();
		} catch (PersistenceException e) {
			FXUtils.openDBErrorDialog();
		} finally {
			session.close();
		}
		if (Session.vendor != null) {
			AuditUtils.getAuditingService().setAudit(
					new Audit(Session.vendor.getLogin(), AuditTypes.DELETE,
							"à supprimer un client id : " + id));
		} else {
			AuditUtils.getAuditingService().setAudit(
					new Audit(Session.admin.getLogin(), AuditTypes.DELETE,
							"à supprimer un client id : " + id));
		}
		AuditUtils.getAuditingService().start();
	}

}
