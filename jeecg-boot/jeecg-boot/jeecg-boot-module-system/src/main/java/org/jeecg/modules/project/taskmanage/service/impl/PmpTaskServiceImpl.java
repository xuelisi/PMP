package org.jeecg.modules.project.taskmanage.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.project.taskmanage.entity.PmpTask;
import org.jeecg.modules.project.taskmanage.mapper.PmpTaskMapper;
import org.jeecg.modules.project.taskmanage.service.IPmpTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 项目管理
 * @Author: jeecg-boot
 * @Date:   2020-03-12
 * @Version: V1.0
 */
@Service
public class PmpTaskServiceImpl extends ServiceImpl<PmpTaskMapper, PmpTask> implements IPmpTaskService {
    @Autowired
    PmpTaskMapper pmpTaskMapper;

    @Override
    public Page<PmpTask> myTaskpri(Page<PmpTask> page, String principal, PmpTask pmpTask) {
        if(oConvertUtils.isEmpty(pmpTask.getProjectname())){
            pmpTask.setProjectname("");
        }
        if(oConvertUtils.isEmpty(pmpTask.getTaskname())){
            pmpTask.setTaskname("");
        }
        return pmpTaskMapper.myTaskpri(page, principal, pmpTask.getProjectname(), pmpTask.getTaskname());
    }

    @Override
    public Page<PmpTask> myTaskpar(Page<PmpTask> page, String principal, PmpTask pmpTask) {
        if(oConvertUtils.isEmpty(pmpTask.getProjectname())){
            pmpTask.setProjectname("");
        }
        if(oConvertUtils.isEmpty(pmpTask.getTaskname())){
            pmpTask.setTaskname("");
        }
        return pmpTaskMapper.myTaskpar(page, principal, pmpTask.getProjectname(), pmpTask.getTaskname());
    }
}
