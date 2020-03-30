<template>
  <div >
    <v-chart :forceFit="true" :height="400" :data="data" padding="auto" :scale="scale">
      <v-tooltip />
      <v-axis data-key="date" :label="label" :tickLine="tickLine" />
      <v-axis data-key="number" :label="label" :title="title" />
      <v-legend />
      <v-interval position="date*number" :adjust="adjust" color="status" />
    </v-chart>
  </div>
</template>

<script>

const data = []

const scale = [
  {
    dataKey: 'number',
    min: 0
  }
]

const label = {
  textStyle: {
    fill: '#aaaaaa'
  }
}

const title = {
  offset: 50
}

const tickLine = {
  alignWithLabel: false,
  length: 0
}

const adjust = [
  {
    type: 'dodge',
    marginRatio: 1 / 32
  }
]

export default {
  props: {
    data: {
      type: Array,
      default: null,
      required: true
    }
  },
  data() {
    return {
      scale,
      label,
      tickLine,
      title,
      adjust
    }
  }
}
</script>