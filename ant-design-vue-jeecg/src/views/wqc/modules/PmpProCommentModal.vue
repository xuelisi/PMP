<template>
  <a-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    :footer="null"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">

        <a-row :gutter="25">
          <a-col :lg="12">
            <a-form-item label="项目名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'projectName', validatorRules.projectName]" :readonly="true" placeholder=""></a-input>
            </a-form-item>
          </a-col>
          <a-col :lg="12">
            <a-form-item label="任务名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'taskName', validatorRules.taskName]" :readonly="true" placeholder=""></a-input>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="24">
          <a-col :lg="12">
            <a-form-item label="提交人员" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'createBy', validatorRules.projectName]" :readonly="true" placeholder=""></a-input>
            </a-form-item>
          </a-col>
          <a-col :lg="12">
            <a-form-item label="提交时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'createTime', validatorRules.taskName]" :readonly="true" placeholder=""></a-input>
            </a-form-item>
          </a-col>
        </a-row>

        <a-form-item label="评论内容" :labelCol="ctlabelCol" :wrapperCol="ccwrapperCol">
          <a-textarea v-decorator="[ 'content', validatorRules.content]" placeholder="评论..."
                      :autosize="{ minRows: 3}"
                      :readOnly="true">
          </a-textarea>
        </a-form-item>

      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JEditor from '@/components/jeecg/JEditor'

  export default {
    name: "PmpCommentModal",
    components: {
      JEditor,
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        width:800,
        visible: false,
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        ctlabelCol: {
          xs: { span: 24 },
          sm: { span: 3 },
        },
        ccwrapperCol: {
          xs: { span: 24 },
          sm: { span: 19 },
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
        }
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
          this.form.setFieldsValue(pick(this.model,'projectName','taskName','createBy','createTime','content'))
        })
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {
        const that = this;
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
            console.log("表单提交数据",formData)
            httpAction(httpurl,formData,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
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


    }
  }
</script>