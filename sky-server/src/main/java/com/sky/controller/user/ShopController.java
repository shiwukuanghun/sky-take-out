package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * @Auther: WuChen
 * @Date: 2024/4/20 8:52
 * @Description:
 **/
@RestController(value = "userShopController")
@RequestMapping("/user/shop")
@Api(tags = "店铺相关接口")
@Slf4j
public class ShopController {
    private static final String KEY_SHOP_STATUS  = "SHOP_STATUS";
    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/status")
    @ApiOperation("获取店铺的营业状态")
    public Result getStatus() {
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY_SHOP_STATUS);
        log.info("获取到店铺的营业状态为：{}", status == 1 ? "营业中" : "打烊中");
        return Result.success(status);
    }
}
