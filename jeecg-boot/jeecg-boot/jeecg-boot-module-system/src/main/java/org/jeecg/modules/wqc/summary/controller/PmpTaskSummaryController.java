package org.jeecg.modules.wqc.summary.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.wqc.summary.entity.PmpSummary;
import org.jeecg.modules.wqc.summary.entity.PmpSummaryInfo;
import org.jeecg.modules.wqc.summary.service.IPmpTaskSummaryService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;

 /**
 * @Description: 任务小结
 * @Author: jeecg-boot
 * @Date:   2020-03-06
 * @Version: V1.0
 */
@RestController
@RequestMapping("/summary/pmpTaskSummary")
@Slf4j
public class PmpTaskSummaryController extends JeecgController<PmpSummary, IPmpTaskSummaryService> {
	@Autowired
	private IPmpTaskSummaryService pmpTaskSummaryService;
	
//	/**
//	 * 分页列表查询
//	 *
//	 * @param pmpTaskSummary
//	 * @param pageNo
//	 * @param pageSize
//	 * @param req
//	 * @return
//	 */
//	@GetMapping(value = "/list")
//	public Result<?> queryPageList(PmpTaskSummary pmpTaskSummary,
//								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
//								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
//								   HttpServletRequest req) {
//		QueryWrapper<PmpTaskSummary> queryWrapper = QueryGenerator.initQueryWrapper(pmpTaskSummary, req.getParameterMap());
//		Page<PmpTaskSummary> page = new Page<PmpTaskSummary>(pageNo, pageSize);
//		IPage<PmpTaskSummary> pageList = pmpTaskSummaryService.page(page, queryWrapper);
//		return Result.ok(pageList);
//	}

	@GetMapping(value = "/list")
	 public Result<?> queryPageList(PmpSummaryInfo info,
                                    @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                    @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                    HttpServletRequest req) {
		 String taskName = req.getParameter("taskName");
		 String projectName = req.getParameter("projectName");
		 Result<Page<PmpSummaryInfo>> result = new Result<Page<PmpSummaryInfo>>();
		 Page<PmpSummaryInfo> pageList = new Page<PmpSummaryInfo>(pageNo, pageSize);

		 taskName = (null == taskName) ? "" : taskName;
		 projectName = (null == projectName) ? "" : projectName;
		 pageList = pmpTaskSummaryService.getPmpSummaryInfoByPTName(pageList, projectName, taskName);//通知公告消息

		 result.setSuccess(true);
		 result.setResult(pageList);
		 return result;
	 }
	
	/**
	 *   添加
	 *
	 * @param summary
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody PmpSummary summary) {
		pmpTaskSummaryService.save(summary);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param pmpTaskSummary
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody PmpSummary pmpTaskSummary) {
		pmpTaskSummaryService.updateById(pmpTaskSummary);
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
		pmpTaskSummaryService.removeById(id);
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
		this.pmpTaskSummaryService.removeByIds(Arrays.asList(ids.split(",")));
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
		PmpSummary pmpTaskSummary = pmpTaskSummaryService.getById(id);
		if(pmpTaskSummary==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(pmpTaskSummary);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param pmpTaskSummary
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PmpSummary pmpTaskSummary) {
        return super.exportXls(request, pmpTaskSummary, PmpSummary.class, "任务小结");
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
