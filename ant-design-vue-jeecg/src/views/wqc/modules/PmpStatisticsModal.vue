<template>
  <a-modal ref="modal"
           :mask="false"
           :title="title"
           :width="width"
           :footer="null"
           :closable="false"
           :visible="visible"
           @cancel="close">
    <a-spin :spinning="false">

      <div class="scroll-wrap">
        <a-list itemLayout="vertical" size="small" :dataSource="summaryData">
          <a-list-item
            slot="renderItem"
            slot-scope="item, index">

            <a-list-item-meta>

              <a slot="title">{{ item.title }}</a>
              <a-avatar slot="avatar" icon="schedule"/>
              <a class="list-content" slot="description" v-html="item.content"/>

            </a-list-item-meta>

            <a-upload :fileList="item.annex">
            </a-upload>

          </a-list-item>
        </a-list>
      </div>

    </a-spin>
  </a-modal>
</template>

<script>

  import {httpAction} from '@/api/manage'
  import {deleteAction, getAction, downFile} from '@/api/manage'
  import {debug, getDay, getDate, getFileName, dateFormat} from '@/api/wqc'

  export default {
    name: "PmpStatisticsModal",
    components: {},
    data() {
      return {
        title: '',
        width: 650,
        visible: false,
        isMove: false,
        isEnter: false,
        summaryData: [],
        curMousePos: { x: '', y: '' },
        url: {
          query: "/summary/pmpSummary/list",
        },
      }
    },
    updated() {
      this.$nextTick(function () {
        this.addLListener();
      })
    },
    methods: {
      show(time, record) {
        this.visible = true;
        this.title = record.realName + "[" + time + "]";

        this.loadSummary(time, record.userName);
      },
      addLListener() {
        window.addEventListener('mousemove', this.globalMoveEvent);

        let eles = document.getElementsByClassName('ant-modal-content');
        if (eles.length > 0) {
          eles[0].addEventListener('mousemove', this.mouseMoveEvent);
          eles[0].addEventListener('mouseenter', this.mouseEnterEvent);
          eles[0].addEventListener('mouseleave', this.mouseLeaveEvent);
        }
      },
      loadAnnex(annex) {
        let linkList = [];

        if ((null != annex) && (annex.length > 0)) {
          let annexList = annex.split(',');

          for (let idx in annexList) {
            linkList.push({
              uid: -1,
              name: getFileName(annexList[idx]),
              url: window._CONFIG['domianURL'] + "/sys/common/download/" + annexList[idx],
            });
          }
        }

        return linkList;
      },
      loadTitle(result) {
        return result.projectName + '-' + result.taskName + '[' + result.createTime + ']';
      },
      loadSummary(time, userName) {
        this.summaryData = [];
        getAction(this.url.query, {summaryTime: time, owner: userName}).then((res) => {
          if (res.success) {
            for (let i = 0; i < res.result.total; ++i) {
              this.summaryData.push({
                title: this.loadTitle(res.result.records[i]),
                content: res.result.records[i].content,
                annex: this.loadAnnex(res.result.records[i].contentAnnex),
              });
            }
          }
        })
      },
      close() {
        this.visible = false;
      },
      globalMoveEvent(e) {
        debug(e.y);
        debug(this.curMousePos.y);
        if ((e.y> this.curMousePos.y) && !this.isEnter)
          this.visible = false;
      },
      mouseMoveEvent() {
        if (!this.isEnter) {
          this.isMove = true;
        }
      },
      mouseEnterEvent() {
        this.isEnter = true;
      },
      mouseLeaveEvent() {
        if (this.isMove) {
          this.visible = false;
          this.curMousePos.x = 0;
          this.curMousePos.y = 0;
        }

        this.isMove = false;
        this.isEnter = false;
      },
    }
  }

</script>

<style>
  .ant-popover-title {
    height: 30px;
    line-height: 30px;
    text-align: left;
  }

  .ant-card-body {
    padding: 0;
  }

  .ant-list-vertical .ant-list-item-meta {
    margin-bottom: 0px;
  }

  .ant-list-vertical .ant-list-item-content {
    margin: 0;
    display: block;
    font-size: 14px;
    color: rgba(0, 0, 0, 0.65);
  }

  .scroll-wrap {
    margin: 0;
    padding: 0;
    width: 600px;
    overflow: hidden;
    overflow-y: scroll;
    height: 300px;
  }
</style>
