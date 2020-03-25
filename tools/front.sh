#!/bin/bash

# git clone address
git_address=git@github.com:Tison-wang/loan.git
# the front code address
CODE_ADDRESS=/usr/local/share/applications/code
# front where dir
FRONT_ADDRESS=/opt/loan/website/front

function downcode()
{
  if [ -d "${CODE_ADDRESS}" ];
  then
    echo "code目录存在"
  else
    echo "code目录不存在，创建目录"
    mkdir ${CODE_ADDRESS}
    chmod 777 ${CODE_ADDRESS}
    echo "code目录创建成功"
    rm -rf ${CODE_ADDRESS}/*
    echo "从git上下载代码...请稍等"
    cd ${CODE_ADDRESS}
    git clone ${git_address}
    echo "下载完毕"
    echo "代码下载地址：${CODE_ADDRESS}"
  fi
}

function clean()
{
   echo "开始构建vue-loan-platform 前端项目..."
   cd ${CODE_ADDRESS}/loan/vue-loan-platform
   echo -e "安装依赖... \c"
   npm install
   echo "安装完毕"
   sleep 1
   echo -e "构建项目... \c"
   npm run build
   echo "构建完毕"
}

function build()
{
   cd ${CODE_ADDRESS}/loan/vue-loan-platform
   echo -e "构建项目... \c"
   npm run build
   echo "构建完毕"
   sleep 1
   deploy
}

function deploy()
{
   echo "开始部署vue-loan-platform 前端项目...请稍等"
   sleep 1
   echo -e "清理front目录内容... \c"
   rm -rf ${FRONT_ADDRESS}/*
   echo "清理完毕"
   sleep 1
   echo -e "复制dist下文件至front目录下... \c"
   cp -r ${CODE_ADDRESS}/loan/vue-loan-platform/dist/* ${FRONT_ADDRESS}
   echo "复制完毕"
   sleep 1
   echo "部署完毕"
}

function buildFront()
{
  downcode
  sleep 1
  clean
  sleep 1
  deploy
}

case $1 in
    buildFront)
    buildFront;;
    clean)
    clean;;
    build)
    build;;
    downcode)
    downcode;;
    *)
      echo -e "\033[0;31m Usage: \033[0m  \033[0;34m sh  $0  {downcode|clean|build|deploy|buildFront|} \033[0m
\033[0;31m Example: \033[0m
      \033[0;33m sh  $0  buildFront \033[0m"
esac
