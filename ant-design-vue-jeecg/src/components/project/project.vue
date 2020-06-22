<template>
  <div>
    <div class="row">
      <dl
        v-for="(datan, index) in dataSource"
        :key="index"
        @click="hreftwo(datan.id)"
        class="linkcas"
        :class="{'link-item-active':index==hoverIndex,'link-item-hover':hoverIndex!=index}"
        @mouseover="hoverIndex = index"
        @mouseout="hoverIndex =-1"
      >
        <dt class="links">
          <img src="/img/logo.68712ac0.png" />
          <div class="linkss">{{datan.schedule}}%</div>
        </dt>
        <dd style="text-align: left;">
          <div style="margin-top:26px;margin-left:40px">
            <p>
              &nbsp;&nbsp;
              <span>{{datan.projectname}}</span>
            </p>
            <p>
              &nbsp;&nbsp;
              <span style="color:#999">负责人：{{datan.principal}}</span>
            </p>
          </div>
        </dd>
      </dl>
    </div>
  </div>
</template>

<script>
export default {
  name: 'projecgname',
  props: {
    dataSource: {
      type: Array,
      default: null,
      required: true
    }
  },
  data() {
    return {
      loadding: false,
      hoverIndex: -1
    }
  },
  methods: {
    initDictConfig() {},
    hreftwo(pid) {
      this.$router.push({ path: 'myproject_task', query: { id: pid } })
    }
  }
}
</script>

<style lang="scss" scoped>
@import '~@assets/less/common.less';
@import '~@assets/less/index.css';
@import '~@assets/less/bootstrap/css/bootstrap.min.css';
.link-item-hover {
  background-color: #f5f7fa;
}
.linkcas {
  float: left;
  width: 400px;
  height: 100px;
  background-color: #ffffff;
  margin: 3px 3px;
  cursor: pointer;
}

.link-item-hover {
  border: 1px solid #ebebeb;
}
.link-item-active {
  border: 1px solid #4ba9f2;
}

.links {
  float: left;
  text-align: center;
  position: relative;
}
.linkss {
  position: absolute;
  left: 40%;
  top: 40%;
  color: #fff;
}
</style>