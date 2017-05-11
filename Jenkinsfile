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
                junit '**/test-results/**/*.xml'
            }
        }
        stage('Instrumentation tests') {
            lock('emulator mobilecd_android-25_google_apis-x86_512M') {}
            steps {
                sh './gradlew tariffsdk::connectedAndroidTest'
            }
        }
    }
}