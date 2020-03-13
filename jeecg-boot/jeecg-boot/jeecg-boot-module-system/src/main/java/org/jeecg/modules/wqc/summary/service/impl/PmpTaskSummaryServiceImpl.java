package org.jeecg.modules.wqc.summary.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.wqc.summary.entity.PmpSummaryInfo;
import org.jeecg.modules.wqc.summary.entity.PmpSummary;
import org.jeecg.modules.wqc.summary.mapper.PmpTaskSummaryMapper;
import org.jeecg.modules.wqc.summary.service.IPmpTaskSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 任务小结
 * @Author: jeecg-boot
 * @Date:   2020-03-06
 * @Version: V1.0
 */
@Service
public class PmpTaskSummaryServiceImpl extends ServiceImpl<PmpTaskSummaryMapper, PmpSummary> implements IPmpTaskSummaryService {
    @Autowired
    private PmpTaskSummaryMapper mapper;

    public Page<PmpSummaryInfo> getPmpSummaryInfoByPTName(Page<PmpSummaryInfo> page, String projectName, String taskName) {
        return page.setRecords(mapper.getPmpSummaryInfoByPTName(page, projectName, taskName));
    }

    public void removeById(String id) {
        mapper.removeById(id);
    }
}
