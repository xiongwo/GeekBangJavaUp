package org.example.miaosha.dao;

import org.apache.ibatis.annotations.Param;
import org.example.miaosha.dto.ItemDO;

import java.util.List;

public interface ItemDOMapper {

    List<ItemDO> listItem();

    int deleteByPrimaryKey(Integer id);

    int insert(ItemDO record);

    int insertSelective(ItemDO record);

    ItemDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ItemDO record);

    int updateByPrimaryKey(ItemDO record);
    int increaseSales(@Param("id") Integer id, @Param("amount") Integer amount);
}