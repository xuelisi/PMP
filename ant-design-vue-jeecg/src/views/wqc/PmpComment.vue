<template>
  <a-card :bordered="false">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">

          <a-col :md="6" :sm="8">
            <a-form-item label="起止日期">
              <a-range-picker
                v-model="rangeDate"
                @change="onChange" />
            </a-form-item>
          </a-col>

          <template v-if="toggleSearchStatus">
            <a-col :md="6" :sm="8">
              <a-form-item label="项目名称">
                <a-input placeholder="请输入项目名称" v-model="queryParam.projectName"></a-input>
              </a-form-item>
            </a-col>

            <a-col :md="6" :sm="8">
              <a-form-item label="任务名称">
                <a-input placeholder="请输入任务名称" v-model="queryParam.taskName"></a-input>
              </a-form-item>
            </a-col>

            <a-col :md="6" :sm="8">
              <a-form-item label="评论人员">
                <a-input placeholder="请输入评论人员" v-model="queryParam.commentator"></a-input>
              </a-form-item>
            </a-col>

            <a-col :md="6" :sm="8">
              <a-form-item label="评论类型">
                <a-input placeholder="请输入评论类型" v-model="queryParam.category"></a-input>
              </a-form-item>
            </a-col>
          </template>

          <a-col :md="6" :sm="8">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>
          </a-col>

        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- table区域-begin -->
    <div>
      <a-table
        bordered
        rowKey="id"
        :loading="loading"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        @change="handleTableChange">

        <template slot="ellipsisSlot" slot-scope="text">
          <j-ellipsis :value="text"/>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="commentEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => commentDelete(record)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <pmp-comment-edt-modal ref="modalForm" @ok="modalFormOk"></pmp-comment-edt-modal>
  </a-card>
</template>

<script>

  import moment from 'moment';
  import JDate from '@/components/jeecg/JDate.vue'
  import JEllipsis from '@/components/jeecg/JEllipsis'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import { deleteAction, getAction,downFile } from '@/api/manage'
  import { mapState, mapGetters, mapActions, mapMutations } from 'vuex'
  import { initDictOptions, filterDictText, filterMultiDictText } from '@/components/dict/JDictSelectUtil'

  import PmpCommentEdtModal from './modules/PmpCommentEdtModal'
  import ACol from "ant-design-vue/es/grid/Col";

  const columns = [
    {
      title: '#',
      dataIndex: '',
      key:'rowIndex',
      width:60,
      align:"center",
      customRender: function (t, r, index) {
        return parseInt(index)+1;
      }
    },
    {
      title:'项目名称',
      align:"center",
      dataIndex: 'projectName'
    },
    {
      title:'任务名称',
      align:"center",
      dataIndex: 'taskName'
    },
    {
      title:'评论内容',
      align:"center",
      dataIndex: 'content',
      scopedSlots: {customRender: 'ellipsisSlot'}
    },
    {
      title:'评论人员',
      align:"center",
      dataIndex: 'realName',
    },
    {
      title:'评论类型',
      align:"center",
      dataIndex: 'category',
    },
    {
      title:'评论时间',
      align:"center",
      dataIndex: 'createTime',
    },
    {
      title: '操作',
      align:"center",
      dataIndex: 'action',
      scopedSlots: { customRender: 'action' }
    }
  ]

  export default {
    name: "PmpComment",
    mixins:[JeecgListMixin, JEllipsis],
    components: {
      ACol,
      JDate,
      JEllipsis,
      PmpCommentEdtModal
    },
    data () {
      return {
        columns,
        queryParam: {
          taskName: '',
          projectName: '',
          commentator: '',
          category: ''
        },
        rangeDate: [moment(this.date), moment(this.date)],
        url: {
          list: "/summary/pmpComment/list",
          delete: "/summary/pmpComment/delete",
        },
      }
    },
    mounted() {
      this.searchReset();
    },
    methods: {
      ...mapGetters(['userInfo']),
      initRangeDate() {
        this.rangeDate = [moment(this.getBeforeDate(30)), moment(this.getBeforeDate(-1))];
      },
      getBeforeDate(count) {
        let newDate = new Date();
        return newDate.setDate(newDate.getDate() - count);
      },
      onChange(date, dateString) {
        if (dateString[0].length < 2) {
          this.initRangeDate();
        }
      },
      searchQuery() {
        this.queryParam.begDate = this.rangeDate[0].format("YYYY-MM-DD");
        this.queryParam.endDate = this.rangeDate[1].format("YYYY-MM-DD");

        this.loadData(1);
      },
      searchReset() {
        this.initRangeDate();

        this.queryParam.taskName = '';
        this.queryParam.projectName = '';
        this.queryParam.commentator = '';
        this.queryParam.category = '';

        this.searchQuery();
      },
      openNotification(title, des) {
        this.$notification.open({
          message: title,
          description: des,
          icon: <a-icon type="frown" style="color: red" />
      })
      },
      isOwner(record) {
        return (record.createBy.indexOf(this.userInfo().username) >= 0);
      },
      commentEdit(record) {
        if (this.isOwner(record)) {
          this.$refs.modalForm.edit(record);
        } else {
          this.openNotification('提示', '权限不够哦,无法编辑评论！')
        }
      },
      commentDelete(record) {
        if (this.isOwner(record)) {
          if(!this.url.delete){
            this.$message.error("请设置url.delete属性!")
            return
          }
          let that = this;
          deleteAction(that.url.delete, {id: record.id}).then((res) => {
            if (res.success) {
              that.$message.success(res.message);
              that.loadData();
            } else {
              that.$message.warning(res.message);
            }
          });
        } else {
          this.openNotification('提示', '权限不够哦,无法删除评论！')
        }
      }
    }
  }
</script>

<style scoped>
  @import '~@assets/less/common.less'
</style>