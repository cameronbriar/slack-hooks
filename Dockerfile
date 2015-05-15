FROM clojure

COPY . /app

WORKDIR /app

RUN lein deps
RUN lein uberjar

CMD "java -jar ${JAVA_OPTS} target/slack-hooks.jar"

EXPOSE 80
