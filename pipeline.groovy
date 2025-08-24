pipeline {
    agent {
        docker {
            image 'yassined97/my_new_stmdevenv'
            args '-u root:root' // Ensures we have proper permissions if needed
        }
    }

    triggers {
        githubPush()  // Trigger when GitHub sends a webhook
    }

    environment {
        PROJECT_DIR = "stm32_Env"
        WORKSPACE_DIR = "workspace"
        STM32CUBEIDE = "/opt/st/stm32cubeide_1.15.0/stm32cubeide"
        PROJECT_NAME = "UART_Transmit"
    }

    stages {


        stage('Update Project') {
            steps {
                sh """
                    cd ${PROJECT_DIR}
                    git pull origin main
                """
            }
        }

        stage('Build') {
            steps {
                sh """
                    cd ${PROJECT_DIR}/${WORKSPACE_DIR}
                    ${STM32CUBEIDE} \
                      --launcher.suppressErrors \
                      -nosplash \
                      -application org.eclipse.cdt.managedbuilder.core.headlessbuild \
                      -data "./" \
                      -import "./${PROJECT_NAME}/" \
                      -build ${PROJECT_NAME}
                """
            }
        }
    }

    post {
        success {
            echo 'Build succeeded!'
        }
        failure {
            echo 'Build failed!'
        }
    }
}
