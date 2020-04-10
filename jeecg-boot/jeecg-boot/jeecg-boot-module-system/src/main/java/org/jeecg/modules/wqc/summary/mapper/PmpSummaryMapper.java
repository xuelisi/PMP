package org.jeecg.modules.wqc.summary.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.jeecg.modules.wqc.summary.entity.*;

import java.util.List;
import java.util.Map;

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

    List<PmpSummaryInfo> query(Page<PmpSummaryInfo> page, Map<String, String> map);

    List<PmpSummaryStatistics> queryStatisticsByDate(Page<PmpSummaryStatistics> page, String date);
}
