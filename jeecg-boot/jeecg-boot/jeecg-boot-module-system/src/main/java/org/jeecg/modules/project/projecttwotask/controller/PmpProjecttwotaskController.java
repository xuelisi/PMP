package org.jeecg.modules.project.projecttwotask.controller;

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
import org.jeecg.modules.project.projecttwotask.entity.PmpProjecttwotask;
import org.jeecg.modules.project.projecttwotask.service.IPmpProjecttwotaskService;

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
 * @Description: 项目任务关联表
 * @Author: jeecg-boot
 * @Date:   2020-03-09
 * @Version: V1.0
 */
@RestController
@RequestMapping("/projecttwotask/pmpProjecttwotask")
@Slf4j
public class PmpProjecttwotaskController extends JeecgController<PmpProjecttwotask, IPmpProjecttwotaskService>{
	@Autowired
	private IPmpProjecttwotaskService pmpProjecttwotaskService;
	
	/**
	 * 分页列表查询
	 *
	 * @param pmpProjecttwotask
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/rootList")
	public Result<?> queryPageList(PmpProjecttwotask pmpProjecttwotask,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		if(oConvertUtils.isEmpty(pmpProjecttwotask.getParentnode())){
			pmpProjecttwotask.setParentnode("0");
		}
		QueryWrapper<PmpProjecttwotask> queryWrapper = QueryGenerator.initQueryWrapper(pmpProjecttwotask, req.getParameterMap());
		Page<PmpProjecttwotask> page = new Page<PmpProjecttwotask>(pageNo, pageSize);
		IPage<PmpProjecttwotask> pageList = pmpProjecttwotaskService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	 /**
      * 获取子数据
      * @param pmpProjecttwotask
      * @param req
      * @return
      */
	@GetMapping(value = "/childList")
	public Result<?> queryPageList(PmpProjecttwotask pmpProjecttwotask,HttpServletRequest req) {
		QueryWrapper<PmpProjecttwotask> queryWrapper = QueryGenerator.initQueryWrapper(pmpProjecttwotask, req.getParameterMap());
		List<PmpProjecttwotask> list = pmpProjecttwotaskService.list(queryWrapper);
		return Result.ok(list);
	}
	
	
	/**
	 *   添加
	 *
	 * @param pmpProjecttwotask
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody PmpProjecttwotask pmpProjecttwotask) {
		pmpProjecttwotaskService.addPmpProjecttwotask(pmpProjecttwotask);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param pmpProjecttwotask
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody PmpProjecttwotask pmpProjecttwotask) {
		pmpProjecttwotaskService.updatePmpProjecttwotask(pmpProjecttwotask);
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
		pmpProjecttwotaskService.deletePmpProjecttwotask(id);
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
		this.pmpProjecttwotaskService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		PmpProjecttwotask pmpProjecttwotask = pmpProjecttwotaskService.getById(id);
		if(pmpProjecttwotask==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(pmpProjecttwotask);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param pmpProjecttwotask
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PmpProjecttwotask pmpProjecttwotask) {
		return super.exportXls(request, pmpProjecttwotask, PmpProjecttwotask.class, "项目任务关联表");
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
		return super.importExcel(request, response, PmpProjecttwotask.class);
    }

}
