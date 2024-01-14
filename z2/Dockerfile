FROM ubuntu:22.04
RUN apt-get -y update
RUN apt-get -y install git
RUN apt-get -y install curl
RUN apt install default-jdk -y
RUN curl -s https://ngrok-agent.s3.amazonaws.com/ngrok.asc | tee /etc/apt/trusted.gpg.d/ngrok.asc >/dev/null && echo "deb https://ngrok-agent.s3.amazonaws.com buster main" | tee /etc/apt/sources.list.d/ngrok.list && apt update && apt install ngrok
#clone repo
RUN git clone https://github.com/snekEnthusiast/AndroidRep
RUN (cd AndroidRep/z2/ktor-sample; ./gradlew build)
ENTRYPOINT ["(cd AndroidRep/z2;./startup.bash)"]
