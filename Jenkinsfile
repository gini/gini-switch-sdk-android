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

                sh '$ANDROID_HOME/platform-tools/adb shell input keyevent KEYCODE_POWER'
                sh './gradlew switchsdk::connectedAndroidTest'
            }


        }
    }

    post {
        always {
            deleteDir()
            withEnv(["PATH+PLATFORM_TOOLS=$ANDROID_HOME/platform-tools"]) {
                sh 'emulator_port=$(cat emulator_port) && adb -s emulator-$emulator_port emu kill || true'
            }
            sh 'rm emulator_port || true'
        }
    }
}