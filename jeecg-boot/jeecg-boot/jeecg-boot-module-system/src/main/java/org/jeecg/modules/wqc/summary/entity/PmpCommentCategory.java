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
 * @Description: 评论类别关联表
 * @Author: jeecg-boot
 * @Date:   2020-04-09
 * @Version: V1.0
 */
@Data
@TableName("pmp_comment_category")
public class PmpCommentCategory implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    private java.lang.String id;
	/**评论id*/
	@Excel(name = "评论id", width = 15)
    private java.lang.String commentId;
	/**类别id*/
	@Excel(name = "类别id", width = 15)
    private java.lang.String categoryId;

	public PmpCommentCategory(String id, String categoryid) {
		this.commentId = id;
		this.categoryId = categoryid;
	}
}
