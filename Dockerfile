FROM clojure

COPY . /app

WORKDIR /app

RUN lein deps
RUN lein uberjar

CMD ["java", "-jar", "target/slack-hooks.jar"]

EXPOSE 80
