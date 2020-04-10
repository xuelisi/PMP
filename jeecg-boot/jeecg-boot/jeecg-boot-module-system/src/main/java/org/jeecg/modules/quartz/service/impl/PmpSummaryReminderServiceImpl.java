package org.jeecg.modules.quartz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.wqc.summary.entity.PmpSummary;
import org.jeecg.modules.quartz.mapper.PmpSummaryReminderMapper;
import org.jeecg.modules.quartz.service.IPmpSummaryReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 定时任务在线管理
 * @Author: jeecg-boot
 * @Date: 2019-04-28
 * @Version: V1.1
 */
@Slf4j
@Service
public class PmpSummaryReminderServiceImpl extends ServiceImpl<PmpSummaryReminderMapper, PmpSummary> implements IPmpSummaryReminderService {
	@Autowired
	private PmpSummaryReminderMapper psrMapper;

	public List<Map<String, String>> unFilledMembers(String date){
		return psrMapper.unFilledMembers(date);
	}
}
