package com.devsolutions.camelus.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.devsolutions.camelus.entities.Vendor;

public interface VendorMapper {
	public static final String DELETED_CONDITION = "deleted = 0";

	@Select("SELECT * FROM vendors WHERE " + DELETED_CONDITION)
	List<Vendor> getAll();

	@Select("SELECT * FROM vendors WHERE login = #{login} AND "
			+ DELETED_CONDITION)
	Vendor getByUserName(String login);

	@Select("SELECT * FROM vendors WHERE sin = #{sin} AND " + DELETED_CONDITION)
	Vendor getBySin(String sin);

	@Insert("INSERT INTO vendors (login, password, fname, lname, hire_date, sin, commission_id)"
			+ " VALUES (#{login}, #{password}, #{fname}, #{lname}, #{hire_date}, #{sin}, #{commission_id})")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id", flushCache = true)
	void add(Vendor vendor);

	@Update("UPDATE vendors SET login = #{login}, password = #{password}, fname = #{fname}, lname = #{lname}, hire_date = #{hire_date}, sin = #{sin}, commission_id = #{commission_id}, updated_at = CURRENT_TIMESTAMP WHERE id = #{id}")
	@Options(flushCache = true)
	void update(Vendor vendor);

	@Update("UPDATE  vendors SET deleted = 1 WHERE id = #{id}")
	@Options(flushCache = true)
	void delete(int id);
}
