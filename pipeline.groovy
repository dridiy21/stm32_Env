pipeline {
    agent any
    stages {
        stage('Check User') {
            steps {
                sh 'whoami'
            }
        }
        stage('Build in Docker') {
            steps {
                script {
                    sh """
                    docker run -i --rm \\
                        -v \$(pwd):/workspace yassined97/my_new_stmdevenv:latest /bin/bash -c '
                        git pull origin main &&
                        /opt/st/stm32cubeide_1.15.0/stm32cubeide --launcher.suppressErrors -nosplash \\
                            -application org.eclipse.cdt.managedbuilder.core.headlessbuild \\
                            -data "./" \\
                            -import "./UART_Transmit/" \\
                            -build UART_Transmit
                    '
                    """
                }
            }
        }
    }
}   
