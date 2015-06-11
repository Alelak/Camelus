package com.devsolutions.camelus.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.devsolutions.camelus.entities.Unit;

public interface UnitMapper {

	@Select("SELECT * FROM units")
	List<Unit> getAll();

	@Insert("INSERT INTO units (description) VALUE (#{description})")
	@Options(flushCache = true)
	void add(String description);

}
