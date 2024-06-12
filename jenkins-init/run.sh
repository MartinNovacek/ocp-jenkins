#!/bin/sh
export GIT_COMMITER_NAME="jenkins-ci"
export GIT_COMMITER_MAIL="jenkins-ci@ocp.cz"

rm -rf /var/lib/jenkins/jenkins_config

git clone https://github.com/MartinNovacek/ocp-jenkins.git /var/lib/jenkins/jenkins_config

cd /var/lib/jenkins/jenkins_config/jenkins-init
sh ./install.sh
