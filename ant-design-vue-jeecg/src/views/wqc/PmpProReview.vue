<template>
  <div>
    <a-card
      style="margin: 30px"
      :bordered="false"
      :title="this.taskInfo">

      <a-list size="large" split="false">
        <a-list-item :key="index"
                     v-for="(item, index) in data">
          <a-list-item-meta :description="item.description">
            <a-avatar slot="avatar" size="small" shape="square" :src="item.avatar"/>
            <a slot="title">{{ item.title }}</a>
          </a-list-item-meta>
          <div class="list-content" style="display: none; width: 500px;">
            <p>{{ item.id }}</p>
          </div>
          <div slot="actions">
            <a>查看</a>
          </div>
          <div slot="actions">
            <a>批阅</a>
          </div>
        </a-list-item>
      </a-list>

    </a-card>
  </div>
</template>

<script>
  import HeadInfo from '@/components/tools/HeadInfo'
  import { deleteAction, getAction,downFile } from '@/api/manage'

  export default {
    name: "PmpProReview",
    components: {
      HeadInfo
    },
    data () {
      return {
        data: [],
        url: {
          info: "/summary/pmpTaskSummary/info",
          list: "/summary/pmpTaskSummary/list",
          summary: "/summary/pmpTaskSummary/summary",
          delete: "/summary/pmpTaskSummary/delete",
          deleteBatch: "/summary/pmpTaskSummary/deleteBatch",
          exportXlsUrl: "/summary/pmpTaskSummary/exportXls",
          importExcelUrl: "summary/pmpTaskSummary/importExcel",
        },
        taskInfo: ""
      }
    },
    mounted() {
      this.loadPmpInfo(this.$route.params.taskid);
      this.loadSummary(this.$route.params.taskid);
    },
    methods: {
      loadPmpInfo(taskid) {
        getAction(this.url.info, { projectid: taskid }).then((res) => {
          if (res.success) {
            this.taskid = res.result.taskId;
            this.taskname = res.result.taskName;
            this.projectid = res.result.projectId;
            this.projectname = res.result.projectName;
            this.taskInfo = "任务名称：" + res.result.taskName;
          }
        })
      },
      loadSummary(taskid) {
        getAction(this.url.summary, { taskid: taskid }).then((res) => {
          if (res.success) {
            for(let i = 0; i < res.result.length; ++i) {
              this.data.push({
                id: res.result[i].id,
                title: res.result[i].createTime,
                description: res.result[i].createBy + '：' + this.strLimitLength(this.removeLabel(res.result[i].content)),
                //avatar: 'https://gw.alipayobjects.com/zos/rmsportal/WdGqmHpayyMjiEhcKoVE.png'
              });
            }
          }
        })
      },
      removeLabel(str) {
        return str.replace(/<[^>]+>/g, '');
      },
      strLimitLength(str) {
        if (str.length > 100)
          return str.substring(0, 50) + '...';
        else
          return str;
      }
    }
  }
</script>

<style lang="scss" scoped>
  .ant-avatar-lg {
    width: 48px;
    height: 48px;
    line-height: 48px;
  }

  .list-content-item {
    color: rgba(0, 0, 0, .45);
    display: inline-block;
    vertical-align: middle;
    font-size: 14px;
    margin-left: 40px;
    span {
      line-height: 20px;
    }
    p {
      margin-top: 4px;
      margin-bottom: 0;
      line-height: 22px;
    }
  }
</style>