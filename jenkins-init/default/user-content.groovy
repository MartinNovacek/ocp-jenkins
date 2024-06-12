String destinationDir = '/var/lib/jenkins/userContent'

String sourceDir = System.getenv("JENKINS_HOME") + '/jenkins_config/jaac/userContent'
File file = new File(sourceDir)
if (file.exists()) {
  new AntBuilder().copy(todir: destinationDir) {
    fileset(dir: sourceDir)
  }
}
