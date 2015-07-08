package com.devsolutions.camelus.auditing;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;

public class GenerateAudits {

	public static void generate(String filename) throws IOException {
		List<Audit> audits = AuditManager.getAll();
		StringBuilder stringBuilder = new StringBuilder();
		for (Audit audit : audits) {
			stringBuilder.append(audit.getCreated_at() + " : "
					+ audit.getAction_user() + " " + audit.getDescription()
					+ System.getProperty("line.separator"));
		}
		RandomAccessFile file = new RandomAccessFile(filename, "rw");
		FileChannel fileChannel = file.getChannel();
		ByteBuffer buf = ByteBuffer.allocate(stringBuilder.length());

		buf.clear();
		buf.put(stringBuilder.toString().getBytes());
		buf.flip();
		while (buf.hasRemaining()) {
			fileChannel.write(buf);
		}
		fileChannel.close();
		file.close();
	}
}
