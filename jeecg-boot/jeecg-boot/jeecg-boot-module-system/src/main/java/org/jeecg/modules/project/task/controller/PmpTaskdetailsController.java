package org.jeecg.modules.project.task.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.project.task.entity.PmpTaskdetails;
import org.jeecg.modules.project.task.entity.PmpTaskdetailsVM;
import org.jeecg.modules.project.task.service.IPmpTaskdetailsService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.project.tree.service.IPmpZuzhituService;
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
 * @Description: 分发详情表
 * @Author: jeecg-boot
 * @Date:   2020-03-04
 * @Version: V1.0
 */
@RestController
@RequestMapping("/task/pmpTaskdetails")
@Slf4j
public class PmpTaskdetailsController extends JeecgController<PmpTaskdetails, IPmpTaskdetailsService> {
	@Autowired
	private IPmpTaskdetailsService pmpTaskdetailsService;
	@Autowired
	private IPmpZuzhituService pmpZuZhiTuService;
	/**
	 * 分页列表查询
	 *
	 * @param pmpTaskdetails
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(PmpTaskdetails pmpTaskdetails,
								   @RequestParam(name="pid") String pid,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<PmpTaskdetails> queryWrapper = QueryGenerator.initQueryWrapper(pmpTaskdetails, req.getParameterMap());
		Page<PmpTaskdetails> page = new Page<PmpTaskdetails>(pageNo, pageSize);
		IPage<PmpTaskdetails> pageList = pmpTaskdetailsService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param vm
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody PmpTaskdetailsVM vm) {
		System.out.println("======================="+vm.toString());
		/*PmpTaskdetails pmpTaskdetails = new PmpTaskdetails();
		pmpTaskdetails.setCreateBy(vm.getCreateBy());
		pmpTaskdetails.setCreateTime(vm.getCreateTime());
		pmpTaskdetails.setUpdateBy(vm.getUpdateBy());
		pmpTaskdetails.setUpdateTime(vm.getUpdateTime());
		pmpTaskdetails.setSysOrgCode(vm.getSysOrgCode());
		pmpTaskdetails.setTaskname(vm.getTaskname());
		pmpTaskdetails.setTaskcontent(vm.getTaskcontent());
		pmpTaskdetails.setTaskprincipal(vm.getTaskprincipal());
		pmpTaskdetails.setParticipant(vm.getParticipant());
		pmpTaskdetails.setTaskstatus(vm.getTaskstatus());
		pmpTaskdetails.setTaskschedule(vm.getTaskschedule());
		pmpTaskdetails.setStartdate(vm.getStartdate());
		pmpTaskdetails.setEnddate(vm.getEnddate());
		pmpTaskdetails.setCompletiontime(vm.getCompletiontime());
		pmpTaskdetails.setEmergencylevel(vm.getEmergencylevel());
		pmpTaskdetails.setTaskmoney(vm.getTaskmoney());
		pmpTaskdetails.setTaskremark(vm.getTaskremark());
		pmpTaskdetails.setTaskannex(vm.getTaskannex());*/
//		pmpTaskdetailsService.save(pmpTaskdetails);
//		pmpZuZhiTuService.addMyNode(UUID.randomUUID().toString().replaceAll("-", ""),"1235111249369812994", pmpTaskdetails.getId());
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param pmpTaskdetails
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody PmpTaskdetails pmpTaskdetails) {
		pmpTaskdetailsService.updateById(pmpTaskdetails);
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
		pmpTaskdetailsService.removeById(id);
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
		this.pmpTaskdetailsService.removeByIds(Arrays.asList(ids.split(",")));
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
		PmpTaskdetails pmpTaskdetails = pmpTaskdetailsService.getById(id);
		if(pmpTaskdetails==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(pmpTaskdetails);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param pmpTaskdetails
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PmpTaskdetails pmpTaskdetails) {
        return super.exportXls(request, pmpTaskdetails, PmpTaskdetails.class, "分发详情表");
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
        return super.importExcel(request, response, PmpTaskdetails.class);
    }

}
