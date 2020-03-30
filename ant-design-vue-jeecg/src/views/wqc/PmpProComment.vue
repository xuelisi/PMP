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
          </a-col>+
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

    <!-- table区域-begin -->
    <div>
      <a-table
        bordered
        rowKey="id"
        :loading="loading"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        @change="handleTableChange">

        <template slot="ellipsisSlot" slot-scope="text">
          <j-ellipsis :value="text"/>
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

  import JEllipsis from '@/components/jeecg/JEllipsis'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import { initDictOptions, filterDictText, filterMultiDictText } from '@/components/dict/JDictSelectUtil'

  import PmpProCommentModal from './modules/PmpProCommentModal'

  const columns = [
    {
      title: '#',
      dataIndex: '',
      key:'rowIndex',
      width:60,
      align:"center",
      customRender: function (t, r, index) {
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
      scopedSlots: {customRender: 'ellipsisSlot'}
    },
    {
      title:'评论人员',
      align:"center",
      dataIndex: 'realName',
    },
    {
      title:'评论时间',
      align:"center",
      dataIndex: 'createTime',
    },
    {
      title: '操作',
      align:"center",
      dataIndex: 'action',
      scopedSlots: { customRender: 'action' }
    }
  ]

  export default {
    name: "PmpProComment",
    mixins:[JeecgListMixin, JEllipsis],
    components: {
      JEllipsis,
      PmpProCommentModal
    },
    data () {
      return {
        columns,
        url: {
          list: "/summary/pmpComment/list",
        },
      }
    },
    methods: {
      details(record) {
        this.$refs.modalForm.show(record);
      },
    }
  }
</script>

<style>
</style>