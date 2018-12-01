<template>
  <div id="app">
    <div>
      <h1 class="icon icon-search title"> SMARTISAN </h1>
      <!-- <nav>
        <router-link to="/searchbox">searchbox</router-link>
      </nav>
      <router-view/> -->
    </div>  
    <div>
      <search-box />
    </div>
    <div>
      <checkbox />
    </div>  
    <div class="row at-row no-gutter flex-center expansion">
      <h2 class="queryexpansion">Expanded Query:</h2>
      <query-expansion 
        v-for="(item, index) in queryExpansionResult"
        :key="index"
        :name="item.name"/>
    </div>
    <div class="row at-row no-gutter flex-center"> 
      <div class="col-md-8 resultBox">
        <h2>Google Result</h2>
        <item 
          v-for="item in googleResult"
          :key="item.name"
          :name="item.name"
          :url="item.url"
          :snippet="item.snippet"
          :display-url="item.displayUrl"/>
      </div>
      <div class="col-md-8 resultBox">
        <h2>Bing Result</h2>
        <item 
          v-for="(item, index) in bingResult"
          :key=index
          :name="item.name"
          :url="item.url"
          :snippet="item.snippet"
          :display-url="item.displayUrl"/>
      </div>
      <div class="col-md-8 resultBox">
        <h2>Our Result</h2>
        <!-- <item 
          v-for="i in 10"
          :key=i
          :name="myResult[i].name"
          :url="myResult[i].url"
          :snippet="myResult[i].snippet"
          :display-url="myResult[i].displayUrl"/> -->
        <item 
          v-for="(item, index) in returnTop10"
          :key=index
          :name="item.name"
          :url="item.url"
          :snippet="item.snippet"
          :display-url="item.displayUrl"/>
      </div>
    </div>
  </div>
</template>

<script>
import item from './components/Item.vue'
import queryExpansion from './components/QueryExpansion.vue'
import searchBox from './components/SearchBox.vue'
import checkbox from './components/Checkbox.vue'

export default {
  name: 'app',
  created() {
    console.log(this.$store)
  },
  computed: {
    googleResult() { return this.$store.getters.googleResult; },
    bingResult() { return this.$store.getters.bingResult; },
    returnTop10(){ return this.$store.getters.displaymine; },
    queryResult() { return this.$store.getters.queryResult; },
    queryExpansionResult() { return this.$store.getters.queryExpansionResult; },
  },
  components: {
    item,
    queryExpansion,
    searchBox,
    checkbox,
  }
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 30px;
}

h1, h2 {
  font-weight: normal;
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  display: inline-block;
  margin: 0 10px;
}

a {
  color: #42b983;
}

.resultBox {
    width: 33%;
}

.queryexpansion {
  padding:5px;
}

#app .title{
  margin: 40px;
  font-family: Helvetica Neue,PingFang SC;
}

.expansion {
  margin: 20px;
}

</style>
