<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <!-- <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-dropdown>
              <a-menu slot="overlay" @click="handleMenuClick">
                <a-menu-item key="1">负责的项目</a-menu-item>
                <a-menu-item key="2">参与的项目</a-menu-item>
                <a-menu-item key="3">创建的项目</a-menu-item>
              </a-menu>
              <a-button style="margin-left: 8px">
                项目分类
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
    </div>-->
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-dropdown>
        <a-menu slot="overlay" @click="handleMenuClick">
          <a-menu-item key="1">负责的项目</a-menu-item>
          <a-menu-item key="2">参与的项目</a-menu-item>
          <a-menu-item key="3">创建的项目</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px">
          项目分类
          <a-icon type="down" />
        </a-button>
      </a-dropdown>
      <a-dropdown-button @click="handleAdd">
        <a-icon type="plus" />添加项目
        <a-menu slot="overlay">
          <a-menu-item key="1">
            <a-upload
              name="file"
              :showUploadList="false"
              :multiple="false"
              :headers="tokenHeader"
              :action="importExcelUrl"
              @change="handleImportExcel"
            >
              <a-icon type="import" />导入
            </a-upload>
          </a-menu-item>
          <a-menu-item key="2">
            <a-icon type="download" @click="handleExportXls('项目主表')" />导出
          </a-menu-item>
        </a-menu>
      </a-dropdown-button>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="3" @click="batchDel">
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
        <div
          slot="filterDropdown"
          slot-scope="{ setSelectedKeys, selectedKeys, confirm, clearFilters }"
          style="padding: 8px"
        >
          <a-input
            v-ant-ref="c => searchInput = c"
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

        <template slot="projectname" slot-scope="text,index">
          <a href="javascript:;" @click="redictHref(index)">{{text}}</a>
        </template>
        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
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
        <span slot="isdelete" slot-scope="text">
          <a-tag :color="text==1 ? 'volcano' : 'green'">{{ text == 0 ? '正常':'禁用'}}</a-tag>
        </span>
        <span slot="action" slot-scope="text, record">
          <a @click="myHandleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">
              更多
              <a-icon type="down" />
            </a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a href="javascript:;" @click="handleDetail(record)">详情</a>
              </a-menu-item>
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

    <pmpProjectManage-modal ref="modalForm" @ok="modalFormOk"></pmpProjectManage-modal>
  </a-card>
</template>

<script>
import { deleteAction, getAction, putAction, downFile } from '@/api/manage'
import { filterObj, isContainPrincipal } from '@/utils/util'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import PmpProjectManageModal from './modules/PmpProjectManageModal'
import { initDictOptions, filterDictText, filterMultiDictText } from '@/components/dict/JDictSelectUtil'

export default {
  name: 'PmpProjectManageList',
  mixins: [JeecgListMixin],
  components: {
    PmpProjectManageModal
  },
  data() {
    return {
      description: '项目主表管理页面',
      projectTypeDictOptions: [],
      principalDictOptions: [],
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
          title: '头像',
          align: 'center',
          dataIndex: 'photo',
          scopedSlots: { customRender: 'photo' }
        },
        {
          title: '项目名称',
          align: 'center',
          dataIndex: 'projectname',
          scopedSlots: {
            filterDropdown: 'filterDropdown',
            filterIcon: 'filterIcon',
            customRender: 'customRender',
            customRender: 'projectname'
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

          /* scopedSlots: { customRender: 'projectname' } */
        },
        {
          title: '负责人',
          align: 'center',
          dataIndex: 'principal',
          customRender: text => {
            //字典值替换通用方法
            return filterMultiDictText(this.principalDictOptions, text)
          },
          scopedSlots: {
            filterDropdown: 'filterDropdown',
            filterIcon: 'filterIcon',
            customRender: 'customRender'
          },
          onFilter: (value, record) =>
            record.principal
              .toString()
              .toLowerCase()
              .includes(value.toLowerCase()),
          onFilterDropdownVisibleChange: visible => {
            if (visible) {
              setTimeout(() => {
                this.searchInput.focus()
              })
            }
          }
        },
        {
          title: '总进度',
          align: 'center',
          dataIndex: 'schedule',
          customRender: function(text) {
            return text + '%'
          }
        },
        {
          title: '项目分类',
          align: 'center',
          dataIndex: 'projecttype',
          customRender: text => {
            //字典值替换通用方法
            return filterDictText(this.projectTypeDictOptions, text)
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
        list: '/protree/pmpProject/myProList',
        list1: '/protree/pmpProject/myProListByPar',
        list2: '/protree/pmpProject/myProListByCreateBy',
        delete: '/protree/pmpProject/delete',
        disable: '/protree/pmpProject/disable',
        enable: '/protree/pmpProject/enable',
        deleteBatch: '/protree/pmpProject/deleteBatch',
        exportXlsUrl: 'protree/pmpProject/exportXls',
        importExcelUrl: 'protree/pmpProject/importExcel'
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
    handleSearch(selectedKeys, confirm) {
      confirm()
      this.searchText = selectedKeys[0]
    },

    handleReset(clearFilters) {
      clearFilters()
      this.searchText = ''
    },
    searchQuery() {
      this.loadData(1, this.url.list)
    },
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
          this.principalDictOptions = res.result
        }
      }),
        //初始化字典 - 项目类型
        initDictOptions('project_type').then(res => {
          if (res.success) {
            this.projectTypeDictOptions = res.result
          }
        })
    },
    loadData(arg = 1, url = this.url.list) {
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
    openNotification(title, des) {
      this.$notification.open({
        message: title,
        description: des,
        icon: <a-icon type="frown" style="color: red" />
      })
    },
    redictHref(index) {
      if (index.isdelete == '0') {
        this.$router.push({ path: '/jgzhu/project/PmpTaskList', query: { data: index } })
      } else {
        this.openNotification('提示', '已禁用,无法查看！')
      }
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>