<template>
  <a-drawer
    :title="title"
    :width="width"
    placement="right"
    :closable="false"
    @close="close"
    :visible="visible">

    <a-spin :spinning="confirmLoading">

      <a-tabs>
        <a-tab-pane tab="日报"
                    key="1">
          <a-list size="small"
                  itemLayout="vertical"
                  :dataSource="summaryData">
            <a-list-item
              slot="renderItem"
              slot-scope="item, index">

              <a-list-item-meta>

                <a slot="title"
                   style="color: blue">
                  {{ item.title }}
                </a>
                <a-avatar slot="avatar"
                          icon="schedule"/>
                <a slot="description"
                   class="list-content"
                   v-html="item.content"
                   style="color: gray; font-size: 12px;"/>

              </a-list-item-meta>

              <a-upload :fileList="item.annex">
              </a-upload>

            </a-list-item>
          </a-list>
        </a-tab-pane>
        <a-tab-pane tab="评论"
                    key="2">
          <a-list size="small"
                  itemLayout="vertical"
                  :dataSource="commentData">
            <a-list-item
              slot="renderItem"
              slot-scope="item, index">

              <a-list-item-meta>
                <a slot="title"
                   style="color: blue">
                  {{ item.title }}
                </a>
                <a-avatar slot="avatar"
                          icon="schedule"/>
                <a class="list-content"
                   v-html="item.content"
                   style="color: gray; font-size: 12px;" slot="description"/>
              </a-list-item-meta>

              <a-upload :fileList="item.annex">
              </a-upload>

            </a-list-item>
          </a-list>
        </a-tab-pane>
      </a-tabs>


    </a-spin>
    <a-button type="primary" @click="close">关闭</a-button>
  </a-drawer>
</template>

<script>

  import pick from 'lodash.pick'
  import {debug, getDay, getDate, getFileName, dateFormat} from '@/api/wqc'
  import {httpAction} from '@/api/manage'
  import JUpload from '@/components/jeecg/JUpload'
  import {validateDuplicateValue} from '@/utils/util'
  import {deleteAction, getAction, downFile} from '@/api/manage'
  import {initDictOptions, filterDictText, myFilterMultiDictText} from '@/components/dict/JDictSelectUtil'

  export default {
    name: "PmpCSUnionModal",
    components: {
      JUpload,
    },
    data() {
      return {
        principal: [],
        title: "操作",
        width: 800,
        visible: false,
        summaryData: [],
        commentData: [],
        confirmLoading: false,
        url: {
          summary: "/summary/pmpComment/union",
        }
      }
    },
    created() {
      //初始化字典 - 创建人
      initDictOptions('sys_user,realname,username').then(res => {
        if (res.success) {
          this.principal = res.result
        }
      })
    },
    methods: {
      edit(record) {
        this.visible = true;
        this.title = record.projectname + '-' + record.taskname;

        this.loadSummary(record.id);
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
        return this.getRealName(result.createBy) + '[' + result.createTime + ']';
      },
      loadSummary(taskid) {
        this.summaryData = [];
        this.commentData = [];
        getAction(this.url.summary, {taskid: taskid}).then((res) => {
          if (res.success) {
            for (let i = 0; i < res.result.length; ++i) {
              if ('1' === res.result[i].type) {
                this.summaryData.push({
                  title: this.loadTitle(res.result[i]),
                  content: res.result[i].content,
                  annex: this.loadAnnex(res.result[i].contentAnnex),
                  type: res.result[i].type,
                });
              } else {
                this.commentData.push({
                  title: this.loadTitle(res.result[i]),
                  content: res.result[i].content,
                  annex: this.loadAnnex(res.result[i].contentAnnex),
                  type: res.result[i].type,
                });
              }
            }
          }
        })
      },
      close() {
        this.visible = false;
      },
      getRealName(text) {
        return myFilterMultiDictText(this.principal, text);
      }
    }
  }
</script>

<style lang="less" scoped>
  /** Button按钮间距 */
  .ant-btn {
    margin-left: 30px;
    margin-bottom: 30px;
    float: right;
  }
</style>