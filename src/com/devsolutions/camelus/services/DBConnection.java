package com.devsolutions.camelus.services;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.devsolutions.camelus.mappers.AdminMapper;
import com.devsolutions.camelus.mappers.CategoryMapper;
import com.devsolutions.camelus.mappers.ClientMapper;
import com.devsolutions.camelus.mappers.CommissionMapper;
import com.devsolutions.camelus.mappers.OrderLineMapper;
import com.devsolutions.camelus.mappers.OrderMapper;
import com.devsolutions.camelus.mappers.ProductMapper;
import com.devsolutions.camelus.mappers.UnitMapper;
import com.devsolutions.camelus.mappers.VendorMapper;
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
			properties.setProperty("url", "jdbc:mysql://" + dbConfig.getIp()
					+ ":" + dbConfig.getPort() + "/" + dbConfig.getSchema());
			Reader reader = Resources.getResourceAsReader(ressource);
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader,
					properties);
			sqlSessionFactory.getConfiguration().addMapper(AdminMapper.class);
			sqlSessionFactory.getConfiguration()
					.addMapper(CategoryMapper.class);
			sqlSessionFactory.getConfiguration().addMapper(ClientMapper.class);
			sqlSessionFactory.getConfiguration().addMapper(
					CommissionMapper.class);
			sqlSessionFactory.getConfiguration().addMapper(
					OrderLineMapper.class);
			sqlSessionFactory.getConfiguration().addMapper(OrderMapper.class);
			sqlSessionFactory.getConfiguration().addMapper(ProductMapper.class);
			sqlSessionFactory.getConfiguration().addMapper(UnitMapper.class);
			sqlSessionFactory.getConfiguration().addMapper(VendorMapper.class);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public static SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}

	public static DBConfig getDBConfig() throws FileNotFoundException {
		JsonReader jsonReader = new JsonReader(new FileReader(
				System.getenv("APPDATA") + "\\Camelus\\DBConfig.json"));
		Gson gson = new Gson();
		return gson.fromJson(jsonReader, DBConfig.class);
	}
}
