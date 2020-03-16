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
          <a-input
            v-decorator="[ 'taskname', validatorRules.taskname]"
            placeholder="请输入名称"
            :read-only="readOnly"
          ></a-input>
        </a-form-item>
        <a-form-item label="任务内容" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input
            v-decorator="[ 'projectcontent', validatorRules.projectcontent]"
            placeholder="请输入任务内容"
          ></a-input>
        </a-form-item>
        <a-form-item label="负责人" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-select-multi-user
            v-decorator="['principal', validatorRules.principal]"
            :trigger-change="true"
          ></j-select-multi-user>
          <!-- <j-select-user-by-dep
            v-decorator="['principal', validatorRules.principal]"
            :trigger-change="true"
          />-->
        </a-form-item>
        <a-form-item label="参与人" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-select-multi-user
            v-decorator="['participant', validatorRules.participant]"
            :trigger-change="true"
          ></j-select-multi-user>
          <!-- <j-select-user-by-dep
            v-decorator="['participant', validatorRules.participant]"
            :trigger-change="true"
          />-->
        </a-form-item>
        <a-form-item label="任务进度" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <!-- <a-input-number v-decorator="[ 'taskschedule', validatorRules.taskschedule]" placeholder="请输入任务进度" style="width: 100%"/> -->
          <a-row>
            <a-col :span="19">
              <a-slider
                :min="0"
                :max="100"
                v-decorator="[ 'schedule', validatorRules.schedule]"
                :trigger-change="true"
              />
            </a-col>
            <a-col :span="4">
              <a-input-number
                :min="0"
                :max="100"
                style="marginLeft: 16px"
                v-decorator="[ 'schedule', validatorRules.schedule]"
                :trigger-change="true"
              />
            </a-col>
          </a-row>
        </a-form-item>
        <a-form-item label="开始日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date
            placeholder="请选择开始日期"
            v-decorator="[ 'startdate', validatorRules.startdate]"
            :trigger-change="true"
            style="width: 100%"
          />
        </a-form-item>
        <a-form-item label="结束日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date
            placeholder="请选择结束日期"
            v-decorator="[ 'enddate', validatorRules.enddate]"
            :trigger-change="true"
            style="width: 100%"
          />
        </a-form-item>
        <!-- <a-form-item label="紧急程度" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag
            type="list"
            v-decorator="['emergencylevel', validatorRules.emergencylevel]"
            :trigger-change="true"
            dictCode="urgent_level"
            placeholder="请选择紧急程度"
          />
        </a-form-item>-->
        <a-form-item label="任务费用" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number
            v-decorator="[ 'projectmoney', validatorRules.projectmoney]"
            placeholder="请输入任务费用"
            style="width: 100%"
          />
        </a-form-item>
        <a-form-item label="任务备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'remark', validatorRules.remark]" placeholder="请输入任务备注"></a-input>
        </a-form-item>
        <a-form-item label="附件" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-upload v-decorator="['annex', validatorRules.annex]" :trigger-change="true"></j-upload>
        </a-form-item>
      </a-form>
    </a-spin>
    <a-row :style="{textAlign:'right'}">
      <a-button :style="{marginRight: '8px'}" @click="handleCancel">关闭</a-button>
      <a-button :disabled="disableSubmit" @click="handleOk" type="primary">确定</a-button>
    </a-row>
  </a-drawer>
</template>

<script>
import { httpAction } from '@/api/manage'
import pick from 'lodash.pick'
import { validateDuplicateValue, randomUUID, handleStatus } from '@/utils/util'
import JDate from '@/components/jeecg/JDate'
import JUpload from '@/components/jeecg/JUpload'
import JSelectUserByDep from '@/components/jeecgbiz/JSelectUserByDep'
import JSelectMultiUser from '@/components/jeecgbiz/JSelectMultiUser'
import JDictSelectTag from '@/components/dict/JDictSelectTag'

export default {
  name: 'PmpTaskdetailsModal',
  components: {
    JDate,
    JUpload,
    JSelectUserByDep,
    JSelectMultiUser,
    JDictSelectTag
  },
  data() {
    return {
      disableSubmit: false,
      readOnly: true,
      form: this.$form.createForm(this),
      title: '操作',
      width: 800,
      visible: false,
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
        taskname: { rules: [{ required: true, message: '请输入名称!' }] },
        projectcontent: { rules: [] },
        principal: { rules: [{ required: true, message: '请输入负责人!' }] },
        participant: { rules: [] },
        schedule: { rules: [] },
        startdate: { rules: [{ required: true, message: '请输入开始日期!' }] },
        enddate: { rules: [{ required: true, message: '请输入结束日期!' }] },
        emergencylevel: { rules: [{ required: true, message: '请输入紧急程度!' }] },
        projectmoney: { rules: [] },
        remark: { rules: [] },
        annex: { rules: [] }
      },
      url: {
        add: '/protree/pmpProject/addTask',
        edit: '/protree/pmpProject/edit'
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
            'projectcontent',
            'principal',
            'participant',
            'schedule',
            'startdate',
            'enddate',
            'emergencylevel',
            'projectmoney',
            'remark',
            'annex',
            'isdelete'
          )
        )
      })
    },
    close() {
      this.$emit('close')
      this.disableSubmit = false
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
          let status = handleStatus(values)
          let params = {
            status: status,
            completiontime: values.schedule === 100 ? new Date().getTime() : null
          }
          let formData = Object.assign(this.model, values, params)
          console.log('表单提交数据', formData)
          httpAction(httpurl, formData, method)
            .then(res => {
              if (res.success) {
                that.$message.success(res.message)
                that.submitSuccess(formData)
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
          'projectcontent',
          'principal',
          'participant',
          'schedule',
          'startdate',
          'enddate',
          'emergencylevel',
          'projectmoney',
          'remark',
          'annex',
          'isdelete'
        )
      )
    },
    submitSuccess(formData) {
      if (!formData.id) {
        let treeData = this.$refs.treeSelect.getCurrTreeData()
        this.expandedRowKeys = []
        this.getExpandKeysByPid(formData[this.pidField], treeData, treeData)
        this.$emit('ok', formData, this.expandedRowKeys.reverse())
      } else {
        this.$emit('ok', formData)
      }
    },
    getExpandKeysByPid(pid, arr, all) {
      if (pid && arr && arr.length > 0) {
        for (let i = 0; i < arr.length; i++) {
          if (arr[i].key == pid) {
            this.expandedRowKeys.push(arr[i].key)
            this.getExpandKeysByPid(arr[i]['parentId'], all, all)
          } else {
            this.getExpandKeysByPid(pid, arr[i].children, all)
          }
        }
      }
    },
    validateMyCode(rule, value, callback) {
      let params = {
        parentnodeId: this.form.getFieldValue('parentnode')
      }
      getAction(this.url.checkCode, params).then(res => {
        if (res.success) {
          callback()
        } else {
          callback(res.message)
        }
      })
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