FROM openjdk:17
WORKDIR /usr/src
COPY . /usr/src
RUN /usr/src/gradlew build
CMD [ "./gradlew", "run" ]
