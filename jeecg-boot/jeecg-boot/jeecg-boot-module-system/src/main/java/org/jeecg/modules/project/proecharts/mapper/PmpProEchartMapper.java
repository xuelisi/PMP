package org.jeecg.modules.project.proecharts.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.project.protree.entity.PmpProject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 项目管理
 * @Author: jeecg-boot
 * @Date: 2020-03-05
 * @Version: V1.0
 */
public interface PmpProEchartMapper extends BaseMapper<PmpProject> {

    //项目饼图根据条件查询
    public List<PmpProject> getPieCountInfo(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("projecttype") String projecttype);

    //项目柱状图年统计
    public List<PmpProject> getYearCountInfo(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("projecttype") String projecttype);

    //项目柱状图月统计
    public List<PmpProject> getMonthCountInfo(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("projecttype") String projecttype);

    //项目柱状图部门统计
    public List<PmpProject> getDepCountInfo(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("projecttype") String projecttype);

    //任务柱状图统计
    public List<PmpProject> getTaskCountInfo(@Param("startTime") String startTime, @Param("endTime") String endTime);

    //任务饼图根据条件查询
    public List<PmpProject> getTaskPieCountInfo(@Param("startTime") String startTime, @Param("endTime") String endTime);

    //项目柱状图人员统计
    public List<Map<String, Object>> myPerCount(@Param("projecttype") String projecttype, @Param("startTime") String startTime, @Param("endTime") String endTime);

    //项目成员统计
    public IPage<Map<String, Object>> myPmpMember(IPage<Map<String, Object>> page, @Param("depart") String depart);

    //任务成员统计
    public IPage<Map<String, Object>> myTaskMember(IPage<Map<String, Object>> page, @Param("depart") String depart);

    //新增项目/任务数统计
    public Integer newPmpTask(@Param("parentnode") Integer parentnode, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("projecttype") String projecttype);

}
