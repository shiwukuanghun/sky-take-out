package com.sky.service;

import com.sky.dto.ShoppingCartDTO;

/**
 * @Auther: WuChen
 * @Date: 2024/4/24 21:05
 * @Description:
 **/
public interface ShoppingCartService {

    /**
     * 添加购物车
     * @param shoppingCartDTO
     */
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);

}
