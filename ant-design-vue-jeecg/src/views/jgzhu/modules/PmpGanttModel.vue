<template>
  <div ref="gantt"></div>
</template>
 
<script>
import { gantt } from 'dhtmlx-gantt'
export default {
  name: 'gantt',
  props: {
    tasks: {
      type: Object,
      default() {
        return { data: [], links: [] }
      }
    }
  },

  mounted: function() {
    gantt.config.xml_date = '%Y-%m-%d'
    gantt.config.columns = [
    //   {name:"wbs", label:"WBS", width:40, template:gantt.getWBSCode }, 
      { name: 'text', label: '任务名', tree: true, width: '350'},
      { name: 'startdate', label: '开始日期', align: 'center', width: '100' },
      { name: 'enddate', label: '结束日期', align: 'center', width: '100' },
      { name: 'durationDay', label: '时长/天', align: 'center', width: '100' },
    ]

    gantt.init(this.$refs.gantt)
    gantt.parse(this.$props.tasks)
  }
}
</script>
 
<style>
@import '~@assets/css/dhtmlxgantt.css';
</style>