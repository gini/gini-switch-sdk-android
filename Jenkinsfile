pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh './gradlew tariffsdk::assembleDebug --no-daemon'
            }
        }
        stage('Unit tests') {
            steps {
                sh './gradlew tariffsdk::test --no-daemon'
            }
        }
        stage('Instrumentation tests') {
            steps {
                sh './gradlew tariffsdk::connectedAndroidTest'
            }
        }
    }

    post {
        success {
            junit '**/test-results/**/*.xml'
        }
        always {
            deleteDir()
        }
    }
}