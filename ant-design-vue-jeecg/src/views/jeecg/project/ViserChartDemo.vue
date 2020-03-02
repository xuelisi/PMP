<template>
  <div>
    <a-row>
      <bar-multid title="任务统计" :height="height" />
    </a-row>
    <a-row>
      <a-col :span="12">
        <pie title="完成状况" :height="height" />
      </a-col>
      <a-col :span="12">
        <pieone title="紧急程度" :height="height" />
      </a-col>
    </a-row>
  </div>
</template>

<script>
import AreaChartTy from '@/components/chart/AreaChartTy'
import Bar from './modules/Bar'
import BarMultid from '@/components/chart/BarMultid'
import DashChartDemo from '@/components/chart/DashChartDemo'
import LineChartMultid from '@/components/chart/LineChartMultid'
import Liquid from '@/components/chart/Liquid'
import MiniBar from '@/components/chart/MiniBar'
import MiniArea from '@/components/chart/MiniArea'
import MiniProgress from '@/components/chart/MiniProgress'
import Pie from './modules/Pie'
import Pieone from './modules/Pie01'
import Radar from '@/components/chart/Radar'
import RankList from '@/components/chart/RankList'
import TransferBar from '@/components/chart/TransferBar'
import Trend from '@/components/chart/Trend'
import BarAndLine from '@/components/chart/BarAndLine'

export default {
  name: 'ViserChartDemo',
  components: {
    Bar,
    MiniBar,
    BarMultid,
    AreaChartTy,
    LineChartMultid,
    Pie,
    Pieone,
    Radar,
    DashChartDemo,
    MiniProgress,
    RankList,
    TransferBar,
    Trend,
    Liquid,
    MiniArea,
    BarAndLine
  },
  data() {
    return {
      height: 320,
      rankList: [],
      barData: [],
      areaData: []
    }
  },
  created() {
    setTimeout(() => {
      this.loadBarData()
      this.loadAreaData()
      this.loadRankListData()
    }, 100)
  },
  methods: {
    loadData(x, y, max, min, before = '', after = '月') {
      let data = []
      for (let i = 0; i < 12; i += 1) {
        data.push({
          [x]: `${before}${i + 1}${after}`,
          [y]: Math.floor(Math.random() * max) + min
        })
      }
      return data
    },
    // 加载柱状图数据
    loadBarData() {
      this.barData = this.loadData('x', 'y', 1000, 200)
    },
    // 加载AreaChartTy的数据
    loadAreaData() {
      this.areaData = this.loadData('x', 'y', 500, 100)
    },
    loadRankListData() {
      this.rankList = this.loadData('name', 'total', 2000, 100, '北京朝阳 ', ' 号店')
    }
  }
}
</script>

<style scoped>
</style>
