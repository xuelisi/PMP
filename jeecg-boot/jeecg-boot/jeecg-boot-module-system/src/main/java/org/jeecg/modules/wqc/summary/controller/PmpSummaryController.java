package org.jeecg.modules.wqc.summary.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.wqc.summary.entity.PmpSummary;
import org.jeecg.modules.wqc.summary.entity.PmpSummaryInfo;
import org.jeecg.modules.wqc.summary.entity.PmpSummaryStatistics;
import org.jeecg.modules.wqc.summary.service.IPmpSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

import org.jeecg.common.system.vo.LoginUser;
import org.apache.shiro.SecurityUtils;

/**
 * @Description: 任务小结
 * @Author: jeecg-boot
 * @Date: 2020-03-06
 * @Version: V1.0
 */
@RestController
@RequestMapping("/summary/pmpSummary")
@Slf4j
public class PmpSummaryController extends JeecgController<PmpSummary, IPmpSummaryService> {
    @Autowired
    private IPmpSummaryService service;

    /**
     * 分页列表查询
     *
     * @param pmpSummary
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //默认分页查询
    @GetMapping(value = "/list")
    public Result<?> queryPageList(PmpSummaryInfo info,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        String owner = req.getParameter("owner");
        String begDate = req.getParameter("begDate");
        String endDate = req.getParameter("endDate");
        String taskName = req.getParameter("taskName");
        String projectName = req.getParameter("projectName");
        String summaryTime = req.getParameter("summaryTime");

        Page<PmpSummaryInfo> pageList = new Page<>(pageNo, pageSize);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        Result<Page<PmpSummaryInfo>> result = new Result<Page<PmpSummaryInfo>>();

        Map<String, String> paramMap = new HashMap<>();
        owner = (null == owner) ? sysUser.getUsername() : owner;

        paramMap.put("owner", owner);
        paramMap.put("begDate", begDate);
        paramMap.put("endDate", endDate);
        paramMap.put("summaryTime", summaryTime);
        paramMap.put("taskName", taskName == null ? "" : taskName);
        paramMap.put("projectName", projectName == null ? "" : projectName);

        pageList = service.query(pageList, paramMap);//通知公告消息

        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    //查询统计结果
    @GetMapping(value = "/statistics")
    public Result<?> queryStatisticsByDate(PmpSummaryInfo info,
                                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                           HttpServletRequest req) {
        Result<Page<PmpSummaryStatistics>> result = new Result<Page<PmpSummaryStatistics>>();
        Page<PmpSummaryStatistics> pageList = new Page<PmpSummaryStatistics>(pageNo, pageSize);

        //设置默认时间为当前月份，前台由于使用mixim，造成初始可能没有查询参数
        String summaryTime = info.getSummaryTime();
        if (null == summaryTime) {
            java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM");
            java.util.Date currTime = new java.util.Date();
            summaryTime = formatter.format(currTime);
        }

        pageList = service.queryStatisticsByDate(pageList, summaryTime);

        result.setSuccess(true);
        result.setResult(pageList);

        return result;
    }

    /**
     * 添加
     *
     * @param pmpSummary
     * @return
     */
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody PmpSummaryInfo info) {
        PmpSummary summary = new PmpSummary();

        summary.setId(generateSummaryId());
        summary.setContent(info.getContent());
        summary.setCreateBy(info.getCreateBy());
        summary.setSummaryTime(info.getSummaryTime());
        summary.setContentAnnex(info.getContentAnnex());

        if ((null != info.getTaskid()) && service.save(summary)) {
            service.addSummaryWithTask(summary, info.getTaskid());
        }

//       if (null != info.queryUserId()) {
//           service.addSummaryWithChief(summary, info.queryUserId());
//       }

        return Result.ok("添加成功！");
    }

    private String generateSummaryId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 编辑
     *
     * @param pmpSummary
     * @return
     */
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody PmpSummaryInfo info) {
        PmpSummary summary = new PmpSummary();

        summary.setId(info.getId());
        summary.setContent(info.getContent());
        //summary.setUpdateBy(info.getCreateBy());
        //summary.setUpdateTime(info.getCreateTime());
        summary.setSummaryTime(info.getSummaryTime());
        summary.setContentAnnex(info.getContentAnnex());

        service.updateById(summary);
        service.editSummaryWithTask(summary, info.getTaskid());
        return Result.ok("编辑成功!");
    }
//   public Result<?> edit(@RequestBody PmpSummary pmpSummary) {
//       service.updateById(pmpSummary);
//       service.editSummaryWithTask(pmpSummary);
//       return Result.ok("编辑成功!");
//   }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        service.removeById(id);
        service.removeSummaryWithTask(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.service.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        PmpSummary pmpSummary = service.getById(id);
        if (pmpSummary == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(pmpSummary);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param pmpSummary
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PmpSummary pmpSummary) {
        return super.exportXls(request, pmpSummary, PmpSummary.class, "任务小结");
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
        return super.importExcel(request, response, PmpSummary.class);
    }

}
