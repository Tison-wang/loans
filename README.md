#prod环境相关配置

jdk安装目录：/opt/java

redis启动端口：6666

mysql安装目录：/usr/local/mysql 端口：33306

nginx安装目录：/usr/local/nginx

ssl证书存放目录：/usr/local/nginx/cert

后台接口jar包存放目录：/opt/loan/api-server

启动流程：cd /opt/loan/api-server && sh springBoot_prod.sh [start|stop|restart] jar包

前端页面项目存放目录：

    前端页面：/opt/loan/web-site/front
    后台页面：/opt/loan/web-site/back
    
    
#站点域名配置
后台新建站点之后，超级管理员在站点配置-》站点列表中找到创建的站点，修改域名信息之后，登录服务器

上传证书文件到/usr/local/nginx/cert/新建文件夹

将如下配置添加到/usr/local/nginx/conf/nginx.conf中


    server {
    	listen       443 ssl;
            server_name  www.域名 域名;
    
            ssl_certificate     /usr/local/nginx/cert/新建文件夹/full_chain.pem;
            ssl_certificate_key  /usr/local/nginx/cert/新建文件夹/private.key;
    
            ssl_session_cache    shared:SSL:1m;
            ssl_session_timeout  5m;
    
            ssl_ciphers  HIGH:!aNULL:!MD5;
            ssl_prefer_server_ciphers  on;
    
    	location /api/ {
        	    proxy_pass          http://127.0.0.1:8081;
                proxy_set_header    Host    $host;
                proxy_set_header    X-Real-IP   $remote_addr;
                proxy_set_header    X-Forwarded-For $proxy_add_x_forwarded_for;
    	}
    
    	location /admin {
                alias  /opt/loan/website/admin;
                try_files $uri $uri/ @router_admin;
            }
    
    	location @router_admin{
                rewrite ^.*$ /admin/index.html break;
            }
    
    	location / {
                root  /opt/loan/website/front;
                try_files $uri $uri/ @router;
    	}
    
    	location @router{
                rewrite ^.*$ /index.html last;
    	}
    
    
       #error_page  404              /404.html;
    
       # redirect server error pages to the static page /50x.html
       #
       error_page   500 502 503 504  /50x.html;
       location = /50x.html {
          root   html;
       }
    }
    
#其他
服务迁移：安装jdk mysql redis nginx

创建loan数据库实例，执行当前工程tools文件夹下的 loan.sql文件创建表