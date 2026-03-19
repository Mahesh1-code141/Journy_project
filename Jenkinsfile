pipeline {
    agent any

    tools {
        maven 'Maven'   // Ensure Maven is configured in Jenkins
    }

    environment {
        GIT_REPO       = "https://github.com/Mahesh1-code141/Journy_project.git"
        GIT_BRANCH     = "main"

        DOCKERHUB_USER = "mahesh2452"
        IMAGE_NAME     = "journey-app"   // lowercase best practice
        IMAGE_TAG      = "${BUILD_NUMBER}"
        LATEST_TAG     = "latest"

        DOCKER_CREDS   = "Docker_CRED"

        CONTAINER_NAME = "journey-app"
        HOST_PORT      = "2027"
        CONTAINER_PORT = "8080"

        WAR_FILE       = "journey_project.war"  // match your actual WAR name
    }

    stages {

        stage('Checkout Code') {
            steps {
                git branch: "${GIT_BRANCH}", url: "${GIT_REPO}"
            }
        }

        stage('Build WAR File') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Verify WAR File') {
            steps {
                sh """
                if [ ! -f target/${WAR_FILE} ]; then
                    echo "ERROR: WAR file target/${WAR_FILE} not found!"
                    ls target/
                    exit 1
                fi
                """
            }
        }

        stage('Build Docker Image') {
            steps {
                sh """
                docker build -t ${DOCKERHUB_USER}/${IMAGE_NAME}:${IMAGE_TAG} .
                docker tag ${DOCKERHUB_USER}/${IMAGE_NAME}:${IMAGE_TAG} ${DOCKERHUB_USER}/${IMAGE_NAME}:${LATEST_TAG}
                """
            }
        }

        stage('DockerHub Login') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: "${DOCKER_CREDS}",
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'
                }
            }
        }

        stage('Push Image to DockerHub') {
            steps {
                sh """
                docker push ${DOCKERHUB_USER}/${IMAGE_NAME}:${IMAGE_TAG}
                docker push ${DOCKERHUB_USER}/${IMAGE_NAME}:${LATEST_TAG}
                """
            }
        }

        stage('Deploy Container') {
            steps {
                sh """
                docker stop ${CONTAINER_NAME} || true
                docker rm ${CONTAINER_NAME} || true

                docker run -d \
                    -p ${HOST_PORT}:${CONTAINER_PORT} \
                    --name ${CONTAINER_NAME} \
                    ${DOCKERHUB_USER}/${IMAGE_NAME}:${IMAGE_TAG}
                """
            }
        }

        stage('Cleanup Old Images') {
            steps {
                sh 'docker image prune -f'
            }
        }
    }
}
