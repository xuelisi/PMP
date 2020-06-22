package org.jeecg.modules.quartz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.wqc.summary.entity.PmpSummary;

import java.util.List;
import java.util.Map;

/**
 * @Description: 定时任务在线管理
 * @Author: jeecg-boot
 * @Date: 2019-04-28
 * @Version: V1.1
 */
public interface IPmpSummaryReminderService extends IService<PmpSummary> {

	public List<Map<String, String>>  unFilledMembers(String date);
}
