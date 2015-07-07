package com.devsolutions.camelus.managers;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.devsolutions.camelus.auditing.Audit;
import com.devsolutions.camelus.auditing.AuditTypes;
import com.devsolutions.camelus.auditing.AuditUtils;
import com.devsolutions.camelus.entities.Commission;
import com.devsolutions.camelus.mappers.CommissionMapper;
import com.devsolutions.camelus.services.DBConnection;
import com.devsolutions.camelus.services.Session;

public class CommissionManager {

	public static void add(Commission commission) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		session.getMapper(CommissionMapper.class).add(commission);
		session.commit();
		session.close();
		AuditUtils.getAuditingService().setAudit(
				new Audit(Session.admin.getLogin(), AuditTypes.INSERT,
						"a ajouter une commission id : " + commission.getId()));
		AuditUtils.getAuditingService().start();
	}

	public static List<Commission> getAll() {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		List<Commission> commissions = session
				.getMapper(CommissionMapper.class).getAll();
		session.close();
		return commissions;
	}

	public static Commission getById(int id) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		Commission commission = session.getMapper(CommissionMapper.class)
				.getById(id);
		session.close();
		return commission;
	}

	public static void delete(int id) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		session.getMapper(CommissionMapper.class).delete(id);
		session.commit();
		session.close();
		AuditUtils.getAuditingService().setAudit(
				new Audit(Session.admin.getLogin(), AuditTypes.DELETE,
						"a supprimer une commission id : " + id));
		AuditUtils.getAuditingService().start();
	}

	public static void update(Commission commission) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		session.getMapper(CommissionMapper.class).update(commission);
		session.commit();
		session.close();
		AuditUtils.getAuditingService()
				.setAudit(
						new Audit(Session.admin.getLogin(), AuditTypes.UPDATE,
								"a modifier une commission id : "
										+ commission.getId()));
		AuditUtils.getAuditingService().start();
	}
}
