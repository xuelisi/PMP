package org.jeecg.modules.project.tree.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.StatementType;
import org.jeecg.modules.project.tree.entity.PmpZuzhitu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 项目组织树
 * @Author: jeecg-boot
 * @Date:   2020-03-04
 * @Version: V1.0
 */
public interface PmpZuzhituMapper extends BaseMapper<PmpZuzhitu> {

    //调用存储过程
    public String addMyNode(@Param("Id") String Id,@Param("parentNodeId") String parentNodeId, @Param("taskId") String taskId);

}
