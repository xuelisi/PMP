package org.jeecg.modules.quartz.job;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.quartz.service.IPmpMessageService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 示例带参定时任务
 * 
 * @Author Scott
 */
@Slf4j
public class PmpMessageJob implements Job {

	@Autowired
	private IPmpMessageService iPmpMessageService;

	@Autowired
	@Lazy
	private ISysBaseAPI sysBaseAPI;


	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

		SimpleDateFormat sDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		Date date=new Date();
		List<Map<String,Object>> list=iPmpMessageService.myPmpMessage(sDateFormat1.format(date));//项目未完成
		List<Map<String,Object>> listtask=iPmpMessageService.myTaskMessage(sDateFormat1.format(date));//任务未完成
		for(Map<String, Object> m : list)
		{
			Map<String, String> map = new HashMap();
			map.put("ts_date",sDateFormat1.format(date));
			String content="你好,"+m.get("name").toString()+"。截止"+sDateFormat1.format(date)+"你有"+m.get("number")+"个项目没有完成，请及时完成。";
			map.put("ts_content",content);
			sysBaseAPI.sendSysAnnouncement("admin", m.get("principal").toString(),"系统消息通知",map,"sys_ts_note");
		}
		for(Map<String, Object> k : listtask)
		{
			Map<String, String> mapt = new HashMap();
			mapt.put("ts_date",sDateFormat1.format(date));
			String content="你好,"+k.get("name").toString()+"。截止"+sDateFormat1.format(date)+"你有"+k.get("number")+"个项任务没有完成，请及时完成。";
			mapt.put("ts_content",content);
			sysBaseAPI.sendSysAnnouncement("admin",k.get("principal").toString(),"系统消息通知",mapt,"sys_ts_note");
		}
		/**
		 * 发送系统消息principal
		 * @param fromUser 发送人(用户登录账户)
		 * @param toUser   发送给(用户登录账户)
		 * @param title    通知标题
		 * @param map  	   模板参数
		 * @param templateCode  模板编码
		 */
		//public void sendSysAnnouncement(String fromUser, String toUser,String title, Map<String, String> map, String templateCode);


	}
}
