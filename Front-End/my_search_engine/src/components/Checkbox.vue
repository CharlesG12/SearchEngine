<template>
    <div>
        <at-checkbox label="pageranking" v-model="my_param" checked>PageRanking</at-checkbox>
        <at-radio-group v-model="radio3" class="check radio">
            <at-radio label="Rochio">Rochio One</at-radio>
            <at-radio label="Association">Association</at-radio>
            <at-radio label="Metric">Metric</at-radio>
            <at-radio label="None">None</at-radio>
        </at-radio-group>
        <br>
    </div>
</template>

<script>
export default {
    name: 'checkbox',
    data() {
        return {
            my_param:["true"],
            radio3:["None"]
        }
    },
    methods: {
        update(param) {
            // this.$data.result += 1;
            this.$store.dispatch('update_my_param', param)
            this.my_search()
        },
        updateRadio(radio_value) {
            let check = this.$data.my_param
            let param = [{"pageRank":check}, { "method": radio_value}]
            this.update(param)
        },
        updateCheck(check_value){
            let radio = this.$data.radio3
            let param = [{"pageRank": check_value}, {"method": radio}]
        },
        my_search(){
            let params = this.$store.getters.my_param
            let query = this.$store.getters.query
            let currentPage = this.$store.getters.currentPage
            console.log(currentPage)
            if( query != null ) {
                let info = [params, [query], [currentPage]]
                console.log(info)
                this.$store.dispatch('fetchMine', info)
            }
        }
    },
    watch: {
        my_param: function(new_value) {
            this.updateCheck(new_value)
        },
        radio3: function(new_value) {
            this.updateRadio(new_value)
        }
    }
}
</script>

<style>
.check {
    display: inline;
}

.radio {
    margin-left: 20px;
}

</style>
