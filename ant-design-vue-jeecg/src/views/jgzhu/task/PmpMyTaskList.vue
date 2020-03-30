<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="5" :sm="8">
            <a-dropdown>
              <a-menu slot="overlay" @click="handleMenuClick">
                <a-menu-item key="1" default>负责的任务</a-menu-item>
                <a-menu-item key="2">参与的任务</a-menu-item>
                <a-menu-item key="3">创建的任务</a-menu-item>
              </a-menu>
              <a-button style="margin-left: 8px">
                {{taskStatusVal}}
                <a-icon type="down" />
              </a-button>
            </a-dropdown>
            <!-- <a-select v-model="queryParam.region" >
              <a-select-option value="1" default>负责的任务</a-select-option>
              <a-select-option value="2">参与的任务</a-select-option>
              <a-select-option value="3">创建的任务</a-select-option>
            </a-select>-->
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="项目名称">
              <a-input placeholder="请输入项目名称" v-model="queryParam.projectname"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="任务名称">
              <a-input placeholder="请输入任务名称" v-model="queryParam.taskname"></a-input>
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
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button type="primary" icon="download" @click="handleExportXls('项目管理')">导出</a-button>
      <a-upload
        name="file"
        :showUploadList="false"
        :multiple="false"
        :headers="tokenHeader"
        :action="importExcelUrl"
        @change="handleImportExcel"
      >
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
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
      </a-dropdown> -->
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择
        <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :scroll="{ y: 500 }"
        :rowSelection="{fixed:true,selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange"
      >
        <!-- 总进度 -->
        <span slot="schedule" slot-scope="text,record">
          <a-progress
            :percent="text"
            size="small"
            :strokeColor="record.isdelete==1 ? 'red':record.status==2 ? 'orange':record.status==4 ? '#FFD700':'' "
          />
        </span>
        <div
          slot="filterDropdown"
          slot-scope="{ setSelectedKeys, selectedKeys, confirm, clearFilters, column }"
          style="padding: 8px"
        >
          <a-input
            v-ant-ref="c => searchInput = c"
            :placeholder="`Search ${column.text}`"
            :value="selectedKeys[0]"
            @change="e => setSelectedKeys(e.target.value ? [e.target.value] : [])"
            @pressEnter="() => handleSearch(selectedKeys, confirm)"
            style="width: 188px; margin-bottom: 8px; display: block;"
          />
          <a-button
            type="primary"
            @click="() => handleSearch(selectedKeys, confirm)"
            icon="search"
            size="small"
            style="width: 90px; margin-right: 8px"
          >查询</a-button>
          <a-button @click="() => handleReset(clearFilters)" size="small" style="width: 90px">重置</a-button>
        </div>
        <a-icon
          slot="filterIcon"
          slot-scope="filtered"
          type="search"
          :style="{ color: filtered ? '#108ee9' : undefined }"
        />
        <template slot="customRender" slot-scope="text">
          <span v-if="searchText">
            <template
              v-for="(fragment, i) in text.toString().split(new RegExp(`(?<=${searchText})|(?=${searchText})`, 'i'))"
            >
              <mark
                v-if="fragment.toLowerCase() === searchText.toLowerCase()"
                :key="i"
                class="highlight"
              >{{fragment}}</mark>
              <template v-else>{{fragment}}</template>
            </template>
          </span>
          <template v-else>{{text}}</template>
        </template>
        <template slot="taskname" slot-scope="text,record">
          <a href="javascript:;" @click="myHandleDetailEdit(record)">{{text}}</a>
        </template>
        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无此图片</span>
          <img
            v-else
            :src="getImgView(text)"
            height="25px"
            alt="图片不存在"
            style="max-width:80px;font-size: 12px;font-style: italic;"
          />
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无此文件</span>
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="uploadFile(text)"
          >下载</a-button>
        </template>
        <span slot="isdelete" slot-scope="text">
          <a-tag :color="text==1 ? 'volcano' : 'green'">{{ text == 0 ? '正常':'禁用'}}</a-tag>
        </span>
        <span slot="action" slot-scope="text, record">
          <a @click="myHandleDetailEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">
              更多
              <a-icon type="down" />
            </a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a href="javascript:;" @click="handleDetail1(record)">详情</a>
              </a-menu-item>
              <a-menu-item>
                <a href="javascript:;" @click="handleComment(record)">评论</a>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>
      </a-table>
    </div>
    <pmpTaskdetails-modal ref="modalForm1" @ok="modalFormOk"></pmpTaskdetails-modal>
    <pmpComment-modal ref="modalForm2" @ok="modalFormOk"></pmpComment-modal>
  </a-card>
