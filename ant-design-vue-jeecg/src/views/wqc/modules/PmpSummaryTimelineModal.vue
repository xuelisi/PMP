<template>
  <a-modal
    :title="title"
    :visible="visible"
    footer=""
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">

    <div class="scroll_block">
      <a-timeline mode="left" :pending="false">

        <a-timeline-item v-for="(item, index) in items" :key="index ">
          <a-icon slot="dot" type="clock-circle-o" style="font-size: 16px;" />
          {{item.log}}
            <a-popover>
              <template slot="content">
                <p>{{ rmHtmlLabel(item.details) }}</p>
              </template>
              <a>详情</a>
            </a-popover>
        </a-timeline-item>

      </a-timeline>
    </div>

  </a-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import { deleteAction, getAction,downFile } from '@/api/manage'

  export default {
    name: "PmpCommentTimelineModal",
    components: {
    },
    data() {
      return {
        recvRecord: [],
        visible: false,
        items: [],
        url: {
          time: "/summary/pmpSummary/queryByDate",
        }
      }
    },
    methods: {
      show(record) {
        this.visible = true;
        this.recvRecord = record;
        this.title = this.recvRecord.time.format('YYYY-MM-DD') + " 日报统计";

        this.loadSummarys(this.recvRecord.time.format('YYYY-MM-DD'));
      },
      handleCancel() {
        this.items = [];
        this.visible = false;
      },
      loadSummarys(time) {
        getAction(this.url.time, { date: time }).then((res) => {
          if (res.success) {
            for(let i = 0; i < res.result.length; ++i) {
              this.items.push({
                log: res.result[i].realName + "在" + res.result[i].createTime + " 提交了小结 ",
                details: res.result[i].content,
              });
            }
          }
        })
      },
      //剔除html标签
      rmHtmlLabel(str) {
        return str.replace(/<[^>]+>/g, '');
      }
    }
  }

</script>

<style>
  .scroll_block {
    padding: 10px;
    height: 500px;
    overflow: hidden;
    overflow-y: scroll;
  }
</style>
