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
      <a-form :form="form">


        <a-row :gutter="24">
          <a-col :lg="12">
            <a-form-item label="项目id" v-show="false" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'projectId', validatorRules.projectId]" :readonly="true" placeholder="请输入项目id"></a-input>
            </a-form-item>
          </a-col>
          <a-col :lg="12">
            <a-form-item label="任务id" v-show="false" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'taskId', validatorRules.taskId]" :readonly="true" placeholder="请输入任务id"></a-input>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="24">
          <a-col :lg="12">
            <a-form-item label="项目名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'projectName', validatorRules.projectName]" :readonly="true" placeholder="请输入项目名称"></a-input>
            </a-form-item>
          </a-col>
          <a-col :lg="12">
            <a-form-item label="任务名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'taskName', validatorRules.taskName]" :readonly="true" placeholder="请输入任务名称"></a-input>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row>
          <a-col :lg="24" pull="3">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="内容"
              style="margin-left: 5px">
              <j-editor  style="width: 130%" v-decorator="[ 'content', validatorRules.content]" triggerChange placeholder="请输入任务小结"></j-editor>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :lg="24" pull="3">
            <a-form-item label="附件" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-upload v-decorator="['annex', validatorRules.annex]" :trigger-change="true"></j-upload>
            </a-form-item>
          </a-col>
        </a-row>

      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JUpload from '@/components/jeecg/JUpload'
  import JEditor from '@/components/jeecg/JEditor'

  export default {
    name: "PmpTaskSummaryModal",
    components: { 
      JUpload,
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
        confirmLoading: false,
        validatorRules: {
          taskId: {rules: [
          ]},
          projectId: {rules: [
          ]},
          taskName: {rules: [
          ]},
          projectName: {rules: [
          ]},
          content: {rules: [
          ]},
          annex: {rules: [
          ]},
        },
        url: {
          add: "/task/pmpTaskSummary/add",
          edit: "/task/pmpTaskSummary/edit",
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
          this.form.setFieldsValue(pick(this.model,'taskId','projectId','taskName','projectName','content','annex'))
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
        this.form.setFieldsValue(pick(row,'taskId','projectId','taskName','projectName','content','annex'))
      },

      
    }
  }
</script>