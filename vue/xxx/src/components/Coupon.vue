<template>
  <div class="hello">
    <h1>{{ msg }}</h1>
    <a @click="test()">link test</a>
  </div>
</template>

<script>
import { Toast, MessageBox } from "mint-ui";

export default {
  // name: "Coupon",
  data() {
    return {
      msg: "领券"
    };
  },
  mounted() {
    // Toast("提示信息");
  },
  methods: {
    test() {
      MessageBox.alert("领券成功", "提示");
    }
  }
};
</script>

<style scoped>

</style>
