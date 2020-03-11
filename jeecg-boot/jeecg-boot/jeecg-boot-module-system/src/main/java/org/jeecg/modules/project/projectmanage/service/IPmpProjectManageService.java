package org.jeecg.modules.project.projectmanage.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.project.projectmanage.entity.PmpProjectManage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 项目主表
 * @Author: jeecg-boot
 * @Date:   2020-03-03
 * @Version: V1.0
 */
public interface IPmpProjectManageService extends IService<PmpProjectManage> {

    Page<PmpProjectManage> selectProListPage(Page<PmpProjectManage> pagination);

}
