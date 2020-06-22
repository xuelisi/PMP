package org.jeecg.modules.project.taskmanage.controller;

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
import org.jeecg.modules.project.taskmanage.entity.PmpTask;
import org.jeecg.modules.project.taskmanage.service.IPmpTaskService;

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
 * @Description: 项目管理
 * @Author: jeecg-boot
 * @Date:   2020-03-12
 * @Version: V1.0
 */
@RestController
@RequestMapping("/taskmanage/pmpTask")
@Slf4j
public class PmpTaskController extends JeecgController<PmpTask, IPmpTaskService> {
	@Autowired
	private IPmpTaskService pmpTaskService;
	
	/**
	 * 分页列表查询
	 *
	 * @param pmpTask
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(PmpTask pmpTask,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		if(oConvertUtils.isEmpty(pmpTask.getIsdelete())){
			pmpTask.setIsdelete("0");
		}
		QueryWrapper<PmpTask> queryWrapper = QueryGenerator.initQueryWrapper(pmpTask, req.getParameterMap());
		queryWrapper.ne("parentnode",'0');
		String taskprincipal = pmpTask.getPrincipal();
		if (oConvertUtils.isNotEmpty(taskprincipal)) {
			String[] result = taskprincipal.split(",");
			int i = 0;
			for (String k : result) {
				if (i == 0) {
					queryWrapper.like("principal", k);
				} else {
					queryWrapper.or().like("principal", k);
				}
				i++;
			}
		}
		Page<PmpTask> page = new Page<PmpTask>(pageNo, pageSize);
		IPage<PmpTask> pageList = pmpTaskService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	 /**
	  * 分页列表查询
	  *
	  * @param pmpTask
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @GetMapping(value = "/myTaskList")
	 public Result<?> queryMyTaskPageList(PmpTask pmpTask,
										 @RequestParam(name="username") String username,
										 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
										 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
										 HttpServletRequest req) {
		 Page<PmpTask> page = new Page<PmpTask>(pageNo, pageSize);
		 IPage<PmpTask> pageList = pmpTaskService.myTaskpri(page, username, pmpTask);
		 return Result.ok(pageList);
	 }

     /**
      * 分页列表查询
      *
      * @param pmpTask
      * @param pageNo
      * @param pageSize
      * @param req
      * @return
      */
     @GetMapping(value = "/myTaskListByPar")
     public Result<?> queryMyTaskPageListByPar(PmpTask pmpTask,
                                          @RequestParam(name="username") String username,
                                          @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                          @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                          HttpServletRequest req) {
         Page<PmpTask> page = new Page<PmpTask>(pageNo, pageSize);
         IPage<PmpTask> pageList = pmpTaskService.myTaskpar(page, username, pmpTask);
         return Result.ok(pageList);
     }

     /**
      * 分页列表查询
      *
      * @param pmpTask
      * @param pageNo
      * @param pageSize
      * @param req
      * @return
      */
     @GetMapping(value = "/myTaskListByCreateBy")
     public Result<?> queryMyProPageListByCreateBy(PmpTask pmpTask,
                                                   @RequestParam(name="username") String username,
                                                   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                   HttpServletRequest req) {
         QueryWrapper<PmpTask> queryWrapper = new QueryWrapper<PmpTask>();
         queryWrapper.ne("parentnode",'0');
         queryWrapper.eq("create_by", username);
		 if(oConvertUtils.isEmpty(pmpTask.getProjectname())){
			 pmpTask.setProjectname("");
		 }
		 if(oConvertUtils.isEmpty(pmpTask.getTaskname())){
			 pmpTask.setTaskname("");
		 }
		 queryWrapper.like("projectname", pmpTask.getProjectname());
		 queryWrapper.like("taskname", pmpTask.getTaskname());
		 queryWrapper.eq("isdelete",'0');
         Page<PmpTask> page = new Page<PmpTask>(pageNo, pageSize);
         IPage<PmpTask> pageList = pmpTaskService.page(page, queryWrapper);
         return Result.ok(pageList);
     }

	/**
	 *   添加
	 *
	 * @param pmpTask
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody PmpTask pmpTask) {
		pmpTaskService.save(pmpTask);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param pmpTask
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody PmpTask pmpTask) {
		pmpTaskService.updateById(pmpTask);
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
		pmpTaskService.removeById(id);
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
		this.pmpTaskService.removeByIds(Arrays.asList(ids.split(",")));
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
		PmpTask pmpTask = pmpTaskService.getById(id);
		if(pmpTask==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(pmpTask);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param pmpTask
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PmpTask pmpTask) {
        return super.exportXls(request, pmpTask, PmpTask.class, "项目管理");
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
        return super.importExcel(request, response, PmpTask.class);
    }

}
