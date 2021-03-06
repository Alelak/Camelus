package com.devsolutions.camelus.auditing;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.devsolutions.camelus.utils.DBConfig;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class AuditDBConnection {
	private static final String ressource = "com/devsolutions/camelus/auditing/myBatis.xml";
	private static SqlSessionFactory sqlSessionFactory;

	static {

		try {
			DBConfig dbConfig = getDBConfig();
			Properties properties = new Properties();
			properties.setProperty("username", dbConfig.getUsername());
			properties.setProperty("password", dbConfig.getPassword());
			properties.setProperty("url", "jdbc:mysql://" + dbConfig.getIp()
					+ ":" + dbConfig.getPort() + "/" + dbConfig.getSchema());
			Reader reader = Resources.getResourceAsReader(ressource);
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader,
					properties);
			sqlSessionFactory.getConfiguration().addMapper(AuditMapper.class);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public static SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}

	public static DBConfig getDBConfig() throws FileNotFoundException {
		JsonReader jsonReader = new JsonReader(new FileReader(
				System.getenv("APPDATA") + "\\Camelus\\AuditDBConfig.json"));
		Gson gson = new Gson();
		return gson.fromJson(jsonReader, DBConfig.class);
	}
}
