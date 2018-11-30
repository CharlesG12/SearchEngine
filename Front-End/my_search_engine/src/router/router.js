import Vue from 'vue'
import Router from 'vue-router'
import searchbox from '@/components/SearchBox.vue'
import Result from '@/App.vue'

// Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Result',
      component: Result
    },
    {
      path: '/searchbox',
      name: 'searchbox',
      component: searchbox
    },
  ]
})