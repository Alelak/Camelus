package com.devsolutions.camelus.utils;

import java.io.File;
import java.io.RandomAccessFile;

public class FileUtils {
	public static boolean isFileLockedReadOnly(File file) {
		try {
			// Get a file channel for the file
			RandomAccessFile test = new RandomAccessFile(file, "rw");
			test.close();
			return false;

		} catch (Exception e) {
			return true;
		}
	}
}
