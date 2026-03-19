pipeline {
    agent any

    environment {
        GIT_REPO       = "https://github.com/Mahesh1-code141/Journy_project.git"
        GIT_BRANCH     = "main"

        DOCKERHUB_USER = "mahesh2452"
        IMAGE_NAME     = "journey_project_img"
        IMAGE_TAG      = "${BUILD_NUMBER}"

        DOCKER_CREDS   = "Docker_CRED"
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
                ls -l target/
                if ! ls target/*.war 1> /dev/null 2>&1; then
                    echo "WAR file not found!"
                    exit 1
                fi
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

        stage('Push Image') {
            steps {
                sh '''
                docker push ${DOCKERHUB_USER}/${IMAGE_NAME}:${IMAGE_TAG}
                docker tag ${DOCKERHUB_USER}/${IMAGE_NAME}:${IMAGE_TAG} ${DOCKERHUB_USER}/${IMAGE_NAME}:latest
                docker push ${DOCKERHUB_USER}/${IMAGE_NAME}:latest
                '''
            }
        }
    }
}
