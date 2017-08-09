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
                withEnv(["PATH+TOOLS=$ANDROID_HOME/tools", "PATH+TOOLS_BIN=$ANDROID_HOME/tools/bin", "PATH+PLATFORM_TOOLS=$ANDROID_HOME/platform-tools"]) {
                sh 'start-emulator.sh mobilecd_android-25_google_apis-x86_512M -prop persist.sys.language=en -prop persist.sys.country=US -no-snapshot-load -no-snapshot-save -camera-back emulated > emulator_port'
                sh 'emulator_port=$(cat emulator_port) && wait-for-emulator-to-boot.sh emulator-$emulator_port 20'
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