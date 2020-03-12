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
          scopedSlots: { customRender: 'projectname' }
        },
        {
          title: '负责人',
          align: 'center',
          dataIndex: 'principal'
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
        list: '/protree/pmpProject/list',
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
    initDictConfig() {
      //初始化字典 - 项目类型
      initDictOptions('project_type').then(res => {
        if (res.success) {
          this.projectTypeDictOptions = res.result
        }
      })
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
        this.$router.push({ name: 'jgzhu-PmpTaskList', query: { data: index } })
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