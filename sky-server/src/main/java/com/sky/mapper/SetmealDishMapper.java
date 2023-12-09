package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Auther: WuChen
 * @Date: 2023/12/9 11:23
 * @Description:
 **/
@Mapper
public interface SetmealDishMapper {
    /**
     * 根据菜品id查询对应的套餐id
     * select setmeal_id from setmeal_dish where dish_id in (1,2,3,4)
     * @param dishIds
     * @return
     */
    List<Long> getSetmealIdsByDishIds(List<Long> dishIds);
}
