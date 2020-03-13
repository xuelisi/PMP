package org.jeecg.modules.wqc.summary.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.wqc.summary.entity.PmpComment;
import org.jeecg.modules.wqc.summary.entity.PmpCommentInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 任务批阅
 * @Author: jeecg-boot
 * @Date:   2020-03-11
 * @Version: V1.0
 */
public interface IPmpCommentService extends IService<PmpComment> {

    public List<PmpComment> getPmpCommentByTaskID(String taskid, String username);

    public Page<PmpCommentInfo> getPmpCommentInfoByPTName(Page<PmpCommentInfo> page, String projectName, String taskName);
}
