To run database migrations: 
'''
mvn flyway:clean flyway:migrate -Dflyway.cleanDisabled=false
'''

To build jar:
'''
mvn clean package
'''

To build dockerfile:
'''
docker image build -t pomodoro-spring-backend . 
'''

docker tag pomodoro-spring-backend:latest 518647659719.dkr.ecr.ap-southeast-1.amazonaws.com/pomodoro-backend:latest