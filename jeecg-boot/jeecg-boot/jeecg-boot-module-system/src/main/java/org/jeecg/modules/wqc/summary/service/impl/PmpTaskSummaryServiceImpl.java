package org.jeecg.modules.wqc.summary.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.wqc.summary.entity.PmpInfo;
import org.jeecg.modules.wqc.summary.entity.PmpSummary;
import org.jeecg.modules.wqc.summary.entity.PmpTaskSummary;
import org.jeecg.modules.wqc.summary.mapper.PmpTaskSummaryMapper;
import org.jeecg.modules.wqc.summary.service.IPmpTaskSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 任务小结
 * @Author: jeecg-boot
 * @Date:   2020-03-06
 * @Version: V1.0
 */
@Service
@Slf4j
public class PmpTaskSummaryServiceImpl extends ServiceImpl<PmpTaskSummaryMapper, PmpTaskSummary> implements IPmpTaskSummaryService {

    @Autowired
    private PmpTaskSummaryMapper summaryMapper;

    // 根据项目id查询
    @Override
    public IPage<PmpTaskSummary> getSummaryByProjectId(Page<PmpTaskSummary> page, String projectId) {
        return summaryMapper.getSummaryByProjectId(page, projectId);
    }

    // 根据项目id查询
    @Override
    public PmpInfo getPmpInfoByProjectID(String projectId) {
        return summaryMapper.getPmpInfoByProjectID(projectId);
    }

    public List<PmpSummary> getPmpSummaryByTaskID(String taskid) {
        return summaryMapper.getPmpSummaryByTaskID(taskid);
    }

}
