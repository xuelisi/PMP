package org.jeecg.modules.project.taskmanage.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.project.taskmanage.entity.PmpTask;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 项目管理
 * @Author: jeecg-boot
 * @Date:   2020-03-12
 * @Version: V1.0
 */
public interface IPmpTaskService extends IService<PmpTask> {
    Page<PmpTask> myTaskpri(Page<PmpTask> page, String principal);

    Page<PmpTask> myTaskpar(Page<PmpTask> page, String principal);
}
