FROM python:3.11-slim-bullseye

# Install prerequisites
RUN apt-get update && apt-get install -y wget gnupg2

# Import the Corretto GPG key and add the repository
RUN wget -O- https://apt.corretto.aws/corretto.key | apt-key add - \
    && echo "deb https://apt.corretto.aws stable main" > /etc/apt/sources.list.d/corretto.list

# Update and install Amazon Corretto JDK 21
RUN apt-get update && apt-get install -y java-21-amazon-corretto-jdk

COPY cli-example /

RUN ./gradlew build

# execute gradlew run in the container
CMD ["./gradlew", "run"]