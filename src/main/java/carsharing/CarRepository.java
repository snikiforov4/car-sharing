package carsharing;

import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CarRepository {

    @Select("SELECT * FROM car WHERE company_id = #{companyId}")
    List<Car> getAllCarsByCompanyId(int companyId);

    @Select("SELECT * FROM car c LEFT JOIN customer cs ON c.id = cs.rented_car_id WHERE c.company_id = #{companyId} AND cs.rented_car_id IS NULL")
    List<Car> getAllFreeCarsByCompanyId(int companyId);

    @Results({
            @Result(property = "id", column = "ID"),
            @Result(property = "name", column = "NAME"),
            @Result(property = "companyId", column = "COMPANY_ID")
    })
    @Select("SELECT * FROM car WHERE id = #{id}")
    Optional<Car> findCarById(int id);

    @Options(useGeneratedKeys = true)
    @Insert("INSERT INTO car (name, company_id) VALUES (#{name}, #{companyId})")
    void insert(Car car);
}
