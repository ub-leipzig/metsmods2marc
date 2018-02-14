FROM openjdk:8-jre

MAINTAINER Christopher Johnson <christopher_hanna.johnson@uni-leipzig.de>
LABEL description = "Provides a Kafka Stream Dataset"

ENV XSLT_API_RUNTIME /opt

WORKDIR ${XSLT_API_RUNTIME}
COPY *.tar .
RUN tar -xvf *.tar && \
mv api-0.1.0-SNAPSHOT api && \
chmod +x ${XSLT_API_RUNTIME}/api/bin/api
COPY entrypoint.sh .
RUN chmod +x entrypoint.sh
ENTRYPOINT [ "./entrypoint.sh" ]