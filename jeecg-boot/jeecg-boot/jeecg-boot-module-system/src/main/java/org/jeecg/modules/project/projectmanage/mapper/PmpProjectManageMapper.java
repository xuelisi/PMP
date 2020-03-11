package org.jeecg.modules.project.projectmanage.mapper;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.project.projectmanage.entity.PmpProjectManage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.project.task.entity.PmpTaskdetails;

/**
 * @Description: 项目主表
 * @Author: jeecg-boot
 * @Date:   2020-03-03
 * @Version: V1.0
 */
public interface PmpProjectManageMapper extends BaseMapper<PmpProjectManage> {

    @Select("SELECT * FROM pmp_projectmain WHERE id in (select project_id from pmp_zuzhitu where parentnode_id = 0)")
    Page<PmpProjectManage> selectProListPage(Page<PmpProjectManage> pagination);

}
