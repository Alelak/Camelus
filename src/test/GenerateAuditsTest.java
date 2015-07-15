package test;

import java.io.IOException;

import com.devsolutions.camelus.auditing.GenerateAudits;

public class GenerateAuditsTest {

	public static void main(String[] args) {
		try {
			GenerateAudits.generate(System.getProperty("user.home")
					+ "/Desktop/log.txt",1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
