<template>
  <a-card :bordered="false">
  <div>
    <div class="table-operations">
      <a-button @click="setAgeSort">任务名排序</a-button>
      <a-button @click="clearFilters">清除过滤规则</a-button>
      <a-button @click="clearAll">清除排序过滤</a-button>
    </div>
    <a-table :columns="columns"
             :dataSource="tableDatas"
             @change="handleChange" >

      <template slot="htmlSlot" slot-scope="text">
        <div v-html="text"></div>
      </template>

      <span slot="actionSummary" slot-scope="text, record">
          <a @click="gotoSummary(record)">{{ text }}</a>
        </span>
      <span slot="actionRespb" slot-scope="text, record">
          <a @click="gotoRespb(record)">{{ text }}</a>
        </span>
    </a-table>
    <!--<pmpProComment-modal ref="modalForm" @ok="modalFormOk"></pmpProComment-modal>-->
  </div>
    <pmpComment-modal ref="modalForm" @ok="modalFormOk"></pmpComment-modal>
    <pmpSummaryTimeline-modal ref="timelineForm" @ok="modalFormOk"></pmpSummaryTimeline-modal>
  </a-card>
</template>

<script>

  import JInput from '@/components/jeecg/JInput'
  import JEllipsis from '@/components/jeecg/JEllipsis'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import { deleteAction, getAction,downFile } from '@/api/manage'
  import PmpCommentModal from '@views/wqc/modules/PmpCommentModal'

  const tableDatas = [
    {
    key: '1',
    respb: '张三',
    tname: '需求分析',
    pname: '某某工程',
      id: '1237280237046595586',
    cdate: '2019-05-10',
  }, {
    key: '2',
    respb: '李四',
    tname: '方案设计',
    pname: '某某工程',
      id: '1235727836212768770',
    cdate: '2019-07-10',
  }, {
    key: '3',
    respb: '王五',
    tname: '研发测试',
    pname: '某某工程',
      id: '1235810671128956929',
    cdate: '2019-011-10',
  }, {
    key: '4',
    respb: '周六',
    tname: '评审验收',
    pname: '某某工程',
      id: '12345674',
    cdate: '2020-02-10',
  }];

  export default {
    name: "tasklist",
    mixins:[JeecgListMixin],
    components: {
      JInput,
      JEllipsis,
      PmpCommentModal,
    },
    data() {
      return {
        tableDatas,
        filteredInfo: null,
        sortedInfo: null,
      }
    },
    computed: {
      columns() {
        let { sortedInfo, filteredInfo } = this;
        sortedInfo = sortedInfo || {};
        filteredInfo = filteredInfo || {};
        const columns = [
          {
          title: '项目名称',
          dataIndex: 'pname',
          key: 'pname',
          sorter: (a, b) => a.pname - b.pname,
          sortOrder: sortedInfo.columnKey === 'pname' && sortedInfo.order,
          scopedSlots: {customRender: 'htmlSlot'}
        }, {
          title: '任务名称',
          dataIndex: 'tname',
          key: 'tname',
          filters: [
            { text: 'Joe', value: 'Joe' },
            { text: 'Jim', value: 'Jim' },
          ],
          filteredValue: filteredInfo.tname || null,
          onFilter: (value, record) => record.tname.includes(value),
          sorter: (a, b) => a.tname.length - b.tname.length,
          sortOrder: sortedInfo.columnKey === 'tname' && sortedInfo.order,
          scopedSlots: { customRender: 'actionSummary' },
        }, {
          title: '任务id',
          dataIndex: 'id',
          key: 'id'
        },{
          title: '负责人',
          dataIndex: 'respb',
          key: 'respb',
          scopedSlots: { customRender: 'actionRespb' },
        },{
          title: '创建时间',
          dataIndex: 'cdate',
          key: 'cdate',
          filters: [
            { text: 'London', value: 'London' },
            { text: 'New York', value: 'New York' },
          ],
          filteredValue: filteredInfo.cdate || null,
          onFilter: (value, record) => record.cdate.includes(value),
          sorter: (a, b) => a.cdate.length - b.cdate.length,
          sortOrder: sortedInfo.columnKey === 'cdate' && sortedInfo.order,
        }];
        return columns;
      }
    },
    methods: {
      gotoSummary(record){
        // this.$router.push({ name: 'demo-comment', params: { taskid: record.taskid }})
        // this.$router.push({ name: 'demo-summary', params: { taskid: record.taskid }})

        let index = 1;
        record.taskname = '任务名称测试';

        switch(index) {
          case 0:
            this.$refs.modalForm.show(record);
            break;
          case 1:
            this.$router.push({ name: 'demo-summary', params: { taskid: record.taskid }})
            break
          default:
          this.$refs.timelineForm.show(record);
        }

      },
      gotoRespb(record){
        this.$router.push({ name: '/wqc/SubordinateList', query: { respb: record.respb }})
      },
      handleChange (pagination, filters, sorter) {
        console.log('Various parameters', pagination, filters, sorter);
        this.filteredInfo = filters;
        this.sortedInfo = sorter;
      },
      clearFilters () {
        this.filteredInfo = null;
      },
      clearAll () {
        this.filteredInfo = null;
        this.sortedInfo = null;
      },
      setAgeSort () {
        this.sortedInfo = {
          order: 'descend',
          columnKey: 'tname',
        }
      }
    },
    created: function () {
      //alert(this.$route.path);
    }
  }
</script>
<style scoped>
  .table-operations {
    margin-bottom: 16px;
  }

  .table-operations > button {
    margin-right: 8px;
  }
</style>