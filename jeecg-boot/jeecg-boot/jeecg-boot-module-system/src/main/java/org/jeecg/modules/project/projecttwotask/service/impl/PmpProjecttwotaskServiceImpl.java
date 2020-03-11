package org.jeecg.modules.project.projecttwotask.service.impl;

import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.project.projecttwotask.entity.PmpProjecttwotask;
import org.jeecg.modules.project.projecttwotask.mapper.PmpProjecttwotaskMapper;
import org.jeecg.modules.project.projecttwotask.service.IPmpProjecttwotaskService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 项目任务关联表
 * @Author: jeecg-boot
 * @Date:   2020-03-09
 * @Version: V1.0
 */
@Service
public class PmpProjecttwotaskServiceImpl extends ServiceImpl<PmpProjecttwotaskMapper, PmpProjecttwotask> implements IPmpProjecttwotaskService {

	@Override
	public void addPmpProjecttwotask(PmpProjecttwotask pmpProjecttwotask) {
		if(oConvertUtils.isEmpty(pmpProjecttwotask.getParentnode())){
			pmpProjecttwotask.setParentnode(IPmpProjecttwotaskService.ROOT_PID_VALUE);
		}else{
			//如果当前节点父ID不为空 则设置父节点的hasChildren 为1
			PmpProjecttwotask parent = baseMapper.selectById(pmpProjecttwotask.getParentnode());
			if(parent!=null && !"1".equals(parent.getHasChild())){
				parent.setHasChild("1");
				baseMapper.updateById(parent);
			}
		}
		baseMapper.insert(pmpProjecttwotask);
	}
	
	@Override
	public void updatePmpProjecttwotask(PmpProjecttwotask pmpProjecttwotask) {
		PmpProjecttwotask entity = this.getById(pmpProjecttwotask.getId());
		if(entity==null) {
			throw new JeecgBootException("未找到对应实体");
		}
		String old_pid = entity.getParentnode();
		String new_pid = pmpProjecttwotask.getParentnode();
		if(!old_pid.equals(new_pid)) {
			updateOldParentNode(old_pid);
			if(oConvertUtils.isEmpty(new_pid)){
				pmpProjecttwotask.setParentnode(IPmpProjecttwotaskService.ROOT_PID_VALUE);
			}
			if(!IPmpProjecttwotaskService.ROOT_PID_VALUE.equals(pmpProjecttwotask.getParentnode())) {
				baseMapper.updateTreeNodeStatus(pmpProjecttwotask.getParentnode(), IPmpProjecttwotaskService.HASCHILD);
			}
		}
		baseMapper.updateById(pmpProjecttwotask);
	}
	
	@Override
	public void deletePmpProjecttwotask(String id) throws JeecgBootException {
		PmpProjecttwotask pmpProjecttwotask = this.getById(id);
		if(pmpProjecttwotask==null) {
			throw new JeecgBootException("未找到对应实体");
		}
		updateOldParentNode(pmpProjecttwotask.getParentnode());
		baseMapper.deleteById(id);
	}
	
	
	
	/**
	 * 根据所传pid查询旧的父级节点的子节点并修改相应状态值
	 * @param pid
	 */
	private void updateOldParentNode(String pid) {
		if(!IPmpProjecttwotaskService.ROOT_PID_VALUE.equals(pid)) {
			Integer count = baseMapper.selectCount(new QueryWrapper<PmpProjecttwotask>().eq("parentnode", pid));
			if(count==null || count<=1) {
				baseMapper.updateTreeNodeStatus(pid, IPmpProjecttwotaskService.NOCHILD);
			}
		}
	}

}
