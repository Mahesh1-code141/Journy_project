pipeline {
    agent any

    environment {
        GIT_REPO       = "https://github.com/Mahesh1-code141/Journy_project.git"
        GIT_BRANCH     = "main"

        DOCKERHUB_USER = "mahesh2452"
        IMAGE_NAME     = "journey_project_img"
        IMAGE_TAG      = "${BUILD_NUMBER}"

        DOCKER_CREDS   = "Docker_CRED"

        CONTAINER_NAME = "journey_project_cont"
        HOST_PORT      = "2006"
        CONTAINER_PORT = "8080"
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
                sh '''
                echo "Checking target folder..."
                ls -l target/

                if ! ls target/*.war 1> /dev/null 2>&1; then
                    echo "ERROR: WAR file not found!"
                    exit 1
                fi
                '''
            }
        }

        stage('Check Docker Access') {
            steps {
                sh '''
                echo "Checking Docker access..."
                docker ps || (echo "ERROR: Docker permission issue" && exit 1)
                '''
            }
        }

        stage('Build Docker Image') {
            steps {
                sh '''
                docker build -t ${DOCKERHUB_USER}/${IMAGE_NAME}:${IMAGE_TAG} .
                '''
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
                sh "docker push ${DOCKERHUB_USER}/${IMAGE_NAME}:${IMAGE_TAG}"
            }
        }

        stage('Deploy Container') {
            steps {
                sh '''
                docker stop ${CONTAINER_NAME} || true
                docker rm ${CONTAINER_NAME} || true

                docker run -d \
                    -p ${HOST_PORT}:${CONTAINER_PORT} \
                    --name ${CONTAINER_NAME} \
                    ${DOCKERHUB_USER}/${IMAGE_NAME}:${IMAGE_TAG}
                '''
            }
        }
    }
}
