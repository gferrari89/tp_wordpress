pipeline {

  agent any

options {
ansiColor("xterm")
}

stages {

    stage ('Clonage repo GIT') {
      steps {
      checkout([
                $class: 'GitSCM',
                branches: [[name: '*/develop']],
                doGenerateSubmoduleConfigurations: false,
                extensions: [],
                submoduleCfg: [],
                userRemoteConfigs: [[credentialsId: 'git', url: 'https://github.com/gferrari89/tp_wordpress.git']]
              ])
      }
    }

    stage ('ANSIBLE LINT') {
      steps {
          sh 'echo "test ansible-lint"'
      }
    }

    stage ('DEV : Installation des produits') {
      environment {
        ANSIBLE_FORCE_COLOR = true
      }
      steps {
        ansiblePlaybook (
          colorized: true,
          playbook: 'install_produit.yml',
          inventory: 'inventories/dev/hosts',
          extras: '${VERBOSE}'
        )
      }
    }
    stage ('DEV : Deploiement Wordpress') {
      environment {
        ANSIBLE_FORCE_COLOR = true
      }
      steps {
        ansiblePlaybook (
          colorized: true,
          playbook: 'deploy_wordpress.yml',
          inventory: 'inventories/dev/hosts',
          extras: '${VERBOSE}'
        )
      }
    }

    stage ('Test Selenium') {
      steps {
          sh 'echo "test selenium"'
      }
    }

/*
    stage ('PROD : Installation des produits') {
      environment {
        ANSIBLE_FORCE_COLOR = true
      }
      steps {
        ansiblePlaybook (
          colorized: true,
          playbook: 'install_produit.yml',
          inventory: 'inventories/prod/hosts',
          extras: '${VERBOSE}'
        )
      }
    }
    stage ('PROD : Deploiement Wordpress') {
      environment {
        ANSIBLE_FORCE_COLOR = true
      }
      steps {
        ansiblePlaybook (
          colorized: true,
          playbook: 'deploy_wordpress.yml',
          inventory: 'inventories/prod/hosts',
          extras: '${VERBOSE}'
        )
      }
    }
*/
  }
}
