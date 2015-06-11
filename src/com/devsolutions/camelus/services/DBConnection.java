package com.devsolutions.camelus.services;

import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class DBConnection {

	private static String ressource = "com/devsolutions/camelus/mappers/myBatis.xml";
	private static InputStream stream;
	private static SqlSessionFactory sqlSessionFactory;

	static {

		try {

			if (sqlSessionFactory == null) {
				stream = Resources.getResourceAsStream(ressource);
				sqlSessionFactory = new SqlSessionFactoryBuilder()
						.build(stream);
				sqlSessionFactory.getConfiguration().addMappers(
						"com/devsolutions/camelus/mappers");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public static SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}
}
