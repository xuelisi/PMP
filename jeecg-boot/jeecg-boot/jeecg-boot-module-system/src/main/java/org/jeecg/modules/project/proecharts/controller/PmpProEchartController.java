package org.jeecg.modules.project.proecharts.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.project.proecharts.service.IPmpProEchartService;
import org.jeecg.modules.project.protree.entity.PmpProject;
import org.jeecg.modules.project.protree.service.IPmpProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 项目管理
 * @Author: jeecg-boot
 * @Date: 2020-03-05
 * @Version: V1.0
 */
@RestController
@RequestMapping("/proecharts/pmpProjectEchart")
@Slf4j
public class PmpProEchartController extends JeecgController<PmpProject, IPmpProjectService> {
    @Autowired
    private IPmpProEchartService pmpProEchartService;
    SimpleDateFormat sDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");

    //项目饼图图表
    @GetMapping(value = "/getPieCountInfo")
    public Result<?> getPieCountInfo(@RequestParam(name = "startTime", defaultValue = "") String startTime, @RequestParam(name = "endTime", defaultValue = "") String endTime, @RequestParam(name = "projecttype", defaultValue = "") String projecttype, HttpServletRequest req) {
        if (oConvertUtils.isEmpty(startTime) && oConvertUtils.isEmpty(endTime)) {
            Date date = DateUtil.lastMonth();
            startTime = sDateFormat1.format(date);
        }
        List<PmpProject> plist = pmpProEchartService.getPieCountInfo(startTime, endTime, projecttype);
        return Result.ok(plist);
    }

    //项目柱状图年统计
    @GetMapping(value = "/getYearCountInfo")
    public Result<?> getYearCountInfo(@RequestParam(name = "startTime", defaultValue = "") String startTime, @RequestParam(name = "endTime", defaultValue = "") String endTime, @RequestParam(name = "projecttype", defaultValue = "") String projecttype, HttpServletRequest req) {
        if (oConvertUtils.isEmpty(startTime) && oConvertUtils.isEmpty(endTime)) {
            Calendar currCal = Calendar.getInstance();
            int currentYear = currCal.get(Calendar.YEAR);
            startTime = currentYear + "-01-01";
        }
        List<PmpProject> plist = pmpProEchartService.getYearCountInfo(startTime, endTime, projecttype);
        return Result.ok(plist);
    }

    //项目柱状图月统计
    @GetMapping(value = "/getMonthCountInfo")
    public Result<?> getMonthCountInfo(@RequestParam(name = "startTime", defaultValue = "") String startTime, @RequestParam(name = "endTime", defaultValue = "") String endTime, @RequestParam(name = "projecttype", defaultValue = "") String projecttype, HttpServletRequest req) {
        if (oConvertUtils.isEmpty(startTime) && oConvertUtils.isEmpty(endTime)) {
            Calendar currCal = Calendar.getInstance();
            int currentYear = currCal.get(Calendar.YEAR);
            startTime = currentYear + "-01-01";
        }
        List<PmpProject> plist = pmpProEchartService.getMonthCountInfo(startTime, endTime, projecttype);
        return Result.ok(plist);
    }

    //项目柱状图人员统计
    @GetMapping(value = "/getPerNoCountInfo")
    public Result<?> getPerNoCountInfo(@RequestParam(name = "startTime", defaultValue = "") String startTime, @RequestParam(name = "endTime", defaultValue = "") String endTime, @RequestParam(name = "projecttype", defaultValue = "") String projecttype, HttpServletRequest req) {
        if (oConvertUtils.isEmpty(startTime) && oConvertUtils.isEmpty(endTime)) {
            Calendar currCal = Calendar.getInstance();
            int currentYear = currCal.get(Calendar.YEAR);
            startTime = currentYear + "-01-01";
        }
        List<Map<String, Object>> plist = pmpProEchartService.myPerCount(projecttype, startTime, endTime);
        return Result.ok(plist);
    }

