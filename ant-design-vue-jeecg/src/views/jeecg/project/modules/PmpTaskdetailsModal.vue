<template>
  <a-drawer
    :title="title"
    :width="width"
    placement="right"
    :closable="false"
    @close="close"
    :visible="visible"
  >
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item label="名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'taskname', validatorRules.taskname]" placeholder="请输入任务名称"></a-input>
        </a-form-item>
        <a-form-item label="任务内容" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'taskcontent', validatorRules.taskcontent]" placeholder="请输入任务内容"></a-input>
        </a-form-item>
        <a-form-item label="负责人" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-select-user-by-dep
            v-decorator="['taskprincipal', validatorRules.taskprincipal]"
            :trigger-change="true"
          />
        </a-form-item>
        <a-form-item label="任务进度" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-row>
            <a-col :span="19">
              <a-slider :min="0" :max="100" v-model="inputValue" />
            </a-col>
            <a-col :span="4">
              <a-input-number :min="0" :max="100" style="marginLeft: 16px" v-model="inputValue" />
            </a-col>
          </a-row>
        </a-form-item>
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="起止日期">
          <a-range-picker v-decorator="['startdate', validatorRules.rangeConfig]" />
        </a-form-item>
        <a-form-item label="紧急程度" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-row>
            <a-col :span="12">
              <j-dict-select-tag
                type="list"
                v-decorator="['emergencylevel', validatorRules.emergencylevel]"
                :trigger-change="true"
                dictCode="urgent_level"
                placeholder="请选择紧急程度"
              />
            </a-col>
          </a-row>
        </a-form-item>
        <a-form-item label="任务费用" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-row>
            <a-col :span="12">
              <a-input-number
                v-decorator="[ 'taskmoney', validatorRules.taskmoney]"
              />
              <span class="ant-form-text">RMB</span>
            </a-col>
          </a-row>
        </a-form-item>
        <a-form-item label="任务备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'taskremark', validatorRules.taskremark]" placeholder="请输入任务备注"></a-input>
        </a-form-item>
      </a-form>
    </a-spin>
    <a-button type="primary" @click="handleOk">确定</a-button>
    <a-button type="primary" @click="handleCancel">取消</a-button>
  </a-drawer>
</template>

<script>
import { httpAction } from '@/api/manage'
import pick from 'lodash.pick'
import { validateDuplicateValue } from '@/utils/util'
import JDate from '@/components/jeecg/JDate'
import JSelectUserByDep from '@/components/jeecgbiz/JSelectUserByDep'
import JDictSelectTag from '@/components/dict/JDictSelectTag'

export default {
  name: 'PmpTaskdetailsModal',
  components: {
    JDate,
    JSelectUserByDep,
    JDictSelectTag
  },
  data() {
    return {
      form: this.$form.createForm(this),
      title: '操作',
      width: 800,
      visible: false,
      inputValue: 0,
      model: {},
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 }
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 }
      },
      confirmLoading: false,
      validatorRules: {
        taskname: { rules: [{ required: true, message: '请输入任务名称!' }] },
        taskcontent: { rules: [] },
        taskprincipal: { rules: [{ required: true, message: '请输入负责人!' }] },
        taskschedule: { rules: [] },
        rangeConfig: {
          rules: [{ type: 'array', required: true, message: '请选择起止日期!' }]
        },
        emergencylevel: { rules: [{ required: true, message: '请输入紧急程度!' }] },
        taskmoney: { rules: [] },
        taskremark: { rules: [] }
      },
      url: {
        add: '/task/pmpTaskdetails/add',
        edit: '/task/pmpTaskdetails/edit'
      }
    }
  },
  created() {},
  methods: {
    add() {
      this.edit({})
    },
    edit(record) {
      this.form.resetFields()
      this.model = Object.assign({}, record)
      this.visible = true
      this.$nextTick(() => {
        this.form.setFieldsValue(
          pick(
            this.model,
            'taskname',
            'taskcontent',
            'taskprincipal',
            'taskschedule',
            'startdate',
            'enddate',
            'emergencylevel',
            'taskmoney',
            'taskremark',
            'isdelete'
          )
        )
      })
    },
    close() {
      this.$emit('close')
      this.visible = false
    },
    handleOk() {
      const that = this
      // 触发表单验证
      this.form.validateFields((err, values) => {
        if (!err) {
          that.confirmLoading = true
          let httpurl = ''
          let method = ''
          if (!this.model.id) {
            httpurl += this.url.add
            method = 'post'
          } else {
            httpurl += this.url.edit
            method = 'put'
          }
          let formData = Object.assign(this.model, values)
          console.log('表单提交数据', formData)
          httpAction(httpurl, formData, method)
            .then(res => {
              if (res.success) {
                that.$message.success(res.message)
                that.$emit('ok')
              } else {
                that.$message.warning(res.message)
              }
            })
            .finally(() => {
              that.confirmLoading = false
              that.close()
            })
        }
      })
    },
    handleCancel() {
      this.close()
    },
    popupCallback(row) {
      this.form.setFieldsValue(
        pick(
          row,
          'taskname',
          'taskcontent',
          'taskprincipal',
          'taskschedule',
          'startdate',
          'enddate',
          'emergencylevel',
          'taskmoney',
          'taskremark',
          'isdelete'
        )
      )
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