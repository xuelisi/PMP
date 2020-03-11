package org.jeecg.modules.project.projectmanage.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.project.projectmanage.entity.PmpProjectManage;
import org.jeecg.modules.project.projectmanage.mapper.PmpProjectManageMapper;
import org.jeecg.modules.project.projectmanage.service.IPmpProjectManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 项目主表
 * @Author: jeecg-boot
 * @Date:   2020-03-03
 * @Version: V1.0
 */
@Service
public class PmpProjectManageServiceImpl extends ServiceImpl<PmpProjectManageMapper, PmpProjectManage> implements IPmpProjectManageService {

    @Autowired
    PmpProjectManageMapper pmpProjectManageMapper;

    @Override
    public Page<PmpProjectManage> selectProListPage(Page<PmpProjectManage> pagination) {
        return pmpProjectManageMapper.selectProListPage(pagination);
    }
}
