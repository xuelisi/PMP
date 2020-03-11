package org.jeecg.modules.project.protree.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
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
import java.util.List;

/**
 * @Description: 项目管理
 * @Author: jeecg-boot
 * @Date: 2020-03-05
 * @Version: V1.0
 */
@RestController
@RequestMapping("/protree/pmpProjectXLF")
@Slf4j
public class PmpProjectXLFController extends JeecgController<PmpProject, IPmpProjectService> {
    @Autowired
    private IPmpProjectService pmpProjectService;

    @Autowired
    private PmpProjectMapper pmpProjectMapper;

    /**
     * 分页列表查询
     *
     * @param pmpProject
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/rootList")
    public Result<IPage<PmpProject>> queryRootPageList(PmpProject pmpProject,
                                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                       HttpServletRequest req) {
        Result<IPage<PmpProject>> result = new Result<IPage<PmpProject>>();

        //--author:os_chengtgen---date:20190804 -----for: 分类字典页面显示错误,issues:377--------start
        //QueryWrapper<SysCategory> queryWrapper = QueryGenerator.initQueryWrapper(sysCategory, req.getParameterMap());
        QueryWrapper<PmpProject> queryWrapper = new QueryWrapper<PmpProject>();
        //--author:os_chengtgen---date:20190804 -----for: 分类字典页面显示错误,issues:377--------end
        if (oConvertUtils.isEmpty(pmpProject.getParentnode())) {
            pmpProject.setParentnode("0");
            queryWrapper.eq("parentnode", pmpProject.getParentnode());
        }
        Page<PmpProject> page = new Page<PmpProject>(pageNo, pageSize);
        IPage<PmpProject> pageList = pmpProjectService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    @GetMapping(value = "/childList")
    public Result<List<PmpProject>> queryPageList(PmpProject pmpProject, HttpServletRequest req) {
        Result<List<PmpProject>> result = new Result<List<PmpProject>>();
        QueryWrapper<PmpProject> queryWrapper = QueryGenerator.initQueryWrapper(pmpProject, req.getParameterMap());
        List<PmpProject> list = pmpProjectService.list(queryWrapper);
        result.setSuccess(true);
        result.setResult(list);
        return result;
    }

    //综合查询
    @GetMapping(value = "/rootLists")
    public Result<IPage<PmpProject>> queryRootPageLists(PmpProject pmpProject,
                                                        @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                        HttpServletRequest req) {
        Result<IPage<PmpProject>> result = new Result<IPage<PmpProject>>();

        //--author:os_chengtgen---date:20190804 -----for: 分类字典页面显示错误,issues:377--------start
        //QueryWrapper<SysCategory> queryWrapper = QueryGenerator.initQueryWrapper(sysCategory, req.getParameterMap());
        QueryWrapper<PmpProject> queryWrapper = new QueryWrapper<PmpProject>();
        //--author:os_chengtgen---date:20190804 -----for: 分类字典页面显示错误,issues:377--------end
        String messge = "1";
        //名称
        if (oConvertUtils.isNotEmpty(pmpProject.getTaskname())) {
            queryWrapper.like("taskname", pmpProject.getTaskname());
            messge = "9";
        }
        //负责人
        if (oConvertUtils.isNotEmpty(pmpProject.getPrincipal())) {
            queryWrapper.like("principal", pmpProject.getPrincipal());
            messge = "9";
        }
        //是否禁用
        if (oConvertUtils.isNotEmpty(pmpProject.getIsdelete())) {
            queryWrapper.eq("isdelete", pmpProject.getIsdelete());
            messge = "9";
        }
        //总进度
        if (oConvertUtils.isNotEmpty(pmpProject.getSchedule())) {
            queryWrapper.eq("shcedule", pmpProject.getSchedule());
            messge = "9";
        }
        //开始日期
        if (oConvertUtils.isNotEmpty(pmpProject.getStartdate())) {
            SimpleDateFormat sDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
            queryWrapper.apply("CONVERT(varchar(100), startdate, 23) >= '" + sDateFormat1.format(pmpProject.getStartdate()) + "'");
            messge = "9";
        }
        //结束日期
        if (oConvertUtils.isNotEmpty(pmpProject.getEnddate())) {
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            queryWrapper.apply("CONVERT(varchar(100), enddate, 23) <= '" + sDateFormat.format(pmpProject.getEnddate()) + "'");
            messge = "9";
        }
        if (messge.equals("1")) {
            pmpProject.setParentnode("0");
            queryWrapper.eq("parentnode", pmpProject.getParentnode());
            result.setMessage(messge);
        }
        Page<PmpProject> page = new Page<PmpProject>(pageNo, pageSize);
        IPage<PmpProject> pageList = pmpProjectService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }
    //项目图表
    @GetMapping(value = "/getYearCountInfo")
    public Result<?> getYearCountInfo(HttpServletRequest req) {
          List<PmpProject> plist= pmpProjectMapper.getCountInfo();
        return Result.ok(plist);
    }
}
