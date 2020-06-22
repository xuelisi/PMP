package org.jeecg.modules.wqc.summary.controller;

import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.wqc.summary.entity.PmpComment;
import org.jeecg.modules.wqc.summary.entity.PmpCommentParam;
import org.jeecg.modules.wqc.summary.entity.PmpCommentInfo;
import org.jeecg.modules.wqc.summary.entity.PmpCommentSummary;
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
import org.springframework.context.annotation.Lazy;
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

	 @Autowired
	 @Lazy
	 private ISysBaseAPI sysBaseAPI;

	/**
	 * 分页列表查询
	 *
	 * @param pmpComment
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(PmpComment pmpComment,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		String begDate = req.getParameter("begDate");
		String endDate = req.getParameter("endDate");
		String taskName = req.getParameter("taskName");
		String projectName = req.getParameter("projectName");
		String category = req.getParameter("category");
		String commentee = req.getParameter("commentee");
		String commentator = req.getParameter("commentator");
		Result<Page<PmpCommentInfo>> result = new Result<Page<PmpCommentInfo>>();
		Page<PmpCommentInfo> pageList = new Page<>(pageNo, pageSize);

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("begDate", begDate);
		paramMap.put("endDate", endDate);
		paramMap.put("taskName", taskName == null ? "" : taskName);
		paramMap.put("projectName", projectName == null ? "" : projectName);
		paramMap.put("category", category == null ? "" : category);
		paramMap.put("commentee", commentee == null ? "" : commentee);
		paramMap.put("commentator", commentator == null ? "" : commentator);
		pageList = pmpCommentService.query(pageList, paramMap);//通知公告消息

		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	 @RequestMapping(value = "/query", method = RequestMethod.GET)
	 public Result<List<PmpCommentInfo>> queryByTask(@RequestParam(name="taskid",required=true) String taskid,
													 @RequestParam(name="userName",required=true) String userName) {
		 Result<List<PmpCommentInfo>> result = new Result<>();
		 List<PmpCommentInfo> cmtList = pmpCommentService.queryByTask(taskid, userName);

		 result.setResult(cmtList);
		 result.setSuccess(true);

		 return result;
	 }

	 @GetMapping(value = "/union")
	 public Result<List<PmpCommentSummary>> queryUnion(@RequestParam(name="taskid",required=true) String taskid) {
		 Result<List<PmpCommentSummary>> result = new Result<>();
		 List<PmpCommentSummary> cmtList = pmpCommentService.queryUnionByTaskid(taskid);

		 result.setResult(cmtList);
		 result.setSuccess(true);

		 return result;
	 }


	 @RequestMapping(value = "/realname", method = RequestMethod.GET)
	 public Result<String> queryRealName(@RequestParam(name="username",required=true) String username) {
		 Result<String> result = new Result<>();
		 String realName = pmpCommentService.queryRealName(username);

		 result.setResult(realName);
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
	public Result<?> add(@RequestBody PmpCommentParam param) {
		PmpComment pmpComment = new PmpComment();
		pmpComment.setId(generateSummaryId());
		pmpComment.setTaskid(param.getTaskid());
		pmpComment.setContent(param.getContent());
		pmpComment.setCommentee(param.getCommentee());
		if (pmpCommentService.save(pmpComment)) {
			pmpCommentService.addCommentWithCategory(pmpComment, param.getCategory());

			String taskname = pmpCommentService.queryTaskNameByTaskid(param.getTaskid());
			String from = ((LoginUser)SecurityUtils.getSubject().getPrincipal()).getUsername();
			String list[] = param.getCommentee().split(",");
			for (String to : list) {
				sendCommentNote(from, to, taskname);
			}
		}

		return Result.ok("评论成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param pmpComment
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody PmpCommentParam param) {
		PmpComment pmpComment = new PmpComment();
		pmpComment.setId(param.getId());
		pmpComment.setContent(param.getContent());

		pmpCommentService.updateById(pmpComment);
		pmpCommentService.editCommentWithCategory(pmpComment, param.getCategory());

		return Result.ok("编辑成功!");
	}
//	 public Result<?> edit(@RequestBody PmpComment pmpComment) {
//		 pmpCommentService.updateById(pmpComment);
//		 return Result.ok("编辑成功!");
//	 }
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		pmpCommentService.removeById(id);
		pmpCommentService.removeCommentWithCategory(id);

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

	 private String generateSummaryId() {
		 return UUID.randomUUID().toString().replace("-", "");
	 }

    private void sendCommentNote(String from, String to, String taskname) {
		String Commentee = pmpCommentService.queryRealName(to);
		String Commentator = pmpCommentService.queryRealName(from);
		HashMap<String, String> paramMap = new HashMap<>();
		paramMap.put("Commentee", Commentee);
		paramMap.put("Commentator", Commentator);
		paramMap.put("taskName", taskname);

		sysBaseAPI.sendSysAnnouncement(from, to, "你有新的评论通知！", paramMap, "bpm_comment_note");
	}
}
