<template>
    <div class="tag">
        <at-tag color="info">{{ name }}</at-tag>
    </div>
</template>

<script>
export default {
  name: 'queryExpansion',
  props: ['name'],
}
</script>

<style>
.tag {
    margin: 2px;
    padding-top: 6px;
}
</style>
