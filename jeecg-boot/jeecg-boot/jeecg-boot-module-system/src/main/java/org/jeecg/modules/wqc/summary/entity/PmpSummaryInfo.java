package org.jeecg.modules.wqc.summary.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
public class PmpSummaryInfo {
    private String id;

    private String taskid;

    private String taskName;

    private String projectName;

    private String createBy;

    private Date createTime;

    private String content;

    private String contentAnnex;
}
