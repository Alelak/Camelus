package com.devsolutions.camelus.auditing;

public class AuditUtils {
	private static AuditingService auditingService;
	static {
		auditingService = new AuditingService();
	}

	public static AuditingService getAuditingService() {
		return auditingService;
	}

}
