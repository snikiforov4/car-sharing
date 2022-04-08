package carsharing;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CustomerRepository {

    @Results({
            @Result(property = "id", column = "ID"),
            @Result(property = "name", column = "NAME"),
            @Result(property = "rentedCarId", column = "RENTED_CAR_ID")
    })
    @Select("SELECT * FROM customer")
    List<Customer> getAllCustomers();

    @Options(useGeneratedKeys = true)
    @Insert("INSERT INTO customer(name) VALUES (#{name})")
    void insert(Customer customer);

    @Update("UPDATE customer SET name = #{name}, RENTED_CAR_ID = #{rentedCarId} WHERE id = #{id}")
    boolean update(Customer customer);
}
