package org.jeecg.modules.project.projecttwotask.service;

import org.jeecg.modules.project.projecttwotask.entity.PmpProjecttwotask;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.exception.JeecgBootException;

/**
 * @Description: 项目任务关联表
 * @Author: jeecg-boot
 * @Date:   2020-03-09
 * @Version: V1.0
 */
public interface IPmpProjecttwotaskService extends IService<PmpProjecttwotask> {

	/**根节点父ID的值*/
	public static final String ROOT_PID_VALUE = "0";
	
	/**树节点有子节点状态值*/
	public static final String HASCHILD = "1";
	
	/**树节点无子节点状态值*/
	public static final String NOCHILD = "0";

	/**新增节点*/
	void addPmpProjecttwotask(PmpProjecttwotask pmpProjecttwotask);
	
	/**修改节点*/
	void updatePmpProjecttwotask(PmpProjecttwotask pmpProjecttwotask) throws JeecgBootException;
	
	/**删除节点*/
	void deletePmpProjecttwotask(String id) throws JeecgBootException;

}
