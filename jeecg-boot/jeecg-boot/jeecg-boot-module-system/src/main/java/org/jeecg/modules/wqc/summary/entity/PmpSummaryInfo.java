package org.jeecg.modules.wqc.summary.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
public class PmpSummaryInfo {
    private String id;

    //任务id
    private String taskid;

    //任务名称
    private String taskName;

    //项目id
    private String projectid;

    //项目名称
    private String projectName;

    //主管id
    private String chiefid;

    //主管名称
    private String chiefName;

    //提交人
    private String createBy;

    private String realName;

    //提交时间
    private Date createTime;

    private String summaryTime;

    //提交内容
    private String content;

    //提交附件
    private String contentAnnex;

    private String category;
}
