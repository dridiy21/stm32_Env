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
                    docker run -it --rm \
                        -v \$(pwd):${WORKSPACE_PATH} ${DOCKER_IMAGE} /bin/bash -c '
                        cd ${WORKSPACE_PATH}/stm32_Env &&
                        git pull origin main &&
                        cd workspace &&
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







pipeline {
    agent any
    
    stages {
        stage('Hello') {
            steps {
                checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/dridiy21/stm32_Env.git']])
                echo 'Hello, World!'
            }
        }
    }
}



//random comment
