<template>
  <div class="container">
    <gantt class="left-container" :tasks="tasks"></gantt>
  </div>
</template>
 
<script>
import Gantt from './modules/PmpGanttModel'

export default {
  name: 'pmpgantt',
  components: { Gantt },
  data() {
    return {
      tasks: {
        data: [
        //   {
        //     text: 'Test001-001', //任务名
        //     start_date: '2017-04-19', //开始时间
        //     id: 1, //任务id
        //     duration: 1, //任务时长，从start_date开始计算
        //     progress: 0.6, //任务完成情况，进度
        //     parent: 2, //父任务ID
        //     user: '李四', //成员
        //     planned_end: '19-04-2017', //计划开始时间
        //     planned_start: '10-04-2017', //计划结束时间
        //     show: false,
        //     open: true, //默认是否打开
        //     type: 'project' // gantt.config.types.milestone为里程碑类型
        //     // project为项目任务类型，也就是摘要任务，
        //     // task为普通任务类型
        //   },
          { id: 1, text: 'Pro #1', startdate: '2017-03-01', enddate:'2017-05-01', durationDay:3,progress: 0.6 ,type:'project',parent:null},
          { id: 2, text: 'Task #1', startdate: '2017-03-01', enddate:'2017-04-05', durationDay:3,progress: 0.6 ,type:'task',parent: 1},
          { id: 3, text: 'Task #2', startdate: '2017-04-02', enddate:'2017-05-01', durationDay:3,progress: 0.6 ,type:'task',parent: 2},
          { id: 4, text: 'Task #3', startdate: '2017-04-20', enddate:'2017-04-30', durationDay:3,progress: 0.4 ,type:'task',parent: 1},
          { id: 5, text: 'Pro #2', startdate: '2020-04-05', enddate:'2020-04-08',durationDay:3, progress: 0.6 ,type:'project',parent: null}

        ],
        links: [
          // links为任务之间连接的线
          //source根源 target目标 也就是从id为1的指向id为2的
          //type:'0'是从1任务完成到2任务开始，type:'1'是1任务开始到2任务开始，
          //type:'2'是从1任务完成到2任务完成，type:'3'是从1任务开始到2任务完成
        //  { id: 1, source: 1, target: 2, type: '0' }
        ]
      }
    }
  },
  methods: {
    addMessage(message) {
      this.messages.unshift(message)
      if (this.messages.length > 40) {
        this.messages.pop()
      }
    },

    logTaskUpdate(id, mode, task) {
      let text = task && task.text ? ` (${task.text})` : ''
      let message = `Task ${mode}: ${id} ${text}`
      this.addMessage(message)
    },

    logLinkUpdate(id, mode, link) {
      let message = `Link ${mode}: ${id}`
      if (link) {
        message += ` ( source: ${link.source}, target: ${link.target} )`
      }
      this.addMessage(message)
    }
  }
}
</script>

<style scoped>
.container {
  height: 700px;
  width: 100%;
}
.left-container {
  overflow: hidden;
  position: relative;
  height: 100%;
}
</style>