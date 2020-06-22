package org.jeecg.modules.quartz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.project.protree.entity.PmpProject;
import org.jeecg.modules.quartz.entity.QuartzJob;
import org.quartz.SchedulerException;

import java.util.List;
import java.util.Map;

/**
 * @Description: 定时任务在线管理
 * @Author: jeecg-boot
 * @Date: 2019-04-28
 * @Version: V1.1
 */
public interface IPmpMessageService extends IService<PmpProject> {

	public List<Map<String,Object>>  myPmpMessage(String date);
	public List<Map<String,Object>>  myTaskMessage(String date);
}
