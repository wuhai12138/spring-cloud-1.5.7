##项目所依赖的镜像
#FROM java:8
##将maven构建好的jar添加到镜像中
#ADD target/*.jar app.jar
##暴露的端口号
#EXPOSE 8080
##镜像所执行的命令
#ENTRYPOINT ["java","-jar","/app.jar"]

FROM java:8
VOLUME /tmptest
ADD target/*.jar /jar/app.jar
EXPOSE 6688
RUN cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
&& echo 'Asia/Shanghai' >/etc/timezone
#ENTRYPOINT [ "java", "-Djava.security.egd=file:/prod/./urandom", "-jar", "/jar/sfy-admin-v1.jar" ]
ENTRYPOINT [ "java", "-jar", "/jar/app.jar" ]