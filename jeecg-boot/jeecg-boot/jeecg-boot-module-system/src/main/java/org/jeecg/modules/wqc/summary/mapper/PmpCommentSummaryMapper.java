package org.jeecg.modules.wqc.summary.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.wqc.summary.entity.PmpCommentSummary;

import java.util.List;

/**
 * @Description: 任务批阅
 * @Author: jeecg-boot
 * @Date:   2020-03-11
 * @Version: V1.0
 */
public interface PmpCommentSummaryMapper extends BaseMapper<PmpCommentSummary> {
    List<PmpCommentSummary> queryUnionByTaskid(@Param("taskid") String taskid);

}
