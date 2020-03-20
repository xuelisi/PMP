package org.jeecg.modules.project.protree.controller;

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
import org.jeecg.modules.project.protree.entity.PmpProject;
import org.jeecg.modules.project.protree.service.IPmpProjectService;

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
 * @Date:   2020-03-05
 * @Version: V1.0
 */
@RestController
@RequestMapping("/protree/pmpProject")
@Slf4j
public class PmpProjectController extends JeecgController<PmpProject, IPmpProjectService> {
	@Autowired
		 private IPmpProjectService pmpProjectService;

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
		 queryWrapper.eq("parentnode","0");
		 Page<PmpProject> page = new Page<PmpProject>(pageNo, pageSize);
		 IPage<PmpProject> pageList = pmpProjectService.page(page, queryWrapper);
		 return Result.ok(pageList);
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
	 @GetMapping(value = "/myProList")
	 public Result<?> queryMyProPageList(PmpProject pmpProject,
									@RequestParam(name="username") String username,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
	     Page<PmpProject> page = new Page<PmpProject>(pageNo, pageSize);
		 IPage<PmpProject> pageList = pmpProjectService.myProject(page, username, pmpProject);
		 return Result.ok(pageList);
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
	 @GetMapping(value = "/myProListByPar")
	 public Result<?> queryMyProPageListByPar(PmpProject pmpProject,
										 @RequestParam(name="username") String username,
										 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
										 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
										 HttpServletRequest req) {
		 Page<PmpProject> page = new Page<PmpProject>(pageNo, pageSize);
		 IPage<PmpProject> pageList = pmpProjectService.myProjectpar(page, username, pmpProject);
		 return Result.ok(pageList);
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
	 @GetMapping(value = "/myProListByCreateBy")
	 public Result<?> queryMyProPageListByCreateBy(PmpProject pmpProject,
											  @RequestParam(name="username") String username,
											  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
											  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
											  HttpServletRequest req) {
		 QueryWrapper<PmpProject> queryWrapper = new QueryWrapper<PmpProject>();
		 queryWrapper.eq("parentnode",'0');
		 queryWrapper.eq("create_by", username);
		 if(oConvertUtils.isEmpty(pmpProject.getIsdelete())){
			 pmpProject.setIsdelete("0");
		 }
		 if(oConvertUtils.isEmpty(pmpProject.getProjectname())){
			 pmpProject.setProjectname("");
		 }
		 queryWrapper.eq("isdelete",pmpProject.getIsdelete());
		 queryWrapper.like("projectname",pmpProject.getProjectname());
		 Page<PmpProject> page = new Page<PmpProject>(pageNo, pageSize);
		 IPage<PmpProject> pageList = pmpProjectService.page(page, queryWrapper);
		 return Result.ok(pageList);
	 }

		 /**
		  * 分页列表查询
		  * @param pmpProject
		  * @param pageNo
		  * @param pageSize
		  * @param req
		  * @return
		  */
		 @GetMapping(value = "/rootList")
		 public Result<IPage<PmpProject>> queryRootPageList(PmpProject pmpProject,
				 @RequestParam(name="pid", required = false) String pid,
				 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
				 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
				 HttpServletRequest req) {
			 if(oConvertUtils.isEmpty(pmpProject.getParentnode())){
				 pmpProject.setParentnode("0");
			 }
			 Result<IPage<PmpProject>> result = new Result<IPage<PmpProject>>();

			 //--author:os_chengtgen---date:20190804 -----for: 分类字典页面显示错误,issues:377--------start
			 //QueryWrapper<SysCategory> queryWrapper = QueryGenerator.initQueryWrapper(sysCategory, req.getParameterMap());
			 QueryWrapper<PmpProject> queryWrapper = new QueryWrapper<PmpProject>();
			 queryWrapper.eq("parentnode", pmpProject.getParentnode());
			 queryWrapper.eq("id", pid);
			 //--author:os_chengtgen---date:20190804 -----for: 分类字典页面显示错误,issues:377--------end

		 Page<PmpProject> page = new Page<PmpProject>(pageNo, pageSize);
		 IPage<PmpProject> pageList = pmpProjectService.page(page, queryWrapper);
		 result.setSuccess(true);
		 result.setResult(pageList);
		 return result;
	 }

	 @GetMapping(value = "/childList")
	 public Result<List<PmpProject>> queryChildPageList(PmpProject pmpProject,HttpServletRequest req) {
		 Result<List<PmpProject>> result = new Result<List<PmpProject>>();
		 QueryWrapper<PmpProject> queryWrapper = QueryGenerator.initQueryWrapper(pmpProject, req.getParameterMap());
		 List<PmpProject> list = pmpProjectService.list(queryWrapper);
		 result.setSuccess(true);
		 result.setResult(list);
		 return result;
	 }

	 /**
	  *   添加
	  * @param pmpProject
	  * @return
	  */
	 @PostMapping(value = "/addTask")
	 public Result<PmpProject> addtask(@RequestBody PmpProject pmpProject) {
		 Result<PmpProject> result = new Result<PmpProject>();
		 try {
			 pmpProjectService.addSysCategory(pmpProject);
			 pmpProjectService.updateMyNode(pmpProject.getProjectname(),pmpProject.getParentnode(),pmpProject.getId());
			 result.success("添加成功！");
		 } catch (Exception e) {
			 log.error(e.getMessage(),e);
			 result.error500("操作失败");
		 }
		 return result;
	 }

	 /**
	  *   添加
	  *
	  * @param pmpProject
	  * @return
	  */
	 @PostMapping(value = "/addPro")
	 public Result<?> addpro(@RequestBody PmpProject pmpProject) {
	 	if(oConvertUtils.isEmpty(pmpProject.getLeftval())){
			pmpProject.setLeftval(1);
		}
		 if(oConvertUtils.isEmpty(pmpProject.getRightval())){
			 pmpProject.setRightval(2) ;
		 }
		 pmpProjectService.addSysCategory(pmpProject);
		 return Result.ok("添加成功！");
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
	  *   通过id禁用
	  *
	  * @param id
	  * @return
	  */
	 @GetMapping(value = "/disable")
	 public Result<?> disable(@RequestParam(name="id",required=true) String id) {
	 	 PmpProject pmpProject = pmpProjectService.getById(id);
		 pmpProjectService.isDelteSubNode(pmpProject.getProjectname(), id,"1");
		 return Result.ok("禁用成功!");
	 }

	 /**
	  *   通过id启用
	  *
	  * @param id
	  * @return
	  */
	 @GetMapping(value = "/enable")
	 public Result<?> enable(@RequestParam(name="id",required=true) String id) {
		 PmpProject pmpProject = pmpProjectService.getById(id);
		 pmpProjectService.isDelteSubNode(pmpProject.getProjectname(), id,"0");
		 return Result.ok("启用成功!");
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
	 @GetMapping(value = "/isSuperior")
	 public Result<?> isSuperior(@RequestParam(name="id",required=true) String id,
								 @RequestParam(name="principal",required=true) String principal) {
		 Integer count = pmpProjectService.IsSuperior(id,principal);
		 if(count==null || count == 0) {
			 return Result.error("未找到对应数据");
		 }
		 return Result.ok(count);
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
	  * 校验编码
	  * @param pid
	  * @param code
	  * @return
	  */
	 @GetMapping(value = "/checkCode")
	 public Result<?> checkCode(@RequestParam(name="parentnode",required = false) String pid,@RequestParam(name="projectcode",required = false) String code) {
		 if(oConvertUtils.isEmpty(code)){
			 return Result.error("错误,类型编码为空!");
		 }
		 if(oConvertUtils.isEmpty(pid)){
			 return Result.ok();
		 }
		 PmpProject parent = this.pmpProjectService.getById(pid);
		 if(code.startsWith(parent.getProjectcode())){
			 return Result.ok();
		 }else{
			 return Result.error("编码不符合规范,须以\""+parent.getProjectcode()+"\"开头!");
		 }

	 }
}
