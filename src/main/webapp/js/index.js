window.onload = function () {
    var vm = new Vue({
        el: '#form',
        data: {
            dataTable: {
                depth: 1,
            },
            columnNum: 0,
            method: 'html',
        },
        methods: {
            submit: function () {
                //发送 post 请求
                if (this.method == 'html'){
                    this.$http.post('/getData', this.dataTable, {emulateJSON: false}).then(function (res) {
                        alert("爬虫成功!")
                    }, function (res) {
                        console.log(res.status);
                    });
                }
                if (this.method == 'post'){
                    this.$http.post('/postData', this.dataTable, {emulateJSON: false}).then(function (res) {
                        alert("爬虫成功!")
                    }, function (res) {
                        console.log(res.status);
                    });
                }

            }
        }
    });
}