package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: WuChen
 * @Date: 2023/12/8 21:10
 * @Description:
 **/
@Service
@Slf4j
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper mDishMapper;
    @Autowired
    private DishFlavorMapper mDishFlavorMapper;
    @Autowired
    private SetmealDishMapper mSetmealDishMapper;

    @Transactional
    @Override
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        // 向菜品表插入1条数据
        mDishMapper.insert(dish);

        // 获取insert语句生成的主键值
        Long dishId = dish.getId();

        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });
            // 向口味表插入n条数据
            mDishFlavorMapper.insertBatch(flavors);
        }
    }

    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> page = mDishFlavorMapper.queryPage(dishPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Transactional
    @Override
    public void deleteBatch(List<Long> ids) {
        // 判断当前菜品是否能够删除---是否存在起售中的菜品
        for (Long id : ids) {
            Dish dish = mDishMapper.getById(id);
            if (dish.getStatus() == StatusConstant.ENABLE) {
                // 当前菜品处于起售中，不能删除
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        // 判断当前菜品是否能够删除---是否被套餐关联了
        List<Long> setmealIds = mSetmealDishMapper.getSetmealIdsByDishIds(ids);
        if (setmealIds != null && setmealIds.size()>0) {
            // 当前菜品被套餐关联了，不能删除
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        // 删除菜品表中的菜品数据
        /*for (Long id: ids) {
            mDishMapper.deleteById(id);
            // 删除菜品关联的口味数据
            mDishFlavorMapper.deleteByDishId(id);
        }*/

        // 优化成批量删除
        // 根据菜品id集合批量删除菜品数据
        // sql:delete from dish where id in (?,?,?)
        mDishMapper.deleteByIds(ids);
        // 根据菜品id集合批量删除
        // sql:delete from dish_flavor where dish_id in (?,?,?)
        mDishFlavorMapper.deleteByDishIds(ids);
    }

    @Override
    public DishVO getByIdWithFlavor(Long id) {
        // 根据id查询菜品数据
        Dish dish = mDishMapper.getById(id);

        // 根据菜品id查询口味数据
        List<DishFlavor> dishFlavors = mDishFlavorMapper.getByDishId(id);
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(dishFlavors);
        return dishVO;
    }

    @Transactional
    @Override
    public void updateWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        // 修改菜品表基本信息
        mDishMapper.update(dish);

        // 删除原有的口味数据
        mDishFlavorMapper.deleteByDishId(dishDTO.getId());

        // 重新插入口味数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors!=null && flavors.size()>0) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishDTO.getId());
            });
            mDishFlavorMapper.insertBatch(flavors);
        }
    }

    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    @Override
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = mDishMapper.list(dish);
        List dishVoList = new ArrayList();
        for (Dish d:dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d, dishVO);

            // 根据菜品id查询对应的口味
            List<DishFlavor> flavors = mDishFlavorMapper.getByDishId(d.getId());
            dishVO.setFlavors(flavors);
            dishVoList.add(dishVO);
        }
        return dishVoList;
    }
}
