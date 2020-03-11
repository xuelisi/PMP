<template>
  <a-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭"
  >
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item label="项目图标" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-upload v-decorator="['photo', validatorRules.photo]" :trigger-change="true"></j-upload>
        </a-form-item>
        <a-form-item label="项目名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'projectname', validatorRules.projectname]" placeholder="请输入项目名称"></a-input>
        </a-form-item>
         <a-form-item label="项目类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-row :gutter="24">
            <a-col :span="12">
              <j-dict-select-tag
                type="list"
                v-decorator="['projecttype', validatorRules.projecttype]"
                :trigger-change="true"
                dictCode="project_type"
                placeholder="请选择项目类型"
              />
            </a-col>
          </a-row>
        </a-form-item>
        <a-form-item label="项目内容" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input
            v-decorator="[ 'projectcontent', validatorRules.projectcontent]"
            placeholder="请输入项目内容"
          ></a-input>
        </a-form-item>
        <a-form-item label="负责人" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-select-user-by-dep
            v-decorator="['principal', validatorRules.principal]"
            :trigger-change="true"
          />
        </a-form-item>
        <a-form-item label="参与人" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-select-user-by-dep
            v-decorator="['participant', validatorRules.participant]"
            :trigger-change="true"
          />
        </a-form-item>
        <a-form-item label="紧急程度" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag
            type="list"
            v-decorator="['emergencylevel', validatorRules.emergencylevel]"
            :trigger-change="true"
            dictCode="urgent_level"
            placeholder="请选择紧急程度"
          />
        </a-form-item>
        <a-form-item label="起止日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
           <!-- <a-range-picker v-decorator="['range-picker']" /> -->
          <a-col :span="11">
            <j-date
              placeholder="请选择起始日期"
              v-decorator="[ 'startdate', validatorRules.startdate]"
              :trigger-change="true"
              style="width: 100%"
            />
          </a-col>
          <a-col :span="1" style="text-align:center"> - </a-col>
          <a-col :span="12">
            <j-date
              placeholder="请选择结束日期"
              v-decorator="[ 'enddate', validatorRules.enddate]"
              :trigger-change="true"
              style="width: 100%"
            />
          </a-col>
        </a-form-item>
        <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'remark', validatorRules.remark]" placeholder="请输入备注"></a-input>
        </a-form-item>
        <a-form-item label="附件" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-upload v-decorator="['annex', validatorRules.annex]" :trigger-change="true"></j-upload>
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
import JDictSelectTag from '@/components/dict/JDictSelectTag'

export default {
  name: 'PmpProjectManageModal',
  components: {
    JDate,
    JUpload,
    JSelectUserByDep,
    JDictSelectTag
  },
  data() {
    return {

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
        photo: { rules: [
          
          // { required: true }
          
          
          ] },
        projectname: { rules: [{ required: true, message: '请输入项目名称!' }] },
        projecttype: { rules: [{ required: true, message: '请输入项目类型!' }] },
        principal: { rules: [{ required: true, message: '请输入负责人!' }] },
        participant: { rules: [] },
        emergencylevel: { rules: [{ required: true, message: '请输入紧急程度!' }] },
        startdate: { rules: [{ required: true, message: '请输入起始日期!' }] },
        enddate: { rules: [{ required: true, message: '请输入结束日期!' }] },
        remark: { rules: [] },
        annex: { rules: [] }
      },
      url: {
        add: '/protree/pmpProject/addpro',
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
            'photo',
            'projectname',
            'projecttype',
            'projectcontent',
            'principal',
            'participant',
            'emergencylevel',
            'startdate',
            'enddate',
            'schedule',
            'remark',
            'annex',
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
          'photo',
          'projectname',
          'projecttype',
          'projectcontent',
          'principal',
          'participant',
          'emergencylevel',
          'startdate',
          'enddate',
          'schedule',
          'remark',
          'annex',
          'isdelete'
        )
      )
    }
  }
}
</script>