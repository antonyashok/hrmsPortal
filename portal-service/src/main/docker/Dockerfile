FROM openjdk:8-jre-alpine

ENV JHIPSTER_SLEEP 5

COPY . /

VOLUME /tmp
EXPOSE 8085

CMD mv /*.jar /app.jar && \ 
	echo "The application will start with profile ${APPLICATION_PROFILES:-none} in ${JHIPSTER_SLEEP}s..." && \
	sleep ${JHIPSTER_SLEEP} && \
	java -Dconfiguration_service_uri=${CONFIGURATION_SERVICE_URI} -Dspring.profiles.active=${APPLICATION_PROFILES} -Djava.security.egd=file:/dev/./urandom -jar /app.jar