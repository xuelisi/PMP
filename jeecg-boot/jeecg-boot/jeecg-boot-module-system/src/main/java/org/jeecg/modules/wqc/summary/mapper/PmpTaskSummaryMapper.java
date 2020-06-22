package org.jeecg.modules.wqc.summary.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.wqc.summary.entity.PmpTaskSummary;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 任务小结对照表
 * @Author: jeecg-boot
 * @Date:   2020-03-17
 * @Version: V1.0
 */
public interface PmpTaskSummaryMapper extends BaseMapper<PmpTaskSummary> {
    void updateBySummaryId(String summaryId, String taskId);

}
