package org.jeecg.modules.project.task.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
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
 * @Description: 分发详情表
 * @Author: jeecg-boot
 * @Date:   2020-02-26
 * @Version: V1.0
 */
@Data
@TableName("pmp_taskdetails")
public class PmpTaskdetails implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    private String id;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    private String createBy;
	/**创建日期*/
	@Excel(name = "创建日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
    private String updateBy;
	/**更新日期*/
	@Excel(name = "更新日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
	/**所属部门*/
	@Excel(name = "所属部门", width = 15)
    private String sysOrgCode;
	/**任务组织图ID*/
	@Excel(name = "任务组织图ID", width = 15)
    private String taskzuzhituid;
	/**名称*/
	@Excel(name = "名称", width = 15)
    private String taskname;
	/**任务内容*/
	@Excel(name = "任务内容", width = 15)
    private String taskcontent;
	/**负责人*/
	@Excel(name = "负责人", width = 15)
    private String taskprincipal;
	/**任务进度*/
	@Excel(name = "任务进度", width = 15)
    private Integer taskschedule;
	/**任务状态*/
	@Excel(name = "任务状态", width = 15)
    private Integer taskstatus;
	/**开始日期*/
	@Excel(name = "开始日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date startdate;
	/**结束日期*/
	@Excel(name = "结束日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date enddate;
	/**实际完成时间*/
	@Excel(name = "实际完成时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date completiontime;
	/**紧急程度*/
	@Excel(name = "紧急程度", width = 15)
    private String emergencylevel;
	/**任务费用*/
	@Excel(name = "任务费用", width = 15)
    private Double taskmoney;
	/**验收人*/
	@Excel(name = "验收人", width = 15)
    private String receiver;
	/**验收人内容*/
	@Excel(name = "验收人内容", width = 15)
    private String recContent;
	/**验收人打分*/
	@Excel(name = "验收人打分", width = 15)
    private Integer recScore;
	/**任务备注*/
	@Excel(name = "任务备注", width = 15)
    private String taskremark;
	/**是否删除*/
	@Excel(name = "是否删除", width = 15)
    private Integer isdelete;
}
