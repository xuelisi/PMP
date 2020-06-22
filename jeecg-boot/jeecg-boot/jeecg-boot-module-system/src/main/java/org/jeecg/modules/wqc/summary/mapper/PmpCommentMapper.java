package org.jeecg.modules.wqc.summary.mapper;

import java.util.Map;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.wqc.summary.entity.PmpComment;
import org.jeecg.modules.wqc.summary.entity.PmpCommentInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 任务批阅
 * @Author: jeecg-boot
 * @Date:   2020-03-11
 * @Version: V1.0
 */
public interface PmpCommentMapper extends BaseMapper<PmpComment> {

    String queryRealName(String username);

    String queryTaskNameByTaskid(String taskid);

    String queryCommenteeByTaskid(String taskid);

    List<PmpCommentInfo> queryByTask(@Param("taskid") String taskid, @Param("username") String username);

    List<PmpCommentInfo> query(Page<PmpCommentInfo> page, Map<String, String> map);

}
