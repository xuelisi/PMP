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
import org.jeecg.modules.wqc.summary.entity.PmpSummaryResult;
import org.jeecg.modules.wqc.summary.service.IPmpSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
* @Description: 任务小结
* @Author: jeecg-boot
* @Date:   2020-03-06
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
   @GetMapping(value = "/list")
    public Result<?> queryPageList(PmpSummaryInfo info,
                                         @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                         @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                         HttpServletRequest req) {
        Result<Page<PmpSummaryInfo>> result = new Result<Page<PmpSummaryInfo>>();
        Page<PmpSummaryInfo> pageList = new Page<PmpSummaryInfo>(pageNo, pageSize);

        pageList = service.querySummaryInfo(pageList);

        result.setSuccess(true);
        result.setResult(pageList);

        return result;
    }

    @GetMapping(value = "/queryByDate")
    public Result<?> querySummaryInfoByDate(@RequestParam(name="date",required=true) String date) {
        Result<List<PmpSummaryInfo>> result = new Result<>();
		List<PmpSummaryInfo> cmtList = service.querySummaryInfoByDate(date);

		result.setResult(cmtList);
        result.setSuccess(true);

        return result;
    }

    @GetMapping(value = "/result")
    public Result<?> querySummaryResultByDate(PmpSummaryInfo info,
                                   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                   HttpServletRequest req) {
        Result<Page<PmpSummaryResult>> result = new Result<Page<PmpSummaryResult>>();
        Page<PmpSummaryResult> pageList = new Page<PmpSummaryResult>(pageNo, pageSize);

        pageList = service.querySummaryResultByDate(pageList, info.getSummaryTime());

        result.setSuccess(true);
        result.setResult(pageList);

        return result;
    }

//   @RequestMapping(value = "/list", method = RequestMethod.GET)
//   public Result<IPage<PmpSummary>> queryPageList(PmpSummary summary, @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
//                                               @RequestParam(name="pageSize", defaultValue="10") Integer pageSize, HttpServletRequest req) {
//       Result<IPage<PmpSummary>> result = new Result<IPage<PmpSummary>>();
//       QueryWrapper<PmpSummary> queryWrapper = QueryGenerator.initQueryWrapper(summary, req.getParameterMap());
//       Page<PmpSummary> page = new Page<PmpSummary>(pageNo, pageSize);
//       IPage<PmpSummary> pageList = service.page(page, queryWrapper);
//       result.setSuccess(true);
//       result.setResult(pageList);
//       return result;
//   }

   /**
    *   添加
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

       service.save(summary);
       if (null != info.getTaskid()) {
           service.addSummaryWithTask(summary, info.getTaskid());
       }

//       if (null != info.queryUserId()) {
//           service.addSummaryWithChief(summary, info.queryUserId());
//       }

       return Result.ok("添加成功！");
   }

    @RequestMapping(value = "/generateSummaryId", method = RequestMethod.GET)
    public String generateSummaryId() {
       return UUID.randomUUID().toString().replace("-", "");
    }

   /**
    *  编辑
    *
    * @param pmpSummary
    * @return
    */
   @PutMapping(value = "/edit")
   public Result<?> edit(@RequestBody PmpSummary pmpSummary) {
       service.updateById(pmpSummary);
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
       service.removeById(id);
       service.removeSummaryWithTask(id);
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
   public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
       PmpSummary pmpSummary = service.getById(id);
       if(pmpSummary==null) {
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
