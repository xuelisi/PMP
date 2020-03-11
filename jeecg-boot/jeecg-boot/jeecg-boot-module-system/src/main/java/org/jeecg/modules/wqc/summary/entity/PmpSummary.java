package org.jeecg.modules.wqc.summary.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("pmp_summary")
public class PmpSummary {
    private String id;

    private Date createTime;

    private String createBy;

    private Date updateTime;

    private String updateBy;

    private String taskid;

    private String content;

    private String contentAnnex;

}
