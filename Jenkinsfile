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
      }
      steps {
        sh './gradlew assembleHockey -PbuildNumber=${BUILD_NUMBER} -PclientId=${CLIENT_ID} -PclientSecret=${CLIENT_SECRET} -PhockeyAppId=${HOCKEYAPP_ID}'
        step([$class   : 'HockeyappRecorder', applications:
            [[filePath          : 'sample/build/outputs/apk/sample-hockey-debug.apk',
              downloadAllowed   : true,
              mandatory         : false,
              notifyTeam        : true,
              teams             : buildParams.hockeyAppTeams.join(','),
              releaseNotesMethod: [$class: 'ChangelogReleaseNotes'],
              uploadMethod      : [$class: 'VersionCreation', appId: HOCKEYAPP_ID]
             ]],
            debugMode: false, failGracefully: true
      ])
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
