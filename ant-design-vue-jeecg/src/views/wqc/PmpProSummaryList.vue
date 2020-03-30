<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">

    </div>
    <!-- 查询区域-END -->

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        bordered
        rowKey="id"
        :columns="columns"
        :loading="loading"
        :dataSource="dataSource"
        :pagination="ipagination"
        @change="handleTableChange">


        <!--<template slot="ellipsisSlot" slot-scope="text">-->
          <!--<j-ellipsis :value="text"></j-ellipsis>-->
        <!--</template>-->

        <template slot="ellipsisSlot" slot-scope="text">
          <div slot="actions">
            <a-popover>
              <template slot="content">
                <p style="max-width: 500px;">{{ rmHtmlLabel(text) }}</p>
              </template>
              <span>{{ subText(rmHtmlLabel(text)) }}</span>
            </a-popover>
          </div>

        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无此图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt="图片不存在" style="max-width:80px;font-size: 12px;font-style: italic;"/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无此文件</span>
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="uploadFile(text)">
            下载
          </a-button>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <pmpTaskSummary-modal ref="modalForm" @ok="modalFormOk"></pmpTaskSummary-modal>
  </a-card>
</template>

<script>

  import JEllipsis from '@/components/jeecg/JEllipsis'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import PmpTaskSummaryModal from './modules/PmpProSummaryModal'

  const columns = [
      {
        title: '#',
        dataIndex: '',
        key:'rowIndex',
        width:60,
        align:"center",
        customRender:function (t,r,index) {
          return parseInt(index)+1;
        }
      },
      {
        title:'项目名称',
        align:"center",
        dataIndex: 'projectName',
      },
      {
        title:'任务名称',
        align:"center",
        dataIndex: 'taskName',
      },
      {
        title:'小结内容',
        align:"center",
        dataIndex: 'content',
        // ellipsis: true,
        scopedSlots: {customRender: 'ellipsisSlot'}
      },
      {
        title:'小结日期',
        align:"center",
        dataIndex: 'summaryTime',
      },
      {
        title:'填报时间',
        align:"center",
        dataIndex: 'createTime',
      },
      {
        title: '操作',
        dataIndex: 'action',
        align:"center",
        scopedSlots: { customRender: 'action' }
      }
    ]

  export default {
    name: "PmpProSummaryList",
    mixins:[JeecgListMixin, JEllipsis],
    components: {
      JEllipsis,
      PmpTaskSummaryModal
    },
    data () {
      return {
        columns,
        url: {
          list: "/summary/pmpSummary/list",
          add: "/summary/pmpSummary/add",
          delete: "/summary/pmpSummary/delete",
        },
      }
    },
    methods: {
      //剔除html标签
      rmHtmlLabel(str) {
        return str.replace(/<[^>]+>/g, '');
      },
      //文本限长
      subText(str) {
        let len = 10;
        if (str.length > len)
          return str.substring(0, len) + '...';
        else
          return str;
      },
       
    }
  }
</script>

<style>
</style>