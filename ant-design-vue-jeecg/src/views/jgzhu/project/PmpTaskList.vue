<template>
  <a-card :bordered="false">
    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" icon="plus">添加任务</a-button>
      <!--<a-button type="primary" icon="download" @click="handleExportXls('分类字典')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :action="importExcelUrl" @change="handleImportExcel">
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
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :expandedRowKeys="expandedRowKeys"
        @change="handleTableChange"
        @expand="handleExpand"
        v-bind="tableProps"
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
          <a
            href="javascript:;"
            @click="myHandleDetailEdit(record)"
            v-if="record.parentnode!='0'"
          >{{text}}</a>
          <span v-else>{{text}}</span>
        </template>
        <span slot="isdelete" slot-scope="text">
          <a-tag :color="text==1 ? 'volcano' : 'green'">{{ text == 0 ? '正常':'禁用'}}</a-tag>
        </span>
        <span slot="action" slot-scope="text, record">
          <a @click="myHandleTaskEdit(record)">节点编辑</a>
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
              <a-menu-item v-show="record.parentnode!=0">
                <a href="javascript:;" @click="handleComment(record)">评论</a>
              </a-menu-item>
              <a-menu-item>
                <a href="javascript:;" @click="handleSummary(record)">日报</a>
              </a-menu-item>
              <!-- <a-menu-item>
                <a href="javascript:;" @click="handleRemind(record)">催办</a>
              </a-menu-item>-->
              <a-menu-item>
                <a-popconfirm
                  v-if="record.isdelete==0"
                  title="确定禁用吗?"
                  @confirm="() => handleDisable(record)"
                >
                  <a>禁用</a>
                </a-popconfirm>
                <a-popconfirm v-else title="确定启用吗?" @confirm="() => handleEnable(record)">
                  <a>启用</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>
      </a-table>
    </div>
    <pmpTaskList-modal ref="modalForm" @ok="modalFormOk"></pmpTaskList-modal>
    <pmpTaskdetails-modal ref="modalForm1" @ok="modalFormOk"></pmpTaskdetails-modal>
    <pmpComment-modal ref="modalForm2" @ok="modalFormOk"></pmpComment-modal>
    <pmpProSummary-modal ref="summaryModal" @ok="modalFormOk"></pmpProSummary-modal>
    <!-- <taskRemind-modal ref="modalForm3" @ok="modalFormOk"></taskRemind-modal> -->
  </a-card>
</template>

<script>
import Vue from 'vue'
import { getAction } from '@/api/manage'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import PmpTaskListModal from './modules/PmpTaskListModal'
import PmpTaskdetailsModal from './modules/PmpTaskdetailsModal'
import PmpCommentModal from '@views/wqc/modules/PmpCommentModal'
import TaskRemindModal from './modules/TaskRemindModal'
import { USER_NAME } from '@/store/mutation-types'
import { isContainPrincipal } from '@/utils/util'
import { initDictOptions, filterDictText, myFilterMultiDictText } from '@/components/dict/JDictSelectUtil'

import PmpProSummaryModal from '@views/wqc/modules/PmpProSummaryModal'
import { mapState, mapGetters, mapActions, mapMutations } from 'vuex'