    //项目柱状图部门统计
    @GetMapping(value = "/getDepCountInfo")
    public Result<?> getDepCountInfo(@RequestParam(name = "startTime", defaultValue = "") String startTime, @RequestParam(name = "endTime", defaultValue = "") String endTime, @RequestParam(name = "projecttype", defaultValue = "") String projecttype, HttpServletRequest req) {
        if (oConvertUtils.isEmpty(startTime) && oConvertUtils.isEmpty(endTime)) {
            Calendar currCal = Calendar.getInstance();
            int currentYear = currCal.get(Calendar.YEAR);
            startTime = currentYear + "-01-01";
        }
        List<PmpProject> plist = pmpProEchartService.getDepCountInfo(startTime, endTime, projecttype);
        return Result.ok(plist);
    }

    //任务饼状图统计
    @GetMapping(value = "/getTaskPieCountInfo")
    public Result<?> getTaskPieCountInfo(@RequestParam(name = "startTime", defaultValue = "") String startTime, @RequestParam(name = "endTime", defaultValue = "") String endTime, HttpServletRequest req) {
        if (oConvertUtils.isEmpty(startTime) && oConvertUtils.isEmpty(endTime)) {
            Calendar currCal = Calendar.getInstance();
            int currentYear = currCal.get(Calendar.YEAR);
            startTime = currentYear + "-01-01";
        }
        List<PmpProject> plist = pmpProEchartService.getTaskPieCountInfo(startTime, endTime);
        return Result.ok(plist);
    }


    //任务柱状图统计
    @GetMapping(value = "/getTaskCountInfo")
    public Result<?> getTaskCountInfo(@RequestParam(name = "startTime", defaultValue = "") String startTime, @RequestParam(name = "endTime", defaultValue = "") String endTime, HttpServletRequest req) {
        if (oConvertUtils.isEmpty(startTime) && oConvertUtils.isEmpty(endTime)) {
            Calendar currCal = Calendar.getInstance();
            int currentYear = currCal.get(Calendar.YEAR);
            startTime = currentYear + "-01-01";
        }
        List<PmpProject> plist = pmpProEchartService.getTaskCountInfo(startTime, endTime);
        return Result.ok(plist);
    }

    //项目人员统计
    @GetMapping(value = "/myPmpMember")
    public Result<?> myPmpMember(@RequestParam(name = "depart", defaultValue = "") String depart,
                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                 HttpServletRequest req) {
        Page<Map<String, Object>> page = new Page<Map<String, Object>>(pageNo, pageSize);
        IPage<Map<String, Object>> pageList = pmpProEchartService.myPmpMember(page, depart);
        return Result.ok(pageList);
    }

    //任务人员统计
    @GetMapping(value = "/myTaskMember")
    public Result<?> myTaskMember(@RequestParam(name = "depart", defaultValue = "") String depart,
                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                 HttpServletRequest req) {
        Page<Map<String, Object>> page = new Page<Map<String, Object>>(pageNo, pageSize);
        IPage<Map<String, Object>> pageList = pmpProEchartService.myTaskMember(page, depart);
        return Result.ok(pageList);
    }
    //新增项目数统计
    @GetMapping(value = "/newPmpnumber")
    public Result<?> newPmpnumber(@RequestParam(name = "startTime", defaultValue = "") String startTime, @RequestParam(name = "endTime", defaultValue = "") String endTime,@RequestParam(name = "projecttype", defaultValue = "")String projecttype,HttpServletRequest req) {
        if (oConvertUtils.isEmpty(startTime) && oConvertUtils.isEmpty(endTime)) {
            Calendar currCal = Calendar.getInstance();
            int currentYear = currCal.get(Calendar.YEAR);
            startTime = currentYear + "-01-01";
        }
        Integer number=pmpProEchartService.newPmpTask(1,startTime,endTime,projecttype);
        return Result.ok(number);
    }

    //新增任务数统计
    @GetMapping(value = "/newTasknumber")
    public Result<?> newTasknumber(@RequestParam(name = "startTime", defaultValue = "") String startTime, @RequestParam(name = "endTime", defaultValue = "") String endTime,HttpServletRequest req) {
        if (oConvertUtils.isEmpty(startTime) && oConvertUtils.isEmpty(endTime)) {
            Calendar currCal = Calendar.getInstance();
            int currentYear = currCal.get(Calendar.YEAR);
            startTime = currentYear + "-01-01";
        }
        Integer number=pmpProEchartService.newPmpTask(2,startTime,endTime,null);
        return Result.ok(number);
    }

}
