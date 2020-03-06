package org.jeecg.modules.project.tree.model;

import org.jeecg.modules.project.task.entity.PmpTaskdetails;
import org.jeecg.modules.system.entity.SysDepart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 部门表 存储树结构数据的实体类
 * <p>
 * 
 * @Author Steve
 * @Since 2019-01-22 
 */
public class TaskTreeModel implements Serializable{

    private static final long serialVersionUID = 1L;

    /** 对应taskdetails中的id字段,前端数据树中的key*/
    private String key;

    /** 对应taskdetails中的id字段,前端数据树中的value*/
    private String value;

    /** 对应taskdetails字段,前端数据树中的title*/
    private String title;


    private boolean isLeaf;
    // 以下所有字段均与taskdetails相同

    private java.lang.String id;

    private java.lang.String createBy;

    private java.util.Date createTime;

    private java.lang.String updateBy;

    private java.util.Date updateTime;

    private java.lang.String sysOrgCode;

    private java.lang.String taskname;

    private java.lang.String taskcontent;

    private java.lang.String taskprincipal;

    private java.lang.String participant;

    private java.lang.Integer taskschedule;

    private java.lang.Integer taskstatus;

    private java.util.Date startdate;

    private java.util.Date enddate;

    private java.util.Date completiontime;

    private java.lang.String emergencylevel;

    private java.lang.Double taskmoney;

    private java.lang.String receiver;

    private java.lang.String recContent;

    private java.lang.Integer recScore;

    private java.lang.String taskremark;

    private java.lang.String taskannex;

    private java.lang.Integer isdelete;

    private java.lang.String parentId;

    private List<TaskTreeModel> children = new ArrayList<>();


    /**
     * 将taskdetail对象转换成taskdetailTreeModel对象
     * @param pmpTaskdetails
     */
	public TaskTreeModel(PmpTaskdetails pmpTaskdetails) {
		this.key = pmpTaskdetails.getId();
        this.value = pmpTaskdetails.getId();
        this.title = pmpTaskdetails.getTaskname();
        this.id = pmpTaskdetails.getId();
        this.parentId = pmpTaskdetails.getParent_id();
        this.taskname = pmpTaskdetails.getTaskname();
        this.taskcontent = pmpTaskdetails.getTaskcontent();
        this.taskprincipal = pmpTaskdetails.getTaskprincipal();
        this.participant = pmpTaskdetails.getParticipant();
        this.taskschedule = pmpTaskdetails.getTaskschedule();
        this.taskstatus = pmpTaskdetails.getTaskstatus();
        this.startdate = pmpTaskdetails.getStartdate();
        this.enddate = pmpTaskdetails.getEnddate();
        this.completiontime = pmpTaskdetails.getCompletiontime();
        this.emergencylevel = pmpTaskdetails.getEmergencylevel();
        this.taskmoney = pmpTaskdetails.getTaskmoney();
        this.receiver = pmpTaskdetails.getReceiver();
        this.recContent = pmpTaskdetails.getRecContent();
        this.recScore = pmpTaskdetails.getRecScore();
        this.taskremark = pmpTaskdetails.getTaskremark();
        this.taskannex = pmpTaskdetails.getTaskannex();
        this.isdelete = pmpTaskdetails.getIsdelete();
        this.createBy = pmpTaskdetails.getCreate_by();
        this.createTime = pmpTaskdetails.getCreate_time();
        this.updateBy = pmpTaskdetails.getUpdate_by();
        this.updateTime = pmpTaskdetails.getUpdate_time();
        this.sysOrgCode = pmpTaskdetails.getSys_org_code();
    }

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


	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<TaskTreeModel> getChildren() {
        return children;
    }

