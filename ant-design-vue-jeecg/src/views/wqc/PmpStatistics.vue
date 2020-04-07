<template>
    <a-card :bordered="false">

      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">

            <a-col :md="4" :sm="8">
              <a-form-item label="年月">
                <a-month-picker
                  @change="onChange"
                  :format="monthFormat"
                  v-model="monthValue"
                  placeholder="请选择年月" />
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

      <div>
        <a-table
          bordered
          ref="table"
          size="middle"
          :loading="loading"
          :columns="columns"
          :dataSource="dataSource"
          :pagination="ipagination"
          @change="handleTableChange">

          <template
            slot="summaryStateSlot"
            slot-scope="text, record">

            <a slot="actions" @click="summaryDetails(text, record)">
              <a-icon v-if="null != text" type="check-circle" />
            </a>

          </template>

        </a-table>
      </div>

      <div class="footer-tip">
        <span>说明：<a-icon type="check-circle" />有日报</span>
      </div>

      <pmp-statistics-modal ref="modalForm" @ok="modalFormOk"></pmp-statistics-modal>
    </a-card>
</template>

<script>

  import moment from 'moment';
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import { deleteAction, getAction, putAction, downFile } from '@/api/manage'
  import PmpStatisticsModal from './modules/PmpStatisticsModal'

  const allCols = [
    {
      title:'用户名',
      align:"center",
      dataIndex: 'realName',
    },
    {
      title:'01',
      align:"center",
      dataIndex: 'first',
      scopedSlots: {customRender: 'summaryStateSlot'},
    },
    {
      title:'02',
      align:"center",
      dataIndex: 'second',
      scopedSlots: {customRender: 'summaryStateSlot'}
    },
    {
      title:'03',
      align:"center",
      dataIndex: 'third',
      scopedSlots: {customRender: 'summaryStateSlot'}
    },
    {
      title:'04',
      align:"center",
      dataIndex: 'fourth',
      scopedSlots: {customRender: 'summaryStateSlot'}
    },
    {
      title:'05',
      align:"center",
      dataIndex: 'fifth',
      scopedSlots: {customRender: 'summaryStateSlot'}
    },
    {
      title:'06',
      align:"center",
      dataIndex: 'sixth',
      scopedSlots: {customRender: 'summaryStateSlot'}
    },
    {
      title:'07',
      align:"center",
      dataIndex: 'seventh',
      scopedSlots: {customRender: 'summaryStateSlot'}
    },
    {
      title:'08',
      align:"center",
      dataIndex: 'eighth',
      scopedSlots: {customRender: 'summaryStateSlot'}
    },
    {
      title:'09',
      align:"center",
      dataIndex: 'ninth',
      scopedSlots: {customRender: 'summaryStateSlot'}
    },
    {
      title:'10',
      align:"center",
      dataIndex: 'tenth',
      scopedSlots: {customRender: 'summaryStateSlot'}
    },
    {
      title:'11',
      align:"center",
      dataIndex: 'eleventh',
      scopedSlots: {customRender: 'summaryStateSlot'}
    },
    {
      title:'12',
      align:"center",
      dataIndex: 'twelfth',
      scopedSlots: {customRender: 'summaryStateSlot'}
    },
    {
      title:'13',
      align:"center",
      dataIndex: 'thirteenth',
      scopedSlots: {customRender: 'summaryStateSlot'}
    },
    {
      title:'14',
      align:"center",
      dataIndex: 'fourteenth',
      scopedSlots: {customRender: 'summaryStateSlot'}
    },
    {
      title:'15',
      align:"center",
      dataIndex: 'fifteenth',
      scopedSlots: {customRender: 'summaryStateSlot'}
    },
    {
      title:'16',
      align:"center",
      dataIndex: 'sixteenth',
      scopedSlots: {customRender: 'summaryStateSlot'}
    },
    {
      title:'17',
      align:"center",
      dataIndex: 'seventeenth',
      scopedSlots: {customRender: 'summaryStateSlot'}
    },
    {
      title:'18',
      align:"center",
      dataIndex: 'eighteenth',
      scopedSlots: {customRender: 'summaryStateSlot'}
    },
    {
      title:'19',
      align:"center",
      dataIndex: 'nineteenth',
      scopedSlots: {customRender: 'summaryStateSlot'}
    },
    {
      title:'20',
      align:"center",
      dataIndex: 'twentieth',
      scopedSlots: {customRender: 'summaryStateSlot'},
    },
    {
      title:'21',
      align:"center",
      dataIndex: 'twentyFirst',
      scopedSlots: {customRender: 'summaryStateSlot'}
    },
    {
      title:'22',
      align:"center",
      dataIndex: 'twentySecond',
      scopedSlots: {customRender: 'summaryStateSlot'}
    },
    {
      title:'23',
      align:"center",
      dataIndex: 'twentyThird',
      scopedSlots: {customRender: 'summaryStateSlot'}
    },
    {
      title:'24',
      align:"center",
      dataIndex: 'twentyFourth',
      scopedSlots: {customRender: 'summaryStateSlot'}
    },
    {
      title:'25',
      align:"center",
      dataIndex: 'twentyFifth',
      scopedSlots: {customRender: 'summaryStateSlot'}
    },
    {
      title:'26',
      align:"center",
      dataIndex: 'twentySixth',
      scopedSlots: {customRender: 'summaryStateSlot'}
    },
    {
      title:'27',
      align:"center",
      dataIndex: 'twentySeventh',
      scopedSlots: {customRender: 'summaryStateSlot'}
    },
    {
      title:'28',
      align:"center",
      dataIndex: 'twentyEighth',
      scopedSlots: {customRender: 'summaryStateSlot'}
    },
    {
      title:'29',
      align:"center",
      dataIndex: 'twentyNinth',
      scopedSlots: {customRender: 'summaryStateSlot'}
    },
    {
      title:'30',
      align:"center",
      dataIndex: 'twentyTenth',
      scopedSlots: {customRender: 'summaryStateSlot'}
    },
    {
      title:'31',
      align:"center",
      dataIndex: 'thirtyFirst',
      scopedSlots: {customRender: 'summaryStateSlot'}
    }
  ]

  export default {
    mixins:[JeecgListMixin],
    components: {
      PmpStatisticsModal
    },
    data() {
      return {
        columns: [],
        allCols: allCols,
        monthFormat: 'YYYY-MM',
        queryParam: {summaryTime: ''},
        monthValue: moment(this.date),
        url: {
          list: "/summary/pmpSummary/statistics",
        },
      }
    },
    mounted() {
      this.searchReset();
    },
    methods: {
      //初始化年月数据
      initMonth() {
        this.monthValue = moment(this.date);
      },
      //初始化表格数据
      initColumns() {
        let dt = this.monthValue.format("YYYY-MM");
        this.columns = this.allCols.slice(0, this.getMonthCount(dt) + 1);
      },
      //年月切换
      onChange(date, dateString) {
        if (dateString.length <= 0) {
          this.initMonth();
        }
      },
      //查询
      searchQuery() {
        this.queryParam.summaryTime = this.monthValue.format("YYYY-MM");

        this.initColumns();
        this.loadData(1);
      },
      //重置
      searchReset() {
        this.initMonth();

        this.searchQuery();
      },
      //获取年月对应天数
      getMonthCount(str) {
        return new Date(str.substr(0, 4), str.substr(5, 2), 0).getDate();
      },
      summaryDetails(time, record) {
        this.$refs.modalForm.show(time, record);
      }
    }
  };

</script>

<style scoped>
  .cc-month ul li {
    margin: 0;
    paddint: 0;
    width: 8.3%;
    float: left;
    list-style: none;
    text-align: center;
    height: 50px;
    line-height: 50px;
  }

  .cc-month ul li:hover {
    cursor: pointer;
    background: lightgray;
  }
</style>