package org.jeecg.modules.project.proecharts.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.project.protree.entity.PmpProject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 项目管理
 * @Author: jeecg-boot
 * @Date:   2020-03-05
 * @Version: V1.0
 */
public interface PmpProEchartMapper extends BaseMapper<PmpProject> {

    public List<PmpProject>  getPieCountInfo(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("projecttype") String projecttype);

    public List<PmpProject>  getYearCountInfo(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("projecttype") String projecttype);

    public List<PmpProject>  getMonthCountInfo(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("projecttype") String projecttype);

    public List<PmpProject>  getPerNoCountInfo(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("projecttype") String projecttype);

    public List<PmpProject>  getDepCountInfo(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("projecttype") String projecttype);

    public List<PmpProject>  getTaskCountInfo(@Param("startTime") String startTime, @Param("endTime") String endTime);

    public List<PmpProject>  getTaskPieCountInfo(@Param("startTime") String startTime, @Param("endTime") String endTime);
}
