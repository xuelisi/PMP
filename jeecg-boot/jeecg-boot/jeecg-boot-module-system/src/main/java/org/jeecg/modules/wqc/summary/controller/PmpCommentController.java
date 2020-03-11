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
import org.jeecg.modules.wqc.summary.entity.PmpComment;
import org.jeecg.modules.wqc.summary.service.IPmpCommentService;

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
 * @Description: 任务批阅
 * @Author: jeecg-boot
 * @Date:   2020-03-11
 * @Version: V1.0
 */
@RestController
@RequestMapping("/summary/pmpComment")
@Slf4j
public class PmpCommentController extends JeecgController<PmpComment, IPmpCommentService> {
	@Autowired
	private IPmpCommentService pmpCommentService;
	
	/**
	 * 分页列表查询
	 *
	 * @param pmpComment
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
//	@GetMapping(value = "/list")
//	public Result<?> queryPageList(PmpComment pmpComment,
//								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
//								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
//								   HttpServletRequest req) {
//		QueryWrapper<PmpComment> queryWrapper = QueryGenerator.initQueryWrapper(pmpComment, req.getParameterMap());
//		Page<PmpComment> page = new Page<PmpComment>(pageNo, pageSize);
//		IPage<PmpComment> pageList = pmpCommentService.page(page, queryWrapper);
//		return Result.ok(pageList);
//	}

//	 @RequestMapping(value = "/list", method = RequestMethod.GET)
//	 public Result<List<PmpComment>> queryPmpComment(@RequestParam(name="taskid",required=true) String taskid) {
//		 Result<List<PmpComment>> result = new Result<>();
//		 List<PmpComment> cmtList = pmpCommentService.getPmpCommentByTaskID(taskid);
//
//		 result.setResult(cmtList);
//		 result.setSuccess(true);
//
//		 return result;
//	 }

	 @RequestMapping(value = "/list", method = RequestMethod.GET)
	 public Result<List<PmpComment>> queryPmpComment(@RequestParam(name="taskid",required=true) String taskid,
													 @RequestParam(name="userName",required=true) String userName) {
		 Result<List<PmpComment>> result = new Result<>();
		 List<PmpComment> cmtList = pmpCommentService.getPmpCommentByTaskID(taskid);

		 if ("" != userName) {
			 for (int i = cmtList.size() - 1; i >= 0; --i) {
				 PmpComment item = cmtList.get(i);
				 if (!userName.equals(item.getCreateBy())) {
					 cmtList.remove(i);
				 }
			 }
		 }

		 result.setResult(cmtList);
		 result.setSuccess(true);

		 return result;
	 }


	 /**
	 *   添加
	 *
	 * @param pmpComment
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody PmpComment pmpComment) {
		pmpCommentService.save(pmpComment);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param pmpComment
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody PmpComment pmpComment) {
		pmpCommentService.updateById(pmpComment);
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
		pmpCommentService.removeById(id);
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
		this.pmpCommentService.removeByIds(Arrays.asList(ids.split(",")));
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
		PmpComment pmpComment = pmpCommentService.getById(id);
		if(pmpComment==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(pmpComment);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param pmpComment
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PmpComment pmpComment) {
        return super.exportXls(request, pmpComment, PmpComment.class, "任务批阅");
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
        return super.importExcel(request, response, PmpComment.class);
    }

}
