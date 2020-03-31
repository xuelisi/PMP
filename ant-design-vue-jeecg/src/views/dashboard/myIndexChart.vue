<template>
  <div class="page-header-index-wide">
    <a-row :gutter="24">
      <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="任务完成率" total="90%">
          <a-tooltip title="指标说明" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <div>
            <mini-progress color="rgb(19, 194, 194)" :target="100" :percentage="90" :height="8" />
          </div>
          <template slot="footer">
            <trend flag="down" style="margin-right: 16px;">
              <span slot="term">同周比</span>
              8%
            </trend>
            <trend flag="up">
              <span slot="term">日环比</span>
              85%
            </trend>
          </template>
        </chart-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="任务延误率" total="11%">
          <a-tooltip title="指标说明" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <div>
            <mini-progress color="red" :target="100" :percentage="11" :height="8" />
          </div>
          <template slot="footer">
            <trend flag="down" style="margin-right: 16px;">
              <span slot="term">同周比</span>
              6%
            </trend>
            <trend flag="up">
              <span slot="term">日环比</span>
              67%
            </trend>
          </template>
        </chart-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="项目完成率" total="78%">
          <a-tooltip title="指标说明" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <div>
            <mini-progress color="rgb(19, 194, 194)" :target="100" :percentage="78" :height="8" />
          </div>
          <template slot="footer">
            <trend flag="down" style="margin-right: 16px;">
              <span slot="term">同周比</span>
              12%
            </trend>
            <trend flag="up">
              <span slot="term">日环比</span>
              80%
            </trend>
          </template>
        </chart-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="项目延误率" total="9%">
          <a-tooltip title="指标说明" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <div>
            <mini-progress color="red" :target="100" :percentage="9" :height="8" />
          </div>
          <template slot="footer">
            <trend flag="down" style="margin-right: 16px;">
              <span slot="term">同周比</span>
              2%
            </trend>
            <trend flag="up">
              <span slot="term">日环比</span>
              0%
            </trend>
          </template>
        </chart-card>
      </a-col>
    </a-row>

    <a-row :gutter="24">
      <a-row>
        <a-col :span="24">
          <a-card
            :loading="loading"
            :bordered="false"
            title="本月登录次数排行榜TOP 6"
            :style="{ marginTop: '24px' }"
          >
            <div style="background: #ECECEC; padding: 30px">
              <a-row :gutter="16">
                <a-list :dataSource="visitTopSixInfo" :grid="{ gutter: 16, column: 6 }">
                  <a-list-item slot="renderItem" slot-scope="item">
                    <a-card>
                      <a-statistic
                        :title="getRealname(item.userid)"
                        :value="item.visit"
                        :precision="0"
                        suffix="次"
                        :valueStyle="{color: '#3f8600'}"
                        style="margin-right: 50px"
                      >
                        <template v-slot:prefix>
                          <a-icon type="arrow-up" />
                        </template>
                      </a-statistic>
                    </a-card>
                  </a-list-item>
                </a-list>
              </a-row>
            </div>
          </a-card>
        </a-col>
      </a-row>
    </a-row>

    <a-row>
      <a-col :span="24">
        <a-card
          :loading="loading"
          :bordered="false"
          title="最近一周访问次数统计"
          :style="{ marginTop: '24px' }"
        >
          <a-row>
            <a-col :span="6">
              <head-info title="今日访问IP数" :content="loginfo.todayIp"></head-info>
            </a-col>
            <a-col :span="2">
              <a-spin class="circle-cust">
                <a-icon slot="indicator" type="environment" style="font-size: 24px" />
              </a-spin>
            </a-col>
            <a-col :span="6">
              <head-info title="今日访问次数" :content="loginfo.todayVisitCount"></head-info>
            </a-col>
            <a-col :span="2">
              <a-spin class="circle-cust">
                <a-icon slot="indicator" type="team" style="font-size: 24px" />
              </a-spin>
            </a-col>
            <a-col :span="6">
              <head-info title="访问总次数" :content="loginfo.totalVisitCount"></head-info>
            </a-col>
            <a-col :span="2">
              <a-spin class="circle-cust">
                <a-icon slot="indicator" type="rise" style="font-size: 24px" />
              </a-spin>
            </a-col>
          </a-row>
          <line-chart-multid :fields="visitFields" :dataSource="visitInfo"></line-chart-multid>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script>
import ChartCard from '@/components/ChartCard'
import ACol from 'ant-design-vue/es/grid/Col'
import ATooltip from 'ant-design-vue/es/tooltip/Tooltip'
import MiniArea from '@/components/chart/MiniArea'
import MiniBar from '@/components/chart/MiniBar'
import MiniProgress from '@/components/chart/MiniProgress'
import Bar from '@/components/chart/Bar'
import LineChartMultid from '@/components/chart/LineChartMultid'
import HeadInfo from '@/components/tools/HeadInfo.vue'

import Trend from '@/components/Trend'
import { getLoginfo, getVisitInfo, getVisitTopSixInfo } from '@/api/api'
import { initDictOptions, filterDictText } from '@/components/dict/JDictSelectUtil'

const barData = []
for (let i = 0; i < 12; i += 1) {
  barData.push({
    x: `${i + 1}月`,
    y: Math.floor(Math.random() * 1000) + 200
  })
}
export default {
  name: 'IndexChart',
  components: {
    ATooltip,
    ACol,
    ChartCard,
    MiniArea,
    MiniBar,
    MiniProgress,
    Bar,
    Trend,
    LineChartMultid,
    HeadInfo
  },
  data() {
    return {
      principalDictOptions: [],
      loading: true,
      center: null,
      barData,
      loginfo: {},
      visitFields: ['ip', 'visit'],
      visitInfo: [],
      visitTopSixInfo: [],
      indicator: <a-icon type="loading" style="font-size: 24px" spin />
    }
  },
  created() {
    setTimeout(() => {
      this.loading = !this.loading
    }, 1000)
    this.initLogInfo()
    //初始化字典 - 项目状态
    initDictOptions('sys_user,realname,username').then(res => {
      if (res.success) {
        this.principalDictOptions = res.result
      }
    })
  },
  methods: {
    getRealname(text) {
      return filterDictText(this.principalDictOptions, text)
    },
    initLogInfo() {
      getLoginfo(null).then(res => {
        if (res.success) {
          Object.keys(res.result).forEach(key => {
            res.result[key] = res.result[key] + ''
          })
          this.loginfo = res.result
        }
      })
      getVisitInfo().then(res => {
        if (res.success) {
          //  console.log('aaaaaa', res.result)
          this.visitInfo = res.result
        }
      })
      getVisitTopSixInfo().then(res => {
        if (res.success) {
          console.log('aaaaaa', res.result)
          this.visitTopSixInfo = res.result
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.circle-cust {
  position: relative;
  top: 28px;
  left: -100%;
}
.extra-wrapper {
  line-height: 55px;
  padding-right: 24px;

  .extra-item {
    display: inline-block;
    margin-right: 24px;

    a {
      margin-left: 24px;
    }
  }
}

/* 首页访问量统计 */
.head-info {
  position: relative;
  text-align: left;
  padding: 0 32px 0 0;
  min-width: 125px;

  &.center {
    text-align: center;
    padding: 0 32px;
  }

  span {
    color: rgba(0, 0, 0, 0.45);
    display: inline-block;
    font-size: 0.95rem;
    line-height: 42px;
    margin-bottom: 4px;
  }
  p {
    line-height: 42px;
    margin: 0;
    a {
      font-weight: 600;
      font-size: 1rem;
    }
  }
}
</style>