package org.jeecg.modules.wqc.summary.service.impl;

import java.util.List;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.wqc.summary.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import org.jeecg.modules.wqc.summary.mapper.PmpSummaryMapper;
import org.jeecg.modules.wqc.summary.mapper.PmpTaskSummaryMapper;
import org.jeecg.modules.wqc.summary.mapper.PmpChiefSummaryMapper;
import org.jeecg.modules.wqc.summary.service.IPmpSummaryService;

/**
 * @Description: 任务小结
 * @Author: jeecg-boot
 * @Date:   2020-03-06
 * @Version: V1.0
 */
@Service
public class PmpSummaryServiceImpl extends ServiceImpl<PmpSummaryMapper, PmpSummary> implements IPmpSummaryService {
    @Autowired
    private PmpSummaryMapper smapper;
    @Autowired
    private PmpTaskSummaryMapper tsMapper;
    @Autowired
    private PmpChiefSummaryMapper csMapper;

    public void removeById(String id) {
        smapper.removeById(id);
    }

    public void removeSummaryWithTask(String summaryid) {
        smapper.removeSummaryWithTask(summaryid);
    }

    public void addSummaryWithTask(PmpSummary summary, String taskid) {
        PmpTaskSummary ts = new PmpTaskSummary(summary.getId(), taskid);
        tsMapper.insert(ts);
    }

    public void editSummaryWithTask(PmpSummary summary, String taskid) {
        PmpTaskSummary ts = new PmpTaskSummary(summary.getId(), taskid);
        tsMapper.updateBySummaryId(ts.getSummaryId(), ts.getTaskId());
    }

    public void addSummaryWithChief(PmpSummary summary, String chiefid) {
        PmpChiefSummary cs = new PmpChiefSummary(summary.getId(), chiefid);
        csMapper.insert(cs);
    }

    public Page<PmpSummaryInfo> queryByName(Page<PmpSummaryInfo> page, String userName) {
        return page.setRecords(smapper.queryByName(page, userName));
    }

    public Page<PmpSummaryInfo> querySummaryInfo(Page<PmpSummaryInfo> page) {
        return page.setRecords(smapper.querySummaryInfo(page));
    }

    public List<PmpSummaryInfo> queryByNameAndDate(String date, String userName) {
        return smapper.queryByNameAndDate(date, userName);
    }

    public Page<PmpSummaryResult> queryStatisticsByDate(Page<PmpSummaryResult> page, String date) {
        return page.setRecords(smapper.queryStatisticsByDate(page, date));
    }
}
