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
    stage('Hockeyapp distribute') {
      when {
                  branch 'hockeyapp'
      }
      steps {
        sh './gradlew assembleHockey'
      }
  }
  post {
    always {
      deleteDir()
      sh '$ANDROID_HOME/platform-tools/adb shell input keyevent KEYCODE_POWER'

    }
  }
}
