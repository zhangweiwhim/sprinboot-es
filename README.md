### pinyin and ik 分词
step1: es集群中安装pinyin 和 ik的plugin
```shell script
./bin/elasticsearch-plugin install https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v7.3.0/elasticsearch-analysis-ik-7.3.0.zip
./bin/elasticsearch-plugin install https://github.com/medcl/elasticsearch-analysis-pinyin/releases/download/v7.3.0/elasticsearch-analysis-pinyin-7.3.0.zip
```
step2: 添加自己的扩展词库
```shell script
# 进入自己es的config目录
cd /usr/share/elasticsearch/config/analysis-ik
vim IKAnalyzer.cfg.xml
```
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd">
<properties>
	<comment>IK Analyzer 扩展配置</comment>
	<!--用户可以在这里配置自己的扩展字典 -->
	<entry key="mytest.dic"></entry>
	 <!--用户可以在这里配置自己的扩展停止词字典-->
	<entry key="ext_stopwords"></entry>
	<!--用户可以在这里配置远程扩展字典 -->
	<!-- <entry key="remote_ext_dict">words_location</entry> -->
	<!--用户可以在这里配置远程扩展停止词字典-->
	<!-- <entry key="remote_ext_stopwords">words_location</entry> -->
</properties>

```
step3：针对GLA 和 XXA级别 等特殊词的特殊处理

    1.分词会将A作为stop给去除-->分词stop字典去除掉a
    2.LA为英文单词--> 在数据建模的时候添加char_filter，将LA => L A

step4：进行springboot开发

    使用elasticsearch-rest-high-level-client的API
    API的使用参照官方网站提供
    https://www.elastic.co/guide/en/elasticsearch/client/java-rest/7.9/java-rest-high.html
    
    已经做好了跨域的配置
    采用Jasypt进行对配置es集群用户名密码的加密
   

step5：进行校验
    postman