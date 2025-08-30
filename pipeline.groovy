##### webhooks + docker run #####
pipeline {
    agent any

    triggers {
        githubPush()  // Detect GitHub webhooks (push events)
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
