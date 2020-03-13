package org.jeecg.modules.wqc.summary.mapper;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.wqc.summary.entity.PmpSummaryInfo;
import org.jeecg.modules.wqc.summary.entity.PmpSummary;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 任务小结
 * @Author: jeecg-boot
 * @Date:   2020-03-06
 * @Version: V1.0
 */
public interface PmpTaskSummaryMapper extends BaseMapper<PmpSummary> {

    List<PmpSummaryInfo> getPmpSummaryInfoByPTName(Page<PmpSummaryInfo> page, String projectName, String taskName);

    void removeById(String id);
}
