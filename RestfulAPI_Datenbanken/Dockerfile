FROM adoptopenjdk/openjdk14:alpine-slim AS build
WORKDIR /app
COPY . ./
RUN ./gradlew --no-daemon --stacktrace clean shadowJar

FROM adoptopenjdk/openjdk14:alpine-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
RUN mkdir data
CMD java -jar app.jar