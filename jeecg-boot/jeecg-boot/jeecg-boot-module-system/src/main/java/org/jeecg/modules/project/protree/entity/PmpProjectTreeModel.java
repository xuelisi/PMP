package org.jeecg.modules.project.protree.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: 项目管理
 * @Author: jeecg-boot
 * @Date:   2020-03-05
 * @Version: V1.0
 */
@Data
public class PmpProjectTreeModel implements Serializable {
    private static final long serialVersionUID = 1L;

	/** 对应PmpProject中的id字段,前端数据树中的key*/
	private String key;

	/** 对应PmpProject中的id字段,前端数据树中的value*/
	private String value;

	/** 对应taskname字段,前端数据树中的title*/
	private String title;

	private boolean isLeaf;
	// 以下所有字段均与PmpProject相同

    private String id;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

    private String sysOrgCode;

    private String photo;

	private String projectname;

	private String taskname;

    private String projectcode;

    private String projecttype;

    private String projectcontent;

    private String principal;

    private String participant;

    private String emergencylevel;

    private Date startdate;

    private Date enddate;

    private Date completiontime;

    private Integer schedule;

    private String status;

    private Double projectmoney;

    private String remark;

    private String annex;

    private String isdelete;

    private String parentnode;

    private Integer leftval;

    private Integer rightval;

    private String haschild;

	private List<PmpProjectTreeModel> children = new ArrayList<>();

	public boolean getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(boolean isleaf) {
		this.isLeaf = isleaf;
	}

	public String getKey() {
		return key;
	}


	public void setKey(String key) {
		this.key = key;
	}


	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}

	public String getParentnode() {
		return parentnode;
	}


	public void setParentnode(String parentnode) {
		this.parentnode = parentnode;
	}
	/**
	 * 将SysDepart对象转换成SysDepartTreeModel对象
	 * @param pmpProject
	 */
	public PmpProjectTreeModel(PmpProject pmpProject) {
		this.key = pmpProject.getId();
		this.value = pmpProject.getId();
		this.title = pmpProject.getTaskname();
		this.id = pmpProject.getId();
		this.createBy = pmpProject.getCreateBy();
		this.createTime = pmpProject.getCreateTime();
		this.updateBy = pmpProject.getUpdateBy();
		this.updateTime = pmpProject.getUpdateTime();
		this.sysOrgCode = pmpProject.getSysOrgCode();
		this.photo = pmpProject.getPhoto();
		this.projectname = pmpProject.getProjectname();
		this.taskname = pmpProject.getTaskname();
		this.projectcode = pmpProject.getProjectcode();
		this.projecttype = pmpProject.getProjecttype();
		this.projectcontent = pmpProject.getProjectcontent();
		this.principal = pmpProject.getPrincipal();
		this.participant = pmpProject.getParticipant();
		this.emergencylevel = pmpProject.getEmergencylevel();
		this.startdate = pmpProject.getStartdate();
		this.enddate = pmpProject.getEnddate();
		this.completiontime = pmpProject.getCompletiontime();
		this.schedule = pmpProject.getSchedule();
		this.status = pmpProject.getStatus();
		this.projectmoney = pmpProject.getProjectmoney();
		this.remark = pmpProject.getRemark();
		this.annex = pmpProject.getAnnex();
		this.isdelete = pmpProject.getIsdelete();
		this.parentnode = pmpProject.getParentnode();
		this.leftval = pmpProject.getLeftval();
		this.rightval = pmpProject.getRightval();
		this.haschild = pmpProject.getHaschild();
	}
}
