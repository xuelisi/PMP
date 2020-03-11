package org.jeecg.modules.wqc.summary.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.jeecg.modules.wqc.summary.entity.PmpInfo;
import org.jeecg.modules.wqc.summary.entity.PmpReview;
import org.jeecg.modules.wqc.summary.entity.PmpSummary;
import org.jeecg.modules.wqc.summary.entity.PmpComment;
import org.jeecg.modules.wqc.summary.entity.PmpTaskSummary;

/**
 * @Description: 任务小结
 * @Author: jeecg-boot
 * @Date:   2020-03-06
 * @Version: V1.0
 */
public interface PmpTaskSummaryMapper extends BaseMapper<PmpTaskSummary> {

    /**
     *  根据项目Id查询用户信息
     * @param projectid
     * @return
     */
    IPage<PmpTaskSummary> getSummaryByProjectId(Page page, @Param("projectid") String projectid);

    PmpInfo getPmpInfoByProjectID(@Param("projectid") String projectid);

    PmpReview getPmpReviewByProjectID(@Param("projectid") String projectid);

    //根据任务id，查询小结信息
    List<PmpSummary> getPmpSummaryByTaskID( @Param("taskid") String taskid);

}
