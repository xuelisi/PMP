package org.jeecg.modules.wqc.summary.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.wqc.summary.entity.PmpTaskComment;

/**
 * @Description: 任务小结对照表
 * @Author: jeecg-boot
 * @Date:   2020-03-17
 * @Version: V1.0
 */
public interface PmpTaskCommentMapper extends BaseMapper<PmpTaskComment> {
    void updateByCommentId(String commentId, String taskId);

    void removeCommentWithTask(String commentId);
}
