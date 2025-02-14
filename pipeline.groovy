pipeline {
    agent any
    environment {
        DOCKER_IMAGE = "yassined97/my_new_stmdevenv:latest"
        WORKSPACE_PATH = "/workspace"
    }
    stages {
        stage('Initialization') {
            steps {
                echo "New push detected"
            }
        }
        stage('Build in Docker') {
            steps {
                script {
                    sh """
                    docker run -i --rm \
                        -v \$(pwd):${WORKSPACE_PATH} ${DOCKER_IMAGE} /bin/bash -c '
                        #cd ${WORKSPACE_PATH}/stm32_Env &&
                        ls &&
                        cd workspace &&
                        ls &&
                        git pull origin main &&
                        #cd ${WORKSPACE_PATH}/stm32_Env/workspace &&
                        /opt/st/stm32cubeide_1.15.0/stm32cubeide --launcher.suppressErrors -nosplash \
                            -application org.eclipse.cdt.managedbuilder.core.headlessbuild \
                            -data "./" \
                            -import "./UART_Transmit/" \
                            -build UART_Transmit
                    '
                    """
                }
            }
        }
    }
    post {
        always {
            echo "Pipeline completed."
        }
        failure {
            echo "Build failed!"
        }
    }
}