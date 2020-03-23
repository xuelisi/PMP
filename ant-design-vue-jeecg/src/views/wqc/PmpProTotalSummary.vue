<template>
    <a-card :bordered="false">

      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">

            <a-col :md="6" :sm="8">
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
            ref="table"
            size="middle"
            bordered
            :columns="columns"
            :dataSource="dataSource"
            :pagination="ipagination"
            :loading="loading"
            @change="handleTableChange">

            <template slot="checkSlot" slot-scope="text">
              <div slot="actions">
                <a-icon v-if="0 != text" type="check-circle" />
              </div>
            </template>

          </a-table>
      </div>

    </a-card>
</template>

<script>

  import moment from 'moment';
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import { deleteAction, getAction, putAction, downFile } from '@/api/manage'
  import PmpSummaryTimelineModal from '@views/wqc/modules/PmpSummaryTimelineModal'

  const tableHeaders = [
    {
      title:'用户名',
      align:"center",
      dataIndex: 'realName',
    },
    {
      title:'01',
      align:"center",
      dataIndex: 'first',
      scopedSlots: {customRender: 'checkSlot'}
    },
    {
      title:'02',
      align:"center",
      dataIndex: 'second',
      scopedSlots: {customRender: 'checkSlot'}
    },
    {
      title:'03',
      align:"center",
      dataIndex: 'third',
      scopedSlots: {customRender: 'checkSlot'}
    },
    {
      title:'04',
      align:"center",
      dataIndex: 'fourth',
      scopedSlots: {customRender: 'checkSlot'}
    },
    {
      title:'05',
      align:"center",
      dataIndex: 'fifth',
      scopedSlots: {customRender: 'checkSlot'}
    },
    {
      title:'06',
      align:"center",
      dataIndex: 'sixth',
      scopedSlots: {customRender: 'checkSlot'}
    },
    {
      title:'07',
      align:"center",
      dataIndex: 'seventh',
      scopedSlots: {customRender: 'checkSlot'}
    },
    {
      title:'08',
      align:"center",
      dataIndex: 'eighth',
      scopedSlots: {customRender: 'checkSlot'}
    },
    {
      title:'09',
      align:"center",
      dataIndex: 'ninth',
      scopedSlots: {customRender: 'checkSlot'}
    },
    {
      title:'10',
      align:"center",
      dataIndex: 'tenth',
      scopedSlots: {customRender: 'checkSlot'}
    },
    {
      title:'11',
      align:"center",
      dataIndex: 'eleventh',
      scopedSlots: {customRender: 'checkSlot'}
    },
    {
      title:'12',
      align:"center",
      dataIndex: 'twelfth',
      scopedSlots: {customRender: 'checkSlot'}
    },
    {
      title:'13',
      align:"center",
      dataIndex: 'thirteenth',
      scopedSlots: {customRender: 'checkSlot'}
    },
    {
      title:'14',
      align:"center",
      dataIndex: 'fourteenth',
      scopedSlots: {customRender: 'checkSlot'}
    },
    {
      title:'15',
      align:"center",
      dataIndex: 'fifteenth',
      scopedSlots: {customRender: 'checkSlot'}
    },
    {
      title:'16',
      align:"center",
      dataIndex: 'sixteenth',
      scopedSlots: {customRender: 'checkSlot'}
    },
    {
      title:'17',
      align:"center",
      dataIndex: 'seventeenth',
      scopedSlots: {customRender: 'checkSlot'}
    },
    {
      title:'18',
      align:"center",
      dataIndex: 'eighteenth',
      scopedSlots: {customRender: 'checkSlot'}
    },
    {
      title:'19',
      align:"center",
      dataIndex: 'nineteenth',
      scopedSlots: {customRender: 'checkSlot'}
    },
    {
      title:'20',
      align:"center",
      dataIndex: 'twentieth',
      scopedSlots: {customRender: 'checkSlot'}
    },
    {
      title:'21',
      align:"center",
      dataIndex: 'twentyFirst',
      scopedSlots: {customRender: 'checkSlot'}
    },
    {
      title:'22',
      align:"center",
      dataIndex: 'twentySecond',
      scopedSlots: {customRender: 'checkSlot'}
    },
    {
      title:'23',
      align:"center",
      dataIndex: 'twentyThird',
      scopedSlots: {customRender: 'checkSlot'}
    },
    {
      title:'24',
      align:"center",
      dataIndex: 'twentyFourth',
      scopedSlots: {customRender: 'checkSlot'}
    },
    {
      title:'25',
      align:"center",
      dataIndex: 'twentyFifth',
      scopedSlots: {customRender: 'checkSlot'}
    },
    {
      title:'26',
      align:"center",
      dataIndex: 'twentySixth',
      scopedSlots: {customRender: 'checkSlot'}
    },
    {
      title:'27',
      align:"center",
      dataIndex: 'twentySeventh',
      scopedSlots: {customRender: 'checkSlot'}
    },
    {
      title:'28',
      align:"center",
      dataIndex: 'twentyEighth',
      scopedSlots: {customRender: 'checkSlot'}
    },
    {
      title:'29',
      align:"center",
      dataIndex: 'twentyNinth',
      scopedSlots: {customRender: 'checkSlot'}
    },
    {
      title:'30',
      align:"center",
      dataIndex: 'twentyTenth',
      scopedSlots: {customRender: 'checkSlot'}
    },
    {
      title:'31',
      align:"center",
      dataIndex: 'thirtyFirst',
      scopedSlots: {customRender: 'checkSlot'}
    }
  ]

  export default {
    mixins:[JeecgListMixin],
    components: {
      PmpSummaryTimelineModal
    },
    data() {
      return {
        columns: tableHeaders,
        monthFormat: 'YYYY-MM',
        monthValue: '',
        url: {
          add: "/summary/pmpSummary/add",
          delete: "/summary/pmpSummary/delete",
          list: "/summary/pmpSummary/result",
          deleteBatch: "/summary/pmpTaskSummary/deleteBatch",
          exportXlsUrl: "/summary/pmpTaskSummary/exportXls",
          importExcelUrl: "summary/pmpTaskSummary/importExcel",
        },
      }
    },
    mounted() {
      this.initMonthValue();

      this.searchQuery();
    },
    methods: {
      initMonthValue() {
        this.monthValue = moment(this.date);
      },
      onChange(date, dateString) {
        if ('' == dateString)
          this.initMonthValue();
      },
      searchQuery() {
        this.queryParam.summaryTime = this.monthValue.format("YYYY-MM");

        this.loadData(1);
      },
      searchReset() {
        this.initMonthValue();

        this.searchQuery();
      }
    },
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