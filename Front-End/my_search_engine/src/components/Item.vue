<template>
    <div class="item" v-on:click="newTab">
        <!-- <h3 class="title">{{ name }}</h3>
        <p class="displayUrl">{{ displayUrl }}</p>
        <p class="snippet">{{ snippet }}</p>
        <br> -->
        <at-card >
            <h4 slot="title">{{ name }}</h4>
            <a class="displayUrl">{{ displayUrl }}</a>
            <p>
                {{ snippet }}
            </p>
        </at-card>
    </div>
</template>

<script>
export default {
  name: 'item',
  props: ['name', 'url', 'snippet', 'displayUrl'],
  methods: {
      newTab: function() {
        window.open( this.url, "_blank");  
      }
  }
}
</script>

<style>
.item a {
    font-size: 0.7em;
    text-align: left;
    overflow: hidden;
    /* white-space: nowrap; */
    display: block;
    width: 100%;
}

.item {
    text-align: left;
    display: block;
    margin: 10px;
}


displayUrl {
    font-size: 7px;
    padding-top: 1px;
}

snippet {
    font-size: small;
    font-family: 'Roboto',arial,sans-serif;
    display: block;
    color: #545454;
    line-height: 18px;  
}

p {
    word-wrap: break-word; 
    display: inline-block;
    line-height: 1.4;
}

</style>
