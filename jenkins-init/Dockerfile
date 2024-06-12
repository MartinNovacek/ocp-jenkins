FROM image-registry.openshift-image-registry.svc:5000/openshift/ubi8-openjdk-11-runtime:1.9

USER 0
RUN yum -y update && yum -y install git && yum clean all

COPY jenkins-init/run.sh /tmp/run.sh
RUN chmod 777 /tmp/run.sh

USER 1001
ENTRYPOINT ['/tmp/run.sh']