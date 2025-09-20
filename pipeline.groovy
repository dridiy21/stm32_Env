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

        stage('Run Docker') {
            steps {
                sh '''
                    docker run --rm \
                    yassined97/my_new_stmdevenv:latest \
                    bash -c "whoami && cd stm32_Env && git pull && cd workspace && /opt/st/stm32cubeide_1.15.0/stm32cubeide --launcher.suppressErrors -nosplash -application org.eclipse.cdt.managedbuilder.core.headlessbuild -data ./ -import ./UART_Transmit/ -build UART_Transmit"
                '''
            }
        }
    }
}
