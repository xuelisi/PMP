package org.jeecg.modules.quartz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.project.protree.entity.PmpProject;
import org.jeecg.modules.quartz.mapper.PmpMessageMapper;
import org.jeecg.modules.quartz.service.IPmpMessageService;
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
public class PmpMessageServiceImpl extends ServiceImpl<PmpMessageMapper, PmpProject> implements IPmpMessageService {
	@Autowired
	private PmpMessageMapper pmpMessageMapper;

	public  List<Map<String,Object>>  myPmpMessage(String date){
		return pmpMessageMapper.myPmpMessage(date);
	}

	public  List<Map<String,Object>>  myTaskMessage(String date){
		return pmpMessageMapper.myTaskMessage(date);
	}
}
