FROM ubuntu:22.04
RUN apt update
RUN apt install software-properties-common -y
RUN add-apt-repository ppa:deadsnakes/ppa -y
RUN apt update
RUN DEBIAN_FRONTEND=noninteractive TZ=Etc/UTC apt-get -y install tzdata
RUN apt install snapd -y
RUN apt install python3.8 -y
#3.0
RUN apt install default-jdk -y
RUN apt install nginx curl zip unzip -y
RUN curl -s https://get.sdkman.io | bash
#sdk install kotlin doesn't work
RUN /bin/bash -c "source /root/.sdkman/bin/sdkman-init.sh; sdk install kotlin"
#3.5
RUN /bin/bash -c "source /root/.sdkman/bin/sdkman-init.sh; sdk install gradle 8.4"
#4.0
RUN apt-get install git -y
RUN git clone https://github.com/snekEnthusiast/java-helloworld-gradle
#4.5
RUN (cd java-helloworld-gradle;./gradlew build)
#RUN /bin/bash -c "sleep 3600"
RUN (cd java-helloworld-gradle/build/classes/java/main;java org.example.Hello)
