#!/bin/bash
export GIT_COMMITER_NAME="jenkins-ci"
export GIT_COMMITER_MAIL="jenkins-ci@ocp.cz"

rm -rf /var/lib/jenkins/jenkins_config

git clone https://github.com/MartinNovacek/ocp-jenkins.git#master /var/lib/jenkins/jenkins_config

cd /var/lib/jenkins/jenkins_config/casc
sh ./install.sh