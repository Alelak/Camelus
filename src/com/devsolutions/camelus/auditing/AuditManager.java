package com.devsolutions.camelus.auditing;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

public class AuditManager {

	public static void insert(Audit audit) {
		SqlSession session = AuditDBConnection.getSqlSessionFactory()
				.openSession();
		session.getMapper(AuditMapper.class).insert(audit);
		session.commit();
		session.close();
	}

	public static List<Audit> getAll() {
		SqlSession session = AuditDBConnection.getSqlSessionFactory()
				.openSession();
		List<Audit> audits = session.getMapper(AuditMapper.class).getAll();
		session.close();
		return audits;
	}
}
