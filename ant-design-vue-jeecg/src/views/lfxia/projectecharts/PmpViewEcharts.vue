<template>
  <a-card :bordered="false">
    <div class="table-page-search-wrapper">
      <a-form layout="inline" style="margin-top: -4px" v-if="tabStatus === 'bar'">
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
            <a-form-item label="项目类型">
              <j-dict-select-tag v-model="projecttype" dictCode="project_type" />
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
    <a-tabs defaultActiveKey="1" type="card" @change="callback">
      <a-tab-pane tab="项目统计报表" key="1">
        <a-row :gutter="24">
          <a-col :span="10" style="text-align:center;">
            <span style="font-size:25px;font-family:'微软雅黑';">项目完成状况</span>
            <pie class="statistic" title="项目完成状况情况" :dataSource="piecountSource" :height="300" />
          </a-col>
          <a-col :span="4">
            <dl style="margin-top:120px">
              <dt style="font-size:70px;color:#23b7e5;font-family:'微软雅黑'">{{number}}</dt>
              <dt style="font-size:15px;font-family:'微软雅黑';color:black">新增项目</dt>
            </dl>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="24" style="text-align:center">
            <a-radio-group :value="barType" @change="statisticst">
              <a-radio-button value="year">按年统计</a-radio-button>
              <a-radio-button value="month">按月统计</a-radio-button>
              <a-radio-button value="category">按人员统计</a-radio-button>
              <a-radio-button value="cabinet">按部门统计</a-radio-button>
            </a-radio-group>
          </a-col>
          <bar-multid class="statistic" title="项目完成状况" :height="400" :data="countSource" />
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
import BarMultid from './BarMultid'
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
      description: '档案统计页面',
      // 查询条件
      queryParam: {},
      // 数据集
      countSource: [],
      piecountSource: [],
      number: 0,
      //项目类型
      projecttype: '',
      // 柱状图
      barType: 'year',
      startTime: '',
      endTime: '',
      // 统计图类型
      tabStatus: 'bar',
      url: {
        getPieCountInfo: '/proecharts/pmpProjectEchart/getPieCountInfo',
        getYearCountInfo: '/proecharts/pmpProjectEchart/getYearCountInfo',
        getMonthCountInfo: '/proecharts/pmpProjectEchart/getMonthCountInfo',
        getPerCountInfo: '/proecharts/pmpProjectEchart/getPerNoCountInfo',
        getDepCountInfo: '/proecharts/pmpProjectEchart/getDepCountInfo',
        newPmpTask: '/proecharts/pmpProjectEchart/newPmpnumber'
      }
    }
  },
  created() {
    this.startTime = new Date().getFullYear() + '-01-01'
    this.endTime = moment(new Date()).format('YYYY-MM-DD')
    let url = this.url.getYearCountInfo
    let urlpie = this.url.getPieCountInfo
    this.loadData(url, 'year', {})
    this.loadDatapie(url, {})
  },
  methods: {
    // 项目柱状图
    loadData(url, type, param) {
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
    // 项目饼图
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
    // 项目饼图
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
      if (key === '1') {
        this.tabStatus = 'bar'
        this.queryDatepie()
      } else {
        this.tabStatus = 'pie'
      }
    },
    // 选择统计类别
    statisticst(e) {
      this.barType = e.target.value
      this.queryDatepie()
    },
    //查询
    queryDatepie() {
      this.getUrl(this.barType, {
        startTime: this.startTime,
        endTime: this.endTime,
        projecttype: this.projecttype
      })
    },
    //重置
    searchReset() {
      this.startTime = ''
      this.endTime = ''
      this.projecttype = ''
      this.depart = null
      this.getUrl(this.barType, {
        startTime: this.startTime,
        endTime: this.endTime,
        projecttype: this.projecttype
      })
    },
    // 选择请求url
    getUrl(type, param) {
      let url = ''
      let urlp = this.url.getPieCountInfo
      this.loadDatapie(urlp, param)
      if (type === 'year') {
        url = this.url.getYearCountInfo
      }
      if (type === 'month') {
        url = this.url.getMonthCountInfo
      }
      if (type === 'category') {
        url = this.url.getPerCountInfo
      }
      if (type === 'cabinet') {
        url = this.url.getDepCountInfo
      }
      this.loadData(url, type, param)
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