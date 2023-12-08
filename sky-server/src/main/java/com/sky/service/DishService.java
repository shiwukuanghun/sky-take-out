package com.sky.service;

import com.sky.dto.DishDTO;

/**
 * @Auther: WuChen
 * @Date: 2023/12/8 21:08
 * @Description:
 **/
public interface DishService {
    /**
     * 新增菜品和对应的口味
     * @param dishDTO
     */
    void saveWithFlavor(DishDTO dishDTO);
}
