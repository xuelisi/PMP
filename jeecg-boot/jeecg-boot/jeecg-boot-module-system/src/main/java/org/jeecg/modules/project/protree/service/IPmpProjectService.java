package org.jeecg.modules.project.protree.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.project.protree.entity.PmpProject;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;

/**
 * @Description: 项目管理
 * @Author: jeecg-boot
 * @Date:   2020-03-05
 * @Version: V1.0
 */
public interface IPmpProjectService extends IService<PmpProject> {

    /**根节点父ID的值*/
    public static final String ROOT_PID_VALUE = "0";

    void updateMyNode(String projectname, String parentNodeId, String id);

    void isDelteSubNode(String projectname, String id, String isdelete);

    Integer IsSuperior(String id, String principal);

    Page<PmpProject> myProject(Page<PmpProject> page, String principal);

    Page<PmpProject> myProjectpar(Page<PmpProject> page, String principal);

    void addSysCategory(PmpProject pmpProject);
}
