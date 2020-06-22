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
        <a-form-item label="项目头像" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-upload
            action="https://www.mocky.io/v2/5cc8019d300000980a055e76"
            listType="picture-card"
            :fileList="fileList"
            @preview="handlePreview"
            @change="handleChange"
            v-decorator="['photo',  validatorRules.photo]"
          >
            <div v-if="fileList.length < 1">
              <a-icon type="plus" />
              <div class="ant-upload-text">上传头像</div>
            </div>
          </a-upload>
          <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
            <img alt="example" style="width: 100%" :src="previewImage" />
          </a-modal>
          <!-- <j-upload v-decorator="['photo', validatorRules.photo]" :trigger-change="true"></j-upload> -->
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
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="起止日期">
          <a-range-picker v-decorator="['range-picker', validatorRules.rangeConfig]" />
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
      previewVisible: false,
      previewImage: '',
      fileList: [],
      confirmLoading: false,
      validatorRules: {
        photo: { rules: [] },
        projectname: { rules: [{ required: true, message: '请输入项目名称!' }] },
        projecttype: { rules: [{ required: true, message: '请输入项目类型!' }] },
        projectcontent: { rules: [] },
        principal: { rules: [{ required: true, message: '请输入负责人!' }] },
        participant: { rules: [] },
        emergencylevel: { rules: [] },
        rangeConfig: {
          rules: [{ type: 'array', required: true, message: '请选择起止日期!' }]
        },
        schedule: { rules: [] },
        annex: { rules: [] }
      },
      url: {
        add: '/projectmanage/pmpProjectManage/add',
        edit: '/projectmanage/pmpProjectManage/edit'
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
            range-picker[0].format('YYYY-MM-DD'),
            range-picker[1].format('YYYY-MM-DD'),
            'schedule',
            'remark',
            'annex',
            0
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
      this.previewVisible = false
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
            range-picker[0].format('YYYY-MM-DD'),
            range-picker[1].format('YYYY-MM-DD'),
            'schedule',
            'remark',
            'annex',
            0
        )
      )
    },
    handlePreview(file) {
      this.previewImage = file.url || file.thumbUrl
      this.previewVisible = true
    },
    handleChange({ fileList }) {
      this.fileList = fileList
    }
  }
}
</script>

<style>
/* you can make up upload button and sample style by using stylesheets */
.ant-upload-select-picture-card i {
  font-size: 30px;
  color: #999;
}

.ant-upload-select-picture-card .ant-upload-text {
  margin-top: 8px;
  color: #666;
}
</style>