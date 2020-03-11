package org.jeecg.modules.wqc.summary.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.wqc.summary.entity.PmpComment;
import org.jeecg.modules.wqc.summary.entity.PmpInfo;
import org.jeecg.modules.wqc.summary.entity.PmpSummary;
import org.jeecg.modules.wqc.summary.entity.PmpTaskSummary;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 任务小结
 * @Author: jeecg-boot
 * @Date:   2020-03-06
 * @Version: V1.0
 */
public interface IPmpTaskSummaryService extends IService<PmpTaskSummary> {

    /**
     * 根据部门Id查询
     * @param
     * @return
     */
    public IPage<PmpTaskSummary> getSummaryByProjectId(Page<PmpTaskSummary> page, String projectId);

    public PmpInfo getPmpInfoByProjectID(String projectId);

    public List<PmpSummary> getPmpSummaryByTaskID(String taskid);


}
