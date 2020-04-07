<template>
  <a-card :bordered="false">
    <div class="table-page-search-wrapper">
      <a-form layout="inline" style="margin-top: -4px">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="起止日期">
              <a-col :span="11">
                <j-date placeholder="请选择起始日期" class="inputnum" v-model="startTime"></j-date>
              </a-col>
              <a-col :span="1" style="text-align:center">-</a-col>
              <a-col :span="12">
                <j-date placeholder="请选择起始日期" class="inputnum" v-model="endTime"></j-date>
              </a-col>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" icon="search" @click="queryDatepie">查询</a-button>
              <a-button
                style="margin-left: 8px"
                type="primary"
                icon="reload"
                @click="searchReset"
              >重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <a-tabs defaultActiveKey="1" @change="callback">
      <a-tab-pane tab="任务统计报表" key="1">
        <a-row :gutter="24">
          <a-col :span="10" style="text-align:center;">
            <span style="font-size:25px;font-family:'微软雅黑';">任务完成状况</span>
            <pie class="statistic" title="任务完成状况" :dataSource="piecountSource" :height="300" />
          </a-col>
          <a-col :span="4">
            <dl style="margin-top:120px">
              <dt style="font-size:70px;color:#23b7e5;font-family:'微软雅黑'">{{number}}</dt>
              <dt style="font-size:15px;font-family:'微软雅黑';color:black">新增任务</dt>
            </dl>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <bar-multid class="statistic" title="任务完成状况" :height="400" :data="countSource" />
        </a-row>
      </a-tab-pane>
    </a-tabs>
  </a-card>
</template>

<script>
import Bar from '@/components/chart/Bar'
import Pie from '@/components/chart/Pie'
import ACol from 'ant-design-vue/es/grid/Col'
import JDate from '@/components/jeecg/JDate.vue'
import { getAction } from '@/api/manage'
import BarMultid from './TaskBarMultid'
import moment from 'moment'

export default {
  name: 'PmpViewEcharts',
  components: {
    ACol,
    JDate,
    Bar,
    Pie,
    BarMultid
  },
  data() {
    return {
      description: '任务统计报表页面',
      // 查询条件
      queryParam: {},
      // 数据集
      countSource: [],
      piecountSource: [],
      number: 0,
      startTime: '',
      endTime: '',
      // 统计图类型
      tabStatus: 'bar',
      url: {
        getPieCountInfo: '/proecharts/pmpProjectEchart/getTaskPieCountInfo',
        getTaskCountInfo: '/proecharts/pmpProjectEchart/getTaskCountInfo',
        newPmpTask: '/proecharts/pmpProjectEchart/newTasknumber'
      }
    }
  },
  created() {
    this.startTime = new Date().getFullYear() + '-01-01'
    this.endTime = moment(new Date()).format('YYYY-MM-DD')
    let url = this.url.getTaskCountInfo
    this.loadData(url, {})
    this.loadDatapie(this.url.getPieCountInfo, {})
  },
  methods: {
    //柱状图
    loadData(url, param) {
      getAction(url, param).then(res => {
        if (res.success) {
          this.countSource = []
          this.countSource = res.result
        } else {
          var that = this
          that.$message.warning(res.message)
        }
      })
    },
    //饼图
    loadDatapie(url, param) {
      getAction(url, param).then(res => {
        if (res.success) {
          this.piecountSource = []

          this.getPieCountSource(res.result)
        } else {
          var that = this
          that.$message.warning(res.message)
        }
      }),
        getAction(this.url.newPmpTask, param).then(res => {
          if (res.success) {
            this.number = res.result
          } else {
            var that = this
            that.$message.warning(res.message)
          }
        })
    },
    getPieCountSource(data) {
      for (let i = 0; i < data.length; i++) {
        this.piecountSource.push({
          item: data[i].status,
          count: data[i].number
        })
      }
    },
    // 选择统计图类别
    callback(key) {
      this.queryDatepie()
    },
    //查询
    queryDatepie() {
      this.getUrl({
        startTime: this.startTime,
        endTime: this.endTime
      })
    },
    //重置
    searchReset() {
      this.startTime = ''
      this.endTime = ''
      this.getUrl({
        startTime: this.startTime,
        endTime: this.endTime
      })
    },
    // 选择请求url
    getUrl(param) {
      let url = this.url.getTaskCountInfo
      let pieurl = this.url.getPieCountInfo
      this.loadData(url, param)
      this.loadDatapie(pieurl, param)
    }
  }
}
</script>
<style scoped>
.ant-card-body .table-operator {
  margin-bottom: 18px;
}

.ant-table-tbody .ant-table-row td {
  padding-top: 15px;
  padding-bottom: 15px;
}

.anty-row-operator button {
  margin: 0 5px;
}

.ant-btn-danger {
  background-color: #ffffff;
}

.ant-modal-cust-warp {
  height: 100%;
}

.ant-modal-cust-warp .ant-modal-body {
  height: calc(100% - 110px) !important;
  overflow-y: auto;
}

.ant-modal-cust-warp .ant-modal-content {
  height: 90% !important;
  overflow-y: hidden;
}

.statistic {
  padding: 0px !important;
  margin-top: 50px;
}

.statistic h4 {
  margin-bottom: 20px;
  text-align: center !important;
  font-size: 24px !important;
}

.statistic #canvas_1 {
  width: 100% !important;
}
</style>