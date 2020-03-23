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
 * @Description: 领导小结对照表
 * @Author: jeecg-boot
 * @Date:   2020-03-17
 * @Version: V1.0
 */
@Data
@TableName("pmp_chief_summary")
public class PmpChiefSummary implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    private String id;
	/**领导Id*/
	@Excel(name = "领导Id", width = 15)
    private String chiefId;
	/**小结id*/
	@Excel(name = "小结id", width = 15)
    private String summaryId;

	public PmpChiefSummary(String id, String chiefid) {
		this.summaryId = id;
		this.chiefId = chiefid;
	}
}
