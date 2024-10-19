package com.dao;

import com.entity.LvyoudongtaiEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;
import com.entity.view.LvyoudongtaiView;

/**
 * 旅游动态 Dao 接口
 *
 * @author 
 */
public interface LvyoudongtaiDao extends BaseMapper<LvyoudongtaiEntity> {

   List<LvyoudongtaiView> selectListView(Pagination page,@Param("params")Map<String,Object> params);

}
