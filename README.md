### pinyin and ik 分词
step1: es集群中安装pinyin 和 ik的plugin
```shell script
./bin/elasticsearch-plugin install https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v7.3.0/elasticsearch-analysis-ik-7.3.0.zip
./bin/elasticsearch-plugin install https://github.com/medcl/elasticsearch-analysis-pinyin/releases/download/v7.3.0/elasticsearch-analysis-pinyin-7.3.0.zip
```
step2: 添加自己的扩展词库

step3：针对GLA 和 XXA级别的分词要特殊对待

    1.分词会将A作为stop给去除-->分词stop字典去除掉a
    2.LA为英文单词--> 在数据建模的时候添加char_filter，将LA => L A

step4：进行springboot开发

step5：