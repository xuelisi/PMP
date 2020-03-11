<template>
  <a-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    :destroyOnClose="true"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">

        <a-form-item label="父级节点" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-tree-select
            ref="treeSelect"
            placeholder="请选择父级节点"
            v-decorator="['parentnode', validatorRules.parentnode]"
            dict="pmp_projecttwotask,taskname,id"
            pidField="parentnode"
            pidValue="0"
            hasChildField="has_child">
          </j-tree-select>
        </a-form-item>
        <a-form-item label="头像" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-upload v-decorator="['photo']" :trigger-change="true"></j-upload>
        </a-form-item>
        <a-form-item label="项目名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'projectname', validatorRules.projectname]" placeholder="请输入项目名称"></a-input>
        </a-form-item>
        <a-form-item label="任务名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'taskname', validatorRules.taskname]" placeholder="请输入任务名称"></a-input>
        </a-form-item>
        <a-form-item label="项目类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['projecttype']" :trigger-change="true" dictCode="project_type" placeholder="请选择项目类型"/>
        </a-form-item>
        <a-form-item label="内容" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'projectcontent', validatorRules.projectcontent]" placeholder="请输入内容"></a-input>
        </a-form-item>
        <a-form-item label="负责人" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-select-user-by-dep v-decorator="['principal']" :trigger-change="true"/>
        </a-form-item>
        <a-form-item label="参与者" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-select-user-by-dep v-decorator="['participant']" :trigger-change="true"/>
        </a-form-item>
        <a-form-item label="开始日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择开始日期" v-decorator="[ 'startdate', validatorRules.startdate]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="结束日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择结束日期" v-decorator="[ 'enddate', validatorRules.enddate]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="进度" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'schedule', validatorRules.schedule]" placeholder="请输入进度" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="费用" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'projectmoney', validatorRules.projectmoney]" placeholder="请输入费用" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'remark', validatorRules.remark]" placeholder="请输入备注"></a-input>
        </a-form-item>
        <a-form-item label="附件" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-upload v-decorator="['annex']" :trigger-change="true"></j-upload>
        </a-form-item>
        
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import JDate from '@/components/jeecg/JDate'  
  import JUpload from '@/components/jeecg/JUpload'
  import JSelectUserByDep from '@/components/jeecgbiz/JSelectUserByDep'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import JTreeSelect from '@/components/jeecg/JTreeSelect'
  
  export default {
    name: "PmpProjecttwotaskModal",
    components: { 
      JDate,
      JUpload,
      JSelectUserByDep,
      JDictSelectTag,
      JTreeSelect
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
        validatorRules:{
        parentnode:{},
        photo:{},
        projectname:{rules: [{ required: true, message: '请输入项目名称!' }]},
        taskname:{rules: [{ required: true, message: '请输入任务名称!' }]},
        projecttype:{rules: [{ required: true, message: '请输入项目类型!' }]},
        projectcontent:{},
        principal:{rules: [{ required: true, message: '请输入负责人!' }]},
        participant:{},
        startdate:{rules: [{ required: true, message: '请输入开始日期!' }]},
        enddate:{rules: [{ required: true, message: '请输入结束日期!' }]},
        schedule:{},
        projectmoney:{},
        remark:{},
        annex:{},
        },
        url: {
          add: "/projecttwotask/pmpProjecttwotask/add",
          edit: "/projecttwotask/pmpProjecttwotask/edit",
        },
        expandedRowKeys:[],
        pidField:"parentnode"
     
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
          this.form.setFieldsValue(pick(this.model,'parentnode','photo','projectname','taskname','projecttype','projectcontent','principal','participant','startdate','enddate','schedule','projectmoney','remark','annex','isdelete'))
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
            let old_pid = this.model[this.pidField]
            let formData = Object.assign(this.model, values);
            let new_pid = this.model[this.pidField]
            console.log("表单提交数据",formData)
            httpAction(httpurl,formData,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.submitSuccess(formData,old_pid==new_pid)
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
        this.form.setFieldsValue(pick(row,'parentnode','photo','projectname','taskname','projecttype','projectcontent','principal','participant','startdate','enddate','schedule','projectmoney','remark','annex','isdelete'))
      },
      submitSuccess(formData,flag){
        if(!formData.id){
          let treeData = this.$refs.treeSelect.getCurrTreeData()
          this.expandedRowKeys=[]
          this.getExpandKeysByPid(formData[this.pidField],treeData,treeData)
          this.$emit('ok',formData,this.expandedRowKeys.reverse());
        }else{
          this.$emit('ok',formData,flag);
        }
      },
      getExpandKeysByPid(pid,arr,all){
        if(pid && arr && arr.length>0){
          for(let i=0;i<arr.length;i++){
            if(arr[i].key==pid){
              this.expandedRowKeys.push(arr[i].key)
              this.getExpandKeysByPid(arr[i]['parentId'],all,all)
            }else{
              this.getExpandKeysByPid(pid,arr[i].children,all)
            }
          }
        }
      }
      
      
    }
  }
</script>