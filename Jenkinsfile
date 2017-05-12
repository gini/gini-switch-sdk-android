pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh './gradlew tariffsdk::assembleDebug'
            }
        }
        stage('Unit tests') {
            steps {
                sh './gradlew tariffsdk::test'
                junit '**/test-results/**/*.xml'
            }
        }
        stage('Instrumentation tests') {
            steps {
                sh './gradlew tariffsdk::connectedAndroidTest'
            }
        }
    }

    post {
        always {
            deleteDir()
        }
    }
}