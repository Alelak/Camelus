package com.devsolutions.camelus.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.devsolutions.camelus.entities.Commission;

public interface CommissionMapper {

	@Insert("INSERT INTO commissions (type,rate,mcondition) VALUES (#{type},#{rate},#{mcondition})")
	@Options(flushCache = true)
	void add(Commission commission);

	@Select("SELECT * FROM commissions")
	List<Commission> getAll();

}
