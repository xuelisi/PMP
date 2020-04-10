<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="项目名称">
              <a-input placeholder="请输入项目名称" v-model="queryParam.projectname"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="项目分类">
              <j-dict-select-tag v-model="queryParam.projecttype" dictCode="project_type" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="起止日期">
              <a-range-picker v-model="rangeDate" @change="onChange" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button
                type="primary"
                @click="searchReset"
                icon="reload"
                style="margin-left: 8px"
              >重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'" />
              </a>
            </span>
          </a-col>
        </a-row>
        <a-row :gutter="24" v-if="this.toggleSearchStatus">
          <a-col :md="6" :sm="8">
            <a-form-item label="项目状态">
              <j-dict-select-tag v-model="queryParam.status" dictCode="project_status" />
            </a-form-item>
          </a-col>

          <a-col :md="6" :sm="8">
            <a-form-item label="负责人">
              <j-select-multi-user v-model="queryParam.principal"></j-select-multi-user>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 操作按钮区域 -->
    <div class="table-operator" style="text-align:right">
      <!-- <a-button @click="handleAdd" icon="plus">添加任务</a-button> -->
      <a-button type="primary" icon="download" @click="handleExportXls('分类字典')">导出</a-button>
      <!-- <a-upload name="file" :showUploadList="false" :multiple="false" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>-->
      <!-- <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel">
            <a-icon type="delete" />删除
          </a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px">
          批量操作
          <a-icon type="down" />
        </a-button>
      </a-dropdown>-->
    </div>
    <div>
      <pmp-gantt-elastic-model v-if="showGantt" :dataSource="tasks"></pmp-gantt-elastic-model>
    </div>
    <div style="text-align:center">
      <span>正常完成</span>
      <a-rate :value="1" :count="1" style="color:#52c41a" />
      <span>延期完成</span>
      <a-rate :value="1" :count="1" style="color:orange" />
      <span>正常进行中</span>
      <a-rate :value="1" :count="1" style="color:#1890ff" />
      <span>延期进行中</span>
      <a-rate :value="1" :count="1" style="color:#FFD700" />
      <span>未开始</span>
      <a-rate :value="1" :count="1" style="color:#BABABA" />
    </div>
    <!-- <a-button @click="addTask" icon="plus">Add task</a-button> -->
  </a-card>
</template>

<style>
</style>

<script>
import moment from 'moment'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import { deleteAction, getAction, putAction, downFile } from '@/api/manage'
import { filterObj, isContainPrincipal } from '@/utils/util'
import JDate from '@/components/jeecg/JDate.vue'
import { initDictOptions, filterDictText, myFilterMultiDictText } from '@/components/dict/JDictSelectUtil'
import JSelectMultiUser from '@/components/jeecgbiz/JSelectMultiUser'
import PmpGanttElasticModel from './modules/PmpGanttElasticModel'

// just helper to get current dates
function getDate(hours) {
  const currentDate = new Date()
  const currentYear = currentDate.getFullYear()
  const currentMonth = currentDate.getMonth()
  const currentDay = currentDate.getDate()
  const timeStamp = new Date(currentYear, currentMonth, currentDay, 0, 0, 0).getTime()
  return new Date(timeStamp + hours * 60 * 60 * 1000).getTime()
}

const finishStyle = {
  base: {
    fill: '#52c41a',
    stroke: '#000000'
  }
}
const delayFinishStyle = {
  base: {
    fill: 'orange',
    stroke: '#000000'
  }
}
const doingStyle = {
  base: {
    fill: '#1890ff',
    stroke: '#000000'
  }
}
const delayDoingStyle = {
  base: {
    fill: '#FFD700',
    stroke: '#000000'
  }
}
const undoingStyle = {
  base: {
    fill: '#BABABA',
    stroke: '#000000'
  }
}

