package org.jeecg.modules.project.projectmanage.entity;

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
 * @Description: 项目主表
 * @Author: jeecg-boot
 * @Date:   2020-03-03
 * @Version: V1.0
 */
@Data
@TableName("pmp_projectmain")
public class PmpProjectManage implements Serializable {
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
	/**项目图标*/
	@Excel(name = "项目图标", width = 15)
    private String photo;
	/**项目名称*/
	@Excel(name = "项目名称", width = 15)
    private String projectname;
	/**项目类型*/
	@Excel(name = "项目类型", width = 15)
    private String projecttype;
	/**项目内容*/
	@Excel(name = "项目内容", width = 15)
    private String projectcontent;
	/**负责人*/
	@Excel(name = "负责人", width = 15)
    private String principal;
	/**参与人*/
	@Excel(name = "参与人", width = 15)
    private String participant;
	/**紧急程度*/
	@Excel(name = "紧急程度", width = 15)
    private String emergencylevel;
	/**起始日期*/
	@Excel(name = "起始日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date startdate;
	/**结束日期*/
	@Excel(name = "结束日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date enddate;
	/**总进度*/
	@Excel(name = "总进度", width = 15)
    private Integer schedule;
	/**备注*/
	@Excel(name = "备注", width = 15)
    private String remark;
	/**附件*/
	@Excel(name = "附件", width = 15)
    private String annex;
	/**是否删除*/
	@Excel(name = "是否删除", width = 15)
    private Integer isdelete;
}
