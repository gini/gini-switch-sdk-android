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
                sh 'emulator -avd mobilecd_android-25_google_apis-x86_512M'
                sh './gradlew tariffsdk::connectedAndroidTest'
            }
        }
    }
}