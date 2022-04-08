package carsharing;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CompanyRepository {

    @Select("SELECT * FROM company")
    List<Company> getAllCompanies();

    @Select("SELECT * FROM company WHERE id = #{id}")
    Optional<Company> findCompanyById(int id);

    @Options(useGeneratedKeys = true)
    @Insert("INSERT INTO company(name) VALUES (#{name})")
    void insert(Company company);
}
