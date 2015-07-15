package com.devsolutions.camelus.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.devsolutions.camelus.entities.Order;
import com.devsolutions.camelus.entities.OrderTV;

public interface OrderMapper {

	@Select("SELECT * FROM orders")
	List<Order> getAll();

	@Select("SELECT orders.id, client_id, status, associated_vendor, commission_id, fname, lname, enterprise_name, comment, ordered_at "
			+ "FROM vendors INNER JOIN orders ON  vendors.id = orders.vendor_id"
			+ "     INNER JOIN clients ON orders.client_id = clients.id"
			+ " WHERE vendor_id = #{vendor_id}")
	List<OrderTV> getByVendorId(int vendor_id);

	@Select("SELECT orders.id, client_id, status, associated_vendor, commission_id, fname, lname, enterprise_name, comment, ordered_at "
			+ "FROM vendors INNER JOIN orders ON  vendors.id = orders.vendor_id"
			+ "     INNER JOIN clients ON orders.client_id = clients.id")
	List<OrderTV> getAllTV();

	@Select("SELECT * FROM orders WHERE client_id = #{client_id}")
	List<Order> getByClientId(long client_id);

	@Select("SELECT * FROM orders WHERE id = #{id}")
	Order getById(long id);

	@Select("SELECT * FROM orders WHERE vendor_id = #{vendor_id} AND YEAR(ordered_at) = #{year} AND MONTH(ordered_at) = #{month}")
	List<Order> getByMonthAndYear(@Param("vendor_id") int vendor_id,
			@Param("year") int year, @Param("month") int month);

	@Select("SELECT * FROM orders WHERE vendor_id = #{vendor_id} AND YEAR(ordered_at) = #{year}")
	List<Order> getByYear(@Param("vendor_id") int vendor_id,
			@Param("year") int year);

	@Insert("INSERT INTO orders (vendor_id,client_id,total,commission,comment) VALUES (#{vendor_id},#{client_id},#{total},#{commission},#{comment})")
	@Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id", flushCache = true)
	void add(Order order);

	@Update("UPDATE orders SET status = 1 WHERE id = #{id} ")
	@Options(flushCache = true)
	void cancel(long id);
}
