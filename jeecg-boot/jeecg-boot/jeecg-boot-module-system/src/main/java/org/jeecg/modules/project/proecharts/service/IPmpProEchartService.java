package org.jeecg.modules.project.proecharts.service;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.project.protree.entity.PmpProject;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 项目管理
 * @Author: jeecg-boot
 * @Date:   2020-03-05
 * @Version: V1.0
 */
public interface IPmpProEchartService extends IService<PmpProject> {

    public List<PmpProject> getPieCountInfo(String startTime,  String endTime,  String projecttype);
    public List<PmpProject>  getYearCountInfo( String startTime,  String endTime,  String projecttype);
    public List<PmpProject>  getMonthCountInfo( String startTime,  String endTime,  String projecttype);
    public List<PmpProject>  getPerNoCountInfo( String startTime,  String endTime,  String projecttype);
    public List<PmpProject>  getDepCountInfo( String startTime,  String endTime,  String projecttype);

    public List<PmpProject>  getTaskPieCountInfo( String startTime,  String endTim);
    public List<PmpProject>  getTaskCountInfo( String startTime,  String endTime);

}
