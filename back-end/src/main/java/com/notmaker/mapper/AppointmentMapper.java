package com.notmaker.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.notmaker.entity.Appointment;
import org.apache.ibatis.annotations.Mapper;

/**
 * 医院预约Mapper接口
 */
@Mapper
public interface AppointmentMapper extends BaseMapper<Appointment> {
}

