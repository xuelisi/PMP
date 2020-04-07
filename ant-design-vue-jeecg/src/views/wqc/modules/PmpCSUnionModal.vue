<template>
  <a-drawer
    :title="title"
    :width="width"
    placement="right"
    :closable="false"
    @close="close"
    :visible="visible">
  
    <a-spin :spinning="confirmLoading">

      <a-list itemLayout="vertical" size="small" :dataSource="unionData">
        <a-list-item
          slot="renderItem"
          slot-scope="item, index">

          <a-list-item-meta>

            <a v-if="2 == item.type" style="color: blue" slot="title">{{ item.title + '(评论)' }}</a>
            <a v-else  style="color: green" slot="title">{{ item.title + '(日报)' }}</a>
            <a-avatar slot="avatar" icon="schedule"/>
            <a class="list-content" style="color: gray; font-size: 12px;" slot="description" v-html="item.content" />

          </a-list-item-meta>

          <a-upload :fileList="item.annex">
          </a-upload>

        </a-list-item>
      </a-list>

    </a-spin>
    <a-button type="primary" @click="close">关闭</a-button>
  </a-drawer>
</template>

<script>

  import pick from 'lodash.pick'
  import { httpAction } from '@/api/manage'
  import JUpload from '@/components/jeecg/JUpload'
  import { validateDuplicateValue } from '@/utils/util'
  import { deleteAction, getAction,downFile } from '@/api/manage'
  import { initDictOptions, filterDictText, myFilterMultiDictText } from '@/components/dict/JDictSelectUtil'

  const debug = (msg) => {
    console.log('*************************');
    console.log('*************************');
    console.log(msg);
    console.log('*************************');
    console.log('*************************');
  }

  const getFileName = (path) => {
    if(path.lastIndexOf("\\")>=0){
      let reg=new RegExp("\\\\","g");
      path = path.replace(reg,"/");
    }
    return path.substring(path.lastIndexOf("/")+1);
  }
  
  export default {
    name: "PmpCSUnionModal",
    components: { 
      JUpload,
    },
    data () {
      return {
        principal: [],
        title:"操作",
        width:800,
        visible: false,
        unionData: [],
        confirmLoading: false,
        url: {
          union: "/summary/pmpComment/union",
        }
      }
    },
    created () {
      //初始化字典 - 创建人
      initDictOptions('sys_user,realname,username').then(res => {
        if (res.success) {
          this.principal = res.result
        }
      })
    },
    methods: {
      edit (record) {
        this.visible = true;
        this.title = record.projectname + '-' + record.taskname;

        this.loadSummary(record.id);
      },
      loadAnnex(annex) {
        let linkList = [];

        if ((null != annex) && (annex.length > 0)) {
          let annexList = annex.split(',');

          for(let idx in annexList) {
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
        return this.getRealName(result.createBy) + '[' +  result.createTime + ']';
      },
      loadSummary(taskid) {
        this.unionData = [];
        getAction(this.url.union, { taskid: taskid }).then((res) => {
          if (res.success) {
            for(let i = 0; i < res.result.length; ++i) {
              this.unionData.push({
                title: this.loadTitle(res.result[i]),
                content: res.result[i].content,
                annex: this.loadAnnex(res.result[i].contentAnnex),
                type: res.result[i].type,
              });
            }
          }
        })
      },
      close () {
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