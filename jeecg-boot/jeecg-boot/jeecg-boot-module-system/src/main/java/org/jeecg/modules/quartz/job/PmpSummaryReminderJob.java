package org.jeecg.modules.quartz.job;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.modules.quartz.service.IPmpSummaryReminderService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.text.SimpleDateFormat;
import java.util.*;

import java.io.*;
import java.net.*;
import cn.hutool.http.*;
import cn.hutool.json.*;

/**
 * 示例带参定时任务
 * 
 * @Author Scott
 */
@Slf4j
public class PmpSummaryReminderJob implements Job {

	@Autowired
	private IPmpSummaryReminderService ipsrService;

	@Autowired
	@Lazy
	private ISysBaseAPI sysBaseAPI;

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		reminder();
	}

	private void reminder() {
		String dateStr = "今日";
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = ymdFormat.format(calendar.getTime());
		if (calendar.get(Calendar.HOUR_OF_DAY) < 12) {
			dateStr = "昨日";
			calendar.set(Calendar.HOUR_OF_DAY, -24);
			date = ymdFormat.format(calendar.getTime());
		}

		//节假日不提醒
		if (!isHoliday(dateStr)) {
			List<Map<String, String>> list = ipsrService.unFilledMembers(date);
			for(Map<String, String> item : list) {
				//log.info("username: " + item.get("username") + "realname: " + item.get("realname"));
				sendSummaryReminder("admin", item.get("username"), item.get("realname"), dateStr);
			}
		}
	}

	private boolean isHoliday(String date) {
		String jsonStr = HttpUtil
				.createGet("http://timor.tech/api/holiday/info/" + date)
				.execute()
				.charset("gbk")
				.body();

		boolean isHoliday = false;
		String dateType = ((JSONObject)new JSONObject(jsonStr).getObj("type")).getStr("type");

		switch (dateType) {
			//工作日
			case "0":
				isHoliday = false;
				break;
			//假日
			case "1":
				isHoliday = true;
				break;
			//节日
			case "2":
				isHoliday = true;
				break;
			//调休
			case "3":
				isHoliday = true;
				break;
			default:
				isHoliday = false;
		}

		return isHoliday;
	}

	private void sendSummaryReminder(String from, String to, String realname, String date) {
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("date", date);
		paramMap.put("realname", realname);
		sysBaseAPI.sendSysAnnouncement(from, to, "你有新的小结通知！", paramMap, "bpm_summary_note");
	}
}