    public void setChildren(List<TaskTreeModel> children) {
        if (children==null){
            this.isLeaf=true;
        }
        this.children = children;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getTaskname() {
        return taskname;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }

    public String getTaskcontent() {
		return taskcontent;
	}

	public void setTaskcontent(String taskcontent) {
		this.taskcontent = taskcontent;
	}

	public String getTaskprincipal() {
        return taskprincipal;
    }

    public void setTaskprincipal(String taskprincipal) {
        this.taskprincipal = taskprincipal;
    }

    public String getParticipant() {
        return participant;
    }

    public void setParticipant(String participant) { this.participant = participant; }

    public Integer getTaskSchedule() {
        return taskschedule;
    }

    public void setTaskschedule(Integer taskschedule) {
        this.taskschedule = taskschedule;
    }

    public Integer getTaskstatus() {
        return taskstatus;
    }

    public void setTaskstatus(Integer taskstatus) {
        this.taskstatus = taskstatus;
    }

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public Date getCompletiontime() {
        return completiontime;
    }

    public void setCompletiontime(Date completiontime) {
        this.completiontime = completiontime;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public String getEmergencylevel() {
        return emergencylevel;
    }

    public void setEmergencylevel(String emergencylevel) {
        this.emergencylevel = emergencylevel;
    }

    public Double getTaskmoney() {
        return taskmoney;
    }

    public void setTaskmoney(Double taskmoney) {
        this.taskmoney = taskmoney;
    }

    public String getReceiver() { return receiver; }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getRecContent() {
        return recContent;
    }

    public void setRecContent(String recContent) {
        this.recContent = recContent;
    }

    public Integer getRecScore() {
        return recScore;
    }

    public void setRecScore(Integer recScore) {
        this.recScore = recScore;
    }

    public String getTaskremark() {
        return taskremark;
    }

    public void setTaskremark(String taskremark) {
        this.taskremark = taskremark;
    }

    public String getTaskannex() {
        return taskannex;
    }

    public void setTaskannex(String taskannex) {
        this.taskannex = taskannex;
    }

    public Integer getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(Integer isdelete) {
        this.isdelete = isdelete;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getSysOrgCode() {
        return sysOrgCode;
    }

    public void setSysOrgCode(String sysOrgCode) {
        this.sysOrgCode = sysOrgCode;
    }

    public TaskTreeModel() { }

    /**
     * 重写equals方法
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
			return true;
		}
        if (o == null || getClass() != o.getClass()) {
			return false;
		}
        TaskTreeModel model = (TaskTreeModel) o;
        return Objects.equals(id, model.id) &&
                Objects.equals(parentId, model.parentId) &&
                Objects.equals(taskname, model.taskname) &&
                Objects.equals(taskcontent, model.taskcontent) &&
                Objects.equals(taskprincipal, model.taskprincipal) &&
                Objects.equals(taskschedule, model.taskschedule) &&
                Objects.equals(taskstatus, model.taskstatus) &&
                Objects.equals(startdate, model.startdate) &&
                Objects.equals(enddate, model.enddate) &&
                Objects.equals(completiontime, model.completiontime) &&
                Objects.equals(emergencylevel, model.emergencylevel) &&
                Objects.equals(taskmoney, model.taskmoney) &&
                Objects.equals(receiver, model.receiver) &&
                Objects.equals(recContent, model.recContent) &&
                Objects.equals(recScore, model.recScore) &&
                Objects.equals(taskremark, model.taskremark) &&
                Objects.equals(isdelete, model.isdelete) &&
                Objects.equals(participant, model.participant) &&
                Objects.equals(taskannex, model.taskannex) &&
                Objects.equals(createBy, model.createBy) &&
                Objects.equals(createTime, model.createTime) &&
                Objects.equals(updateBy, model.updateBy) &&
                Objects.equals(updateTime, model.updateTime) &&
                Objects.equals(sysOrgCode, model.sysOrgCode) &&
                Objects.equals(children, model.children);

    }
    
    /**
     * 重写hashCode方法
     */
    @Override
    public int hashCode() {

        return Objects.hash(id, parentId, taskname, taskcontent, taskprincipal,
                taskschedule, taskstatus, startdate, enddate, completiontime, emergencylevel, taskmoney, receiver,
                recContent, recScore, taskremark,isdelete,participant,taskannex, createBy, createTime, updateBy, updateTime,
                sysOrgCode, children);
    }

}
