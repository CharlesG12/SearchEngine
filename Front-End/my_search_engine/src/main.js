import Vue from 'vue'
import App from './App.vue'
import dataWareHouse from './DataWarehouse.js'
import atui from 'at-ui'
import Router from 'vue-router'

Vue.use(atui)

let vm = new Vue({
  el: '#app',
  Router,
  store: dataWareHouse,
  render: h => h(App)
})
