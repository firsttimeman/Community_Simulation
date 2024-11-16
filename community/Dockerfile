# 자바 17 런타임을 사용하는 slim 이미지를 사용합니다.
FROM openjdk:17-jdk-slim

# 빌드 과정에서 사용할 JAR 파일 이름을 변수로 선언합니다.
ARG JAR_FILE=build/libs/*.jar

# 현재 디렉토리에서 JAR 파일을 app.jar로 컨테이너 내부에 복사합니다.
COPY ${JAR_FILE} app.jar

# 컨테이너가 시작될 때 실행할 명령어를 설정합니다.
ENTRYPOINT ["java", "-jar", "/app.jar"]