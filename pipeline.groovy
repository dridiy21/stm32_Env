pipeline {
    agent any
    
    environment {
        DOCKER_IMAGE = "yassined97/my_new_stmdevenv:latest" // Replace with the name of your Docker image
        WORKSPACE_DIR = "/workspace/stm32_project" // Directory inside the Docker container for the project
        GIT_REPO = "https://github.com/dridiy21/stm32_Env.git" // Your GitHub repository URL
    }

    stages {
        stage('Initialisation') {
            steps {
                echo 'New push detected' //Future feature : echo the commit message
            }
        }

        stage('Build in Docker') {
            environment {
                DOCKER_IMAGE = "yassined97/my_new_stmdevenv:latest" // Docker image name
                GIT_PROJECT_DIR = "/workspace/stm32_project" // Path to the project directory inside the container
            }
            steps {
                script {
                    // Run the Docker container and execute your commands step-by-step
                    sh """
                    docker run --rm -v \$(pwd):/workspace ${DOCKER_IMAGE} bash -c '
                        # Navigate to the GitHub project directory
                        cd ${GIT_PROJECT_DIR}
                        
                        # Pull the latest changes from the main branch
                        git pull origin main
                        
                        # Navigate to the root workspace
                        cd /workspace
                        
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
