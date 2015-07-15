package com.devsolutions.camelus.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.devsolutions.camelus.entities.Admin;

public interface AdminMapper {
	public static final String DELETED_CONDITION = "deleted = 0";

	@Select("SELECT * FROM admins WHERE " + DELETED_CONDITION)
	List<Admin> getAll();

	@Select("SELECT * FROM admins WHERE login = #{login} and "
			+ DELETED_CONDITION)
	Admin getByUserName(String login);

	@Select("SELECT * FROM admins WHERE sin = #{sin} and " + DELETED_CONDITION)
	Admin getBySin(String sin);

	@Insert("INSERT INTO admins (login, password, fname, lname, hire_date, sin)"
			+ " VALUES (#{login}, SHA1(#{password}), #{fname}, #{lname}, #{hire_date}, #{sin})")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id", flushCache = true)
	void add(Admin admin);

	@Update("UPDATE admins SET login = #{login}, password = #{password}, fname = #{fname}, lname = #{lname}, hire_date = #{hire_date}, sin = #{sin}, updated_at = CURRENT_TIMESTAMP WHERE id = #{id}")
	@Options(flushCache = true)
	void update(Admin admin);

	@Update("UPDATE  admins SET deleted = 1,updated_at = CURRENT_TIMESTAMP WHERE id = #{id}")
	@Options(flushCache = true)
	void delete(int id);
}
