package org.jeecg.modules.wqc.summary.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.wqc.summary.entity.PmpComment;
import org.jeecg.modules.wqc.summary.entity.PmpCommentInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.wqc.summary.entity.PmpCommentSummary;

import java.util.Map;
import java.util.List;

/**
 * @Description: 任务批阅
 * @Author: jeecg-boot
 * @Date:   2020-03-11
 * @Version: V1.0
 */
public interface IPmpCommentService extends IService<PmpComment> {

    public String queryRealName(String username);

    public String queryTaskNameByTaskid(String taskid);

    public String queryCommenteeByTaskid(String taskid);

    public List<PmpCommentSummary> queryUnionByTaskid(String taskid);

    public List<PmpCommentInfo> queryByTask(String taskid, String username);

    public Page<PmpCommentInfo> query(Page<PmpCommentInfo> page, Map<String, String> map);
}
