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
      environment {
        CLIENT_ID = credentials('SwitchSdkClientId')
        CLIENT_SECRET = credentials('SwitchSdkClientSecret')
        HOCKEYAPP_ID = credentials('SwitchHockeyappId')
        HOCKEYAPP_API_KEY = credentials('SwitchHockeyappAPIKey')
      }
      steps {
        sh './gradlew assembleHockey -PbuildNumber=${BUILD_NUMBER} -PclientId=${CLIENT_ID} -PclientSecret=${CLIENT_SECRET} -PhockeyAppId=${HOCKEYAPP_ID}'

        step([$class: 'HockeyappRecorder', applications: [[apiToken: env.HOCKEYAPP_API_KEY, downloadAllowed: true, filePath: 'sample/build/outputs/apk/sample-hockey-debug.apk', mandatory: false, notifyTeam: false, releaseNotesMethod: [$class: 'NoReleaseNotes'], uploadMethod: [$class: 'VersionCreation', appId: env.HOCKEYAPP_ID]]], debugMode: false, failGracefully: false])
      }
    }
  }
  post {
    always {
      deleteDir()
      sh '$ANDROID_HOME/platform-tools/adb shell input keyevent KEYCODE_POWER'

    }
  }
}
