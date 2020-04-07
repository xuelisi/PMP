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

        <a-row :gutter="10">
          <a-col :lg="12">
            <a-form-item label="任务名称" :labelCol="inputlabelCol" :wrapperCol="inputwrapperCol">
              <j-select-pmp
                v-decorator="['taskid', validatorRules.taskid]"
                :trigger-change="true"
                customReturnField="id">
              </j-select-pmp>
            </a-form-item>
          </a-col>

          <a-col :lg="12">
            <a-form-item label="小结日期" :labelCol="inputlabelCol" :wrapperCol="inputwrapperCol">
              <j-date
                v-model="summaryTime"
                :showTime="false"
                dateFormat="YYYY-MM-DD"
                v-decorator="['summaryTime', validatorRules.summaryTime]"
              />
            </a-form-item>
          </a-col>
        </a-row>

        <a-form-item
          label="日报内容"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol">
          <j-editor
            :toolbar="customToolBar"
            v-decorator="['content', validatorRules.content]"
            :trigger-change="true"/>
        </a-form-item>

        <a-form-item
          label="附件"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol">
          <j-upload
            v-decorator="['contentAnnex', validatorRules.contentAnnex]"
            :trigger-change="true">
          </j-upload>
        </a-form-item>

      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>

  import pick from 'lodash.pick'
  import { httpAction } from '@/api/manage'
  import JDate from '@/components/jeecg/JDate'
  import JUpload from '@/components/jeecg/JUpload'
  import JEditor from '@/components/jeecg/JEditor'
  import { validateDuplicateValue } from '@/utils/util'
  import JTreeSelect from '@/components/jeecg/JTreeSelect'
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  import { mapState, mapGetters, mapActions, mapMutations } from 'vuex'
  import JSelectPmp from '@/components/jeecgbiz/JSelectPmp.vue'

  export default {
    name: "PmpSummaryModal",
    components: {
      JDate,
      JUpload,
      JEditor,
      JSelectPmp,
      JDictSelectTag,
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        width:800,
        visible: false,
        model: {},
        summaryTime: '',
        customToolBar: 'undo redo |  formatselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent',
        labelCol: {
          xs: { span: 24 },
          sm: { span: 3 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 20 },
        },
        inputlabelCol: {
          xs: { span: 20 },
          sm: { span: 7 },
        },
        inputwrapperCol: {
          xs: { span: 20 },
          sm: { span: 15 },
        },
        confirmLoading: false,
        validatorRules: {
          taskid: {rules: [
              {required: true, message: '请选择任务!'},
            ]},
          summaryTime: {rules: [
              {required: true, message: '请输入日报日期!'},
              {validator: this.isDateValid, trigger: 'blur', message: '日期超出范围，请重新选择!'}
            ]},
          content: {rules: [
            {required: true, message: '请输入日报内容!'},
          ]},
        },
        url: {
          add: "/summary/pmpSummary/add",
          edit: "/summary/pmpSummary/edit",
        },
      }
    },
    created () {
    },
    methods: {
      ...mapGetters(['userInfo']),
      add () {
        this.edit({ summaryTime: this.getNow() });
      },
      add (id) {
        this.edit({ taskid: id, summaryTime: this.getNow() });
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'id','taskid', 'summaryTime','content','contentAnnex'))
        })
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      isDateValid() {
        if (this.summaryTime <= this.getNow())
          return true;

        return false;
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
            formData.createBy = this.userInfo().username;
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
        this.form.setFieldsValue(pick(row,'taskid','content','contentAnnex'))
      },
      getNow() {
        var date = new Date();
        var sign = "-";
        var year = date.getFullYear();
        var month = date.getMonth() + 1;
        var day = date.getDate();

        if (month >= 1 && month <= 9) {
          month = "0" + month;
        }

        if (day >= 0 && day <= 9) {
          day = "0" + day;
        }

        return year + sign + month + sign + day;
      }
    }
  }
</script>