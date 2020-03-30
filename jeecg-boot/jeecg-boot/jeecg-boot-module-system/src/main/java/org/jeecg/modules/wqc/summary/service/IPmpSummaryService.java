package org.jeecg.modules.wqc.summary.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import org.jeecg.modules.wqc.summary.entity.PmpSummary;
import org.jeecg.modules.wqc.summary.entity.PmpSummaryInfo;
import org.jeecg.modules.wqc.summary.entity.PmpSummaryResult;


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

    public Page<PmpSummaryInfo> queryByName(Page<PmpSummaryInfo> page, String userName);

    public Page<PmpSummaryInfo> querySummaryInfo(Page<PmpSummaryInfo> page);

    public List<PmpSummaryInfo> queryByNameAndDate(String date, String userName);

    public Page<PmpSummaryResult> queryStatisticsByDate(Page<PmpSummaryResult> page, String date);
}
