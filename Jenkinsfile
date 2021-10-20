pipeline {
    agent any
    stages {
        stage('Test') {
            steps {
                dir('<your-project-folder>'){
                sh 'docker run -t --rm -v "$(pwd)":/tmp/project katalonstudio/katalon katalonc.sh -projectPath=/tmp/project -browserType="Chrome" -retry=0 -statusDelay=15 -testSuitePath="Test Suites/TS_RegressionTest" -apiKey=<your-api-key>'
                }
                }
        }
    }
    post {
        always {
            archiveArtifacts artifacts: 'report/**/*.*', fingerprint: true
            junit 'report/**/JUnit_Report.xml'
        }
    }
}
