pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh './gradlew tariffsdk::assembleDebug --no-daemon'
            }
        }
        stage('Test') {
            steps {
                sh './gradlew tariffsdk::test --no-daemon'
            }
        }
    }
}