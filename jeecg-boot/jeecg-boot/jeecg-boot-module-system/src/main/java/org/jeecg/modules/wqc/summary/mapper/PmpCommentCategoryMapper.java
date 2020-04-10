package org.jeecg.modules.wqc.summary.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.wqc.summary.entity.PmpCommentCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 评论类别关联表
 * @Author: jeecg-boot
 * @Date:   2020-04-09
 * @Version: V1.0
 */
public interface PmpCommentCategoryMapper extends BaseMapper<PmpCommentCategory> {
    void removeByCommentId(String commentid);

    void updateByCatetoryId(String commentid, String categoryid);
}
