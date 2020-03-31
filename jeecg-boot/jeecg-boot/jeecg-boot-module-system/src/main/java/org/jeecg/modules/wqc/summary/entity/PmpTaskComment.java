package org.jeecg.modules.wqc.summary.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;

/**
 * @Description: 任务评论对照表
 * @Author: jeecg-boot
 * @Date:   2020-03-17
 * @Version: V1.0
 */
@Data
@TableName("pmp_task_comment")
public class PmpTaskComment implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    private String id;
	/**任务id*/
	@Excel(name = "任务id", width = 15)
    private String taskId;
	/**小结Id*/
	@Excel(name = "小结Id", width = 15)
    private String commentId;

	public PmpTaskComment(String id, String taskid) {
		this.commentId = id;
		this.taskId = taskid;
	}
}

