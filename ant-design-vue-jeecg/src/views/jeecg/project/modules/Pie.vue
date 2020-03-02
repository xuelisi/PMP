<template>
  <div>
    <h3 :style="{ margin: '20px' }">{{ title }}</h3>
    <v-chart :forceFit="true" :height="height" :data="data" :scale="scale">
      <v-tooltip :showTitle="false" data-key="item*percent" />
      <v-axis />
      <v-legend data-key="item" />
      <v-pie position="percent" color="item" :v-style="pieStyle" :label="labelConfig" />
      <v-coord type="theta" />
    </v-chart>
  </div>
</template>

<script>
const DataSet = require('@antv/data-set')

export default {
  props: {
    title: {
      type: String,
      default: ''
    },
    height: {
      type: Number,
      default: 254
    },
    dataSource: {
      type: Array,
      default: () => [
        { item: '正常完成', count: 3 },
        { item: '延期完成', count: 2 },
        { item: '正常进行中', count: 39 },
        { item: '延期进行中', count: 57 }
      ]
    }
  },
  data() {
    return {
      scale: [
        {
          dataKey: 'percent',
          min: 0,
          formatter: '.0%'
        }
      ],
      pieStyle: {
        stroke: '#fff',
        lineWidth: 1
      },
      labelConfig: [
        'percent',
        {
          formatter: (val, item) => {
            return item.point.item + ': ' + val
          }
        }
      ]
    }
  },
  computed: {
    data() {
      let dv = new DataSet.View().source(this.dataSource)
      // 计算数据百分比
      dv.transform({
        type: 'percent',
        field: 'count',
        dimension: 'item',
        as: 'percent'
      })
      return dv.rows
    }
  }
}
</script>