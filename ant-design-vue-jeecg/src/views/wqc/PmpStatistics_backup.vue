<template>
    <a-card :bordered="false">

      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">

            <a-col :md="4" :sm="8">
              <a-form-item label="年月">
                <a-month-picker
                  @change="monthPickerChange"
                  :format="monthFormat"
                  v-model="monthPickerValue"
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
          :customHeaderRow="headerRow"
          @change="handleTableChange">

          <!--//鼠标点击-->
          <!--<template-->
            <!--slot="summaryStateSlot"-->
            <!--slot-scope="text, record">-->

            <!--<a slot="actions"-->
               <!--@mouseenter="clickOpen(text, record)">-->
              <!--<a-icon v-if="null != text" type="check-circle" />-->
            <!--</a>-->

          <!--</template>-->

          <!--//标题-->
          <template
            slot="headerSlot"
            slot-scope="text, record">
            <span>{{ text }}</span>
          </template>

          <!--//鼠标悬浮-->
          <template
            slot="summaryStateSlot"
            slot-scope="text, record">
            <div>
              <a-popover
                trigger="hover"
                @mouseenter="hoverOpen(text, record)">
                <template slot="title">
                  <p>{{ record.realName +'[' + text +']' }}</p>
                </template>
                <template slot="content">
                  <a-card :bordered="false">
                    <a-spin :spinning="spinning">

                      <div class="scroll-wrap">
                        <a-list itemLayout="vertical" size="small" :dataSource="summaryData">
                          <a-list-item
                            slot="renderItem"
                            slot-scope="item, index">

                            <a-list-item-meta>

                              <a slot="title">{{ item.title }}</a>
                              <a-avatar slot="avatar" icon="schedule"/>
                              <a class="list-content" slot="description" v-html="item.content" />

                            </a-list-item-meta>

                            <a-upload :fileList="item.annex">
                            </a-upload>

                          </a-list-item>
                        </a-list>
                      </div>

                    </a-spin>
                  </a-card>
                </template>

                <a-icon v-if="null != text" type="check-circle" />

              </a-popover>
            </div>
          </template>


        </a-table>
      </div>

      <div class="footer-tip">
        <span>说明：<a-icon type="check-circle" />有日报</span>
      </div>

      <!--<pmp-statistics-modal ref="modalForm" @ok="modalFormOk"></pmp-statistics-modal>-->
    </a-card>
</template>

