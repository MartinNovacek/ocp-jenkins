import jenkins.*
import jenkins.model.*
import hudson.*
import hudson.model.*
import jenkins.model.Jenkins
import org.jenkinsci.plugins.authorizeproject.*
import org.jenkinsci.plugins.authorizeproject.strategy.*
import jenkins.security.QueueItemAuthenticatorConfiguration

System.setProperty('org.apache.commons.jelly.tags.fmt.timeZone', 'CET')
System.setProperty('user.timezone', 'CET')