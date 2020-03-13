package org.jeecg.modules.wqc.summary.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.wqc.summary.entity.PmpSummaryInfo;
import org.jeecg.modules.wqc.summary.entity.PmpSummary;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 任务小结
 * @Author: jeecg-boot
 * @Date:   2020-03-06
 * @Version: V1.0
 */
public interface IPmpTaskSummaryService extends IService<PmpSummary> {

    public Page<PmpSummaryInfo> getPmpSummaryInfoByPTName(Page<PmpSummaryInfo> page, String projectName, String taskName);

    public void removeById(String id);
}
