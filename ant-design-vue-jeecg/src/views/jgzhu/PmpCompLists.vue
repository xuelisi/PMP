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
          <!--       <a-col :md="6" :sm="8">
            <a-form-item label="负责人">
              <j-select-multi-user v-model="queryParam.principal"></j-select-multi-user>
            </a-form-item>
          </a-col>-->
          <a-col :md="6" :sm="8">
            <a-form-item label="是否禁用">
              <a-select placeholder="请选择是否禁用" v-model="queryParam.isdelete" allowClear>
                <a-select-option value="0">正常</a-select-option>
                <a-select-option value="1">禁用</a-select-option>
              </a-select>
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
            <a-form-item label="项目状态">
              <j-dict-select-tag v-model="queryParam.status" dictCode="project_status" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="项目分类">
              <j-dict-select-tag v-model="queryParam.projecttype" dictCode="project_type" />
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

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择
        <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
        <span style="float:right;">
          <a @click="loadData()">
            <a-icon type="sync" />刷新
          </a>
          <a-divider type="vertical" />
          <a-popover title="自定义列" trigger="click" placement="leftBottom">
            <template slot="content">
              <a-checkbox-group
                @change="onColSettingsChange"
                v-model="settingColumns"
                :defaultValue="settingColumns"
              >
                <a-row>
                  <template v-for="(item,index) in defColumns">
                    <template v-if="item.key!='rowIndex'&& item.dataIndex!='action'">
                      <a-col :span="12" :key="index">
                        <a-checkbox :value="item.dataIndex">{{ item.title }}</a-checkbox>
                      </a-col>
                    </template>
                  </template>
                </a-row>
              </a-checkbox-group>
            </template>
            <a>
              <a-icon type="setting" />自定义列
            </a>
          </a-popover>
        </span>
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
        :scroll="{ x: '220%',y: 500}"
        @change="handleTableChange"
        @expand="handleExpand"
        v-bind="tableProps"
      >
        <!--    <template slot="projectname" slot-scope="text,record">
          <a href="javascript:;" @click="handleEditTaskDetail(record)">{{text}}</a>
        </template>-->
        <template slot="photo" slot-scope="text">
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
        <!-- 内容 -->
        <template slot="htmlSlot" slot-scope="text">
          <j-ellipsis :value="text" :length="8" />
        </template>
        <!-- 备注 -->
        <template slot="htmlremark" slot-scope="text">
          <j-ellipsis :value="text" :length="8" />
        </template>
        <!-- 总进度 -->
        <span slot="schedule" slot-scope="text,record">
          <a-progress
            :percent="text"
            size="small"
            :strokeColor="record.isdelete==1 ? 'red':record.status==2 ? 'orange':record.status==4 ? '#FFD700':'' "
          />
        </span>
        <!-- 是否删除 -->
        <span slot="isdelete" slot-scope="text">
          <a-tag :color="text==1 ? 'volcano' : 'green'">{{ text == 0 ? '正常':'禁用'}}</a-tag>
        </span>
        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>
        </span>
      </a-table>
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
    </div>

    <!--    <pmpTaskList-modal ref="modalForm" @ok="modalFormOk"></pmpTaskList-modal>
    <pmpTaskdetails-modal ref="modalForm1" @ok="modalFormOk"></pmpTaskdetails-modal>-->
  </a-card>
</template>

