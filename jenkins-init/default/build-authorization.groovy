import jenkins.*
import jenkins.model.*
import hudson.model.*
import jenkins.model.Jenkins
import org.jenkinsci.plugins.authorizeproject.*
import org.jenkinsci.plugins.authorizeproject.strategy.*
import jenkins.security.QueueItemAuthenticatorConfiguration

def instance = Jenkins.getInstance()

println("Configuring build authorization strategies")

// Define which strategies you want to allow to be set per project
def strategyMap = [
  (instance.getDescriptor(AnonymousAuthorizationStrategy.class).getId()): true, 
  (instance.getDescriptor(TriggeringUsersAuthorizationStrategy.class).getId()): true,
  (instance.getDescriptor(SpecificUsersAuthorizationStrategy.class).getId()): true,
  (instance.getDescriptor(SystemAuthorizationStrategy.class).getId()): true
]

def authenticators = QueueItemAuthenticatorConfiguration.get().getAuthenticators()

authenticators.remove(GlobalQueueItemAuthenticator.class)
authenticators.remove(ProjectQueueItemAuthenticator.class)

authenticators.add(new ProjectQueueItemAuthenticator(strategyMap))
authenticators.add(new GlobalQueueItemAuthenticator(new TriggeringUsersAuthorizationStrategy()))

instance.save()
