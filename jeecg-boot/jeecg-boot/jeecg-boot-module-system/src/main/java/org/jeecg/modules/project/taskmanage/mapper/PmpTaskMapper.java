package org.jeecg.modules.project.taskmanage.mapper;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.project.taskmanage.entity.PmpTask;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 项目管理
 * @Author: jeecg-boot
 * @Date:   2020-03-12
 * @Version: V1.0
 */
public interface PmpTaskMapper extends BaseMapper<PmpTask> {
    //调用存储过程
    Page<PmpTask> myTaskpri(Page<PmpTask> page, @Param("principal") String principal, @Param("projectname") String projectname, @Param("taskname") String taskname);

    //调用存储过程
    Page<PmpTask> myTaskpar(Page<PmpTask> page, @Param("principal") String principal, @Param("projectname") String projectname, @Param("taskname") String taskname);
}
