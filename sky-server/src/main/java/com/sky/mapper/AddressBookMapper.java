package com.sky.mapper;

import com.sky.entity.AddressBook;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @Auther: WuChen
 * @Date: 2024/4/26 21:43
 * @Description:
 **/
@Mapper
public interface AddressBookMapper {
    /**
     * 条件查询
     * @param addressBook
     * @return
     */
    List<AddressBook> list(AddressBook addressBook);

    @Insert("insert into address_book(user_id, consignee, sex, phone, province_code, province_name, city_code, city_name," +
            " district_code, district_name, detail, label, is_default) values(#{userId}, #{consignee}, #{sex}, #{phone}," +
            " #{provinceCode}, #{provinceName}, #{cityCode}, #{cityName}, #{districtCode}, #{districtName}, #{detail}," +
            " #{label}, #{isDefault})")
    Long insert(AddressBook addressBook);

    @Select("select * from address_book where id = #{id}")
    AddressBook getById(Long id);

    void update(AddressBook addressBook);

    @Update("update address_book set is_default = #{isDefault} where user_id = #{userId}")
    void updateIsDefaultByUserId(AddressBook addressBook);

    /**
     * 根据id删除地址
     * @param id
     */
    @Delete("delete from address_book where id = #{id}")
    void deleteById(Long id);
}
