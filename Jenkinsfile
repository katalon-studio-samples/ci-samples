pipeline {
    environment {
        KATALON_OPTS='-browserType="Chrome" -retry=0 -statusDelay=15 -testSuitePath="Test Suites/TS_RegressionTest"'
    }
    agent {
        docker {
            image 'katalonstudio/katalon:test'
            args "-e KATALON_OPTS=\"${env.KATALON_OPTS}\""
        }
    }
    stages {
        stage('Test') {
            steps {
                sh 'katalon-execute.sh'
            }
        }
    }
}