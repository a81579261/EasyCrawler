<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>我爬</title>
    <script src="https://cdn.staticfile.org/vue/2.4.2/vue.min.js"></script>
    <script src="https://cdn.staticfile.org/vue-resource/1.5.1/vue-resource.min.js"></script>
    <!-- 新 Bootstrap4 核心 CSS 文件 -->
    <link rel="stylesheet" href="https://cdn.staticfile.org/twitter-bootstrap/4.3.1/css/bootstrap.min.css">
    <!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
    <script src="https://cdn.staticfile.org/jquery/3.2.1/jquery.min.js"></script>
    <!-- bootstrap.bundle.min.js 用于弹窗、提示、下拉菜单，包含了 popper.min.js -->
    <script src="https://cdn.staticfile.org/popper.js/1.15.0/umd/popper.min.js"></script>
    <!-- 最新的 Bootstrap4 核心 JavaScript 文件 -->
    <script src="https://cdn.staticfile.org/twitter-bootstrap/4.3.1/js/bootstrap.min.js"></script>
    <script src="https://cdn.staticfile.org/axios/0.18.0/axios.min.js"></script>

    <script src="js/index.js"></script>
</head>
<body>
<div class="col-6 mx-auto">
    <h1 align="center" style="color: blueviolet">我要一步一步往上爬</h1>
</div>

<form id="form">
    <div class="col-6 mx-auto">
        <div class="form-group">
            <label>爬取网站:</label>
            <input type="text" class="form-control" v-model="dataTable.page">
        </div>
        <div>
            <label class="radio-inline col-3" for="html"><input id="html" type="radio" v-model="method" value="html">HTML页面爬取</label>
            <label class="radio-inline col-3" for="post"><input id="post" type="radio" v-model="method" value="post">POST请求爬取</label>
        </div>
        <div v-if="method == 'html'">
            <div class="form-group">
                <label>获取第几个页面:</label>
                <select class="form-control" v-model="dataTable.depth">
                    <option value="1">1</option>
                    <option value="2">2</option>
                    <option value="3">3</option>
                    <option value="4">4</option>
                    <option value="5">5</option>
                </select>
            </div>
            <template v-if="dataTable.depth >= 2">
                <div class="form-group">
                    <label>第二页获取:</label>
                    <input type="text" class="form-control" v-model="dataTable.links1">
                </div>
            </template>
            <template v-if="dataTable.depth >= 3">
                <div class="form-group">
                    <label>第三页获取:</label>
                    <input type="text" class="form-control" v-model="dataTable.links2">
                </div>
            </template>
            <template v-if="dataTable.depth >= 4">
                <div class="form-group">
                    <label>第四页获取:</label>
                    <input type="text" class="form-control" v-model="dataTable.links3">
                </div>
            </template>
            <template v-if="dataTable.depth >= 5">
                <div class="form-group">
                    <label>第五页获取:</label>
                    <input type="text" class="form-control" v-model="dataTable.links4">
                </div>
            </template>
        </div>
        <div v-if="method == 'post'">
            <div class="form-group">
                <label for="content">请求报文:</label>
                <textarea class="form-control" rows="5" id="content" v-model="dataTable.content"></textarea>
            </div>
            <div class="form-group">
                <label>结果筛选表达式:</label>
                <input type="text" class="form-control" v-model="dataTable.jsonPathStr">
            </div>
            <div class="form-group">
                <label>增量参数:</label>
                <input type="text" class="form-control" v-model="dataTable.ascParam">
            </div>
            <div class="form-group">
                <label>增量参数最大值:</label>
                <input type="text" class="form-control" v-model="dataTable.maxAscParam">
            </div>
        </div>
        <div class="form-group">
            <label>获取几个字段:</label>
            <select class="form-control" v-model="columnNum">
                <option value="0">0</option>
                <option value="1">1</option>
                <option value="2">2</option>
                <option value="3">3</option>
                <option value="4">4</option>
                <option value="5">5</option>
                <option value="6">6</option>
                <option value="7">7</option>
                <option value="8">8</option>
                <option value="9">9</option>
                <option value="10">10</option>
            </select>
        </div>
        <template v-if="columnNum >= 1">
            <div class="form-group">
                <label>获取字段1:</label>
                <input type="text" class="form-control" v-model="dataTable.column1">
            </div>
        </template>
        <template v-if="columnNum >= 2">
            <div class="form-group">
                <label>获取字段2:</label>
                <input type="text" class="form-control" v-model="dataTable.column2">
            </div>
        </template>
        <template v-if="columnNum >= 3">
            <div class="form-group">
                <label>获取字段3:</label>
                <input type="text" class="form-control" v-model="dataTable.column3">
            </div>
        </template>
        <template v-if="columnNum >= 4">
            <div class="form-group">
                <label>获取字段4:</label>
                <input type="text" class="form-control" v-model="dataTable.column4">
            </div>
        </template>
        <template v-if="columnNum >= 5">
            <div class="form-group">
                <label>获取字段5:</label>
                <input type="text" class="form-control" v-model="dataTable.column5">
            </div>
        </template>
        <template v-if="columnNum >= 6">
            <div class="form-group">
                <label>获取字段6:</label>
                <input type="text" class="form-control" v-model="dataTable.column6">
            </div>
        </template>
        <template v-if="columnNum >= 7">
            <div class="form-group">
                <label>获取字段7:</label>
                <input type="text" class="form-control" v-model="dataTable.column7">
            </div>
        </template>
        <template v-if="columnNum >= 8">
            <div class="form-group">
                <label>获取字段8:</label>
                <input type="text" class="form-control" v-model="dataTable.column8">
            </div>
        </template>
        <template v-if="columnNum >= 9">
            <div class="form-group">
                <label>获取字段9:</label>
                <input type="text" class="form-control" v-model="dataTable.column9">
            </div>
        </template>
        <template v-if="columnNum >= 10">
            <div class="form-group">
                <label>获取字段10:</label>
                <input type="text" class="form-control" v-model="dataTable.column10">
            </div>
        </template>

        <div>
            <input type="button" @click="submit()" class="btn btn-info btn-lg col-12" value="确认">
        </div>
        <div style="margin: 5px">
            <input type="button" class="btn btn-primary btn-lg col-12"
                   data-toggle="modal" data-target="#exportModal" value="导出Excel">
        </div>

        <!-- 模态框 -->
        <div class="modal fade" id="exportModal">
            <div class="modal-dialog">
                <div class="modal-content">

                    <!-- 模态框头部 -->
                    <div class="modal-header">
                        <h4 class="modal-title">导出Excel</h4>
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                    </div>

                    <!-- 模态框主体 -->
                    <div class="modal-body">
                        <div class="form-group">
                            <label>提取码:</label>
                            <input type="text" class="form-control" v-model="dataTable.exportKey">
                        </div>
                    </div>

                    <!-- 模态框底部 -->
                    <div class="modal-footer">
                        <button type="button" @click="exportExcel()" class="btn btn-primary" data-dismiss="modal">确认</button>
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">关闭</button>
                    </div>

                </div>
            </div>
        </div>
    </div>
</form>
</body>
</html>