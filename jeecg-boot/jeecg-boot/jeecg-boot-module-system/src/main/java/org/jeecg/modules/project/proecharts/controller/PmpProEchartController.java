package org.jeecg.modules.project.proecharts.controller;

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
import org.jeecg.modules.project.protree.mapper.PmpProjectMapper;
import org.jeecg.modules.project.protree.service.IPmpProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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

    //项目饼图图表
    @GetMapping(value = "/getPieCountInfo")
    public Result<?> getPieCountInfo(@RequestParam(name = "startTime", defaultValue = "") String startTime,@RequestParam(name = "endTime", defaultValue = "") String endTime, @RequestParam(name = "projecttype", defaultValue = "") String projecttype, HttpServletRequest req) {
        List<PmpProject> plist = pmpProEchartService.getPieCountInfo(startTime,endTime,projecttype);
        return Result.ok(plist);
    }
    //项目柱状图年统计
    @GetMapping(value = "/getYearCountInfo")
    public Result<?> getYearCountInfo(@RequestParam(name = "startTime", defaultValue = "") String startTime,@RequestParam(name = "endTime", defaultValue = "") String endTime, @RequestParam(name = "projecttype", defaultValue = "") String projecttype, HttpServletRequest req) {
        List<PmpProject> plist = pmpProEchartService.getYearCountInfo(startTime,endTime,projecttype);
        return Result.ok(plist);
    }
}
