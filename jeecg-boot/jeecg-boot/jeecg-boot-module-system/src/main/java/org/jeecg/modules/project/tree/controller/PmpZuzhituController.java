package org.jeecg.modules.project.tree.controller;

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
import org.jeecg.modules.project.projectmanage.entity.PmpProjectManage;
import org.jeecg.modules.project.projectmanage.service.IPmpProjectManageService;
import org.jeecg.modules.project.tree.entity.PmpZuzhitu;
import org.jeecg.modules.project.tree.service.IPmpZuzhituService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.system.entity.SysCategory;
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
 * @Description: 项目组织树
 * @Author: jeecg-boot
 * @Date:   2020-03-04
 * @Version: V1.0
 */
@RestController
@RequestMapping("/tree/pmpZuzhitu")
@Slf4j
public class PmpZuzhituController extends JeecgController<PmpZuzhitu, IPmpZuzhituService> {
	@Autowired
	private IPmpZuzhituService pmpZuzhituService;
	 @Autowired
	 private IPmpProjectManageService pmpProjectManageService;
	 @Autowired
	 private IPmpZuzhituService pmpZuZhiTuService;

	 /**
	  * 分页列表查询
	  * @param pmpZuzhitu
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @GetMapping(value = "/rootList")
	 public Result<IPage<PmpProjectManage>> queryPageList(PmpZuzhitu pmpZuzhitu,
														  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
														  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
														  HttpServletRequest req) {
		 if(oConvertUtils.isEmpty(pmpZuzhitu.getParentnodeId())){
			 pmpZuzhitu.setParentnodeId("0");
		 }
		 Result<IPage<PmpProjectManage>> result = new Result<IPage<PmpProjectManage>>();

		 Page<PmpProjectManage> page = new Page<PmpProjectManage>(pageNo, pageSize);
		 IPage<PmpProjectManage> pageList = pmpProjectManageService.selectProListPage(page);
		 result.setSuccess(true);
		 result.setResult(pageList);
		 return result;
	 }

	 @GetMapping(value = "/childList")
	 public Result<List<PmpZuzhitu>> queryPageList(PmpZuzhitu pmpZuzhitu,HttpServletRequest req) {
		 Result<List<PmpZuzhitu>> result = new Result<List<PmpZuzhitu>>();
		 QueryWrapper<PmpZuzhitu> queryWrapper = QueryGenerator.initQueryWrapper(pmpZuzhitu, req.getParameterMap());
		 List<PmpZuzhitu> list = pmpZuzhituService.list(queryWrapper);
		 result.setSuccess(true);
		 result.setResult(list);
		 return result;
	 }

	 /**
	  *   添加
	  * @param pmpZuzhitu
	  * @return
	  */
	 @PostMapping(value = "/add")
	 public Result<PmpZuzhitu> add(@RequestBody PmpZuzhitu pmpZuzhitu) {
		 Result<PmpZuzhitu> result = new Result<PmpZuzhitu>();
		 try {
			 pmpZuzhituService.addSysCategory(pmpZuzhitu);
			 result.success("添加成功！");
		 } catch (Exception e) {
			 log.error(e.getMessage(),e);
			 result.error500("操作失败");
		 }
		 return result;
	 }


	 /**
	 *  编辑
	 *
	 * @param pmpZuzhitu
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody PmpZuzhitu pmpZuzhitu) {
		pmpZuzhituService.updateById(pmpZuzhitu);
		return Result.ok("编辑成功!");
	}

	 /**
	  *   通过id删除
	  * @param id
	  * @return
	  */
	 @DeleteMapping(value = "/delete")
	 public Result<PmpZuzhitu> delete(@RequestParam(name="id",required=true) String id) {
		 Result<PmpZuzhitu> result = new Result<PmpZuzhitu>();
		 PmpZuzhitu pmpZuzhitu = pmpZuzhituService.getById(id);
		 if(pmpZuzhitu==null) {
			 result.error500("未找到对应实体");
		 }else {
			 boolean ok = pmpZuzhituService.removeById(id);
			 if(ok) {
				 result.success("删除成功!");
			 }
		 }

		 return result;
	 }
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.pmpZuzhituService.removeByIds(Arrays.asList(ids.split(",")));
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
		PmpZuzhitu pmpZuzhitu = pmpZuzhituService.getById(id);
		if(pmpZuzhitu==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(pmpZuzhitu);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param pmpZuzhitu
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PmpZuzhitu pmpZuzhitu) {
        return super.exportXls(request, pmpZuzhitu, PmpZuzhitu.class, "项目组织树");
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
        return super.importExcel(request, response, PmpZuzhitu.class);
    }

}
