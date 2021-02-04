### pinyin and ik 分词
step1: es集群中安装pinyin 和 hanlp的plugin
```shell script
./bin/elasticsearch-plugin install https://github.com/KennFalcon/elasticsearch-analysis-hanlp/releases/download/v7.3.0/elasticsearch-analysis-hanlp-7.3.0.zip
./bin/elasticsearch-plugin install https://github.com/medcl/elasticsearch-analysis-pinyin/releases/download/v7.3.0/elasticsearch-analysis-pinyin-7.3.0.zip
```

step2： 针对无法分解出的字母特殊处理

    1.在自定义的analyser时，对chart filter做map映射处理 参照mapping.file

step3：进行springboot开发

    使用elasticsearch-rest-high-level-client的API
    API的使用参照官方网站提供
    https://www.elastic.co/guide/en/elasticsearch/client/java-rest/7.9/java-rest-high.html
    
    已经做好了跨域的配置
    采用Jasypt进行对配置es集群用户名密码的加密
   

step4：进行校验
    postman