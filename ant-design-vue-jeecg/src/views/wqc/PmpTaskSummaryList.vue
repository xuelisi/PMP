<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">

          <!--<a-col :md="6" :sm="12">-->
            <!--<a-form-item label="项目id">-->
              <!--<j-input placeholder="请输入项目id查询" v-model="queryParam.projectid"></j-input>-->
            <!--</a-form-item>-->
          <!--</a-col>-->

          <!--<a-col :md="6" :sm="8">-->
            <!--<span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">-->
              <!--<a-button type="primary" @click="searchQuery" icon="search">查询</a-button>-->
              <!--<a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>-->
            <!--</span>-->
          <!--</a-col>-->

        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->
    
    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="summaryAdd" type="primary" icon="plus">新增</a-button>
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
          <j-ellipsis :value="text" v-html="text" :length="1"/>
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

  import JInput from '@/components/jeecg/JInput'
  import JEllipsis from '@/components/jeecg/JEllipsis'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import PmpTaskSummaryModal from './modules/PmpTaskSummaryModal'
  import { deleteAction, getAction,downFile } from '@/api/manage'

  const tableHeaders = [
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
      title:'任务小结',
      align:"center",
      dataIndex: 'content',
      scopedSlots: {customRender: 'htmlSlot'}
    },
    {
      title:'提交时间',
      align:"center",
      dataIndex: 'createTime'
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
      JInput,
      JEllipsis,
      PmpTaskSummaryModal
    },
    data () {
      return {
        description: '任务小结管理页面',
        // 表头
        columns: tableHeaders,
        url: {
          info: "/summary/pmpTaskSummary/info",
          list: "/summary/pmpTaskSummary/list",
          delete: "/summary/pmpTaskSummary/delete",
          deleteBatch: "/summary/pmpTaskSummary/deleteBatch",
          exportXlsUrl: "/summary/pmpTaskSummary/exportXls",
          importExcelUrl: "summary/pmpTaskSummary/importExcel",
        },
        dictOptions:{
        },
        taskid: '',
        taskname: '',
        projectid: '',
        projectname: '',
        jellipsis: {
          value: '这是一串很长很长的文字段落。这是一串很长很长的文字段落。这是一串很长很长的文字段落。这是一串很长很长的文字段落。'
        }
      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    beforeCreate: function(){
    },
    created() {
      this.queryParam.projectid = this.$route.params.taskid;
      if(!this.disableMixinCreated){
        console.log(' -- mixin created -- ')
        this.loadData();
        //初始化字典配置 在自己页面定义
        this.initDictConfig();
      }

      getAction(this.url.info, { projectid: this.queryParam.projectid }).then((res) => {
        if (res.success) {
          this.taskid = res.result.taskId;
          this.taskname = res.result.taskName;
          this.projectid = res.result.projectId;
          this.projectname = res.result.projectName;
        }

        if(res.code===510){
          this.$message.warning(res.message);
        }
      })
    },
    methods: {
      summaryAdd() {
        this.$refs.modalForm.title = "新增";
        this.$refs.modalForm.disableSubmit = false;
        this.$refs.modalForm.edit({
          taskId: this.taskid,
          projectId: this.projectId,
          taskName: this.taskname,
          projectName: this.projectname,
          });
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>