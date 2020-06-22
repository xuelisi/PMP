<template>
  <a-card :bordered="false">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline"
              @keyup.enter.native="searchQuery">
        <a-row :gutter="24">

          <a-col :md="4" :sm="8">
            <a-form-item label="年月">
              <a-month-picker @change="monthPickerChange"
                              :format="monthPickerFormat"
                              v-model="monthPickerValue"
                              placeholder="请选择年月"/>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
              <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
                <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
                <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              </span>
          </a-col>

        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <div>
      <a-table :bordered="true"
               :loading="loading"
               :dataSource="dataSource"
               :pagination="ipagination"
               @change="handleTableChange"
               @mouseenter.native="tableEnter">
        <a-table-column key="realName"
                        dataIndex="realName">
          <a-tag slot="title"
                 :style="getHeaderStyle('用户名')"
                 :color="getHeaderColor('用户名')">
            用户名
          </a-tag>
          <template slot-scope="realName">
            <span>
            <a-tag :key="realName"
                   :style="getBodyStyle('用户名', realName)"
                   :color="getBodyColor('用户名', realName)">
                {{ realName }}
              </a-tag>
            </span>
          </template>
        </a-table-column>
        <a-table-column key="first"
                        dataIndex="first">
          <a-tag slot="title"
                 :style="getHeaderStyle('01')"
                 :color="getHeaderColor('01')">
            {{ getDay('01') }}
          </a-tag>
          <template slot-scope="first, record">
          <span>
            <a-tag :key="first"
                   :style="getBodyStyle('01', first)"
                   :color="getBodyColor('01', first)"
                   @mouseover="tagEnter('01', first, record, $event)">
              {{ getState('01', first) }}
            </a-tag>
          </span>
          </template>
        </a-table-column>
        <a-table-column key="second"
                        dataIndex="second">
          <a-tag slot="title"
                 :style="getHeaderStyle('02')"
                 :color="getHeaderColor('02')">
            {{ getDay('02') }}
          </a-tag>
          <template slot-scope="second, record">
          <span>
            <a-tag :key="second"
                   :style="getBodyStyle('02', second)"
                   :color="getBodyColor('02', second)"
                   @mouseover="tagEnter('02', second, record, $event)">
              {{ getState('02', second) }}
            </a-tag>
          </span>
          </template>
        </a-table-column>
        <a-table-column key="third"
                        dataIndex="third">
          <a-tag slot="title"
                 :style="getHeaderStyle('03')"
                 :color="getHeaderColor('03')">
            {{ getDay('03') }}
          </a-tag>
          <template slot-scope="third, record">
          <span>
            <a-tag :key="third"
                   :style="getBodyStyle('03', third)"
                   :color="getBodyColor('03', third)"
                   @mouseover="tagEnter('03', third, record, $event)">
              {{ getState('03', third) }}
            </a-tag>
          </span>
          </template>
        </a-table-column>
        <a-table-column key="fourth"
                        dataIndex="fourth">
          <a-tag slot="title"
                 :style="getHeaderStyle('04')"
                 :color="getHeaderColor('04')">
            {{ getDay('04') }}
          </a-tag>
          <template slot-scope="fourth, record">
          <span>
            <a-tag :key="fourth"
                   :style="getBodyStyle('04', fourth)"
                   :color="getBodyColor('04', fourth)"
                   @mouseover="tagEnter('04', fourth, record, $event)">
              {{ getState('04', fourth) }}
            </a-tag>
          </span>
          </template>
        </a-table-column>
        <a-table-column key="fifth"
                        dataIndex="fifth">
          <a-tag slot="title"
                 :style="getHeaderStyle('05')"
                 :color="getHeaderColor('05')">
            {{ getDay('05') }}
          </a-tag>
          <template slot-scope="fifth, record">
          <span>
            <a-tag :key="fifth"
                   :style="getBodyStyle('05', fifth)"
                   :color="getBodyColor('05', fifth)"
                   @mouseover="tagEnter('05', fifth, record, $event)">
              {{ getState('05', fifth) }}
            </a-tag>
          </span>
          </template>
        </a-table-column>
        <a-table-column key="sixth"
                        dataIndex="sixth">
          <a-tag slot="title"
                 :style="getHeaderStyle('06')"
                 :color="getHeaderColor('06')">
            {{ getDay('06') }}
          </a-tag>
          <template slot-scope="sixth, record">
          <span>
            <a-tag :key="sixth"
                   :style="getBodyStyle('06', sixth)"
                   :color="getBodyColor('06', sixth)"
                   @mouseover="tagEnter('06', sixth, record, $event)">
              {{ getState('06', sixth) }}
            </a-tag>
          </span>
          </template>
        </a-table-column>
        <a-table-column key="seventh"
                        dataIndex="seventh">
          <a-tag slot="title"
                 :style="getHeaderStyle('07')"
                 :color="getHeaderColor('07')">
            {{ getDay('07') }}
          </a-tag>
          <template slot-scope="seventh, record">
          <span>
            <a-tag :key="seventh"
                   :style="getBodyStyle('07', seventh)"
                   :color="getBodyColor('07', seventh)"
                   @mouseover="tagEnter('07', seventh, record, $event)">
              {{ getState('07', seventh) }}
            </a-tag>
          </span>
          </template>
        </a-table-column>
        <a-table-column key="eighth"
                        dataIndex="eighth">
          <a-tag slot="title"
                 :style="getHeaderStyle('08')"
                 :color="getHeaderColor('08')">
            {{ getDay('08') }}
          </a-tag>
          <template slot-scope="eighth, record">
          <span>
            <a-tag :key="eighth"
                   :style="getBodyStyle('08', eighth)"
                   :color="getBodyColor('08', eighth)"
                   @mouseover="tagEnter('08', eighth, record, $event)">
              {{ getState('08', eighth) }}
            </a-tag>
          </span>
          </template>
        </a-table-column>
        <a-table-column key="ninth"
                        dataIndex="ninth">
          <a-tag slot="title"
                 :style="getHeaderStyle('09')"
                 :color="getHeaderColor('09')">
            {{ getDay('09') }}
          </a-tag>
          <template slot-scope="ninth, record">
          <span>
            <a-tag :key="ninth"
                   :style="getBodyStyle('09', ninth)"
                   :color="getBodyColor('09', ninth)"
                   @mouseover="tagEnter('09', ninth, record, $event)">
              {{ getState('09', ninth) }}
            </a-tag>
          </span>
          </template>
        </a-table-column>
        <a-table-column key="tenth"
                        dataIndex="tenth">
          <a-tag slot="title"
                 :style="getHeaderStyle('10')"
                 :color="getHeaderColor('10')">
            {{ getDay('10') }}
          </a-tag>
          <template slot-scope="tenth, record">
          <span>
            <a-tag :key="tenth"
                   :style="getBodyStyle('10', tenth)"
                   :color="getBodyColor('10', tenth)"
                   @mouseover="tagEnter('10', tenth, record, $event)">
              {{ getState('10', tenth) }}
            </a-tag>
          </span>
          </template>
        </a-table-column>
        <a-table-column key="eleventh"
                        dataIndex="eleventh">
          <a-tag slot="title"
                 :style="getHeaderStyle('11')"
                 :color="getHeaderColor('11')">
            {{ getDay('11') }}
          </a-tag>
          <template slot-scope="eleventh, record">
          <span>
            <a-tag :key="eleventh"
                   :style="getBodyStyle('11', eleventh)"
                   :color="getBodyColor('11', eleventh)"
                   @mouseover="tagEnter('11', eleventh, record, $event)">
              {{ getState('11', eleventh) }}
            </a-tag>
          </span>
          </template>
        </a-table-column>
        <a-table-column key="twelfth"
                        dataIndex="twelfth">
          <a-tag slot="title"
                 :style="getHeaderStyle('12')"
                 :color="getHeaderColor('12')">
            {{ getDay('12') }}
          </a-tag>
          <template slot-scope="twelfth, record">
          <span>
            <a-tag :key="twelfth"
                   :style="getBodyStyle('12', twelfth)"
                   :color="getBodyColor('12', twelfth)"
                   @mouseover="tagEnter('12', twelfth, record, $event)">
              {{ getState('12', twelfth) }}
            </a-tag>
          </span>
          </template>
        </a-table-column>
        <a-table-column key="thirteenth"
                        dataIndex="thirteenth">
          <a-tag slot="title"
                 :style="getHeaderStyle('13')"
                 :color="getHeaderColor('13')">
            {{ getDay('13') }}
          </a-tag>
          <template slot-scope="thirteenth, record">
          <span>
            <a-tag :key="thirteenth"
                   :style="getBodyStyle('13', thirteenth)"
                   :color="getBodyColor('13', thirteenth)"
                   @mouseover="tagEnter('13', thirteenth, record, $event)">
              {{ getState('13', thirteenth) }}
            </a-tag>
          </span>
          </template>
        </a-table-column>
        <a-table-column key="fourteenth"
                        dataIndex="fourteenth">
          <a-tag slot="title"
                 :style="getHeaderStyle('14')"
                 :color="getHeaderColor('14')">
            {{ getDay('14') }}
          </a-tag>
          <template slot-scope="fourteenth, record">
          <span>
            <a-tag :key="fourteenth"
                   :style="getBodyStyle('14', fourteenth)"
                   :color="getBodyColor('14', fourteenth)"
                   @mouseover="tagEnter('14', fourteenth, record, $event)">
              {{ getState('14', fourteenth) }}
            </a-tag>
          </span>
          </template>
        </a-table-column>
        <a-table-column key="fifteenth"
                        dataIndex="fifteenth">
          <a-tag slot="title"
                 :style="getHeaderStyle('15')"
                 :color="getHeaderColor('15')">
            {{ getDay('15') }}
          </a-tag>
          <template slot-scope="fifteenth, record">
          <span>
            <a-tag :key="fifteenth"
                   :style="getBodyStyle('15', fifteenth)"
                   :color="getBodyColor('15', fifteenth)"
                   @mouseover="tagEnter('15', fifteenth, record, $event)">
              {{ getState('15', fifteenth) }}
            </a-tag>
          </span>
          </template>
        </a-table-column>
        <a-table-column key="sixteenth"
                        dataIndex="sixteenth">
          <a-tag slot="title"
                 :style="getHeaderStyle('16')"
                 :color="getHeaderColor('16')">
            {{ getDay('16') }}
          </a-tag>
          <template slot-scope="sixteenth, record">
          <span>
            <a-tag :key="sixteenth"
                   :style="getBodyStyle('16', sixteenth)"
                   :color="getBodyColor('16', sixteenth)"
                   @mouseover="tagEnter('16', sixteenth, record, $event)">
              {{ getState('16', sixteenth) }}
            </a-tag>
          </span>
          </template>
        </a-table-column>
        <a-table-column key="seventeenth"
                        dataIndex="seventeenth">
          <a-tag slot="title"
                 :style="getHeaderStyle('17')"
                 :color="getHeaderColor('17')">
            {{ getDay('17') }}
          </a-tag>
          <template slot-scope="seventeenth, record">
          <span>
            <a-tag :key="seventeenth"
                   :style="getBodyStyle('17', seventeenth)"
                   :color="getBodyColor('17', seventeenth)"
                   @mouseover="tagEnter('17', seventeenth, record, $event)">
              {{ getState('17', seventeenth) }}
            </a-tag>
          </span>
          </template>
        </a-table-column>
        <a-table-column key="eighteenth"
                        dataIndex="eighteenth">
          <a-tag slot="title"
                 :style="getHeaderStyle('18')"
                 :color="getHeaderColor('18')">
            {{ getDay('18') }}
          </a-tag>
          <template slot-scope="eighteenth, record">
          <span>
            <a-tag :key="eighteenth"
                   :style="getBodyStyle('18', eighteenth)"
                   :color="getBodyColor('18', eighteenth)"
                   @mouseover="tagEnter('18', eighteenth, record, $event)">
              {{ getState('18', eighteenth) }}
            </a-tag>
          </span>
          </template>
        </a-table-column>
        <a-table-column key="nineteenth"
                        dataIndex="nineteenth">
          <a-tag slot="title"
                 :style="getHeaderStyle('19')"
                 :color="getHeaderColor('19')">
            {{ getDay('19') }}
          </a-tag>
          <template slot-scope="nineteenth, record">
          <span>
            <a-tag :key="nineteenth"
                   :style="getBodyStyle('19', nineteenth)"
                   :color="getBodyColor('19', nineteenth)"
                   @mouseover="tagEnter('19', nineteenth, record, $event)">
              {{ getState('19', nineteenth) }}
            </a-tag>
          </span>
          </template>
        </a-table-column>
        <a-table-column key="twentieth"
                        dataIndex="twentieth">
          <a-tag slot="title"
                 :style="getHeaderStyle('20')"
                 :color="getHeaderColor('20')">
            {{ getDay('20') }}
          </a-tag>
          <template slot-scope="twentieth, record">
          <span>
            <a-tag :key="twentieth"
                   :style="getBodyStyle('20', twentieth)"
                   :color="getBodyColor('20', twentieth)"
                   @mouseover="tagEnter('20', twentieth, record, $event)">
              {{ getState('20', twentieth) }}
            </a-tag>
          </span>
          </template>
        </a-table-column>
        <a-table-column key="twentyFirst"
                        dataIndex="twentyFirst">
          <a-tag slot="title"
                 :style="getHeaderStyle('21')"
                 :color="getHeaderColor('21')">
            {{ getDay('21') }}
          </a-tag>
          <template slot-scope="twentyFirst, record">
          <span>
            <a-tag :key="twentyFirst"
                   :style="getBodyStyle('21', twentyFirst)"
                   :color="getBodyColor('21', twentyFirst)"
                   @mouseover="tagEnter('21', twentyFirst, record, $event)">
              {{ getState('21', twentyFirst) }}
            </a-tag>
          </span>
          </template>
        </a-table-column>
        <a-table-column key="twentySecond"
                        dataIndex="twentySecond">
          <a-tag slot="title"
                 :style="getHeaderStyle('22')"
                 :color="getHeaderColor('22')">
            {{ getDay('22') }}
          </a-tag>
          <template slot-scope="twentySecond, record">
          <span>
            <a-tag :key="twentySecond"
                   :style="getBodyStyle('22', twentySecond)"
                   :color="getBodyColor('22', twentySecond)"
                   @mouseover="tagEnter('22', twentySecond, record, $event)">
              {{ getState('22', twentySecond) }}
            </a-tag>
          </span>
          </template>
        </a-table-column>
        <a-table-column key="twentyThird"
                        dataIndex="twentyThird">
          <a-tag slot="title"
                 :style="getHeaderStyle('23')"
                 :color="getHeaderColor('23')">
            {{ getDay('23') }}
          </a-tag>
          <template slot-scope="twentyThird, record">
          <span>
            <a-tag :key="twentyThird"
                   :style="getBodyStyle('23', twentyThird)"
                   :color="getBodyColor('23', twentyThird)"
                   @mouseover="tagEnter('23', twentyThird, record, $event)">
              {{ getState('23', twentyThird) }}
            </a-tag>
          </span>
          </template>
        </a-table-column>
        <a-table-column key="twentyFourth"
                        dataIndex="twentyFourth">
          <a-tag slot="title"
                 :style="getHeaderStyle('24')"
                 :color="getHeaderColor('24')">
            {{ getDay('24') }}
          </a-tag>
          <template slot-scope="twentyFourth, record">
          <span>
            <a-tag :key="twentyFourth"
                   :style="getBodyStyle('24', twentyFourth)"
                   :color="getBodyColor('24', twentyFourth)"
                   @mouseover="tagEnter('24', twentyFourth, record, $event)">
              {{ getState('24', twentyFourth) }}
            </a-tag>
          </span>
          </template>
        </a-table-column>
        <a-table-column key="twentyFifth"
                        dataIndex="twentyFifth">
          <a-tag slot="title"
                 :style="getHeaderStyle('25')"
                 :color="getHeaderColor('25')">
            {{ getDay('25') }}
          </a-tag>
          <template slot-scope="twentyFifth, record">
          <span>
            <a-tag :key="twentyFifth"
                   :style="getBodyStyle('25', twentyFifth)"
                   :color="getBodyColor('25', twentyFifth)"
                   @mouseover="tagEnter('25', twentyFifth, record, $event)">
              {{ getState('25', twentyFifth) }}
            </a-tag>
          </span>
          </template>
        </a-table-column>
        <a-table-column key="twentySixth"
                        dataIndex="twentySixth">
          <a-tag slot="title"
                 :style="getHeaderStyle('26')"
                 :color="getHeaderColor('26')">
            {{ getDay('26') }}
          </a-tag>
          <template slot-scope="twentySixth, record">
          <span>
            <a-tag :key="twentySixth"
                   :style="getBodyStyle('26', twentySixth)"
                   :color="getBodyColor('26', twentySixth)"
                   @mouseover="tagEnter('26', twentySixth, record, $event)">
              {{ getState('26', twentySixth) }}
            </a-tag>
          </span>
          </template>
        </a-table-column>
        <a-table-column key="twentySeventh"
                        dataIndex="twentySeventh">
          <a-tag slot="title"
                 :style="getHeaderStyle('27')"
                 :color="getHeaderColor('27')">
            {{ getDay('27') }}
          </a-tag>
          <template slot-scope="twentySeventh, record">
          <span>
            <a-tag :key="twentySeventh"
                   :style="getBodyStyle('27', twentySeventh)"
                   :color="getBodyColor('27', twentySeventh)"
                   @mouseover="tagEnter('27', twentySeventh, record, $event)">
              {{ getState('27', twentySeventh) }}
            </a-tag>
          </span>
          </template>
        </a-table-column>
        <a-table-column key="twentyEighth"
                        dataIndex="twentyEighth">
          <a-tag slot="title"
                 :style="getHeaderStyle('28')"
                 :color="getHeaderColor('28')">
            {{ getDay('28') }}
          </a-tag>
          <template slot-scope="twentyEighth, record">
          <span>
            <a-tag :key="twentyEighth"
                   :style="getBodyStyle('28', twentyEighth)"
                   :color="getBodyColor('28', twentyEighth)"
                   @mouseover="tagEnter('28', twentyEighth, record, $event)">
              {{ getState('28', twentyEighth) }}
            </a-tag>
          </span>
          </template>
        </a-table-column>
        <a-table-column key="twentyNinth"
                        dataIndex="twentyNinth"
                        v-if="isInMonth('29')">
          <a-tag slot="title"
                 :style="getHeaderStyle('29')"
                 :color="getHeaderColor('29')">
            {{ getDay('29') }}
          </a-tag>
          <template slot-scope="twentyNinth, record">
          <span>
            <a-tag :key="twentyNinth"
                   :style="getBodyStyle('29', twentyNinth)"
                   :color="getBodyColor('29', twentyNinth)"
                   @mouseover="tagEnter('29', twentyNinth, record, $event)">
              {{ getState('29', twentyNinth) }}
            </a-tag>
          </span>
          </template>
        </a-table-column>
        <a-table-column key="twentyTenth"
                        dataIndex="twentyTenth"
                        v-if="isInMonth('30')">
          <a-tag slot="title"
                 :style="getHeaderStyle('30')"
                 :color="getHeaderColor('30')">
            {{ getDay('30') }}
          </a-tag>
          <template slot-scope="twentyTenth, record">
            <span>
            <a-tag :key="twentyTenth"
                   :style="getBodyStyle('30', twentyTenth)"
                   :color="getBodyColor('30', twentyTenth)"
                   @mouseover="tagEnter('30', twentyTenth, record, $event)">
              {{ getState('30', twentyTenth) }}
            </a-tag>
          </span>
          </template>
        </a-table-column>
        <a-table-column key="thirtyFirst"
                        dataIndex="thirtyFirst"
                        v-if="isInMonth('31')">
          <a-tag slot="title"
                 :style="getHeaderStyle('31')"
                 :color="getHeaderColor('31')">
            {{ getDay('31') }}
          </a-tag>
          <template slot-scope="thirtyFirst, record">
          <span>
            <a-tag :key="thirtyFirst"
                   :style="getBodyStyle('31', thirtyFirst)"
                   :color="getBodyColor('31', thirtyFirst)"
                   @mouseover="tagEnter('31', thirtyFirst, record, $event)">
              {{ getState('31', thirtyFirst) }}
            </a-tag>
          </span>
          </template>
        </a-table-column>
      </a-table>
    </div>

    <!--<div class="footer-tip">-->
    <!--<span>说明：<a-icon type="check-circle" />有日报</span>-->
    <!--</div>-->

    <pmp-statistics-modal ref="modalForm"
                          @ok="modalFormOk"></pmp-statistics-modal>
  </a-card>
