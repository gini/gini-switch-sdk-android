pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        sh './gradlew switchsdk::assembleDebug'
      }
    }
    stage('Tests') {
      steps {
          parallel(
            "Unit Tests": {
              sh './gradlew switchsdk::test'
              junit '**/test-results/**/*.xml'
              publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'switchsdk/build/reports/tests/testDebugUnitTest/', reportFiles: 'index.html', reportName: 'Unit Tests Results', reportTitles: ''])
            },
            "Instrumentation Tests": {
                    sh '$ANDROID_HOME/platform-tools/adb shell input keyevent KEYCODE_POWER'
                    sh './gradlew switchsdk::connectedAndroidTest'
                    junit 'switchsdk/build/outputs/androidTest-results/**/*.xml'
                    publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'switchsdk/build/reports/androidTests/connected/', reportFiles: 'index.html', reportName: 'Instrumentation Tests Results', reportTitles: ''])
            }
          )
      }
    }
    stage('Hockeyapp Distribute') {
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
    stage('Release SDK') {
      when {
        branch 'master'
      }
      steps {
        script {
             def bintrayCredentials = input message: 'Enter your Bintray credentials', parameters: [string(defaultValue: '', description: 'Bintray username', name: 'BINTRAY_USERNAME'), password(defaultValue: '', description: 'Bintray api key', name: 'BINTRAY_KEY')]

             env.BINTRAY_USERNAME = bintrayCredentials['BINTRAY_USERNAME']
             env.BINTRAY_KEY = bintrayCredentials['BINTRAY_KEY']

          }
          sh './gradlew clean build bintrayUpload -PbintrayUser=${env.BINTRAY_USERNAME} -PbintrayKey=${env.BINTRAY_KEY} -PdryRun=false'
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
