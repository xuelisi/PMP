<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-row :gutter="24">
        <a-col :span="2">
          <a-dropdown>
            <a-menu slot="overlay" @click="handleMenuClick">
              <a-menu-item key="1">表格视图</a-menu-item>
              <a-menu-item key="2">分类视图</a-menu-item>
              <a-menu-item key="3">普通视图</a-menu-item>
            </a-menu>
            <a-button style="margin-left: 8px">
              视图
              <a-icon type="down" />
            </a-button>
          </a-dropdown>
        </a-col>
        <a-col :span="18">
          <a-form layout="inline" @keyup.enter.native="searchQuery">
            <a-row :gutter="24">
              <a-col :md="6" :sm="8">
                <a-form-item>
                  <a-input placeholder="请输入项目名称" v-model="queryParam.projectname"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="8">
                <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
                  <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
                </span>
              </a-col>
            </a-row>
          </a-form>
        </a-col>
        <a-col :span="4" style="text-align:right">
          <a-dropdown-button @click="handleAdd" icon="plus">
            添加项目
            <a-menu slot="overlay" @click="handleMenuClick">
              <a-menu-item key="1">
                <a-icon type="import" />导入
              </a-menu-item>
              <a-menu-item key="2">
                <a-icon type="download" />导出
              </a-menu-item>
            </a-menu>
          </a-dropdown-button>
        </a-col>
      </a-row>
    </div>
    <!-- 查询区域-END -->

    <!-- 列表区域-begin -->
    <div class="page_body1" v-if="is_list">
      <div class="row">
        <div class="col-md-3 col-xs-12 col-sm-6" @click="hreftwo">
          <div class="item-box1">
            <div class="item-box-content">
              <a href class="box1">
                <span class="glyphicon glyphicon-fire" style="color: red;"></span>0%
              </a>
              <h4 class="data fg-lightskyblue">项目1</h4>
              <b>负责人:&nbsp;</b>
              <span>王经理</span>
            </div>
          </div>
        </div>
        <div class="col-md-3 col-xs-12 col-sm-6">
          <div class="item-box2">
            <div class="item-box-content">
              <a href class="box2">
                <span class="glyphicon glyphicon-fire" style="color: red;"></span> 0%
              </a>
              <h4 class="data fg-lightskyblue">项目2</h4>
              <b>负责人:&nbsp;</b>
              <span>王经理</span>
            </div>
          </div>
        </div>
        <div class="col-md-3 col-xs-12 col-sm-6">
          <div class="item-box3">
            <div class="item-box-content">
              <a href class="box1">
                <span class="glyphicon glyphicon-fire" style="color: red;"></span>0%
              </a>
              <h4 class="data fg-lightskyblue">项目3</h4>
              <b>负责人:&nbsp;</b>
              <span>王经理</span>
            </div>
          </div>
        </div>
        <div class="col-md-3 col-xs-12 col-sm-6">
          <div class="item-box4">
            <div class="item-box-content">
              <a href class="box4">
                <span class="glyphicon glyphicon-fire" style="color: red;"></span>0%
              </a>
              <h4 class="data fg-lightskyblue">项目4</h4>
              <b>负责人:&nbsp;</b>
              <span>王经理</span>
            </div>
          </div>
        </div>
      </div>
      <div class="row">
        <div class="col-md-3 col-xs-12 col-sm-6">
          <div class="item-box1">
            <div class="item-box-content">
              <a href class="box1">
                <span class="glyphicon glyphicon-fire" style="color: red;"></span>0%
              </a>
              <h4 class="data fg-lightskyblue">项目1</h4>
              <b>负责人:&nbsp;</b>
              <span>王经理</span>
            </div>
          </div>
        </div>
        <div class="col-md-3 col-xs-12 col-sm-6">
          <div class="item-box2">
            <div class="item-box-content">
              <a href class="box2">
                <span class="glyphicon glyphicon-fire" style="color: red;"></span> 0%
              </a>
              <h4 class="data fg-lightskyblue">项目2</h4>
              <b>负责人:&nbsp;</b>
              <span>王经理</span>
            </div>
          </div>
        </div>
        <div class="col-md-3 col-xs-12 col-sm-6">
          <div class="item-box3">
            <div class="item-box-content">
              <a href class="box1">
                <span class="glyphicon glyphicon-fire" style="color: red;"></span>0%
              </a>
              <h4 class="data fg-lightskyblue">项目3</h4>
              <b>负责人:&nbsp;</b>
              <span>王经理</span>
            </div>
          </div>
        </div>
        <div class="col-md-3 col-xs-12 col-sm-6">
          <div class="item-box4">
            <div class="item-box-content">
              <a href class="box4">
                <span class="glyphicon glyphicon-fire" style="color: red;"></span>0%
              </a>
              <h4 class="data fg-lightskyblue">项目4</h4>
              <b>负责人:&nbsp;</b>
              <span>王经理</span>
            </div>
          </div>
        </div>
      </div>
      <div class="row">
        <div class="col-md-3 col-xs-12 col-sm-6">
          <div class="item-box1">
            <div class="item-box-content">
              <a href class="box1">
                <span class="glyphicon glyphicon-fire" style="color: red;"></span>0%
              </a>
              <h4 class="data fg-lightskyblue">项目1</h4>
              <b>负责人:&nbsp;</b>
              <span>王经理</span>
            </div>
          </div>
        </div>
        <div class="col-md-3 col-xs-12 col-sm-6">
          <div class="item-box2">
            <div class="item-box-content">
              <a href class="box2">
                <span class="glyphicon glyphicon-fire" style="color: red;"></span> 0%
              </a>
              <h4 class="data fg-lightskyblue">项目2</h4>
              <b>负责人:&nbsp;</b>
              <span>王经理</span>
            </div>
          </div>
        </div>
        <div class="col-md-3 col-xs-12 col-sm-6">
          <div class="item-box3">
            <div class="item-box-content">
              <a href class="box1">
                <span class="glyphicon glyphicon-fire" style="color: red;"></span>0%
              </a>
              <h4 class="data fg-lightskyblue">项目3</h4>
              <b>负责人:&nbsp;</b>
              <span>王经理</span>
            </div>
          </div>
        </div>
        <div class="col-md-3 col-xs-12 col-sm-6">
          <div class="item-box4">
            <div class="item-box-content">
              <a href class="box4">
                <span class="glyphicon glyphicon-fire" style="color: red;"></span>0%
              </a>
              <h4 class="data fg-lightskyblue">项目4</h4>
              <b>负责人:&nbsp;</b>
              <span>王经理</span>
            </div>
          </div>
        </div>
      </div>
      <div class="row">
        <div class="col-md-3 col-xs-12 col-sm-6">
          <div class="item-box1">
            <div class="item-box-content">
              <a href class="box1">
                <span class="glyphicon glyphicon-fire" style="color: red;"></span>0%
              </a>
              <h4 class="data fg-lightskyblue">项目1</h4>
              <b>负责人:&nbsp;</b>
              <span>王经理</span>
            </div>
          </div>
        </div>
        <div class="col-md-3 col-xs-12 col-sm-6">
          <div class="item-box2">
            <div class="item-box-content">
              <a href class="box2">
                <span class="glyphicon glyphicon-fire" style="color: red;"></span> 0%
              </a>
              <h4 class="data fg-lightskyblue">项目2</h4>
              <b>负责人:&nbsp;</b>
              <span>王经理</span>
            </div>
          </div>
        </div>
        <div class="col-md-3 col-xs-12 col-sm-6">
          <div class="item-box3">
            <div class="item-box-content">
              <a href class="box1">
                <span class="glyphicon glyphicon-fire" style="color: red;"></span>0%
              </a>
              <h4 class="data fg-lightskyblue">项目3</h4>
              <b>负责人:&nbsp;</b>
              <span>王经理</span>
            </div>
          </div>
        </div>
        <div class="col-md-3 col-xs-12 col-sm-6">
          <div class="item-box4">
            <div class="item-box-content">
              <a href class="box4">
                <span class="glyphicon glyphicon-fire" style="color: red;"></span>0%
              </a>
              <h4 class="data fg-lightskyblue">项目4</h4>
              <b>负责人:&nbsp;</b>
              <span>王经理</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="page_body1" v-if="is_table">
      <!-- table区域-begin -->
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择
        <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{fixed:true,selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange"
      >
        <template slot="projectname" slot-scope="text">
          <!-- <a @click="hreftwo">{{text}}</a> -->
          <router-link :to="{ name: 'myproject_task' }">{{text}}</router-link>
        </template>
        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无此图片</span>
          <img
            v-else
            :src="getImgView(text)"
            height="25px"
            alt="图片不存在"
            style="max-width:80px;font-size: 12px;font-style: italic;"
          />
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无此文件</span>
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="uploadFile(text)"
          >下载</a-button>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">
              更多
              <a-icon type="down" />
            </a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>
      </a-table>
    </div>
    <pmpProjectManage-modal ref="modalForm" @ok="modalFormOk"></pmpProjectManage-modal>
  </a-card>
</template>

<script>
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import PmpProjectManageModal from './modules/PmpProjectManageModal'
import { initDictOptions, filterMultiDictText } from '@/components/dict/JDictSelectUtil'

export default {
  name: 'PmpProjectManageList',
  mixins: [JeecgListMixin],
  components: {
    PmpProjectManageModal
  },
  data() {
    return {
      description: '项目主表管理页面',
      is_list: false,
      is_table: true,
      // 表头
      columns: [
        {
          title: '#',
          dataIndex: '',
          key: 'rowIndex',
          width: 60,
          align: 'center',
          customRender: function(t, r, index) {
            return parseInt(index) + 1
          }
        },
        {
          title: '项目名称',
          align: 'center',
          dataIndex: 'projectname',
          scopedSlots: { customRender: 'projectname' }
        },
        {
          title: '负责人',
          align: 'center',
          dataIndex: 'principal'
        },
        {
          title: '起始日期',
          align: 'center',
          dataIndex: 'startdate',
          customRender: function(text) {
            return !text ? '' : text.length > 10 ? text.substr(0, 10) : text
          }
        },
        {
          title: '结束日期',
          align: 'center',
          dataIndex: 'enddate',
          customRender: function(text) {
            return !text ? '' : text.length > 10 ? text.substr(0, 10) : text
          }
        },
        {
          title: '总进度',
          align: 'center',
          dataIndex: 'schedule'
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: '/projectmanage/pmpProjectManage/list',
        delete: '/projectmanage/pmpProjectManage/delete',
        deleteBatch: '/projectmanage/pmpProjectManage/deleteBatch',
        exportXlsUrl: '/projectmanage/pmpProjectManage/exportXls',
        importExcelUrl: 'projectmanage/pmpProjectManage/importExcel'
      },
      dictOptions: {}
    }
  },
  computed: {
    importExcelUrl: function() {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    }
  },
  methods: {
    initDictConfig() {},
    // hreftwo() {
    //   this.$router.push({ path: '/jeecg/project/myproject_task' })
    // },
    handleMenuClick(e) {
      if (e.key == 1) {
        this.is_list = false
        this.is_table = true
      } else if (e.key == 2) {
      } else {
        this.is_list = true
        this.is_table = false
      }
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
@import '~@assets/less/index.css';
@import '~@assets/less/bootstrap/css/bootstrap.min.css';
</style>