<script>
import { getAction } from '@/api/manage'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
//import PmpTaskListModal from '@views/jgzhu/project/modules/PmpTaskListModal'
//import PmpTaskdetailsModal from '@views/jgzhu/project/modules/PmpTaskdetailsModal'
import JDate from '@/components/jeecg/JDate.vue'
import { initDictOptions, filterDictText, filterMultiDictText } from '@/components/dict/JDictSelectUtil'
import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
import JInput from '@/components/jeecg/JInput.vue'
import Vue from 'vue'
import JEllipsis from '@/components/jeecg/JEllipsis'
import JSelectMultiUser from '@/components/jeecgbiz/JSelectMultiUser'
import { filterObj } from '@/utils/util'
export default {
  name: 'SysCategoryList',
  mixins: [JeecgListMixin],
  components: {
    // PmpTaskListModal,
    JDate,
    //PmpTaskdetailsModal,
    JSuperQuery,
    JSelectMultiUser,
    JEllipsis,
    JInput
  },
  data() {
    return {
      description: '综合查询页面',
      //字典数组缓存-项目分类
      projectTypeDictOptions: [],
      //字典数组缓存-紧急程度
      dictOptions: [],
      //字典数组缓存-负责人
      principal: [],
      participant: [],
      createBys: [],
      updateBy: [],
      sysOrgCodes: [],
      projectstatus: [],
      //表头
      columns: [],
      //列设置
      settingColumns: [],
      // 列定义
      defColumns: [
        {
          title: '项目名称',
          align: 'left',
          dataIndex: 'projectname',
          width: 300
          //scopedSlots: { customRender: 'projectname' }
        },
        {
          title: '任务名称',
          align: 'center',
          dataIndex: 'taskname',
          width: 300
        },
        {
          title: '负责人',
          align: 'center',
          width: 200,
          dataIndex: 'principal',
          customRender: text => {
            //字典值替换通用方法
            return filterMultiDictText(this.principal, text)
          }
        },
        {
          title: '参与人',
          align: 'center',
          width: 260,
          dataIndex: 'participant',
          customRender: text => {
            //字典值替换通用方法
            return filterMultiDictText(this.participant, text)
          }
        },
        {
          title: '总进度',
          align: 'center',
          dataIndex: 'schedule',
          width: 160,
          scopedSlots: { customRender: 'schedule' }
        },
        {
          title: '项目状态',
          align: 'center',
          width: 150,
          dataIndex: 'status',
          // scopedSlots: { customRender: 'status' },
          customRender: text => {
            //字典值替换通用方法
            return filterDictText(this.projectstatus, text)
          }
        },
        {
          title: '项目分类',
          align: 'center',
          width: 150,
          dataIndex: 'projecttype',
          customRender: text => {
            //字典值替换通用方法
            return filterDictText(this.projectTypeDictOptions, text)
          }
        },
        {
          title: '是否禁用',
          align: 'center',
          width: 100,
          dataIndex: 'isdelete',
          scopedSlots: { customRender: 'isdelete' }
        },
        {
          title: '起始日期',
          align: 'center',
          width: 150,
          dataIndex: 'startdate',
          customRender: function(text) {
            return !text ? '' : text.length > 10 ? text.substr(0, 10) : text
          }
        },
        {
          title: '结束日期',
          align: 'center',
          width: 150,
          dataIndex: 'enddate',
          customRender: function(text) {
            return !text ? '' : text.length > 10 ? text.substr(0, 10) : text
          }
        },
        {
          title: '完成日期',
          align: 'center',
          width: 150,
          dataIndex: 'completiontime',
          customRender: function(text) {
            return !text ? '' : text.length > 10 ? text.substr(0, 10) : text
          }
        },

        {
          title: '备注',
          align: 'center',
          width: 200,
          dataIndex: 'remark',
          scopedSlots: { customRender: 'htmlremark' }
        },
        {
          title: '创建人',
          align: 'center',
          width: 100,
          dataIndex: 'createBy',
          customRender: text => {
            //字典值替换通用方法
            return filterMultiDictText(this.createBys, text)
          }
        },
        {
          title: '创建日期',
          align: 'center',
          width: 150,
          dataIndex: 'createTime',
          customRender: function(text) {
            return !text ? '' : text.length > 10 ? text.substr(0, 10) : text
          }
        },
        {
          title: '更新人',
          align: 'center',
          width: 100,
          dataIndex: 'updateBy',
          customRender: text => {
            //字典值替换通用方法
            return filterMultiDictText(this.updateBy, text)
          }
        },
        {
          title: '更新日期',
          align: 'center',
          width: 150,
          dataIndex: 'updateTime',
          customRender: function(text) {
            return !text ? '' : text.length > 10 ? text.substr(0, 10) : text
          }
        },
        {
          title: '所属部门',
          align: 'center',
          width: 200,
          dataIndex: 'sysOrgCode',
          customRender: text => {
            //字典值替换通用方法
            return filterMultiDictText(this.sysOrgCodes, text)
          }
        },
        {
          title: '头像',
          align: 'center',
          width: 100,
          dataIndex: 'photo',
          scopedSlots: { customRender: 'photo' }
        },

        {
          title: '内容',
          align: 'center',
          width: 230,
          dataIndex: 'projectcontent',
          scopedSlots: { customRender: 'htmlSlot' }
        },

        /*         {
          title: '紧急程度',
          align: 'center',
          width: 100,
          dataIndex: 'emergencylevel',
          customRender: text => {
            return filterDictText(this.dictOptions, text)
          }
        }, */

        {
          title: '附件',
          align: 'center',
          width: 100,
          dataIndex: 'annex',
          scopedSlots: { customRender: 'fileSlot' }
        }

        /* {
          title: '操作',
          dataIndex: 'action',
          width: 100,
          align: 'center',
          fixed: 'right',
          scopedSlots: { customRender: 'action' }
        } */
      ],
      url: {
        list: '/protree/pmpProjectXLF/rootLists',
        childList: '/protree/pmpProjectXLF/childList',
        delete: '/protree/pmpProject/delete',
        deleteBatch: '/protree/pmpProject/deleteBatch',
        exportXlsUrl: '/protree/pmpProject/exportXls',
        importExcelUrl: 'protree/pmpProject/importExcel'
      },
      expandedRowKeys: [],
      hasChildrenField: 'haschild',
      pidField: 'parentnode',
      dictOptions: {}
    }
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
    //剔除html标签
    rmHtmlLabel(str) {
      return str.replace(/<[^>]+>/g, '')
    },
    //文本限长
    subText(str) {
      let len = 8
      if (str.length > len) return str.substring(0, len) + '...'
      else return str
    },
    //列设置更改事件
    onColSettingsChange(checkedValues) {
      var key = this.$route.name + ':colsettings'
      Vue.ls.set(key, checkedValues, 7 * 24 * 60 * 60 * 1000)
      this.settingColumns = checkedValues
      const cols = this.defColumns.filter(item => {
        if (item.key == 'rowIndex' || item.dataIndex == 'action') {
          return true
        }
        if (this.settingColumns.includes(item.dataIndex)) {
          return true
        }
        return false
      })
      this.columns = cols
    },
    loadData(arg) {
      if (arg == 1) {
        this.ipagination.current = 1
      }
      this.loading = true
      this.expandedRowKeys = []
      let params = this.getQueryParams()
      return new Promise(resolve => {
        getAction(this.url.list, params).then(res => {
          if (res.success) {
            let result = res.result
            if (Number(result.total) > 0) {
              this.ipagination.total = Number(result.total)
              if (res.message == '1') {
                this.dataSource = this.getDataByResult(res.result.records)
              } else {
                this.dataSource = res.result.records
              }
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
        this.loadData()
      } else {
        this.expandedRowKeys = []
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
    },
    initColumns() {
      //权限过滤（列权限控制时打开，修改第二个参数为授权码前缀）
      //this.defColumns = colAuthFilter(this.defColumns,'testdemo:');
      var key = this.$route.name + ':colsettings'
      let colSettings = Vue.ls.get(key)
      if (colSettings == null || colSettings == undefined) {
        let allSettingColumns = []
        this.defColumns.forEach(function(item, i, array) {
          allSettingColumns.push(item.dataIndex)
        })
        this.settingColumns = allSettingColumns
        this.columns = this.defColumns
      } else {
        this.settingColumns = colSettings
        const cols = this.defColumns.filter(item => {
          if (item.key == 'rowIndex' || item.dataIndex == 'action') {
            return true
          }
          if (colSettings.includes(item.dataIndex)) {
            return true
          }
          return false
        })
        this.columns = cols
      }
    }
  },
  created() {
    this.initColumns()
    //初始化字典 - 项目类型
    initDictOptions('project_type').then(res => {
      if (res.success) {
        this.projectTypeDictOptions = res.result
      }
    }),
      //初始化字典 - 项目状态
      initDictOptions('project_status').then(res => {
        if (res.success) {
          this.projectstatus = res.result
        }
      }),
      //初始化字典 - 创建人
      initDictOptions('sys_user,realname,username').then(res => {
        if (res.success) {
          this.createBys = res.result
          this.updateBy = res.result
          this.principal = res.result
          this.participant = res.result
        }
      }),
      //初始化字典 - 所属部门
      initDictOptions('sys_depart,depart_name,org_code').then(res => {
        if (res.success) {
          this.sysOrgCodes = res.result
        }
      }),
      //初始化字典 - 紧急程度
      initDictOptions('urgent_level').then(res => {
        if (res.success) {
          this.dictOptions = res.result
        }
      })
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';

.inputnum {
  width: 100%;
}
</style>