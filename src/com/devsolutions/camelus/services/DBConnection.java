package com.devsolutions.camelus.services;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.devsolutions.camelus.utils.DBConfig;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class DBConnection {

	private static final String ressource = "com/devsolutions/camelus/mappers/myBatis.xml";
	private static SqlSessionFactory sqlSessionFactory;

	static {

		try {
			DBConfig dbConfig = getDBConfig();
			Properties properties = new Properties();
			properties.setProperty("username", dbConfig.getUsername());
			properties.setProperty("password", dbConfig.getPassword());
			properties.setProperty("url", dbConfig.getUrl());
			Reader reader = Resources.getResourceAsReader(ressource);
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader,
					properties);
			sqlSessionFactory.getConfiguration().addMappers(
					"com/devsolutions/camelus/mappers");

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public static SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}

	public static DBConfig getDBConfig() {
		DBConfig dbConfig = null;
		try {
			FileReader fileReader = new FileReader("DBConfig.json");
			JsonReader jsonReader = new JsonReader(fileReader);
			Gson gson = new Gson();
			dbConfig = gson.fromJson(jsonReader, DBConfig.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dbConfig;
	}
}
