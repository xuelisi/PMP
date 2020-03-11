package org.jeecg.modules.project.tree.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.modules.project.tree.entity.PmpZuzhitu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;

/**
 * @Description: 项目组织树
 * @Author: jeecg-boot
 * @Date:   2020-03-04
 * @Version: V1.0
 */
public interface IPmpZuzhituService extends IService<PmpZuzhitu> {

     String addMyNode(String Id, String parentNodeId, String taskId);

     String removeNodeById(Serializable id);

     /**根节点父ID的值*/
     public static final String ROOT_PID_VALUE = "0";

     void addSysCategory(PmpZuzhitu pmpZuzhitu);
}
