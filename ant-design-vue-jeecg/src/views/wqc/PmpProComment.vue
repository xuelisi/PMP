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
      <a-button type="primary" icon="download" @click="handleExportXls('任务批阅')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <!--<a-dropdown v-if="selectedRowKeys.length > 0">-->
        <!--<a-menu slot="overlay">-->
          <!--<a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>-->
        <!--</a-menu>-->
        <!--<a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>-->
      <!--</a-dropdown>-->
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
          <a @click="details(record)">查看</a>
        </span>

      </a-table>
    </div>

    <pmpProComment-modal ref="modalForm" @ok="modalFormOk"></pmpProComment-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import PmpProCommentModal from './modules/PmpProCommentModal'
  import { initDictOptions, filterDictText, filterMultiDictText } from '@/components/dict/JDictSelectUtil'

  export default {
    name: "PmpCommentList",
    mixins:[JeecgListMixin],
    components: {
      PmpProCommentModal
    },
    data () {
      return {
        description: '任务批阅管理页面',
        principalDictOptions: [],
        // 表头
        columns: [
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
            title:'评论内容',
            align:"center",
            dataIndex: 'content',
            scopedSlots: {customRender: 'htmlSlot'}
          },
          {
            title:'评论时间',
            align:"center",
            dataIndex: 'createTime',
          },
          {
            title:'评论人员',
            align:"center",
            dataIndex: 'realName',
            // customRender: text => {
            //   //字典值替换通用方法
            //   return filterMultiDictText(this.principalDictOptions, text)
            // },
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: "/summary/pmpComment/list",
          delete: "/summary/pmpComment/delete",
          deleteBatch: "/summary/pmpComment/deleteBatch",
          exportXlsUrl: "/summary/pmpComment/exportXls",
          importExcelUrl: "summary/pmpComment/importExcel",
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
      initDictConfig() {
        //初始化字典 - 项目状态
        initDictOptions('sys_user,realname,username').then(res => {
          if (res.success) {
            this.principalDictOptions = res.result
          }
        })
      },
      details(record) {
        this.$refs.modalForm.edit(record);
        this.$refs.modalForm.title = "详情";
        this.$refs.modalForm.disableSubmit = false;
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