package org.jeecg.modules.project.tree.entity;

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
 * @Description: 项目组织树
 * @Author: jeecg-boot
 * @Date:   2020-03-04
 * @Version: V1.0
 */
@Data
@TableName("pmp_zuzhitu")
public class PmpZuzhitu implements Serializable {
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
	/**项目ID*/
	@Excel(name = "项目ID", width = 15)
    private String projectId;
	/**任务ID*/
	@Excel(name = "任务ID", width = 15)
    private String taskId;
	/**父节点id*/
	@Excel(name = "父节点id", width = 15)
    private String parentnodeId;
	/**左值*/
	@Excel(name = "左值", width = 15)
    private Integer leftVal;
	/**右值*/
	@Excel(name = "右值", width = 15)
    private Integer rightVal;
	/**状态*/
	@Excel(name = "状态", width = 15)
    private Integer nodeStatus;
	/**是否删除*/
	@Excel(name = "是否删除", width = 15)
    private Integer isDelete;
	/**是否有子节点*/
	@Excel(name = "是否有子节点", width = 15)
	private java.lang.String hasChild;
}
