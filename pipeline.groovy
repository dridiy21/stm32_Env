pipeline {
    agent any

    triggers {
        githubPush()  // Déclenche le pipeline sur push vers GitHub
    }

    environment {
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
                    userRemoteConfigs: [[url: "${REPO_URL}"]]
                )
            }
        }

        stage('Build inside Docker') {
            steps {
                sh """
                    docker run --rm \
                    -v \$(pwd):/workspace \\
                    yassined97/my_new_stmdevenv:latest \\
                    bash -c "
                        whoami && \
                        cd /workspace/stm32_Env/${WORKSPACE_DIR} && \
                        git pull origin main && \
                        ${STM32CUBEIDE} --launcher.suppressErrors -nosplash -application org.eclipse.cdt.managedbuilder.core.headlessbuild -data ./ -import ./${PROJECT_NAME}/ -build ${PROJECT_NAME}
                    "
                """
            }
        }
    }
}
