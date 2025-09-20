pipeline {
    agent any

    triggers {
        githubPush()  // Detect GitHub webhooks (push events)
    }
    
    environment {
        DOCKER_IMG = "yassined97/my_new_stmdevenv:latest"
        GIT_REPO = "/workspace/stm32_Env"
        STM32CUBEIDE = "/opt/st/stm32cubeide_1.15.0/stm32cubeide"
        WORKSPACE_DIR = "workspace"
        PROJECT_NAME = "UART_Transmit"
        REPO_URL = "https://github.com/dridiy21/stm32_Env.git"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scmGit(
                    branches: [[name: '*/main']],
                    extensions: [],
                    userRemoteConfigs: [[url: 'https://github.com/dridiy21/stm32_Env.git']]
                )
            }
        }

        stage('Run Docker') {
            steps {
                sh '''
                    docker run --rm ${DOCKER_IMG} \
                    bash -c "
                    whoami && \
                    cd ${GIT_REPO} && \
                    git pull && \
                    cd ${WORKSPACE_DIR} && \
                    ${STM32CUBEIDE} --launcher.suppressErrors -nosplash -application \
                    org.eclipse.cdt.managedbuilder.core.headlessbuild -data ./ -import ./UART_Transmit/ -build ${PROJECT_NAME}"
                '''
            }
        }
    }
}
