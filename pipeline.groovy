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
            steps {
                script {
                    // Run the Docker container and execute commands step-by-step
                    sh """
                    docker run --rm -v \$(pwd):${WORKSPACE_DIR} ${DOCKER_IMAGE} bash -c '
                        # Clone the repo if the directory does not exist
                        if [ ! -d "${WORKSPACE_DIR}/.git" ]; then
                            git clone ${GIT_REPO} ${WORKSPACE_DIR};
                        else
                            # Pull latest changes if repo already cloned
                            cd ${WORKSPACE_DIR} && git pull;
                        fi
                        
                        # Change to the project directory and compile
                        cd ${WORKSPACE_DIR} && make
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
