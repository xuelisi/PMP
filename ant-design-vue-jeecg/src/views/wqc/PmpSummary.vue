<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="起止日期">
              <a-range-picker
                v-model="rangeDate"
                @change="onChange" />
            </a-form-item>
          </a-col>

          <template v-if="toggleSearchStatus">
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
          </template>

          <a-col :md="6" :sm="8">
            <span style="float: left; overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>
          </a-col>

        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->
    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
    </div>

    <!-- table区域-begin -->
    <div>
      <a-table
        bordered
        rowKey="id"
        :columns="columns"
        :loading="loading"
        :dataSource="dataSource"
        :pagination="ipagination"
        @change="handleTableChange">


        <template slot="ellipsisSlot" slot-scope="text">
          <j-ellipsis :value="rmHtmlLabel(text)"></j-ellipsis>
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

    <pmp-summary-modal ref="modalForm" @ok="modalFormOk"></pmp-summary-modal>
  </a-card>
</template>

<script>

  import moment from 'moment';
  import JEllipsis from '@/components/jeecg/JEllipsis'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import PmpSummaryModal from './modules/PmpSummaryModal'

  const columns = [
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
        dataIndex: 'projectName',
      },
      {
        title:'任务名称',
        align:"center",
        dataIndex: 'taskName',
      },
      {
        title:'小结内容',
        align:"center",
        dataIndex: 'content',
        // ellipsis: true,
        scopedSlots: {customRender: 'ellipsisSlot'}
      },
      {
        title:'小结日期',
        align:"center",
        dataIndex: 'summaryTime',
      },
      {
        title:'填报时间',
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
    name: "PmpSummary",
    mixins:[JeecgListMixin, JEllipsis],
    components: {
      JEllipsis,
      PmpSummaryModal
    },
    mounted() {
      this.searchReset();
    },
    data () {
      return {
        columns,
        queryParam: {taskName: '', projectName: ''},
        rangeDate: [moment(this.date), moment(this.date)],
        url: {
          list: "/summary/pmpSummary/list",
          add: "/summary/pmpSummary/add",
          delete: "/summary/pmpSummary/delete",
        },
      }
    },
    methods: {
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
      initRangeDate() {
        this.rangeDate = [moment(this.getBeforeDate(30)), moment(this.date)];
      },
      getBeforeDate(count) {
        let newDate = new Date();
        return newDate.setDate(newDate.getDate() - count);
      },
      onChange(date, dateString) {
        if (dateString[0].length < 2) {
          this.initRangeDate();
        }
      },
      searchQuery() {
        this.queryParam.begDate = this.rangeDate[0].format("YYYY-MM-DD");
        this.queryParam.endDate = this.rangeDate[1].format("YYYY-MM-DD");

        this.loadData(1);
      },
      searchReset() {
        this.initRangeDate();

        this.queryParam.taskName = '';
        this.queryParam.projectName = '';

        this.searchQuery();
      },
    }
  }
</script>

<style>
</style>