</template>

<script>

  import moment from 'moment';
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import {debug, getDay, getDate, getFileName, dateFormat} from '@/api/wqc'
  import {deleteAction, getAction, putAction, downFile} from '@/api/manage'

  import PmpStatisticsPop from './PmpStatisticsPop'
  import PmpStatisticsModal from './modules/PmpStatisticsModal'

  export default {
    mixins: [JeecgListMixin],
    components: {
      PmpStatisticsPop,
      PmpStatisticsModal
    },
    data() {
      return {
        columns: [],
        hoverData: '',
        colorBlack: '',
        colorRed: 'red',
        colorBlue: 'blue',
        colorGreen: 'green',
        colorVolcano: 'volcano',
        monthPickerFormat: 'YYYY-MM',
        queryParam: {summaryTime: ''},
        monthPickerValue: moment(this.date),
        url: {
          list: "/summary/pmpSummary/statistics",
        },
      }
    },
    mounted() {
      this.searchReset();
    },
    methods: {
      //初始化年月数据
      initMonthPicker() {
        this.monthPickerValue = moment(this.date);
      },
      //年月切换
      monthPickerChange(date, dateString) {
        if (dateString.length <= 0) {
          this.initMonthPicker();
        }

        this.searchQuery();
      },
      //查询
      searchQuery() {
        this.queryParam.summaryTime = this.monthPickerValue.format("YYYY-MM");

        this.loadData(1);
      },
      //重置
      searchReset() {
        this.initMonthPicker();

        this.searchQuery();
      },
      getDay(title) {
        let dt = this.monthPickerValue.format("YYYY-MM") + '-' + title;
        return title + '\r\n' + getDay(dt);
      },
      getState(title, data) {
        if (null === data)
          return '';

        return title === data.substring(8, 10) ? '正常' : '补录';
      },
      isInMonth(title) {
        let mp = this.monthPickerValue.toDate();
        return title <= new Date(mp.getFullYear(), mp.getMonth() + 1, 0).getDate();
      },
      tagClick(data, record) {
        this.$refs.modalForm.show(data, record);
      },
      tableEnter() {
        this.hoverData = '';
      },
      tagEnter(title, data, record, e) {
        if (null === data) {
          this.hoverData = '';
          return;
        }

        let userName = record.userName + data;
        if ((userName !== this.hoverData)) {
          this.hoverData = userName;
          this.$refs.modalForm.curMousePos.x = e.x;
          this.$refs.modalForm.curMousePos.y = e.y + 15;
          let dt = this.monthPickerValue.format("YYYY-MM") + '-' + title;
          this.$refs.modalForm.show(dt, record);
        } else {
          this.hoverData = '';
        }
      },
      getHeaderStyle(title) {
        let now = new Date().getDate();
        return 'border: ' + (title === now ? '1' : '0') + ';';
      },
      getHeaderColor(title) {
        title = this.monthPickerValue.format("YYYY-MM") + '-' + title;
        return title === dateFormat(new Date()) ? this.colorBlue : this.colorBlack;
      },
      getBodyStyle(title, data) {
        if ((null === data) || ('用户名' === title))
          return 'border: 0';

        return 'border: 1';
      },
      getBodyColor(title, data) {
        if ((null === data) || ('用户名' === title))
          return this.colorBlack;

        title = this.monthPickerValue.format("YYYY-MM") + '-' + title;
        return title === dateFormat(data) ? this.colorGreen : this.colorVolcano;
      },
    },
  };

</script>

<style>
  .ant-card-body {
    padding: 0;
  }

  .ant-table-thead tr th {
    padding: 0;
    font-weight: bold;
    font-size: 12px;
    overflow: hidden;
    text-align: center;
    height: 40px;
    line-height: 40px;
  }

  .ant-table-thead .ant-tag {
    margin: 0;
    padding: 0;
    height: 15px;
    line-height: 15px;
    white-space: pre-line;
  }

  .ant-table-tbody .ant-table-row td {
    font-size: 12px;
    text-align: center;
    white-space: normal;
    padding: 15px 0 15px 0;
  }

  .ant-tag {
    margin: 0;
    padding: 0;
  }

  .ant-list-vertical .ant-list-item-meta {
    margin-bottom: 0;
  }

  .ant-list-vertical .ant-list-item-content {
    display: block;
    margin: 0;
    color: rgba(0, 0, 0, 0.65);
    font-size: 14px;
  }

  .scroll-wrap {
    width: 600px;
    margin: 0;
    padding: 0;
    max-height: 300px;
    overflow: hidden;
    overflow-y: scroll;
  }
</style>