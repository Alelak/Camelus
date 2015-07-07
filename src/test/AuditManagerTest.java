package test;

import com.devsolutions.camelus.auditing.Audit;
import com.devsolutions.camelus.auditing.AuditManager;
import com.devsolutions.camelus.auditing.AuditTypes;

public class AuditManagerTest {
	public void insert() {
		AuditManager.insert(new Audit("admin1", AuditTypes.LOGIN, null));
	}

	public void getAll() {
		for (Audit audit : AuditManager.getAll()) {
			System.out.println(audit.getDescription());
		}
	}

	public static void main(String[] args) {
		AuditManagerTest auditManagerTest = new AuditManagerTest();
		auditManagerTest.insert();
		auditManagerTest.getAll();
	}
}
