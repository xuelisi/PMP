<template>
  <gantt-elastic :options="options" :tasks="dataSource" v-if="true">
    <gantt-header slot="header"></gantt-header>
  </gantt-elastic>
  <!-- <a-button @click="addTask" icon="plus">Add task</a-button> -->
</template>

<style>
</style>

<script>
import moment from 'moment'
import GanttElastic from 'gantt-elastic'
import GanttHeader from 'gantt-elastic-header'
import dayjs from 'dayjs'

let tasks = [
  {
    id: '0',
    label: '无',
    user: '无',
    start: '2020-01-01',
    duration: 0,
    percent: 0,
    type: 'project',
    Status: '5'
    //collapsed: true,
  }
]
const options = {
  taskMapping: {
    progress: 'percent'
  },
  maxRows: 100,
  maxHeight: 700,
  title: {
    label: 'Your project title as html (link or whatever...)',
    html: false
  },
  row: {
    height: 24
  },
  calendar: {
    hour: {
      display: false
    }
  },
  chart: {
    progress: {
      bar: false
    },
    expander: {
      display: true
    }
  },
  taskList: {
    expander: {
      straight: false
    },
    columns: [
      {
        id: 2,
        label: '任务名',
        value: 'label',
        width: 200,
        expander: true,
        html: true,
        events: {
          click({ data, column }) {
            alert('description clicked!n' + data.label)
          }
        }
      },
      {
        id: 3,
        label: '负责人',
        value: 'user',
        width: 130,
        html: true
      },
      {
        id: 3,
        label: '开始日期',
        value: 'start',
        width: 78
      },
      {
        id: 4,
        label: '类型',
        value: 'projecttype',
        width: 68
      },
      {
        id: 5,
        label: '%',
        value: 'progress',
        width: 35,
        style: {
          'task-list-header-label': {
            'text-align': 'center',
            width: '100%'
          },
          'task-list-item-value-container': {
            'text-align': 'center',
            width: '100%'
          }
        }
      }
    ]
  },
  locale: {
    // name: "en",
    // Now: "Now",
    // "X-Scale": "Zoom-X",
    // "Y-Scale": "Zoom-Y",
    // "Task list width": "Task list",
    // "Before/After": "Expand",
    // "Display task list": "Task list"
    weekdays: ['周日', '周一', '周二', '周三', '周四', '周五', '周六'],
    months: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月']
  }
}

export default {
  name: 'PmpGanttElasticModel',
  components: {
    GanttElastic,
    GanttHeader
  },
  props: {
    dataSource: {
      type: Array,
      require: true
    }
  },
  data() {
    return {
      options,
      dynamicStyle: {},
    }
  },
  methods: {}
}
</script>