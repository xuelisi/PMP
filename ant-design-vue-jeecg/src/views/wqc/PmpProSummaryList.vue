<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">

          <a-col :md="6" :sm="8">
            <a-form-item label="项目名称">
              <a-input placeholder="请输入项目名称" v-model="queryParam.projectName"></a-input>
            </a-form-item>
          </a-col>

          <a-col :md="6" :sm="8">
            <a-form-item label="任务名称">
              <a-input placeholder="请输入任务名称" v-model="queryParam.taskName"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8" >
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
          </a-col>

        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->
    
    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('任务小结')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
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
        
        @change="handleTableChange">

        <template slot="htmlSlot" slot-scope="text">
          <div slot="actions">
            <a-popover>
              <template slot="content">
                <p style="max-width: 500px;">{{ rmHtmlLabel(text) }}</p>
              </template>
              <span>{{ subText(rmHtmlLabel(text)) }}</span>
            </a-popover>
          </div>

        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无此图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt="图片不存在" style="max-width:80px;font-size: 12px;font-style: italic;"/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无此文件</span>
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="uploadFile(text)">
            下载
          </a-button>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <pmpTaskSummary-modal ref="modalForm" @ok="modalFormOk"></pmpTaskSummary-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import PmpTaskSummaryModal from './modules/PmpProSummaryModal'

  const tableHeaders = [
      {
        title: '#',
        dataIndex: '',
        key:'rowIndex',
        width:60,
        align:"center",
        customRender:function (t,r,index) {
          return parseInt(index)+1;
        }
      },
    {
      title:'项目名称',
      align:"center",
      dataIndex: 'projectName'
    },
      {
        title:'任务名称',
        align:"center",
        dataIndex: 'taskName'
      },
      {
        title:'小结内容',
        align:"center",
        dataIndex: 'content',
        scopedSlots: {customRender: 'htmlSlot'}
      },
      {
        title:'提交时间',
        align:"center",
        dataIndex: 'createTime',
      },
      {
        title: '操作',
        dataIndex: 'action',
        align:"center",
        scopedSlots: { customRender: 'action' }
      }
    ]

  export default {
    name: "PmpTaskSummaryList",
    mixins:[JeecgListMixin],
    components: {
      PmpTaskSummaryModal
    },
    data () {
      return {
        description: '任务小结管理页面',
        // 表头
        columns: tableHeaders,
        url: {
          list: "/summary/pmpTaskSummary/list",
          delete: "/summary/pmpTaskSummary/delete",
          deleteBatch: "/summary/pmpTaskSummary/deleteBatch",
          exportXlsUrl: "/summary/pmpTaskSummary/exportXls",
          importExcelUrl: "summary/pmpTaskSummary/importExcel",
        },
        dictOptions:{
        },
      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
      initDictConfig(){
      },
      //剔除html标签
      rmHtmlLabel(str) {
        return str.replace(/<[^>]+>/g, '');
      },
      //文本限长
      subText(str) {
        let len = 10;
        if (str.length > len)
          return str.substring(0, len) + '...';
        else
          return str;
      },
       
    }
  }
</script>

<style scoped>
  @import '~@assets/less/common.less'

</style>