</template>

<script>
import { getAction } from '@/api/manage'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import { initDictOptions, filterDictText, myFilterMultiDictText } from '@/components/dict/JDictSelectUtil'
import PmpTaskdetailsModal from '@views/jgzhu/project/modules/PmpTaskdetailsModal'
import PmpCommentModal from '@views/wqc/modules/PmpCommentModal'
import { filterObj, isContainPrincipal } from '@/utils/util'
import Vue from 'vue'
import { USER_NAME } from '@/store/mutation-types'

export default {
  name: 'PmpProjectList',
  mixins: [JeecgListMixin],
  components: {
    PmpTaskdetailsModal,
    PmpCommentModal
  },
  data() {
    return {
      taskStatusVal: '负责的任务',
      description: '项目管理管理页面',
      projectstatus: [],
      projectisdelete: [],
      principal: [],
      projectTypeDictOptions: [],
      taskTypeDictOptions: [],
      searchText: '',
      searchInput: null,
      // 表头
      columns: [
        {
          title: '#',
          dataIndex: '',
          key: 'rowIndex',
          width: 60,
          align: 'center',
          customRender: function(t, r, index) {
            return parseInt(index) + 1
          }
        },
        {
          title: '项目名称',
          align: 'center',
          width: '18%',
          dataIndex: 'projectname',
          text: '项目名',
          scopedSlots: {
            filterDropdown: 'filterDropdown',
            filterIcon: 'filterIcon',
            customRender: 'customRender'
          },
          onFilter: (value, record) =>
            record.projectname
              .toString()
              .toLowerCase()
              .includes(value.toLowerCase()),
          onFilterDropdownVisibleChange: visible => {
            if (visible) {
              setTimeout(() => {
                this.searchInput.focus()
              }, 0)
            }
          }
        },
        {
          title: '任务名称',
          align: 'center',
          dataIndex: 'taskname',
          text: '任务名',
          scopedSlots: {
            filterDropdown: 'filterDropdown',
            filterIcon: 'filterIcon',
            customRender: 'customRender',
            customRender: 'taskname'
          },
          onFilter: (value, record) =>
            record.taskname
              .toString()
              .toLowerCase()
              .includes(value.toLowerCase()),
          onFilterDropdownVisibleChange: visible => {
            if (visible) {
              setTimeout(() => {
                this.searchInput.focus()
              }, 0)
            }
          }
        },
        {
          title: '负责人',
          align: 'center',
          width: '10%',
          dataIndex: 'principal',
          customRender: text => {
            //字典值替换通用方法
            return myFilterMultiDictText(this.principal, text)
          },
          filters: [],
          onFilter: (value, record) => record.principal.indexOf(value) != 0
        },
        {
          title: '总进度',
          align: 'center',
          width: '10%',
          dataIndex: 'schedule',
          scopedSlots: { customRender: 'schedule' },
          onFilter: (value, record) => record.schedule.indexOf(value) === 0,
          sorter: (a, b) => a.schedule - b.schedule,
          sortDirections: ['descend', 'ascend']
        },
        {
          title: '任务分类',
          align: 'center',
          width: '8%',
          dataIndex: 'projecttype',
          customRender: text => {
            return filterDictText(this.projectTypeDictOptions, text) == text
              ? filterDictText(this.taskTypeDictOptions, text)
              : filterDictText(this.projectTypeDictOptions, text)
          },
          onFilter: (value, record) => record.projecttype.indexOf(value) === 0,
          sorter: (a, b) => a.projecttype.toUpperCase() > b.projecttype.toUpperCase(),
          sortDirections: ['descend', 'ascend']
        },
        {
          title: '开始日期',
          align: 'center',
          width: '6%',
          dataIndex: 'startdate',
          customRender: function(text) {
            return !text ? '' : text.length > 10 ? text.substr(0, 10) : text
          },
          onFilter: (value, record) => record.startdate.indexOf(value) === 0,
          sorter: (a, b) => {
            let aTimeString = a.startdate
            let bTimeString = b.startdate
            aTimeString = aTimeString.replace(/-/g, '/')
            bTimeString = bTimeString.replace(/-/g, '/')
            let aTime = new Date(aTimeString).getTime()
            let bTime = new Date(bTimeString).getTime()
            return aTime - bTime
          },
          sortDirections: ['descend', 'ascend']
        },
        {
          title: '结束日期',
          align: 'center',
          width: '6%',
          dataIndex: 'enddate',
          customRender: function(text) {
            return !text ? '' : text.length > 10 ? text.substr(0, 10) : text
          },
          onFilter: (value, record) => record.enddate.indexOf(value) === 0,
          sorter: (a, b) => {
            let aTimeString = a.enddate
            let bTimeString = b.enddate
            aTimeString = aTimeString.replace(/-/g, '/')
            bTimeString = bTimeString.replace(/-/g, '/')
            let aTime = new Date(aTimeString).getTime()
            let bTime = new Date(bTimeString).getTime()
            return aTime - bTime
          },
          sortDirections: ['descend', 'ascend']
        },
        // {
        //   title: '状态',
        //   align: 'center',
        //   dataIndex: 'status',
        //   customRender: text => {
        //     //字典值替换通用方法
        //     return filterDictText(this.projectstatus, text)
        //   }
        // },
        {
          title: '是否删除',
          align: 'center',
          width: '6%',
          dataIndex: 'isdelete',
          scopedSlots: { customRender: 'isdelete' }
        },
        {
          title: '操作',
          dataIndex: 'action',
          width: '10%',
          align: 'center',
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: '/taskmanage/pmpTask/myTaskList',
        list1: '/taskmanage/pmpTask/myTaskListByPar',
        list2: '/taskmanage/pmpTask/myTaskListByCreateBy',
        delete: '/taskmanage/pmpTask/delete',
        deleteBatch: '/taskmanage/pmpTask/deleteBatch',
        exportXlsUrl: '/taskmanage/pmpTask/exportXls',
        importExcelUrl: '/taskmanage/pmpTask/importExcel',
        isSuperior: 'protree/pmpProject/isSuperior'
      },
      dictOptions: {}
    }
  },
  created() {
    //初始化字典 - 项目状态
    initDictOptions('project_status').then(res => {
      if (res.success) {
        this.projectstatus = res.result
      }
    })
    //初始化字典 - 创建人
    initDictOptions('sys_user,realname,username').then(res => {
      if (res.success) {
        this.principal = res.result
        let user = this.username
        this.columns[3].filters = [{ text: myFilterMultiDictText(this.principal, user), value: user }]
      }
    })
    //初始化字典 - 项目类型
    initDictOptions('task_type').then(res => {
      if (res.success) {
        this.taskTypeDictOptions = res.result
      }
    })
    //初始化字典 - 项目类型
    initDictOptions('project_type').then(res => {
      if (res.success) {
        this.projectTypeDictOptions = res.result
      }
    })
  },
  computed: {
    importExcelUrl: function() {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    }
  },
  methods: {
    handleSearch(selectedKeys, confirm) {
      confirm()
      this.searchText = selectedKeys[0]
    },

    handleReset(clearFilters) {
      clearFilters()
      this.searchText = ''
    },
    searchQuery() {
      if (this.taskStatusVal == '参与的任务') {
        this.loadData(1, this.url.list1)
      } else if (this.taskStatusVal == '创建的任务') {
        this.loadData(1, this.url.list2)
      } else {
        this.loadData(1, this.url.list)
      }
    },
    handleTableChange(pagination, filters, sorter) {
      //分页、排序、筛选变化时触发
      //TODO 筛选
      if (Object.keys(sorter).length > 0) {
        this.isorter.column = sorter.field
        this.isorter.order = 'ascend' == sorter.order ? 'asc' : 'desc'
      }
      this.ipagination = pagination
      if (this.taskStatusVal == '参与的任务') {
        this.loadData(0, this.url.list1)
      } else if (this.taskStatusVal == '创建的任务') {
        this.loadData(0, this.url.list2)
      } else {
        this.loadData(0, this.url.list)
      }
    },
    handleMenuClick(e) {
      if (e.key == '2') {
        this.taskStatusVal = '参与的任务'
        this.loadData(1, this.url.list1)
      } else if (e.key == '3') {
        this.taskStatusVal = '创建的任务'
        this.loadData(1, this.url.list2)
      } else {
        this.taskStatusVal = '负责的任务'
        this.loadData(1, this.url.list)
      }
    },
    loadData(arg, url = this.url.list) {
      if (!url) {
        this.$message.error('请设置url.list属性!')
        return
      }
      //加载数据 若传入参数1则加载第一页的内容
      if (arg === 1) {
        this.ipagination.current = 1
      }
      var params = this.getQueryParams() //查询条件
      this.loading = true
      getAction(url, params).then(res => {
        if (res.success) {
          this.dataSource = res.result.records
          this.ipagination.total = res.result.total
        }
        if (res.code === 510) {
          this.$message.warning(res.message)
        }
        this.loading = false
      })
    },
    getQueryParams() {
      //获取查询条件
      let sqp = {}
      if (this.superQueryParams) {
        sqp['superQueryParams'] = encodeURI(this.superQueryParams)
      }
      let params = {
        username: this.username
      }
      var param = Object.assign(sqp, this.queryParam, this.isorter, this.filters, params)
      param.field = this.getQueryField()
      param.pageNo = this.ipagination.current
      param.pageSize = this.ipagination.pageSize
      return filterObj(param)
    },
    getQueryField() {
      //TODO 字段权限控制
      var str = 'id,'
      this.columns.forEach(function(value) {
        str += ',' + value.dataIndex
      })
      return str
    },
    handleComment: function(record) {
      if (record.isdelete == '0') {
        let params = {
          id: record.id,
          principal: this.username
        }
        getAction(this.url.isSuperior, params).then(res => {
          if (res.success) {
            this.$refs.modalForm2.show(record)
            // callback()
          } else {
            this.openNotification('提示', '权限不够哦,无法评论！')
            // callback(res.message)
          }
        })
      } else {
        this.openNotification('提示', '已禁用,无法评论！')
      }
    },
    handleDetail1: function(record) {
      this.$refs.modalForm1.edit(record)
      this.$refs.modalForm1.title = '详情'
      this.$refs.modalForm1.disableSubmit = true
    },
    openNotification(title, des) {
      this.$notification.open({
        message: title,
        description: des,
        icon: <a-icon type="frown" style="color: red" />
      })
    },
    myHandleDetailEdit(record) {
      if (record.isdelete == '0') {
        if (record.createBy == this.username || isContainPrincipal(record.principal, this.username)) {
          this.$refs.modalForm1.edit(record)
          this.$refs.modalForm1.title = '编辑'
          this.$refs.modalForm1.disableSubmit = false
        } else {
          this.openNotification('提示', '权限不够哦,禁止编辑！')
        }
      } else {
        this.openNotification('提示', '已禁用,无法编辑！')
      }
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>