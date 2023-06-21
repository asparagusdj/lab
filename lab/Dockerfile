FROM ibmjava:8-jre
RUN mkdir /lab
COPY ./lab.jar /lab
WORKDIR /lab
ENTRYPOINT ["/bin/sh","-c","java -jar lab.jar"]
