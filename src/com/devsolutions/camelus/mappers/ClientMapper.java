package com.devsolutions.camelus.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.devsolutions.camelus.entities.Client;

public interface ClientMapper {

	public static final String DELETED_CONDITION = "deleted = 0";

	@Select("SELECT * FROM clients WHERE " + DELETED_CONDITION)
	List<Client> getAll();

	@Select("SELECT * FROM clients WHERE associated_vendor = #{associated_vendor} AND "
			+ DELETED_CONDITION)
	List<Client> getByVendorId(int associated_vendor);

	@Select("SELECT * FROM clients WHERE id = #{id} AND " + DELETED_CONDITION)
	Client getById(long id);

	@Select("SELECT * FROM clients WHERE enterprise_name = #{enterprise_name} AND contact_name = #{contact_name} AND "
			+ DELETED_CONDITION)
	Client getByEntrepriseAndClientName(
			@Param("enterprise_name") String enterprise_name,
			@Param("contact_name") String contact_name);

	@Insert("INSERT INTO clients (associated_vendor, enterprise_name, contact_name, contact_tel, contact_email, address, description)"
			+ " VALUES (#{associated_vendor}, #{enterprise_name}, #{contact_name}, #{contact_tel}, #{contact_email}, #{address}, #{description})")
	@Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id", flushCache = true)
	void add(Client client);

	@Update("UPDATE clients SET associated_vendor = #{associated_vendor}, enterprise_name = #{enterprise_name}, contact_name = #{contact_name}, contact_tel = #{contact_tel}, contact_email = #{contact_email}, address = #{address}, description = #{description}, updated_at = CURRENT_TIMESTAMP WHERE id = #{id}")
	@Options(flushCache = true)
	void update(Client client);

	@Update("UPDATE  clients SET deleted = 1 WHERE id = #{id}")
	@Options(flushCache = true)
	void delete(long id);

}
