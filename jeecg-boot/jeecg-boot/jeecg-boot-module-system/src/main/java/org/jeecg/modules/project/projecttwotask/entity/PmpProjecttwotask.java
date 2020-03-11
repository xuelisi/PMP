package org.jeecg.modules.project.projecttwotask.entity;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;

/**
 * @Description: 项目任务关联表
 * @Author: jeecg-boot
 * @Date:   2020-03-09
 * @Version: V1.0
 */
@Data
@TableName("pmp_projecttwotask")
public class PmpProjecttwotask implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
	private java.lang.String id;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
	private java.lang.String createBy;
	/**创建日期*/
	@Excel(name = "创建日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date createTime;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
	private java.lang.String updateBy;
	/**更新日期*/
	@Excel(name = "更新日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date updateTime;
	/**所属部门*/
	@Excel(name = "所属部门", width = 15)
	private java.lang.String sysOrgCode;
	/**父级节点*/
	@Excel(name = "父级节点", width = 15)
	private java.lang.String parentnode;
	/**是否有子节点*/
	@Excel(name = "是否有子节点", width = 15)
	private java.lang.String hasChild;
	/**头像*/
	@Excel(name = "头像", width = 15)
	private java.lang.String photo;
	/**项目名称*/
	@Excel(name = "项目名称", width = 15)
	private java.lang.String projectname;
	/**任务名称*/
	@Excel(name = "任务名称", width = 15)
	private java.lang.String taskname;
	/**编码*/
	@Excel(name = "编码", width = 15)
	private java.lang.String projectcode;
	/**项目类型*/
	@Excel(name = "项目类型", width = 15)
	private java.lang.String projecttype;
	/**内容*/
	@Excel(name = "内容", width = 15)
	private java.lang.String projectcontent;
	/**负责人*/
	@Excel(name = "负责人", width = 15)
	private java.lang.String principal;
	/**参与者*/
	@Excel(name = "参与者", width = 15)
	private java.lang.String participant;
	/**紧急程度*/
	@Excel(name = "紧急程度", width = 15)
	private java.lang.String emergencylevel;
	/**开始日期*/
	@Excel(name = "开始日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private java.util.Date startdate;
	/**结束日期*/
	@Excel(name = "结束日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private java.util.Date enddate;
	/**完成时间*/
	@Excel(name = "完成时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private java.util.Date completiontime;
	/**进度*/
	@Excel(name = "进度", width = 15)
	private java.lang.Integer schedule;
	/**状态*/
	@Excel(name = "状态", width = 15)
	private java.lang.String status;
	/**费用*/
	@Excel(name = "费用", width = 15)
	private java.lang.Double projectmoney;
	/**备注*/
	@Excel(name = "备注", width = 15)
	private java.lang.String remark;
	/**附件*/
	@Excel(name = "附件", width = 15)
	private java.lang.String annex;
	/**是否删除*/
	@Excel(name = "是否删除", width = 15)
	private java.lang.String isdelete;
	/**左值*/
	@Excel(name = "左值", width = 15)
	private java.lang.Integer leftval;
	/**右值*/
	@Excel(name = "右值", width = 15)
	private java.lang.Integer rightval;
}
