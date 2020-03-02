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

        <a-form-item label="项目图标" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-upload v-decorator="['photo', validatorRules.photo]" :trigger-change="true"></j-upload>
        </a-form-item>
        <a-form-item label="项目名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'xiangmumingcheng', validatorRules.xiangmumingcheng]" placeholder="请输入项目名称"></a-input>
        </a-form-item>
        <a-form-item label="项目编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'xiangmubianhao', validatorRules.xiangmubianhao]" placeholder="请输入项目编号"></a-input>
        </a-form-item>
        <a-form-item label="项目类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['xiangmuleixing', validatorRules.xiangmuleixing]" :trigger-change="true" dictCode="project_type" placeholder="请选择项目类型"/>
        </a-form-item>
        <a-form-item label="项目内容" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-editor v-decorator="['xiangmuneirong',{trigger:'input'}]"/>
        </a-form-item>
        <a-form-item label="负责人" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-select-user-by-dep v-decorator="['fuzeren', validatorRules.fuzeren]" :trigger-change="true"/>
        </a-form-item>
        <a-form-item label="起始日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择起始日期" v-decorator="[ 'qishiriqi', validatorRules.qishiriqi]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="结束日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择结束日期" v-decorator="[ 'jieshuriqi', validatorRules.jieshuriqi]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'beizhu', validatorRules.beizhu]" placeholder="请输入备注"></a-input>
        </a-form-item>
        <a-form-item label="附件" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-upload v-decorator="['fujian', validatorRules.fujian]" :trigger-change="true"></j-upload>
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
  import JUpload from '@/components/jeecg/JUpload'
  import JSelectUserByDep from '@/components/jeecgbiz/JSelectUserByDep'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import JEditor from '@/components/jeecg/JEditor'

  export default {
    name: "ProjectManageModal",
    components: { 
      JDate,
      JUpload,
      JSelectUserByDep,
      JDictSelectTag,
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
          photo: {rules: [
          ]},
          xiangmumingcheng: {rules: [
            {required: true, message: '请输入项目名称!'},
          ]},
          xiangmubianhao: {rules: [
            {required: true, message: '请输入项目编号!'},
            { validator: (rule, value, callback) => validateDuplicateValue('xiangmumain', 'xiangmubianhao', value, this.model.id, callback)},
          ]},
          xiangmuleixing: {rules: [
            {required: true, message: '请输入项目类型!'},
          ]},
          xiangmuneirong: {rules: [
          ]},
          fuzeren: {rules: [
            {required: true, message: '请输入负责人!'},
          ]},
          qishiriqi: {rules: [
            {required: true, message: '请输入起始日期!'},
          ]},
          jieshuriqi: {rules: [
            {required: true, message: '请输入结束日期!'},
          ]},
          beizhu: {rules: [
          ]},
          fujian: {rules: [
          ]},
        },
        url: {
          add: "/project/projectManage/add",
          edit: "/project/projectManage/edit",
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
          this.form.setFieldsValue(pick(this.model,'photo','xiangmumingcheng','xiangmubianhao','xiangmuleixing','xiangmuneirong','fuzeren','qishiriqi','jieshuriqi','zongjindu','beizhu','fujian','isdelete'))
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
        this.form.setFieldsValue(pick(row,'photo','xiangmumingcheng','xiangmubianhao','xiangmuleixing','xiangmuneirong','fuzeren','qishiriqi','jieshuriqi','zongjindu','beizhu','fujian','isdelete'))
      },

      
    }
  }
</script>