FROM openjdk:11
EXPOSE 8081:8081
RUN mkdir /app
RUN mkdir /app/profile_pictures
RUN mkdir /app/banner_images
RUN mkdir /app/post_contents
COPY ./build/libs/*-all.jar /app/app-backend.jar
ENTRYPOINT ["java", "-jar", "/app/app-backend.jar"]
