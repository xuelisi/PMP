package org.jeecg.modules.project.tree.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.common.util.FillRuleUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.project.tree.entity.PmpZuzhitu;
import org.jeecg.modules.project.tree.mapper.PmpZuzhituMapper;
import org.jeecg.modules.project.tree.service.IPmpZuzhituService;
import org.jeecg.modules.system.service.ISysCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.io.Serializable;

/**
 * @Description: 项目组织树
 * @Author: jeecg-boot
 * @Date:   2020-03-04
 * @Version: V1.0
 */
@Service
public class PmpZuzhituServiceImpl extends ServiceImpl<PmpZuzhituMapper, PmpZuzhitu> implements IPmpZuzhituService {

    @Autowired
    PmpZuzhituMapper pmpZuzhituMapper;


    @Override
    public String addMyNode(String Id, String parentNodeId, String taskId) {
        return pmpZuzhituMapper.addMyNode(Id, parentNodeId, taskId);
    }

    @Override
    public String removeNodeById(Serializable id) {
        return "";
    }

    @Override
    public void addSysCategory(PmpZuzhitu pmpZuzhitu) {
        String categoryPid = ISysCategoryService.ROOT_PID_VALUE;
        if (oConvertUtils.isNotEmpty(pmpZuzhitu.getParentnodeId())) {
            categoryPid = pmpZuzhitu.getParentnodeId();

            //PID 不是根节点 说明需要设置父节点 hasChild 为1
            if (!ISysCategoryService.ROOT_PID_VALUE.equals(categoryPid)) {
                PmpZuzhitu parent = baseMapper.selectById(categoryPid);
                if (parent != null && !"1".equals(parent.getHasChild())) {
                    parent.setHasChild("1");
                    baseMapper.updateById(parent);
                }
            }
        }
        //update-begin--Author:baihailong  Date:20191209 for：分类字典编码规则生成器做成公用配置
        JSONObject formData = new JSONObject();
        formData.put("parentnode_id",categoryPid);
        pmpZuzhitu.setTaskId(pmpZuzhitu.getTaskId());
        pmpZuzhitu.setParentnodeId(categoryPid);
        baseMapper.insert(pmpZuzhitu);
    }
}
