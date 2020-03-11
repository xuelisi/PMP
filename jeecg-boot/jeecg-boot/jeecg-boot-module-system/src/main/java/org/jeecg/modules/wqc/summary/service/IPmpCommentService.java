package org.jeecg.modules.wqc.summary.service;

import org.jeecg.modules.wqc.summary.entity.PmpComment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 任务批阅
 * @Author: jeecg-boot
 * @Date:   2020-03-11
 * @Version: V1.0
 */
public interface IPmpCommentService extends IService<PmpComment> {

    public List<PmpComment> getPmpCommentByTaskID(String taskid);
}
