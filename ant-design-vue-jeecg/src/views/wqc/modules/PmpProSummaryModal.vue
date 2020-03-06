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

        <a-form-item label="名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <j-tree-select
              v-decorator="[ 'projectid', validatorRules.projectid]"
              placeholder="请选择菜单"
              dict="pmp_project,projectname,id"
              pidField="parentnode"
              pidValue="0"
            />
        </a-form-item>
        <a-form-item label="任务小结" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-editor v-decorator="['content',{trigger:'input'}]"/>
        </a-form-item>
        <a-form-item label="附件" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-upload v-decorator="['sumannex', validatorRules.sumannex]" :trigger-change="true"></j-upload>
        </a-form-item>

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
  import JTreeSelect from '@/components/jeecg/JTreeSelect'

  export default {
    name: "PmpTaskSummaryModal",
    components: { 
      JUpload,
      JEditor,
      JTreeSelect,
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
          projectid: {rules: [
            {required: true, message: '请输入任务id!'},
          ]},
          content: {rules: [
            {required: true, message: '请输入任务小结!'},
          ]},
          sumannex: {rules: [
          ]},
        },
        url: {
          add: "/summary/pmpTaskSummary/add",
          edit: "/summary/pmpTaskSummary/edit",
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
          this.form.setFieldsValue(pick(this.model,'projectid','content','sumannex'))
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
        this.form.setFieldsValue(pick(row,'projectid','content','sumannex'))
      },

      
    }
  }
</script>