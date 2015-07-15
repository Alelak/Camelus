package com.devsolutions.camelus.auditing;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface AuditMapper {

	@Insert("INSERT INTO audits (action_user, action_type,description) VALUES (#{action_user}, #{action_type}, #{description})")
	@Options(flushCache = true)
	void insert(Audit audit);

	@Select("SELECT * FROM audits LIMIT 1000")
	List<Audit> getAll();

	@Select("SELECT * FROM audits WHERE created_at >= DATE_SUB(NOW(),INTERVAL #{duration} HOUR)")
	List<Audit> getBySpecificTime(@Param("duration") int duration);
}
