package org.jeecg.modules.project.proecharts.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.jeecg.common.util.oConvertUtils;
import com.alibaba.fastjson.JSONObject;
import org.jeecg.modules.project.proecharts.mapper.PmpProEchartMapper;
import org.jeecg.modules.project.proecharts.service.IPmpProEchartService;
import org.jeecg.modules.project.protree.entity.PmpProject;
import org.jeecg.modules.project.protree.mapper.PmpProjectMapper;
import org.jeecg.modules.project.protree.service.IPmpProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Description: 项目管理
 * @Author: jeecg-boot
 * @Date: 2020-03-05
 * @Version: V1.0
 */
@Service
public class PmpProEchartServiceImpl extends ServiceImpl<PmpProEchartMapper, PmpProject> implements IPmpProEchartService {
    @Autowired
    PmpProEchartMapper pmpProEchartMapper;

    public List<PmpProject> getPieCountInfo(String startTime, String endTime, String projecttype) {
        return pmpProEchartMapper.getPieCountInfo(startTime, endTime, projecttype);
    }

    public List<PmpProject> getYearCountInfo(String startTime, String endTime, String projecttype) {
        return pmpProEchartMapper.getYearCountInfo(startTime, endTime, projecttype);
    }

    public List<PmpProject> getMonthCountInfo(String startTime, String endTime, String projecttype) {
        return pmpProEchartMapper.getMonthCountInfo(startTime, endTime, projecttype);
    }

    public List<PmpProject> getDepCountInfo(String startTime, String endTime, String projecttype) {
        return pmpProEchartMapper.getDepCountInfo(startTime, endTime, projecttype);
    }

    public List<PmpProject> getTaskPieCountInfo(String startTime, String endTime) {
        return pmpProEchartMapper.getTaskPieCountInfo(startTime, endTime);
    }

    public List<PmpProject> getTaskCountInfo(String startTime, String endTime) {
        return pmpProEchartMapper.getTaskCountInfo(startTime, endTime);
    }

    public List<Map<String, Object>> myPerCount(String projecttype, String startTime, String endTime) {
        return pmpProEchartMapper.myPerCount(projecttype, startTime, endTime);
    }

    public IPage<Map<String, Object>> myPmpMember(IPage<Map<String, Object>> page, String depart) {
        return pmpProEchartMapper.myPmpMember(page, depart);
    }

    public IPage<Map<String, Object>> myTaskMember(IPage<Map<String, Object>> page, String depart) {
        return pmpProEchartMapper.myTaskMember(page, depart);
    }

    public Integer newPmpTask(Integer parentnode, String startTime, String endTime, String projecttype) {
        return pmpProEchartMapper.newPmpTask(parentnode, startTime, endTime, projecttype);
    }
}
