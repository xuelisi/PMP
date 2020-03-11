package org.jeecg.modules.project.projecttwotask.mapper;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.project.projecttwotask.entity.PmpProjecttwotask;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 项目任务关联表
 * @Author: jeecg-boot
 * @Date:   2020-03-09
 * @Version: V1.0
 */
public interface PmpProjecttwotaskMapper extends BaseMapper<PmpProjecttwotask> {

	/**
	 * 编辑节点状态
	 * @param id
	 * @param status
	 */
	void updateTreeNodeStatus(@Param("id") String id,@Param("status") String status);

}