export default {
  name: 'PmpGanttElastic',
  mixins: [JeecgListMixin],
  components: {
    JSelectMultiUser,
    PmpGanttElasticModel
  },
  data() {
    return {
      //字典数组缓存-负责人
      principal: [],
      projectTypeDictOptions: [],
      taskTypeDictOptions: [],
      finishStyle,
      delayFinishStyle,
      doingStyle,
      delayDoingStyle,
      undoingStyle,
      tasks: [],
      showGantt: false,
      rangeDate: [moment().startOf('year'), moment(this.date)],
      url: {
        list: '/protree/pmpProject/ganttList'
      }
    }
  },
  created() {
    //初始化字典 - 创建人
    initDictOptions('sys_user,realname,username').then(res => {
      if (res.success) {
        this.principal = res.result
      }
    })
    //初始化字典 - 项目类型
    initDictOptions('task_type').then(res => {
      if (res.success) {
        this.taskTypeDictOptions = res.result
        console.log(this.taskTypeDictOptions)
      }
    })
    //初始化字典 - 项目类型
    initDictOptions('project_type').then(res => {
      if (res.success) {
        this.projectTypeDictOptions = res.result
      }
    })
  },
  methods: {
    // addTask() {
    //   this.tasks.push({
    //     id: this.lastId++,
    //     label:
    //       '<a href="https://images.pexels.com/photos/423364/pexels-photo-423364.jpeg?auto=compress&cs=tinysrgb&h=650&w=940" target="_blank" style="color:#0077c0;">Yeaahh! you have added a task bro!</a>',
    //     user:
    //       '<a href="https://images.pexels.com/photos/423364/pexels-photo-423364.jpeg?auto=compress&cs=tinysrgb&h=650&w=940" target="_blank" style="color:#0077c0;">Awesome!</a>',
    //     start: getDate(24 * 3),
    //     duration: 1 * 24 * 60 * 60 * 1000,
    //     percent: 50,
    //     type: "project"
    //   });
    // },
    initRangeDate() {
      this.rangeDate = [moment().startOf('year'), moment(this.date)]
    },
    onChange(date, dateString) {
      if (dateString[0].length < 2) {
        this.initRangeDate()
      }
    },
    searchQuery() {
      this.queryParam.startdate = this.rangeDate[0].format('YYYY-MM-DD')
      this.queryParam.enddate = this.rangeDate[1].format('YYYY-MM-DD')

      this.loadData(1)
    },
    searchReset() {
      this.initRangeDate()
      this.queryParam = {}

      this.searchQuery()
    },
    openNotification(title, des) {
      this.$notification.open({
        message: title,
        description: des,
        icon: <a-icon type="frown" style="color: red" />
      })
    },
    loadData(arg) {
      if (!this.url.list) {
        this.$message.error('请设置url.list属性!')
        return
      }
      this.queryParam.startdate = this.rangeDate[0].format('YYYY-MM-DD')
      this.queryParam.enddate = this.rangeDate[1].format('YYYY-MM-DD')
      //加载数据 若传入参数1则加载第一页的内容
      if (arg === 1) {
        this.ipagination.current = 1
      }
      var params = this.getQueryParams() //查询条件
      getAction(this.url.list, params).then(res => {
        if (res.success) {
          for (var i = 0; i < res.result.length; i++) {
            var preList = []
            var temp = {}
            if (res.result[i].parentId == 'NULL') {
              temp = {
                id: res.result[i].id,
                label: res.result[i].taskname,
                user: myFilterMultiDictText(this.principal, res.result[i].principal),
                start:
                  res.result[i].startdate.length > 10 ? res.result[i].startdate.substr(0, 10) : res.result[i].startdate,
                duration: res.result[i].duration * 24 * 60 * 60 * 1000,
                percent: res.result[i].schedule,
                projecttype: filterDictText(this.projectTypeDictOptions, res.result[i].projecttype),
                type: 'project',
                Status: res.result[i].status
              }
            } else {
              temp = {
                id: res.result[i].id,
                label: res.result[i].taskname,
                user: myFilterMultiDictText(this.principal, res.result[i].principal),
                start:
                  res.result[i].startdate.length > 10 ? res.result[i].startdate.substr(0, 10) : res.result[i].startdate,
                duration: res.result[i].duration * 24 * 60 * 60 * 1000,
                percent: res.result[i].schedule,
                projecttype: filterDictText(this.taskTypeDictOptions, res.result[i].projecttype),
                Status: res.result[i].status,
                type: 'task',
                parentId: res.result[i].parentnode
              }
            }
            // if (res.result[i].PreTaskId != '') {
            //   preList.push(resJson.Data[i].PreTaskId)
            //   temp.dependentOn = preList
            // }
            if (temp.Status == '1') {
              temp.style = this.finishStyle
            } else if (temp.Status == '2') {
              temp.style = this.delayFinishStyle
            } else if (temp.Status == '3') {
              temp.style = this.doingStyle
            } else if (temp.Status == '4') {
              temp.style = this.delayDoingStyle
            } else {
              temp.style = this.undoingStyle
            }
            this.tasks.push(temp)
          }
          // this.tasksUpdate(this.dataSource)
          if (this.tasks.length > 0) {
            console.log('tasks:', this.tasks)
            this.showGantt = true
          } else {
            this.openNotification('提示', ' 未查到相关数据！')
          }
        }
        if (res.code === 510) {
          this.$message.warning(res.message)
        }
      })
    },
    getQueryParams() {
      //获取查询条件
      let sqp = {}
      if (this.superQueryParams) {
        sqp['superQueryParams'] = encodeURI(this.superQueryParams)
      }
      var param = Object.assign(sqp, this.queryParam, this.isorter, this.filters)
      param.pageNo = this.ipagination.current
      param.pageSize = this.ipagination.pageSize
      return filterObj(param)
    }
  }
}
</script>