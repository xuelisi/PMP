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
            <a-form-item label="任务名称">
              <a-input placeholder="请输入任务名称" v-model="queryParam.taskname"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="3" :sm="8">
            <a-form-item>
              <a-radio-group name="radioGroup" :defaultValue="0" v-model="queryParam.isdelete">
                <a-radio :value="0">正常</a-radio>
                <a-radio :value="1">禁用</a-radio>
              </a-radio-group>
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
            <a-form-item label="负责人">
              <j-select-multi-user v-model="queryParam.principal"></j-select-multi-user>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="总进度">
              <a-input-number
                placeholder="请输入总进度"
                :min="0"
                :max="100"
                class="inputnum"
                v-model="queryParam.schedule"
              />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="任务分类">
              <j-dict-select-tag v-model="queryParam.projecttype" dictCode="task_type" placeholder="请输入任务分类"/>
            </a-form-item>
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
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel">
            <a-icon type="delete" />删除
          </a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px">
          批量操作
          <a-icon type="down" />
        </a-button>
      </a-dropdown>
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
        :scroll="{ y: 500 }"
        :loading="loading"
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
import JSelectMultiUser from '@/components/jeecgbiz/JSelectMultiUser'

export default {
  name: 'PmpProjectList',
  mixins: [JeecgListMixin],
  components: {
    PmpTaskdetailsModal,
    PmpCommentModal,
    JSelectMultiUser
  },
  data() {
    return {
      description: '项目管理管理页面',
      projectstatus: [],
      projectisdelete: [],
      principal: [],
      projectTypeDictOptions: [],
      taskTypeDictOptions: [],
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
          dataIndex: 'projectname'
        },
        {
          title: '任务名称',
          align: 'center',
          dataIndex: 'taskname',
          scopedSlots: { customRender: 'taskname' }
        },
        {
          title: '负责人',
          align: 'center',
          dataIndex: 'principal',
          width: '10%',
          customRender: text => {
            //字典值替换通用方法
            return myFilterMultiDictText(this.principal, text)
          }
        },
        {
          title: '总进度',
          align: 'center',
          dataIndex: 'schedule',
          width: '8%',
          scopedSlots: { customRender: 'schedule' },
          onFilter: (value, record) => record.schedule.indexOf(value) === 0,
          sorter: (a, b) => a.schedule - b.schedule,
          sortDirections: ['descend', 'ascend']
        },
        {
          title: '任务分类',
          align: 'center',
          dataIndex: 'projecttype',
          width: '8%',
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
          dataIndex: 'startdate',
          width: '6%',
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
          dataIndex: 'enddate',
          width: '6%',
          customRender: function(text) {
            return !text ? '' : text.length > 10 ? text.substr(0, 10) : text
          },
          onFilter: (value, record) => record.enddate.indexOf(value) === 0,
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
          align: 'center',
          width: '10%',
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: '/taskmanage/pmpTask/list',
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
.inputnum {
  width: 100%;
}
</style>