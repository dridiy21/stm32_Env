##### webhooks + docker run #####
pipeline {
<<<<<<< HEAD
    agent any

=======
    agent {
        docker {
            image 'yassined97/my_new_stmdevenv'
            args '-u root:root' // Ensures we have proper permissions if needed
        }
    }
    
>>>>>>> cf340a73918909373829e60392828e6d9b40c690
    triggers {
        githubPush()  // Detect GitHub webhooks (push events)
    }

    stages {
<<<<<<< HEAD
        stage('Checkout') {
=======

        stage('Initialization') {
            steps {
                echo "New push detected"
            }
        }
        
        stage('Update Project') {
>>>>>>> cf340a73918909373829e60392828e6d9b40c690
            steps {
                checkout scmGit(
                    branches: [[name: '*/main']],
                    extensions: [],
                    userRemoteConfigs: [[url: 'https://github.com/dridiy21/stm32_Env.git']]
                )
            }
        }

        stage('Run Docker and Print User') {
            steps {
                script {
                    docker.image('yassined97/my_new_stmdevenv').inside {
                        sh 'whoami'
                    }
                }
            }
        }
    }
}

##### webhooks only #####
pipeline {
    agent any

    stages {
        stage('Hello') {
            steps { 
                checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/dridiy21/stm32_Env.git']])
            }
        }
    }
}
