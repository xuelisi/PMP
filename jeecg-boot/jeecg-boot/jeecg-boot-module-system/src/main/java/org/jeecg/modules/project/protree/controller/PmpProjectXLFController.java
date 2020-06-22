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
import org.springframework.util.StringUtils;
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
    @GetMapping(value = "/list")
    public Result<?> queryPageList(PmpProject pmpProject,
                                   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<PmpProject> queryWrapper = QueryGenerator.initQueryWrapper(pmpProject, req.getParameterMap());
        Page<PmpProject> page = new Page<PmpProject>(pageNo, pageSize);
        IPage<PmpProject> pageList = pmpProjectService.page(page, queryWrapper);
        return Result.ok(pageList);
    }
    /**
     *  编辑
     *
     * @param pmpProject
     * @return
     */
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody PmpProject pmpProject) {
        pmpProjectService.updateById(pmpProject);
        return Result.ok("编辑成功!");
    }

    /**
     *   通过id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name="id",required=true) String id) {
        pmpProjectService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     *  批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
        this.pmpProjectService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
        PmpProject pmpProject = pmpProjectService.getById(id);
        if(pmpProject==null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(pmpProject);
    }
    /**
     * 导出excel
     *
     * @param request
     * @param pmpProject
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PmpProject pmpProject) {
        return super.exportXls(request, pmpProject, PmpProject.class, "项目管理");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, PmpProject.class);
    }
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
        String messge = "1";
        QueryWrapper<PmpProject> queryWrapper = QueryGenerator.initQueryWrapper(pmpProject, req.getParameterMap());
        if(oConvertUtils.isEmpty(pmpProject.getTaskname())){
            queryWrapper.eq("parentnode",'0');
        }else{
            queryWrapper.ne("parentnode",'0');
            messge="9";
        }
        Page<PmpProject> page = new Page<PmpProject>(pageNo, pageSize);
        IPage<PmpProject> pageList = pmpProjectService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        result.setMessage(messge);
        return result;
    }
}
