# Combosearch聚合搜索平台

## 项目介绍

### 项目简介
该项目基于 Spring Boot + MyBatis-Plus + MySQL + Elasticsearch + Logstash 开发的数据聚合搜索平台，能够在一个页面集中搜索出不同来源与内容的数据，目的是为了提高用户的检索体验。

### 项目结构
如图所示
![图片](https://github.com/Penglg/combosearch-backend/assets/109449337/cfcef789-52a8-4a85-9051-6f72500ee0ab)


## 快速开始
### 前端项目安装启动
1. 下载前端项目，地址：https://github.com/Penglg/combosearch-frontend
   ```git clone https://github.com/Penglg/combosearch-frontend.git```
2. 启动项目
   打开package.json文件，运行`serve`命令

### 后端项目安装启动
1. 下载后端项目，地址：https://github.com/Penglg/combosearch-backend
   ```git clone https://github.com/Penglg/combosearch-backend.git```
2. 下载ik分词器（地址：https://github.com/infinilabs/analysis-ik）
   ik分词器对中文友好
   7.17.18版本下载地址：https://github.com/infinilabs/analysis-ik/releases/tag/v7.17.18
   在elasticsearch下载完成后，将ik分词器解压到elasticsearch/plugins目录下
3. 安装elasticsearch并启动
   本人为7.17.19
   在bin目录下执行`elasticsearch.bat`，即可启动elasticsearch
   在combosearch项目的yml文件中将elasticsearch配置为自己的elasticsearch
4. 安装logstash并启动
   本人为7.17.19（elasticsearch、logstash版本要强一致）
   在config目录下新建`mytask.conf`文件
   将文件内容设置为
   ```
   input {
        jdbc {
            jdbc_driver_library => "E:\Developer\mysql-connector-java\mysql-connector-java-8.0.28\mysql-connector-java-8.0.28.jar"
            jdbc_driver_class => "com.mysql.cj.jdbc.Driver"
            jdbc_connection_string => "jdbc:mysql://localhost:3306/combo_search"
            jdbc_user => "root"
            jdbc_password => "123456"
            statement => "SELECT * FROM post WHERE updateTime > :sql_last_value AND updateTime < NOW() ORDER BY updateTime DESC"
            tracking_column => "updatetime"
            tracking_column_type => "timestamp"
            use_column_value => true
            parameters => { "favorite_artist" => "Beethoven" }
            schedule => "*/5 * * * * *"
            jdbc_default_timezone => "Asia/Shanghai"
        }
    }
    
    filter {
        mutate {
            rename => {
                "updatetime" => "updateTime"
                "userid" => "userId"
                "createtime" => "createTime"
                "isdelete" => "isDelete"
            }
            remove_field => ["thumbnum", "favournum"]
        }
    }
   
    output {
        elasticsearch {
            hosts => "127.0.0.1:9200"
            index => "post_v1"
            document_id => "%{id}"
        }
    }

   然后在logstash目录下，运行`.\bin\logstash.bat -f .\config\mytask.conf`启动logstash

5. 启动后端项目


## 项目展示

### 接口文档
访问http://localhost:8101/api/doc.html
![图片](https://github.com/Penglg/combosearch-backend/assets/109449337/c5611d48-6317-41ee-b506-5a11dd3ec4ac)

### Kibana可视化 
若安装了Kibana并配置好elasticsearch
可访问localhost:5601
打开Kibana的dev tools，执行GET命令
![图片](https://github.com/Penglg/combosearch-backend/assets/109449337/fcb0a1ec-0ddb-40b6-813a-421bc7fa7f5f)

### logstash同步MySQL数据库数据
![图片](https://github.com/Penglg/combosearch-backend/assets/109449337/cff09b5b-f1d0-46b7-aa0c-fe97ea1515a2)

### 搜索主页
访问localhost:8080
- 文章搜索
  ![图片](https://github.com/Penglg/combosearch-backend/assets/109449337/a9489d1d-fded-4f90-901f-4a5fb7d08ac2)
  ![图片](https://github.com/Penglg/combosearch-backend/assets/109449337/ff7622e3-efc0-495b-9eba-a313c0f7c563)
- 图片搜索
  ![图片](https://github.com/Penglg/combosearch-backend/assets/109449337/76ca191e-023d-4576-9b2c-3364e213f75e)
  ![图片](https://github.com/Penglg/combosearch-backend/assets/109449337/8e3aa78c-bb62-4681-b070-9153d7ad60ea)
- 用户搜索
  ![图片](https://github.com/Penglg/combosearch-backend/assets/109449337/efe1f41b-f6fe-43bf-b47c-7b22f2bf28cb)
