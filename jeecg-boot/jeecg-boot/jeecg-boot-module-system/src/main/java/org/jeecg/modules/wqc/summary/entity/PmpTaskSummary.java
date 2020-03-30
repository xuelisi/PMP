package org.jeecg.modules.wqc.summary.entity;

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
 * @Description: 任务小结对照表
 * @Author: jeecg-boot
 * @Date:   2020-03-17
 * @Version: V1.0
 */
@Data
@TableName("pmp_task_summary")
public class PmpTaskSummary implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    private java.lang.String id;
	/**任务id*/
	@Excel(name = "任务id", width = 15)
    private java.lang.String taskId;
	/**小结Id*/
	@Excel(name = "小结Id", width = 15)
    private java.lang.String summaryId;

	public PmpTaskSummary(String id, String taskid) {
		this.summaryId = id;
		this.taskId = taskid;
	}
}

