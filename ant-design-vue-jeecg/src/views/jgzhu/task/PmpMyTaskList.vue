<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="5" :sm="8">
            <a-dropdown>
              <a-menu slot="overlay" @click="handleMenuClick">
                <a-menu-item key="1">负责的任务</a-menu-item>
                <a-menu-item key="2">参与的任务</a-menu-item>
                <a-menu-item key="3">创建的任务</a-menu-item>
              </a-menu>
              <a-button style="margin-left: 8px">
                任务分类
                <a-icon type="down" />
              </a-button>
            </a-dropdown>
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

export default {
  name: 'PmpProjectList',
  mixins: [JeecgListMixin],
  components: {
    PmpTaskdetailsModal,
    PmpCommentModal
  },
  data() {
    return {
      description: '项目管理管理页面',
      projectstatus: [],
      projectisdelete: [],
      principal: [],
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
          customRender: text => {
            //字典值替换通用方法
            return myFilterMultiDictText(this.principal, text)
          }
        },
        {
          title: '总进度',
          align: 'center',
          dataIndex: 'schedule',
          scopedSlots: { customRender: 'schedule' }
        },
        {
          title: '开始日期',
          align: 'center',
          dataIndex: 'startdate',
          customRender: function(text) {
            return !text ? '' : text.length > 10 ? text.substr(0, 10) : text
          }
        },
        {
          title: '结束日期',
          align: 'center',
          dataIndex: 'enddate',
          customRender: function(text) {
            return !text ? '' : text.length > 10 ? text.substr(0, 10) : text
          }
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
          dataIndex: 'isdelete',
          scopedSlots: { customRender: 'isdelete' }
        },
        {
          title: '操作',
          dataIndex: 'action',
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
  computed: {
    importExcelUrl: function() {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    }
  },
  methods: {
    handleMenuClick(e) {
      if (e.key == '2') {
        this.loadData(1, this.url.list1)
      } else if (e.key == '3') {
        this.loadData(1, this.url.list2)
      } else {
        this.loadData(1, this.url.list)
      }
    },
    initDictConfig() {
      //初始化字典 - 项目状态
      initDictOptions('sys_user,realname,username').then(res => {
        if (res.success) {
          this.principal = res.result
        }
      }),
        //初始化字典 - 项目状态
        initDictOptions('project_status').then(res => {
          if (res.success) {
            this.projectstatus = res.result
          }
        })
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
      let params = {
        field: this.getQueryField(),
        username: this.username,
        pageNo: this.ipagination.current,
        pageSize: this.ipagination.pageSize
      }
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