export default {
  name: 'SysCategoryList',
  mixins: [JeecgListMixin],
  components: {
    PmpTaskListModal,
    PmpTaskdetailsModal,
    PmpCommentModal,
    TaskRemindModal,
    PmpProSummaryModal
  },
  data() {
    return {
      //字典数组缓存-负责人
      principal: [],
      projectTypeDictOptions: [],
      taskTypeDictOptions: [],
      description: '任务管理页面',
      // 表头
      columns: [
        {
          title: '名称',
          align: 'left',
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
          title: '任务分类',
          align: 'center',
          dataIndex: 'projecttype',
          customRender: text => {
            return filterDictText(this.projectTypeDictOptions, text) == text
              ? filterDictText(this.taskTypeDictOptions, text)
              : filterDictText(this.projectTypeDictOptions, text)
          }
        },
        {
          title: '起始日期',
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
        {
          title: '是否禁用',
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
        list: '/protree/pmpProject/rootList',
        childList: '/protree/pmpProject/childList',
        delete: '/protree/pmpProject/delete',
        deleteBatch: '/protree/pmpProject/deleteBatch',
        disable: '/protree/pmpProject/disable',
        enable: '/protree/pmpProject/enable',
        exportXlsUrl: '/protree/pmpProject/exportXls',
        importExcelUrl: 'protree/pmpProject/importExcel',
        isSuperior: 'protree/pmpProject/isSuperior'
      },
      expandedRowKeys: [],
      hasChildrenField: 'haschild',
      pidField: 'parentnode',
      dictOptions: {}
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
    importExcelUrl() {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    },
    tableProps() {
      let _this = this
      return {
        // 列表项是否可选择
        rowSelection: {
          selectedRowKeys: _this.selectedRowKeys,
          onChange: selectedRowKeys => (_this.selectedRowKeys = selectedRowKeys)
        }
      }
    }
  },
  methods: {
    openNotification(title, des) {
      this.$notification.open({
        message: title,
        description: des,
        icon: <a-icon type="frown" style="color: red" />
      })
    },
    handleSummary: function(record) {
      if (record.isdelete == '0') {
        if (record.principal.indexOf(this.username) >= 0 || record.participant.indexOf(this.username) >= 0) {
          this.$refs.summaryModal.add(record.id)
        } else {
          this.openNotification('提示', '权限不够哦,无法填写日报！')
        }
      } else {
        this.openNotification('提示', '已禁用,无法填写日报！')
      }
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
    // handleRemind: function(record) {
    //   if (record.isdelete == '0') {
    //     let params = {
    //       id: record.id,
    //       principal: this.username
    //     }
    //     getAction(this.url.isSuperior, params).then(res => {
    //       if (res.success) {
    //         this.$refs.modalForm3.showModal(record)
    //         // callback()
    //       } else {
    //         this.openNotification('提示', '权限不够哦,无法催办！')
    //         // callback(res.message)
    //       }
    //     })
    //   } else {
    //     this.openNotification('提示', '已禁用,无法评论！')
    //   }
    // },
    handleDetail1: function(record) {
      this.$refs.modalForm1.edit(record)
      this.$refs.modalForm1.title = '详情'
      this.$refs.modalForm1.disableSubmit = true
    },
    handleAdd: function() {
      this.$refs.modalForm.add()
      this.$refs.modalForm.title = '新增'
      this.$refs.modalForm.disableSubmit = false
      this.$refs.modalForm.projectName = this.$route.query.data.projectname
    },
    myHandleTaskEdit(record) {
      if (record.isdelete == '0') {
        if (record.createBy == this.username) {
          this.$refs.modalForm.edit(record)
          this.$refs.modalForm.title = '编辑'
          this.$refs.modalForm.disableSubmit = false
        } else {
          this.openNotification('提示', '权限不够哦！禁止编辑！')
        }
      } else {
        this.openNotification('提示', '已禁用,无法编辑！')
      }
    },
    myHandleDetailEdit(record) {
      if (record.isdelete == '0') {
        if (record.createBy == this.username || isContainPrincipal(record.principal, this.username)) {
          this.$refs.modalForm1.edit(record)
          this.$refs.modalForm1.title = '编辑'
          this.$refs.modalForm1.disableSubmit = false
          this.$refs.modalForm1.description = this.$route.query.data.projecttype
        } else {
          this.openNotification('提示', '权限不够哦,禁止编辑！')
        }
      } else {
        this.openNotification('提示', '已禁用,无法编辑！')
      }
    },
    loadData(arg) {
      if (arg == 1) {
        this.ipagination.current = 1
      }
      this.loading = true
      this.expandedRowKeys = []
      let params = {
        field: this.getQueryField(),
        pid: this.$route.query.data.id,
        pageNo: this.ipagination.current,
        pageSize: this.ipagination.pageSize
      }
      return new Promise(resolve => {
        getAction(this.url.list, params).then(res => {
          if (res.success) {
            let result = res.result
            if (Number(result.total) > 0) {
              this.ipagination.total = Number(result.total)
              this.dataSource = this.getDataByResult(res.result.records)
              resolve()
            } else {
              this.ipagination.total = 0
              this.dataSource = []
            }
          } else {
            this.$message.warning(res.message)
          }
          this.loading = false
        })
      })
    },
    getDataByResult(result) {
      return result.map(item => {
        //判断是否标记了带有子节点
        if (item[this.hasChildrenField] == '1') {
          let loadChild = { id: item.id + '_loadChild', name: 'loading...', isLoading: true }
          item.children = [loadChild]
        }
        return item
      })
    },
    handleExpand(expanded, record) {
      // 判断是否是展开状态
      if (expanded) {
        this.expandedRowKeys.push(record.id)
        if (record.children.length > 0 && record.children[0].isLoading === true) {
          let params = this.getQueryParams() //查询条件
          params[this.pidField] = record.id
          getAction(this.url.childList, params).then(res => {
            if (res.success) {
              if (res.result && res.result.length > 0) {
                record.children = this.getDataByResult(res.result)
                this.dataSource = [...this.dataSource]
              } else {
                record.children = ''
                record.hasChildrenField = '0'
              }
            } else {
              this.$message.warning(res.message)
            }
          })
        }
      } else {
        let keyIndex = this.expandedRowKeys.indexOf(record.id)
        if (keyIndex >= 0) {
          this.expandedRowKeys.splice(keyIndex, 1)
        }
      }
    },
    initDictConfig() {},
    modalFormOk(formData, arr) {
      if (!formData.id) {
        this.addOk(formData, arr)
      } else {
        this.editOk(formData, this.dataSource)
        this.dataSource = [...this.dataSource]
      }
    },
    editOk(formData, arr) {
      if (arr && arr.length > 0) {
        for (let i = 0; i < arr.length; i++) {
          if (arr[i].id == formData.id) {
            arr[i] = formData
            break
          } else {
            this.editOk(formData, arr[i].children)
          }
        }
      }
    },
    async addOk(formData, arr) {
      if (!formData[this.pidField]) {
        this.loadData(1)
      } else {
        this.expandedRowKeys = []
        //  console.log('22222', arr)
        for (let i of arr) {
          await this.expandTreeNode(i)
        }
      }
    },
    expandTreeNode(nodeId) {
      return new Promise((resolve, reject) => {
        this.getFormDataById(nodeId, this.dataSource)
        let row = this.parentFormData
        this.expandedRowKeys.push(nodeId)
        let params = this.getQueryParams() //查询条件
        params[this.pidField] = nodeId
        getAction(this.url.childList, params).then(res => {
          console.log('11111', res)
          if (res.success) {
            if (res.result && res.result.length > 0) {
              row.children = this.getDataByResult(res.result)
              this.dataSource = [...this.dataSource]
              resolve()
            } else {
              reject()
            }
          } else {
            reject()
          }
        })
      })
    },
    getFormDataById(id, arr) {
      if (arr && arr.length > 0) {
        for (let i = 0; i < arr.length; i++) {
          if (arr[i].id == id) {
            this.parentFormData = arr[i]
          } else {
            this.getFormDataById(id, arr[i].children)
          }
        }
      }
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>