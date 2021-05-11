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

    stage ('DEV Invoke Ansible Playbook install_wordpress.yml') {
      environment {
        ANSIBLE_FORCE_COLOR = true
      }
      steps {
        ansiblePlaybook (
          colorized: true,
          playbook: 'install_wordpress_roles.yml',
          inventory: 'inventories/dev/hosts',
          extras: '${VERBOSE}'
        )
      }
    }
  }
}
