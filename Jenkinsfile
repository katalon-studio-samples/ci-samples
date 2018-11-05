pipeline {
    agent {
        docker {
            image 'katalonstudio/katalon:test'
            args "-u root"
        }
    }
    stages {
        stage('Test') {
            steps {
                sh 'katalon-execute.sh -browserType="Chrome" -retry=0 -statusDelay=15 -testSuitePath="Test Suites/TS_RegressionTest"'
            }
        }
    }
}