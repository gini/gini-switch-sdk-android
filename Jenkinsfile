pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh './gradlew switchsdk::assembleDebug'
            }
        }
        stage('Unit tests') {
            steps {
                sh './gradlew switchsdk::test'
                junit '**/test-results/**/*.xml'
            }
        }
        stage('Instrumentation tests') {
            steps {
                sh './gradlew switchsdk::connectedAndroidTest'
            }
        }
    }

    post {
        always {
            deleteDir()
        }
    }
}