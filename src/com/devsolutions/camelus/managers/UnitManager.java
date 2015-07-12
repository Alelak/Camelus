package com.devsolutions.camelus.managers;

import java.util.Collections;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;

import com.devsolutions.camelus.auditing.Audit;
import com.devsolutions.camelus.auditing.AuditTypes;
import com.devsolutions.camelus.auditing.AuditUtils;
import com.devsolutions.camelus.entities.Unit;
import com.devsolutions.camelus.mappers.UnitMapper;
import com.devsolutions.camelus.services.DBConnection;
import com.devsolutions.camelus.services.Session;
import com.devsolutions.camelus.utils.FXUtils;

public class UnitManager {

	public static List<Unit> getAll() {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		List<Unit> units =  Collections.emptyList();
		try {
			units = session.getMapper(UnitMapper.class).getAll();
		} catch (PersistenceException e) {
			FXUtils.openDBErrorDialog();
		} finally {
			session.close();
		}

		return units;
	}

	public static Unit getById(int id) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		Unit unit = null;
		try {
			unit = session.getMapper(UnitMapper.class).getById(id);
		} catch (PersistenceException e) {
			FXUtils.openDBErrorDialog();
		} finally {

			session.close();
		}

		return unit;
	}

	public static void add(Unit unit) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		session.getMapper(UnitMapper.class).add(unit);
		try {
			session.commit();
		} catch (PersistenceException e) {
			FXUtils.openDBErrorDialog();
		} finally {
			session.close();
		}
		AuditUtils.getAuditingService().setAudit(
				new Audit(Session.admin.getLogin(), AuditTypes.INSERT,
						"a ajouter une unite id : " + unit.getId()));
		AuditUtils.getAuditingService().start();
	}

	public static void delete(int id) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		session.getMapper(UnitMapper.class).delete(id);
		try {
			session.commit();
		} catch (PersistenceException e) {
			FXUtils.openDBErrorDialog();
		} finally {
			session.close();
		}
		AuditUtils.getAuditingService().setAudit(
				new Audit(Session.admin.getLogin(), AuditTypes.DELETE,
						"a supprimer une unite id : " + id));
		AuditUtils.getAuditingService().start();
	}

	public static void update(Unit unit) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		session.getMapper(UnitMapper.class).update(unit);
		try {
			session.commit();
		} catch (PersistenceException e) {
			FXUtils.openDBErrorDialog();
		} finally {
			session.close();
		}
		AuditUtils.getAuditingService().setAudit(
				new Audit(Session.admin.getLogin(), AuditTypes.UPDATE,
						"a modifier une unite id : " + unit.getId()));
		AuditUtils.getAuditingService().start();

	}
}
