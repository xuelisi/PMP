package org.jeecg.modules.wqc.summary.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import org.jeecg.modules.wqc.summary.entity.PmpSummary;
import org.jeecg.modules.wqc.summary.entity.PmpSummaryInfo;
import org.jeecg.modules.wqc.summary.entity.PmpSummaryStatistics;


/**
 * @Description: 任务小结
 * @Author: jeecg-boot
 * @Date:   2020-03-06
 * @Version: V1.0
 */
public interface IPmpSummaryService extends IService<PmpSummary> {

    public void removeById(String id);

    public void removeSummaryWithTask(String summaryid);

    public void addSummaryWithTask(PmpSummary summary, String taskid);

    public void editSummaryWithTask(PmpSummary summary, String taskid);

    public void addSummaryWithChief(PmpSummary summary, String userid);

    public Page<PmpSummaryInfo> query(Page<PmpSummaryInfo> page, Map<String, String> map);

    public Page<PmpSummaryStatistics> queryStatisticsByDate(Page<PmpSummaryStatistics> page, String date);
}
