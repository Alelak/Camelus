package com.devsolutions.camelus.managers;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.devsolutions.camelus.entities.Client;
import com.devsolutions.camelus.mappers.ClientMapper;
import com.devsolutions.camelus.services.DBConnection;

public class ClientManager {

	public static List<Client> getAll() {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		List<Client> clients = session.getMapper(ClientMapper.class).getAll();
		session.close();
		return clients;
	}

	public static List<Client> getByVendorId(int associated_vendor) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		List<Client> clients = session.getMapper(ClientMapper.class)
				.getByVendorId(associated_vendor);
		session.close();
		return clients;
	}

	public static Client getById(long id) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		Client client = session.getMapper(ClientMapper.class).getById(id);
		session.close();
		return client;
	}
	
	public static Client getByEntrepriseAndClientName(Client client) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		Client theClient = session.getMapper(ClientMapper.class).getByEntrepriseAndClientName(client);
		session.close();
		return theClient;
	}
	

	public static void update(Client client) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		session.getMapper(ClientMapper.class).update(client);
		session.commit();
		session.close();
	}

	public static void add(Client client) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		session.getMapper(ClientMapper.class).add(client);
		session.commit();
		session.close();
	}

	public static void delete(long id) {
		SqlSession session = DBConnection.getSqlSessionFactory().openSession();
		session.getMapper(ClientMapper.class).delete(id);
		session.commit();
		session.close();
	}

}
