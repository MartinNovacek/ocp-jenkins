import hudson.model.*
import jenkins.model.*

folder('ci-cd-seed-jobs') {
  displayName('CI-CD-Seed-Jobs')
  description("Jobs related to creation, update and deletetion of available environment types in OCP")
}
