package org.jeecg.modules.wqc.summary.service.impl;

import org.jeecg.modules.wqc.summary.entity.PmpComment;
import org.jeecg.modules.wqc.summary.mapper.PmpCommentMapper;
import org.jeecg.modules.wqc.summary.service.IPmpCommentService;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * @Description: 任务批阅
 * @Author: jeecg-boot
 * @Date:   2020-03-11
 * @Version: V1.0
 */
@Service
@Slf4j
public class PmpCommentServiceImpl extends ServiceImpl<PmpCommentMapper, PmpComment> implements IPmpCommentService {

    @Autowired
    private PmpCommentMapper mapper;

    //根据小结id，查询批阅信息
    public List<PmpComment> getPmpCommentByTaskID(String taskid) {
        return mapper.getPmpCommentByTaskID(taskid);
    }
}
