package org.jeecg.modules.quartz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.project.protree.entity.PmpProject;
import java.util.List;
import java.util.Map;

/**
 * @Description: 定时任务在线管理
 * @Author: jeecg-boot
 * @Date:  2019-01-02
 * @Version: V1.0
 */
public interface PmpMessageMapper extends BaseMapper<PmpProject> {

	public List<Map<String,Object>>  myPmpMessage(@Param("date") String date);

	public List<Map<String,Object>>  myTaskMessage(@Param("date") String date);
}
