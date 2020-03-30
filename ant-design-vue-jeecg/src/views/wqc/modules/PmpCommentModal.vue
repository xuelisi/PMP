<template>
  <a-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">

      <div class="scroll-wrap">
        <a-list size="small">
          <a-list-item :key="index" v-for="(item, index) in allCmtData">
            <a-list-item-meta
              :description="item.description">
              <a-avatar
                slot="avatar"
                size="small"
                shape="square"
                :src="item.avatar"
              />
              <a slot="title">{{ item.title }}</a>
            </a-list-item-meta>
          </a-list-item>
        </a-list>
      </div>

      <a-form :form="form" >
            <a-form-item label="任务id" :labelCol="labelCol" :wrapperCol="wrapperCol" style="display:none;">
              <a-input v-decorator="[ 'taskid', validatorRules.taskid]" placeholder="请输入任务id"></a-input>
            </a-form-item>
            <a-form-item label="评论" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-textarea v-decorator="[ 'content', validatorRules.content]" placeholder="评论..."
                          :autosize="{ minRows: 3}">
              </a-textarea>
            </a-form-item>
      </a-form>

    </a-spin>
  </a-modal>
</template>

<script>

  import pick from 'lodash.pick'
  import { httpAction } from '@/api/manage'
  import JEditor from '@/components/jeecg/JEditor'
  import { validateDuplicateValue } from '@/utils/util'
  import ATextarea from "ant-design-vue/es/input/TextArea";
  import { deleteAction, getAction,downFile } from '@/api/manage'

  export default {
    name: "PmpCommentModal",
    components: {
      ATextarea,
      JEditor,
    },
    data () {
      return {
        recvRecord: [],
        allCmtData: [],
        ownerCmtData: [],
        form: this.$form.createForm(this),
        title:"操作",
        width: 1000,
        visible: false,
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 2 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 21 },
        },
        confirmLoading: false,
        validatorRules: {
          taskid: {rules: [
          ]},
          content: {rules: [
          ]},
        },
        url: {
          add: "/summary/pmpComment/add",
          edit: "/summary/pmpComment/edit",
          query: "/summary/pmpComment/query",
        },
        hovered: false,
      }
    },
    created () {
    },
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'taskid','content'))
        })
      },
      initPage(record) {
        this.content = "";
        this.allCmtData = [];
        this.taskid = record.id;
        this.title = "当前任务：" + record.taskname;

        this.initComments(this.taskid);
        this.edit({ taskid: this.taskid });
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      show(record) {
        this.visible = true;
        this.recvRecord = record;
        this.initPage(this.recvRecord);
      },
      initComments(taskid) {
        getAction(this.url.query, { taskid: taskid, userName: '' }).then((res) => {
          if (res.success) {
            for(let i = 0; i < res.result.length; ++i) {
              this.allCmtData.push({
                title: res.result[i].createTime,
                description: res.result[i].realName + '：' + res.result[i].content,
                //details: res.result[i].content,
                //avatar: 'https://gw.alipayobjects.com/zos/rmsportal/WdGqmHpayyMjiEhcKoVE.png'
              });
            }
          }
        })
      },
      handleOk () {
        const that = this;
        debugger
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            let formData = Object.assign(this.model, values);
            httpAction(httpurl,formData,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              //that.close();

              this.initPage(this.recvRecord);
            })
          }
        })
      },
      handleCancel () {
        this.close()
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'taskid','content'))
      },
      //剔除html标签
      rmHtmlLabel(str) {
        return str.replace(/<[^>]+>/g, '');
      },
      //文本限长
      subText(str) {
        if (str.length > 100)
          return str.substring(0, 50) + '...';
        else
          return str;
      },
    }
  }
</script>

<style>
  .scroll-wrap {
    padding: 15px;
    height: 280px;
    overflow: hidden;
    overflow-y:scroll;
    margin-bottom: 35px;
  }

</style>