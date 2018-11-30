<template>
    <div class="searchBox">
        <at-input v-model="input" size="large" placeholder="Please input" append-button>
            <template slot="append">
                <i v-on:click="submit" class="icon icon-search"></i>
            </template>
        </at-input>
        <at-button-group class="button">
            <at-button v-on:click="backward"><i class="icon icon-chevron-left"></i>Go back</at-button>
            <p class="page">Page {{ page_offset | page }} </p>
            <at-button @click="forward">Go forward<i class="icon icon-chevron-right"></i></at-button>
        </at-button-group>
    </div>
</template>

<script>
export default {
  name: 'searchBox',
  data: function() {
        return {
            input: '',
            page_offset: 10,
        }
  },
  methods: {
        submit: function(){
            let param = {"key1": this.$data.input, "key2": this.$data.page_offset}
            this.$store.dispatch('update_query', this.$data.input)
            this.my_search()
            // this.$store.dispatch('emptyGoogle')
            // this.$store.dispatch('fecthGoogle', param)
            // this.$store.dispatch('emptyBing')
            // this.$store.dispatch('fecthBing', param)
        },
        onEnterClick: function(){
            this.submit();
        },
        backward: function(){
            if( this.$data.page_offset > 10 ){
                this.$data.page_offset -= 10;
                this.$store.dispatch("update_page", this.$data.page_offset)
                this.submit();
            }
        },
        forward: function(){
            this.$data.page_offset += 10;
            this.$store.dispatch("update_page", this.$data.page_offset)
            this.submit();
        },
        my_search: function(){
            let params = this.$store.getters.my_param
            let info = [params, [this.$data.input], [this.$data.page_offset]]
            this.$store.dispatch('fetchMine', info)
        }
  },
  filters: {
      page: num => `${num / 10}`,
  }

}
</script>

<style>
.searchBox {
    width: 80%;
    margin: auto;
}

.searchBox .button {
    margin: 10px;
}
.searchBox .page {
    margin: 20px;
}
</style>
