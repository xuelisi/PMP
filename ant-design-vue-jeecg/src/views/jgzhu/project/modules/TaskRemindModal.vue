<template>
  <div>
    <a-modal
      title="催办"
      :visible="visible"
      @ok="handleOk"
      :confirmLoading="confirmLoading"
      @cancel="handleCancel"
      :width="width"
    >
      <a-form :model="form" :label-col="labelCol" :wrapper-col="wrapperCol">
        <a-form-item label="任务名" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-model="form.taskname" />
        </a-form-item>
        <a-form-item label="到期日前" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-select v-model="form.region" placeholder="请输入天数">
            <a-select-option value="0">不提醒</a-select-option>
            <a-select-option value="1">1天</a-select-option>
            <a-select-option value="2">2天</a-select-option>
            <a-select-option value="3">3天</a-select-option>
            <a-select-option value="4">4天</a-select-option>
            <a-select-option value="5">5天</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="提醒" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-checkbox-group v-model="form.type">
            <a-checkbox value="1" name="type">创建人</a-checkbox>
            <a-checkbox value="2" name="type">负责人</a-checkbox>
            <a-checkbox value="3" name="type">参与人</a-checkbox>
          </a-checkbox-group>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>
<script>
export default {
  data() {
    return {
      width: 800,
      labelCol: {
        xs: { span: 8 },
        sm: { span: 2 }
      },
      wrapperCol: {
        xs: { span: 8 },
        sm: { span: 21 }
      },
      form: {
        taskname: '',
        enddate: '',
        region: undefined,
        type: []
      },
      visible: false,
      confirmLoading: false
    }
  },
  methods: {
    showModal(record) {
      this.visible = true
      this.form.taskname = record.taskname
    },
    handleOk(e) {
      this.ModalText = 'The modal will be closed after two seconds'
      this.confirmLoading = true
      setTimeout(() => {
        this.visible = false
        this.confirmLoading = false
      }, 2000)
    },
    handleCancel(e) {
      console.log('Clicked cancel button')
      this.visible = false
    }
  }
}
</script>