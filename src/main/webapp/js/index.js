window.onload = function () {
    var vm = new Vue({
        el: '#form',
        data: {
            dataTable: {
                depth: 1,
                exportKey: '',
            },
            columnNum: 0,
            method: 'html',
        },
        methods: {
            submit: function () {
                //发送 post 请求
                if (this.method == 'html') {
                    this.$http.post('/getData', this.dataTable, {emulateJSON: false}).then(function (res) {
                        alert("爬虫成功!");
                    }, function (res) {
                        console.log(res.status);
                    });
                }
                if (this.method == 'post') {
                    this.$http.post('/postData', this.dataTable, {emulateJSON: false}).then(function (res) {
                        alert("爬虫成功!提取码为:" + res.data.data);
                        console.log(res);
                    }, function (res) {
                        console.log(res.status);
                    });
                }
            },
            exportExcel: function () {
                axios.get('/exportExcel', {params: {key: this.dataTable.exportKey}})
                    .then(function (response) {
                        alert("导出成功!");
                        console.log(response);
                    })
                    .catch(function (error) { // 请求失败处理
                        console.log(error);
                    });
            }
        }
    });
}