<script>

  import moment from 'moment';
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import { deleteAction, getAction, putAction, downFile } from '@/api/manage'

  import PmpStatisticsPop from './PmpStatisticsPop'
  import PmpStatisticsModal from './modules/PmpStatisticsModal'

  const debug = (msg) => {
    console.log('*************************');
    console.log('*************************');
    console.log(msg);
    console.log('*************************');
    console.log('*************************');
  }

  const getFileName = (path) => {
    if(path.lastIndexOf("\\")>=0){
      let reg=new RegExp("\\\\","g");
      path = path.replace(reg,"/");
    }
    return path.substring(path.lastIndexOf("/")+1);
  }

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
      PmpStatisticsPop,
      PmpStatisticsModal
    },
    data() {
      return {
        columns: [],
        spinning: true,
        summaryData: [],
        allCols: allCols,
        monthFormat: 'YYYY-MM',
        colorBlack: '',
        colorRed: 'red',
        colorBlue: 'blue',
        colorVolcano: 'orange',
        queryParam: {summaryTime: ''},
        monthPickerValue: moment(this.date),
        url: {
          list: "/summary/pmpSummary/statistics",
          query: "/summary/pmpSummary/list",
        },
      }
    },
    mounted() {
      this.searchReset();
    },
    methods: {
      //初始化年月数据
      initMonthPicker() {
        this.monthPickerValue = moment(this.date);
      },
      //初始化表格数据
      initColumns() {
        let dt = this.monthPickerValue.format("YYYY-MM");
        this.columns = this.allCols.slice(0, this.getMonthCount(dt) + 1);
      },
      //年月切换
      monthPickerChange(date, dateString) {
        if (dateString.length <= 0) {
          this.initMonthPicker();
        }
      },
      //查询
      searchQuery() {
        this.queryParam.summaryTime = this.monthPickerValue.format("YYYY-MM");

        this.initColumns();
        this.loadData(1);
      },
      //重置
      searchReset() {
        this.initMonthPicker();

        this.searchQuery();
      },
      //获取年月对应天数
      getDate() {
        return new Date().getDate();
      },
      getDay(text) {
        return this.getStr(text, 8, 10);
      },
      getWeekDate(time) {
        let day = new Date().setDate(time).getDay();
        let weeks = new Array("周日", "周一", "周二", "周三", "周四", "周五", "周六");
        return weeks[day];
      },
      getFontSize(type) {
        switch(type) {
          case 0:
            return 'font-size: 15px; ';
          case 1:
            return 'font-size: 12px; ';
          default:
            return 'font-size: 15px; ';
        }
      },
      //0-header 1-data 2-common data
      getBorder(text, type) {
        switch(type) {
          case 0:
            return 'border: ' + (text === this.getDate() ? '1' : '0') + ';';
          case 1:
            return 'border: ' + (null === text ? '0' : '1') + ';';
          default:
            return 'border: 0;';
        }
      },
      //0-header 1-data 2-common data
      getColor(src, dst, type) {
        switch(type) {
          case 0:
            return src === this.getDate() ? this.colorVolcano : this.colorBlue;
          case 1:
            if (null === dst)
              return this.colorBlue;

            return src === this.getStr(dst, 8, 10) ? this.colorBlue : this.colorVolcano;
          default:
            return this.colorBlack;
        }
      },
      getState(src, dst) {
        if (null === dst)
          return '';

        return src === this.getStr(dst, 8, 10) ? '正常' : '补录';
      },
      getMonthCount(str) {
        return new Date(str.substr(0, 4), str.substr(5, 2), 0).getDate();
      },
      clickOpen(time, record) {
        this.$refs.modalForm.show(time, record);
      },
      hoverOpen(text, record) {
        this.load(text, record);
      },
      load(time, record) {
        this.summaryData = [];
        getAction(this.url.query, { summaryTime: time, owner: record.userName }).then((res) => {
          if (res.success) {
            this.spinning = false;
            for(let i = 0; i < res.result.total; ++i) {
              this.summaryData.push({
                title: this.loadTitle(res.result.records[i]),
                content: res.result.records[i].content,
                annex: this.loadAnnex(res.result.records[i].contentAnnex),
              });
            }
          }
        })
      },
      loadAnnex(annex) {
        let linkList = [];

        if ((null != annex) && (annex.length > 0)) {
          let annexList = annex.split(',');

          for(let idx in annexList) {
            linkList.push({
              uid: -1,
              name: getFileName(annexList[idx]),
              url: window._CONFIG['domianURL'] + "/sys/common/download/" + annexList[idx],
            });
          }
        }

        return linkList;
      },
      loadTitle(result) {
        return result.projectName + '-' + result.taskName;
      },
      getStr(text, beg, end) {
        return text.substring(beg, end);
      },
      headerRow(column){
        return {
          on: {
            click: () => {
            },
            onload: () => {
              alert('load');
              debug(column);
            }
          },
          }
        }
      }
  };

</script>

<style>
  .ant-popover-title {
    text-align: left;
    height: 30px;
    line-height: 30px;
  }

  .ant-card-body {
    padding: 0;
  }

  .ant-list-vertical .ant-list-item-meta {
    margin-bottom: 0px;
  }

  .ant-list-vertical .ant-list-item-content {
    display: block;
    margin: 0;
    color: rgba(0, 0, 0, 0.65);
    font-size: 14px;
  }

  .scroll-wrap {
    width: 600px;
    margin: 0;
    padding: 0;
    max-height: 300px;
    overflow: hidden;
    overflow-y: scroll;
  }
</style>