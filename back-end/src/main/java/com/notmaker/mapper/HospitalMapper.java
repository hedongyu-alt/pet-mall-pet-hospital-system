package com.notmaker.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.notmaker.entity.Hospital;
import org.apache.ibatis.annotations.Mapper;

/**
 * 宠物医院Mapper接口
 */
@Mapper
public interface HospitalMapper extends BaseMapper<Hospital> {
}

