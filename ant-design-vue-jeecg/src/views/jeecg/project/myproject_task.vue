<template>
  <page-layout
    title="项目1"
    logo="https://gw.alipayobjects.com/zos/rmsportal/nxkuOJlFJuAUhzlMTCEe.png"
  >
    <detail-list slot="headerContent" size="small" :col="6" class="detail-layout">
      <detail-list-item term="负责人">张三</detail-list-item>
      <detail-list-item term="进度">0%</detail-list-item>
      <detail-list-item term="起始日">2018-08-07</detail-list-item>
      <detail-list-item term="截止日">2018-08-07</detail-list-item>
      <detail-list-item term="紧急程度">正常</detail-list-item>
    </detail-list>
    <!-- actions -->
    <template slot="action">
      <a-button type="primary" @click="handleAdd" icon="plus">安排任务</a-button>
    </template>
    <a-table :columns="columns" :dataSource="data" :rowSelection="rowSelection">
      <template slot="name" slot-scope="text">
      <a @click="handleEdit(record)">{{text}}</a>
    </template>
    </a-table>

    <pmpTaskdetails-modal ref="modalForm" @ok="modalFormOk"></pmpTaskdetails-modal>
  </page-layout>
</template>
<script>
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import PmpTaskdetailsModal from './modules/PmpTaskdetailsModal'
import { initDictOptions, filterMultiDictText } from '@/components/dict/JDictSelectUtil'
import { mixinDevice } from '@/utils/mixin.js'
import PageLayout from '@/components/page/PageLayout'
import DetailList from '@/components/tools/DetailList'

const DetailListItem = DetailList.Item

const columns = [
  {
    title: '任务名',
    dataIndex: 'name',
    key: 'name',
    width: '34%',
     scopedSlots: { customRender: 'name' },
  },
  {
    title: '负责人',
    dataIndex: 'principal',
    key: 'principal',
    width: '15%'
  },
  {
    title: '进度',
    dataIndex: 'schedule',
    width: '10%',
    key: 'schedule'
  },
  {
    title: '起始日',
    dataIndex: 'startdate',
    width: '13%',
    key: 'startdate'
  },
  {
    title: '截止日',
    dataIndex: 'enddate',
    width: '13%',
    key: 'enddate'
  },
  {
    title: '紧急程度',
    dataIndex: 'emergencylevel',
    width: '15%',
    key: 'emergencylevel'
  }
]

const data = [
  {
    key: 1,
    name: '任务1',
    principal: '张三',
    schedule: '0%',
    startdate: '2020.01.01',
    enddate: '2020.03.01',
    emergencylevel: '正常',
    children: [
      {
        key: 11,
        name: '任务1-1',
        principal: '张三',
        schedule: '0%',
        startdate: '2020.01.01',
        enddate: '2020.03.01',
        emergencylevel: '正常'
      },
      {
        key: 12,
        name: '任务1-2',
        principal: '张三',
        schedule: '0%',
        startdate: '2020.01.01',
        enddate: '2020.03.01',
        emergencylevel: '正常',
        children: [
          {
            key: 121,
            name: '任务1-2-1',
            principal: '张三',
            schedule: '0%',
            startdate: '2020.01.01',
            enddate: '2020.03.01',
            emergencylevel: '正常'
          }
        ]
      },
      {
        key: 13,
        name: '任务1-3',
        principal: '张三',
        schedule: '0%',
        startdate: '2020.01.01',
        enddate: '2020.03.01',
        emergencylevel: '正常',
        children: [
          {
            key: 131,
            name: '任务1-3-1',
            principal: '张三',
            schedule: '0%',
            startdate: '2020.01.01',
            enddate: '2020.03.01',
            emergencylevel: '正常',
            children: [
              {
                key: 1311,
                name: '任务1-3-1-1',
                principal: '张三',
                schedule: '0%',
                startdate: '2020.01.01',
                enddate: '2020.03.01',
                emergencylevel: '正常'
              },
              {
                key: 1312,
                name: '任务1-3-1-2',
                principal: '张三',
                schedule: '0%',
                startdate: '2020.01.01',
                enddate: '2020.03.01',
                emergencylevel: '正常'
              }
            ]
          }
        ]
      }
    ]
  },
  {
    key: 2,
    name: '任务2',
    principal: '李四',
    schedule: '10%',
    startdate: '2020.01.01',
    enddate: '2020.03.01',
    emergencylevel: '正常'
  },
  {
    key: 2,
    name: '任务3',
    principal: '王五',
    schedule: '30%',
    startdate: '2020.01.01',
    enddate: '2020.03.01',
    emergencylevel: '正常'
  }
]

const rowSelection = {
  onChange: (selectedRowKeys, selectedRows) => {
    console.log(`selectedRowKeys: ${selectedRowKeys}`, 'selectedRows: ', selectedRows)
  },
  onSelect: (record, selected, selectedRows) => {
    console.log(record, selected, selectedRows)
  },
  onSelectAll: (selected, selectedRows, changeRows) => {
    console.log(selected, selectedRows, changeRows)
  }
}

export default {
  name: 'myproject_task',
  components: {
    PageLayout,
    DetailList,
    DetailListItem,
    PmpTaskdetailsModal
  },
  mixins: [JeecgListMixin],
  data() {
    return {
      data,
      columns,
      rowSelection
    }
  }
}
</script>

<style lang="scss" scoped>
@import '~@assets/less/common.less';
.detail-layout {
  margin-left: 44px;
}
.text {
  color: rgba(0, 0, 0, 0.45);
}

.heading {
  color: rgba(0, 0, 0, 0.85);
  font-size: 20px;
}

.no-data {
  color: rgba(0, 0, 0, 0.25);
  text-align: center;
  line-height: 64px;
  font-size: 16px;

  i {
    font-size: 24px;
    margin-right: 16px;
    position: relative;
    top: 3px;
  }
}

.mobile {
  .detail-layout {
    margin-left: unset;
  }
  .text {
  }
  .status-list {
    text-align: left;
  }
}
</style>