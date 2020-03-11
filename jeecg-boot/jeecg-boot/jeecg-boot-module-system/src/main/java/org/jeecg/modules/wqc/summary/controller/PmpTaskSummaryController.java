package org.jeecg.modules.wqc.summary.controller;

import java.util.ArrayList;
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
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.wqc.summary.entity.PmpComment;
import org.jeecg.modules.wqc.summary.entity.PmpInfo;
import org.jeecg.modules.wqc.summary.entity.PmpSummary;
import org.jeecg.modules.wqc.summary.entity.PmpTaskSummary;
import org.jeecg.modules.wqc.summary.service.IPmpTaskSummaryService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

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

 /**
 * @Description: 任务小结
 * @Author: jeecg-boot
 * @Date:   2020-03-06
 * @Version: V1.0
 */
@RestController
@RequestMapping("/summary/pmpTaskSummary")
@Slf4j
public class PmpTaskSummaryController extends JeecgController<PmpTaskSummary, IPmpTaskSummaryService> {
	@Autowired
	private IPmpTaskSummaryService pmpTaskSummaryService;
	
	/**
	 * 分页列表查询
	 *
	 * @param pmpTaskSummary
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
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
	 @RequestMapping(value = "/list", method = RequestMethod.GET)
	 public Result<IPage<PmpTaskSummary>> queryPageList(PmpTaskSummary summary,
												 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
												 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
												 HttpServletRequest req) {
		 Result<IPage<PmpTaskSummary>> result = new Result<IPage<PmpTaskSummary>>();
		 QueryWrapper<PmpTaskSummary> queryWrapper = QueryGenerator.initQueryWrapper(summary, req.getParameterMap());
		 Page<PmpTaskSummary> page = new Page<PmpTaskSummary>(pageNo, pageSize);
		 String projectid = req.getParameter("projectid");
		 IPage<PmpTaskSummary> pageList = pmpTaskSummaryService.getSummaryByProjectId(page, projectid);

		 result.setSuccess(true);
		 result.setResult(pageList);
		 return result;
	 }

	 @RequestMapping(value = "/info", method = RequestMethod.GET)
	 public Result<PmpInfo> queryPmpInfo(@RequestParam(name="projectid",required=true) String projectid) {
		 Result<PmpInfo> result = new Result<PmpInfo>();
		 PmpInfo tmp = pmpTaskSummaryService.getPmpInfoByProjectID(projectid);

		 if (null == tmp) {
			 result.error500("未找到对应实体");
		 } else {
			 result.setResult(tmp);
			 result.setSuccess(true);
		 }
		 return result;
	 }

	 @RequestMapping(value = "/summary", method = RequestMethod.GET)
	 public Result<List<PmpSummary>> queryPmpSummary(@RequestParam(name="taskid",required=true) String taskid) {
		 Result<List<PmpSummary>> result = new Result<>();
		 List<PmpSummary> tmp = pmpTaskSummaryService.getPmpSummaryByTaskID(taskid);

		 result.setResult(tmp);
		 result.setSuccess(true);

		 return result;
	 }

//	 //测试使用，暂存在问题，使用上面同名函数
//	 @RequestMapping(value = "/summary", method = RequestMethod.GET)
//	 public Object queryPmpSummary(@RequestParam(name="taskid",required=true) String taskid) {
//		 Result<List<PmpSummary>> result = new Result<>();
//		 List<PmpSummary> tmp = pmpTaskSummaryService.getPmpSummaryByTaskID(taskid);
//
//		 List<JSONObject> jsonObjects = new ArrayList<>();
//		 for (PmpSummary l : tmp) {
//			 JSONObject jsonObject = new JSONObject();
//			 try {
//				 jsonObject.put("title", l.getCreateTime());
//				 jsonObject.put("description", l.getCreateBy() + ": " + l.getContent());
//			 } catch (NumberFormatException e) {
//				 jsonObject.put("title", "");
//				 jsonObject.put("description", "");
//			 }
//			 jsonObjects.add(jsonObject);
//		 }
//
//		 log.info("---------" + jsonObjects);
//
//		 return jsonObjects;
//	 }

	 /**
	 *   添加
	 *
	 * @param pmpTaskSummary
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody PmpTaskSummary pmpTaskSummary) {
		pmpTaskSummaryService.save(pmpTaskSummary);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param pmpTaskSummary
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody PmpTaskSummary pmpTaskSummary) {
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
		PmpTaskSummary pmpTaskSummary = pmpTaskSummaryService.getById(id);
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
    public ModelAndView exportXls(HttpServletRequest request, PmpTaskSummary pmpTaskSummary) {
        return super.exportXls(request, pmpTaskSummary, PmpTaskSummary.class, "任务小结");
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
        return super.importExcel(request, response, PmpTaskSummary.class);
    }

}
