pipeline {
    agent {
        docker {
            image 'katalonstudio/katalon'
            args "-u root"
        }
    }
    stages {
        stage('Test') {
            steps {
                sh 'katalonc.sh -browserType="Chrome" -apiKey="122eabee-fce8-4f50-a4df-1773ddb37722" -retry=0 -statusDelay=15 -testSuitePath="Test Suites/TS_RegressionTest"'
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
