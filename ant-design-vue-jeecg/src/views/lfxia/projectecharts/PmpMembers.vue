<template>
  <a-card :bordered="false">
    <div class="table-page-search-wrapper">
      <a-form layout="inline" style="margin-top: -4px">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="部门">
              <j-select-depart v-model="queryParam.depart" customReturnField="orgCode"></j-select-depart>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" icon="search" @click="searchQuery">查询</a-button>
              <!--    <a-button
                style="margin-left: 8px"
                type="primary"
                icon="reload"
                @click="searchReset"
              >重置</a-button>-->
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- table区域-begin -->
    <div>
      <a-table
        ref="table"
        size="middle"
        rowKey="name"
        bordered
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :scroll="{ x:800,y: 470 }"
        @change="handleTableChange"
      ></a-table>
    </div>
  </a-card>
</template>

<script>
import { getAction } from '@/api/manage'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import { initDictOptions,  filterMultiDictText } from '@/components/dict/JDictSelectUtil'
import { filterObj, isContainPrincipal } from '@/utils/util'
import JSelectDepart from '@/components/jeecgbiz/JSelectDepart'

export default {
  name: 'PmpMembers',
  mixins: [JeecgListMixin],
  components: {
    JSelectDepart
  },
  props: {},
  data() {
    return {
      description: '项目人员统计',
      dataSource: [],
      name: [],
      loading: false,
      isorter: {
        column: 'name',
        order: 'ascend'
      },
      // 表头
      columns: [
        {
          title: '序号',
          dataIndex: '',
          key: 'rowIndex',
          width: 60,
          align: 'center',
          customRender: function(t, r, index) {
            return parseInt(index) + 1
          }
        },
        {
          title: '成员',
          align: 'center',
          dataIndex: 'name',
          customRender: text => {
            //字典值替换通用方法
            return filterMultiDictText(this.name, text)
          }
        },
        {
          title: '项目数',
          align: 'center',
          width: '9%',
          dataIndex: 'pmpnumber'
        },
        {
          title: '待完成',
          children: [
            {
              title: '延期',
              align: 'center',
              width: '9%',
              dataIndex: 'delaynumber'
            },
            {
              title: '正常',
              align: 'center',
              width: '9%',
              dataIndex: 'normalnumber'
            },
            {
              title: '未开始',
              align: 'center',
              width: '9%',
              dataIndex: 'nstartnumber'
            }
          ]
        },
        {
          title: '已完成',
          children: [
            {
              title: '延期',
              align: 'center',
              width: '9%',
              dataIndex: 'delaycnumber'
            },
            {
              title: '正常',
              align: 'center',
              width: '9%',
              dataIndex: 'nornumber'
            }
          ]
        },
        {
          title: '任务总数',
          align: 'center',
          width: '9%',
          dataIndex: 'tasknumber'
        },
        {
          title: '延误率',
          align: 'center',
          width: '9%',
          dataIndex: 'delayrate',
          customRender: function(t, r, index) {
            return r.delayrate + '%'
          }
        },
        {
          title: '成员完成度',
          align: 'center',
          width: '9%',
          dataIndex: 'completion',
          customRender: function(t, r, index) {
            return r.completion + '%'
          }
        }
      ],
      url: {
        list: '/proecharts/pmpProjectEchart/myPmpMember'
      }
    }
  },
  computed: {},
  methods: {},
  created() {
    //初始化字典 - 成员
    initDictOptions('sys_user,realname,username').then(res => {
      if (res.success) {
        this.name = res.result
      }
    })
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>