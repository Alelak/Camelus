package com.devsolutions.camelus.auditing;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class AuditingService extends Service<Void> {
	private Audit audit;

	@Override
	protected Task<Void> createTask() {

		return new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				AuditManager.insert(audit);
				return null;
			}
		};

	}

	public Audit getAudit() {
		return audit;
	}

	public void setAudit(Audit audit) {
		this.audit = audit;
	}

	@Override
	protected void succeeded() {
		super.succeeded();
		this.reset();
	}

	@Override
	protected void failed() {
		super.failed();
		this.reset();
	}

}
