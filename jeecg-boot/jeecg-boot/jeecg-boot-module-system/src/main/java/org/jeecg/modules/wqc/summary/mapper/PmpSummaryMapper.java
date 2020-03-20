package org.jeecg.modules.wqc.summary.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.jeecg.modules.wqc.summary.entity.PmpSummary;
import org.jeecg.modules.wqc.summary.entity.PmpSummaryInfo;
import org.jeecg.modules.wqc.summary.entity.PmpSummaryResult;

import java.util.List;

/**
 * @Description: 任务小结
 * @Author: jeecg-boot
 * @Date:   2020-03-06
 * @Version: V1.0
 */
public interface PmpSummaryMapper extends BaseMapper<PmpSummary> {

    void removeById(String id);

    String queryUserId(String taskid);

    void removeSummaryWithTask(String summaryid);

    List<PmpSummaryInfo> querySummaryInfoByDate(String date);

    List<PmpSummaryInfo> querySummaryInfo(Page<PmpSummaryInfo> page);

    List<PmpSummaryInfo> querySummaryInfoByUsername(Page<PmpSummaryInfo> page, String userName);

    List<PmpSummaryResult> querySummaryResultByDate(Page<PmpSummaryResult> page, String date);
}
