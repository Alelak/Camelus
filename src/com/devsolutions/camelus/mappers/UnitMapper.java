package com.devsolutions.camelus.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.devsolutions.camelus.entities.Unit;

public interface UnitMapper {

	@Select("SELECT * FROM units WHERE deleted = 0")
	List<Unit> getAll();

	@Select("SELECT * FROM units WHERE id = #{id} AND deleted = 0")
	Unit getById(int id);

	@Insert("INSERT INTO units (description) VALUE (#{description})")
	@Options(flushCache = true)
	void add(String description);

	@Update("UPDATE units SET deleted = 1 WHERE id = #{id}")
	@Options(flushCache = true)
	void delete(int id);

}
