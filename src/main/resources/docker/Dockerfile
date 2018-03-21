FROM openjdk:8-jre

MAINTAINER Christopher Johnson <christopher_hanna.johnson@uni-leipzig.de>
LABEL description = "Provides an XSLT api"

ENV XSLT_API_RUNTIME /opt
ENV JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-amd64/jre

RUN apt-get update \
&& apt-get install openssl

WORKDIR ${XSLT_API_RUNTIME}
COPY *.tar .
RUN tar -xvf *.tar && \
mv api-0.2.0-SNAPSHOT api && \
chmod +x ${XSLT_API_RUNTIME}/api/bin/api
COPY entrypoint.sh .
RUN chmod +x entrypoint.sh
RUN openssl s_client -connect goobi-prod.ub.uni-leipzig.de:12105 > goobi.pem \
   && keytool -import -alias amazon1 -storepass changeit -noprompt -keystore ${JAVA_HOME}/lib/security/cacerts -file goobi.pem
ENTRYPOINT [ "./entrypoint.sh" ]