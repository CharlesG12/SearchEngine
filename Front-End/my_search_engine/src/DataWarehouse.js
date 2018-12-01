import Vue from 'vue';
import Vuex from 'vuex';
import axios from 'axios'

Vue.use(Vuex);

const dataWareHouse =  new Vuex.Store({
  state: {
    google: [
      { name: 'An Item', url: '//placehold.it/200', snippet: "this is an example", displayUrl: "https://www.amazon.com/one-plus/s?page=1&rh=i:aps,k:one plus one"},
      { name: 'Thing', url: '//placehold.it/200', snippet: "this is an example", displayUrl: "https://www.amazon.com/one-plus/s?page=1&rh=i:aps,k:one plus one" },
      { name: 'Doo-dad', url: '//placehold.it/200', snippet: "this is an example", displayUrl: "https://www.amazon.com/one-plus/s?page=1&rh=i:aps,k:one plus one" },
      { name: 'Other thing', url: '//placehold.it/200', snippet: "this is an example", displayUrl: "https://www.amazon.com/one-plus/s?page=1&rh=i:aps,k:one plus one" },
    ],
    bing: [
      { name: 'Amazon.com: one plus one', url: 'https://www.amazon.com/one-plus/s?page=1&rh=i%3Aaps%2Ck%3Aone%20plus%20one', snippet: "Feitenn Leather Case for One Plus 6T, Stand Credit ID Card Slots Cover Wallet PU Leather TPU Rubber Flip Folio Magnetic Slim Bumper Shockproof Absorption Protective One Plus Six 6T Case 2018 - Black", displayUrl: "https://www.amazon.com/one-plus/s?page=1&rh=i:aps,k:one plus one" },
      { name: 'Thing', url: '//placehold.it/200', snippet: "this is an example", displayUrl: "https://www.amazon.com/one-plus/s?page=1&rh=i:aps,k:one plus one" },
      { name: 'Doo-dad', url: '//placehold.it/200', snippet: "this is an example", displayUrl: "https://www.amazon.com/one-plus/s?page=1&rh=i:aps,k:one plus one" },
      { name: 'Other thing', url: '//placehold.it/200', snippet: "this is an example", displayUrl: "https://www.amazon.com/one-plus/s?page=1&rh=i:aps,k:one plus one" },
    ],
    mine: [
      { name: 'An Item', url: '//placehold.it/200', snippet: "this is an example", displayUrl: "https://www.amazon.com/one-plus/s?page=1&rh=i:aps,k:one plus one" },
      { name: 'Thing', url: '//placehold.it/200', snippet: "this is an example", displayUrl: "https://www.amazon.com/one-plus/s?page=1&rh=i:aps,k:one plus one" },
      { name: 'Doo-dad', url: '//placehold.it/200', snippet: "this is an example", displayUrl: "https://www.amazon.com/one-plus/s?page=1&rh=i:aps,k:one plus one" },
      { name: 'Other thing', url: '//placehold.it/200', snippet: "this is an example", displayUrl: "https://www.amazon.com/one-plus/s?page=1&rh=i:aps,k:one plus one" },
    ],
    displaymine: [
      { name: 'An Item', url: '//placehold.it/200', snippet: "this is an example", displayUrl: "https://www.amazon.com/one-plus/s?page=1&rh=i:aps,k:one plus one" },
    ],
    query: "onePlus" ,
    query_expansion: [
      {name: 'oneplus',},
      {name: 'oneplus2',},
      {name: 'oneplus3',},
      {name: 'oneplus4',},
    ],
    query_cluster:[],
    myparam: [
      {"pageRank": "true"},
      { "method": "None"}
    ],
    currentPage: [],
    search_offset: [],
  },
  getters: {
    googleResult: state => state.google,
    bingResult: state => state.bing,
    myResult: state => state.mine,
    queryExpansionResult: state => state.query_expansion,
    my_param: state => state.myparam,
    query: state => state.query,
    currentpage: state => state.currentPage,
    displaymine: state => state.displaymine,
  },
  mutations: {
    GOOGLE_SEARCH(state, result) { state.google = result },
    BING_SEARCH(state, result) { state.bing = result },
    MY_SEARCH(state, result) { state.ming = result },
    SET_QUERY(state, result) { state.query = result },
    SET_QUERT_EXPANSION(state, result) { state.query_expansion = result },
    EMPTY_BING(state) { state.bing.splice(0, state.bing.length) },
    ADD_BING(state, result) { state.bing = result },
    EMPTY_GOOGLE(state) { state.google.splice(0, state.google.length) },
    ADD_GOOGLE(state, result) { state.google = result },
    EMPTY_MINE(state) { state.mine.splice(0, state.google.length) },
    ADD_MINE(state, result) { state.mine = result },
    UPDATE_MY_PARAM(state, result) { state.myparam = result },
    UPDATE_QUERY(state, result) { state.query = result },
    UPDATE_PAGE(state, result) { state.query = result},
    UPDATE_QUERY_EXPANSION(state, result) { state.query_expansion = result },
    UPDATE_OFFSET(state, result) { state.search_offset = result },
    UPDATE_DISPLAYMINE(state, result) { state.displaymine = result }
  },
  actions: {
    update_query(context, query) {
      context.commit('UPDATE_QUERY', query)
    },

    update_displaymine(context, content) {
      context.commit('UPDATE_DISPLAYMINE', content)
    },

    update_my_param(context, param) {
      context.commit('UPDATE_MY_PARAM', param)
    },

    update_page(context, page_num) {
      context.commit('UPDATE_PAGE', page_num)
    },

    emptyBing(context) {
      context.commit('EMPTY_BING')
    },

    update_offset(context, off) {
      context.commit('UPDATE_OFFSET', off)
    },

    fetchMine(context, infos) {
      console.log("infos in fecth mine: " + infos)
      console.log("query: " + infos)
      let pR = infos[0][0].pageRank
      let exp = infos[0][1].method
      let query = infos[1]
      let current_page = infos[2]

      console.log("applypageranking: " + pR)
      console.log("expansion method: " + exp)
      console.log("query: " + query)
      console.log("current_page: " + current_page)
      let url = `http://localhost:8090/get?q=${query}&applyPR=${pR}&exp=${exp}`
      console.log("URL is: " + url)

      axios.get(url, {
      })
      .then(function (response) {
        console.log(response)
        let expan_query = response.data.expanded_query;
        console.log(expan_query)
        let query_list = expan_query.split(" ");
        let expansion_result = []
        for( var i = 0; i < query_list.length; i++ ){
          let item = {
            name: query_list[i]
          }
          expansion_result.push(item)
        }

        context.commit('UPDATE_QUERY_EXPANSION', expansion_result)
        let hits = response.data.hits;
        let result = []
        console.log(hits);
        for( var i = 0; i < hits.length; i++ ){
          let item = {
            // name: "hit " + i, 
            // url: "", 
            // snippet: "no description", 
            // displayUrl: hits[i].path
            name: "Rank " + i, 
            url: hits[i].url, 
            snippet: "path:" + hits[i].path, 
            displayUrl: hits[i].url
          }
          result.push(item)
        }
        console.log(result);
        context.commit('ADD_MINE', result)
      })
      .catch(function (error) {
        console.log(error);
      });
    },
    
    fecthBing(context, param) {
      console.log("fetch bing")
      let start = param.key2;
      let query = param.key1
      console.log(query)
      let offset = 0;
      axios.get('https://api.cognitive.microsoft.com/bing/v5.0/search?', {
        params: {
          q: query,
          offset: start,
          count: "30",
          mkt: "en-us",
          customconfig:"",
        },
        headers: {"Ocp-Apim-Subscription-Key": ""}
      })
      .then(function (response) {
        let observableData = response.data.webPages.value;
        let result = []
        console.log(observableData);
        for( var i = 0; i < observableData.length; i++ ){
          let item = {
            name: observableData[i].name, 
            url: observableData[i].url, 
            snippet: observableData[i].snippet, 
            displayUrl: observableData[i].displayUrl
          }
          result.push(item)
        }
        console.log(result);
        context.commit('ADD_BING', result)
      })
      .catch(function (error) {
        console.log(error);
      });
    },

    emptyGoogle(context) {
      context.commit('EMPTY_GOOGLE')
    },

    fecthGoogle(context, param) {
      console.log("fetch google")
      console.log(param)
      let offset = param.key2;
      let query = param.key1
      axios.get('https://www.googleapis.com/customsearch/v1?', {
        params: {
          key:" ",
          cx:"",
          q: query,
          num: 10,
          start:offset,
        },
      })
      .then(function (response) {
        console.log(response)
        let observableData = response.data.items;
        let result = []
        console.log(observableData);
        for( var i = 0; i < observableData.length; i++ ){
          let item = {
            name: observableData[i].title, 
            url: observableData[i].link, 
            snippet: observableData[i].snippet, 
            displayUrl: observableData[i].displayLink
          }
          result.push(item)
        }
        console.log(result);
        context.commit('ADD_GOOGLE', result)
      })
      .catch(function (error) {
        console.log(error);
      });
    },

  },

});

export default dataWareHouse
