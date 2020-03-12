<template>
  <a-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭"
    :okButtonProps="{ props: {disabled: disableSubmit} }"
  >
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <!-- <a-form-item label="项目图标" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-upload v-decorator="['photo', validatorRules.photo]" :trigger-change="true"></j-upload>
        </a-form-item>-->
        <a-form-item label="头像" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-upload
            listType="picture-card"
            class="avatar-uploader"
            :showUploadList="false"
            :action="uploadAction"
            :data="{'isup':1}"
            :headers="headers"
            :beforeUpload="beforeUpload"
            @change="handleChange"
          >
            <img v-if="picUrl" :src="getAvatarView()" alt="头像" style="height:104px;max-width:300px" />
            <div v-else>
              <a-icon :type="uploadLoading ? 'loading' : 'plus'" />
              <div class="ant-upload-text">上传</div>
            </div>
          </a-upload>
        </a-form-item>
        <a-form-item label="项目名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'projectname', validatorRules.projectname]" placeholder="请输入项目名称"></a-input>
        </a-form-item>
        <!--<a-form-item label="项目编码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'projectcode', validatorRules.projectcode]" placeholder="请输入项目编码"></a-input>
        </a-form-item>-->

        <!--<a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol">
          <span style="font-size: 12px;color:red" slot="label">编码规则(注)</span>
          <span style="font-size: 12px;color:red">
            编码值前缀需和父节点保持一致,比如父级节点编码是A01则当前编码必须以A01开头
          </span>
        </a-form-item>-->
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
          <a-col :span="1" style="text-align:center">-</a-col>
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
import Vue from 'vue'
import { validateDuplicateValue } from '@/utils/util'
import JDate from '@/components/jeecg/JDate'
import JUpload from '@/components/jeecg/JUpload'
import { ACCESS_TOKEN } from '@/store/mutation-types'
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
      disableSubmit: false,
      picUrl: '',
      uploadLoading: false,
      headers: {},
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
        projectname: {
          rules: [
            { required: true, message: '请输入项目名称!' },
            {
              validator: (rule, value, callback) =>
                validateDuplicateValue('pmp_project', 'projectname', value, this.model.id, callback)
            }
          ]
        },
        projecttype: { rules: [{ required: true, message: '请输入项目类型!' }] },
        principal: { rules: [{ required: true, message: '请输入负责人!' }] },
        participant: { rules: [] },
        emergencylevel: { rules: [{ required: true, message: '请输入紧急程度!' }] },
        startdate: { rules: [{ required: true, message: '请输入起始日期!' }] },
        enddate: { rules: [{ required: true, message: '请输入结束日期!' }] },
        remark: { rules: [] },
        annex: { rules: [] },
        projectcode: {
          rules: [
            {
              required: true,
              message: '请输入项目编码!'
            },
            {
              validator: this.validateMyCode
            }
          ]
        }
      },
      url: {
        fileUpload: window._CONFIG['domianURL'] + '/sys/common/upload',
        imgerver: window._CONFIG['domianURL'] + '/sys/common/view',
        add: '/protree/pmpProject/addpro',
        edit: '/protree/pmpProject/edit',
        checkCode: '/protree/pmpProject/checkCode'
      }
    }
  },
  created() {
    const token = Vue.ls.get(ACCESS_TOKEN)
    this.headers = { 'X-Access-Token': token }
  },
  computed: {
    uploadAction: function() {
      return this.url.fileUpload
    }
  },
  methods: {
    add() {
      this.picUrl = ''
      this.edit({})
    },
    edit(record) {
      this.form.resetFields()
      this.model = Object.assign({}, record)
      this.visible = true
      if (record.hasOwnProperty('id')) {
        this.picUrl = 'Has no pic url yet'
      }
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
            'isdelete',
            'projectcode'
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
          let photo = that.model.photo
          if (!this.model.id) {
            httpurl += this.url.add
            method = 'post'
          } else {
            httpurl += this.url.edit
            method = 'put'
          }
          let params = {
            taskname: values.projectname,
            photo: photo
          }
          let formData = Object.assign(this.model, values, params)
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
          'isdelete',
          'projectcode'
        )
      )
    },
    validateMyCode(rule, value, callback) {
      let params = {
        projectcode: value
      }
      getAction(this.url.checkCode, params).then(res => {
        if (res.success) {
          callback()
        } else {
          callback(res.message)
        }
      })
    },
    beforeUpload: function(file) {
      var fileType = file.type
      if (fileType.indexOf('image') < 0) {
        this.$message.warning('请上传图片')
        return false
      }
      //TODO 验证文件大小
    },
    handleChange(info) {
      this.picUrl = ''
      if (info.file.status === 'uploading') {
        this.uploadLoading = true
        return
      }
      if (info.file.status === 'done') {
        var response = info.file.response
        this.uploadLoading = false
        console.log(response)
        if (response.success) {
          this.model.photo = response.message
          this.picUrl = 'Has no pic url yet'
        } else {
          this.$message.warning(response.message)
        }
      }
    },
    getAvatarView() {
      return this.url.imgerver + '/' + this.model.photo
    }
  }
}
</script>
<style scoped>
.avatar-uploader > .ant-upload {
  width: 104px;
  height: 104px;
}
.ant-upload-select-picture-card i {
  font-size: 49px;
  color: #999;
}

.ant-upload-select-picture-card .ant-upload-text {
  margin-top: 8px;
  color: #666;
}
</style>