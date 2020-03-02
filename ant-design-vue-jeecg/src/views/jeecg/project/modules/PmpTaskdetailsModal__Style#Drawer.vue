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
          <a-input v-decorator="[ 'taskname', validatorRules.taskname]" placeholder="请输入名称"></a-input>
        </a-form-item>
        <a-form-item label="任务内容" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'taskcontent', validatorRules.taskcontent]" placeholder="请输入任务内容"></a-input>
        </a-form-item>
        <a-form-item label="负责人" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-select-user-by-dep v-decorator="['taskprincipal', validatorRules.taskprincipal]" :trigger-change="true"/>
        </a-form-item>
        <a-form-item label="任务进度" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'taskschedule', validatorRules.taskschedule]" placeholder="请输入任务进度" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="开始日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择开始日期" v-decorator="[ 'startdate', validatorRules.startdate]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="结束日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择结束日期" v-decorator="[ 'enddate', validatorRules.enddate]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="紧急程度" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['emergencylevel', validatorRules.emergencylevel]" :trigger-change="true" dictCode="urgent_level" placeholder="请选择紧急程度"/>
        </a-form-item>
        <a-form-item label="任务费用" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'taskmoney', validatorRules.taskmoney]" placeholder="请输入任务费用"></a-input>
        </a-form-item>
        <a-form-item label="任务备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'taskremark', validatorRules.taskremark]" placeholder="请输入任务备注"></a-input>
        </a-form-item>

      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JDate from '@/components/jeecg/JDate'  
  import JSelectUserByDep from '@/components/jeecgbiz/JSelectUserByDep'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"

  export default {
    name: "PmpTaskdetailsModal",
    components: { 
      JDate,
      JSelectUserByDep,
      JDictSelectTag,
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
          taskname: {rules: [
            {required: true, message: '请输入名称!'},
          ]},
          taskcontent: {rules: [
          ]},
          taskprincipal: {rules: [
            {required: true, message: '请输入负责人!'},
          ]},
          taskschedule: {rules: [
          ]},
          startdate: {rules: [
            {required: true, message: '请输入开始日期!'},
          ]},
          enddate: {rules: [
            {required: true, message: '请输入结束日期!'},
          ]},
          emergencylevel: {rules: [
            {required: true, message: '请输入紧急程度!'},
          ]},
          taskmoney: {rules: [
          ]},
          taskremark: {rules: [
          ]},
        },
        url: {
          add: "/task/pmpTaskdetails/add",
          edit: "/task/pmpTaskdetails/edit",
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
          this.form.setFieldsValue(pick(this.model,'taskname','taskcontent','taskprincipal','taskschedule','startdate','enddate','emergencylevel','taskmoney','taskremark','isdelete'))
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
        this.form.setFieldsValue(pick(row,'taskname','taskcontent','taskprincipal','taskschedule','startdate','enddate','emergencylevel','taskmoney','taskremark','isdelete'))
      },

      
    }
  }
</script>