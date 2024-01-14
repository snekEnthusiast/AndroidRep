FROM ubuntu:22.04
RUN apt-get -y update
RUN apt-get -y install git
#RUN snap install ngrok
#clone repo
RUN git clone https://github.com/snekEnthusiast/AndroidRep
RUN (cd AndroidRep/z2/ktor-sample; ./gradlew build)
ENTRYPOINT ["(cd AndroidRep/z2;./startup.bash)"]
