pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "yassined97/my_new_stmdevenv:latest" // Docker image
        GIT_PROJECT_DIR = "/workspace/stm32_Env"           // Path to the GitHub project inside the container
        BUILD_DIR = "/workspace/stm32_Env/workspace"       // Directory to navigate to before compilation
    }

    stages {
        stage('Initialization') {
            steps {
                echo 'New push detected' // Future enhancement: echo the commit message
            }
        }

        stage('Build in Docker') {
            steps {
                script {
                    sh """
                    docker run --rm -v \$(pwd):/workspace ${DOCKER_IMAGE} bash -c '
                        # Navigate to the GitHub project directory
                        cd ${GIT_PROJECT_DIR}
                        
                        # Pull the latest changes from the main branch
                        git pull origin main
                        
                        # Navigate to the build directory
                        cd ${BUILD_DIR}
                        
                        # Compile the project using STM32CubeIDE headless build
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
        success {
            echo "Build succeeded!"
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
