#!/bin/bash

# git clone address
git_address=git@github.com:Tison-wang/loan.git
# the java code address
code_address=/usr/local/share/applications/code
# jar where dir
jar_address=/opt/loan/api-server

function downcode()
{
  if [ -d "${code_address}" ];
  then
    echo "code目录存在"
  else
    echo "code目录不存在，创建目录"
    mkdir ${code_address}
    chmod 777 ${code_address}
    echo "code目录创建成功"
  fi

  rm -rf ${code_address}/*
  echo "从git上下载代码...请稍等"
  cd ${code_address}
  git clone ${git_address}
  echo "下载完毕"
  echo "代码下载地址：${code_address}"
}

function clean()
{
   if [ -d "${code_address}/loan/loan/target" ];
   then
     echo "清理 target 目录"
     cd ${code_address}/loan/loan/target
     rm -rf ./*
     echo "清理完毕"
   else
     echo "target目录不存在，无需清理"
   fi
}

function package()
{
   if [ -d "${code_address}/loan/loan" ];
   then
     cd ${code_address}/loan/loan/
     echo "开始打包项目...请稍等"
     mvn clean package -P prod -Dmaven.test.skip=true
     echo "打包完毕"
   else
     downcode
   fi
}

function deploy()
{
   echo "开始部署...请稍等"
   sleep 1
   echo "删除 jar 包"
   rm -rf ${jar_address}/*.jar
   echo "复制 jar 包"
   cp ${code_address}/loan/loan/target/*.jar ${jar_address}
   echo "打开 jar 包所在目录"
   cd ${jar_address}
   echo "正在启动..."
   sh springBoot_prod.sh restart *.jar
   echo "启动成功"
   echo "部署完毕"
}

function execjar()
{
  downcode
  sleep 1
  clean
  sleep 1
  package
  sleep 1
  deploy
}

case $1 in
    execjar)
    execjar;;
    clean)
    clean;;
    package)
    package;;
    downcode)
    downcode;;
    *)
      echo -e "\033[0;31m Usage: \033[0m  \033[0;34m sh  $0  {downcode|clean|package|execjar|} \033[0m
\033[0;31m Example: \033[0m
      \033[0;33m sh  $0  execjar \033[0m"
esac
