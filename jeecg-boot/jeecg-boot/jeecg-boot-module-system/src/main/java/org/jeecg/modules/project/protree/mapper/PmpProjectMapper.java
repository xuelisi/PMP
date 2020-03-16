package org.jeecg.modules.project.protree.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.project.protree.entity.PmpProject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 项目管理
 * @Author: jeecg-boot
 * @Date:   2020-03-05
 * @Version: V1.0
 */
public interface PmpProjectMapper extends BaseMapper<PmpProject> {
    //调用存储过程
     void updateMyNode(@Param("projectname") String projectname, @Param("parentNodeId") String parentNodeId, @Param("id") String id);

    //调用存储过程
    void isDelteSubNode(@Param("projectname") String projectname, @Param("id") String id, @Param("isdelete") String isdelete);

    //调用存储过程
    Integer IsSuperior(@Param("id") String id, @Param("principal") String principal);

    //调用存储过程
    IPage<PmpProject> myProject(IPage<PmpProject> page, @Param("principal") String principal);

    //调用存储过程
    Page<PmpProject> myProjectpar(Page<PmpProject> page, @Param("principal") String principal);

    public List<PmpProject>  getCountInfo();
}
