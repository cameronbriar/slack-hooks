FROM clojure

COPY . /app

WORKDIR /app

RUN lein deps
RUN lein uberjar

ENTRYPOINT ["java", "-jar", "target/slack-hooks.jar"]

CMD [""]

EXPOSE 80
