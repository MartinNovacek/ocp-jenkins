import hudson.model.*
import jenkins.model.*
import java.util.ArrayList
import org.yaml.snakeyaml.Yaml
import io.jenkins.plugins.casc.SecretSourceResolver
import io.jenkins.plugins.casc.ConfiguratorRegistry
import io.jenkins.plugins.casc.ConfigurationContext
import com.cloudbees.plugins.credentials.Credentials
import com.cloudbees.plugins.credentials.CredentialsScope
import com.cloudbees.plugins.credentials.domains.Domain
import com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl
import org.jenkinsci.plugins.plaincredentials.impl.StringCredentialsImpl
import com.cloudbees.jenkins.plugins.sshcredentials.impl.BasicSSHUserPrivateKey
import com.cloudbees.jenkins.plugins.sshcredentials.impl.BasicSSHUserPrivateKey.PrivateKeySource
import com.cloudbees.jenkins.plugins.sshcredentials.impl.BasicSSHUserPrivateKey.DirectEntryPrivateKeySource
import hudson.util.Secret
import com.datapipe.jenkins.vault.credentials.common.VaultUsernamePasswordCredentialImpl
import com.datapipe.jenkins.vault.credentials.common.VaultSSHUserPrivateKeyImpl
import com.datapipe.jenkins.vault.credentials.VaultKubernetesCredential
import groovy.io.FileType

def addCredential(def store, def domain, def credentials) {

  def cred

  if (credentials['usernamePassword']) {
    def data = credentials['usernamePassword']
    cred = new UsernamePasswordCredentialsImpl(CredentialsScope.GLOBAL, data.id, data.description, data.username, data.password)
    println("Setting up credential ${data.id}")
  } else if (credentials['string']) {
    def data = credentials['string']
    Secret secret = Secret.fromString(data.secret)
    cred = new StringCredentialsImpl(CredentialsScope.GLOBAL, data.id, data.description, secret)
    println("Setting up credential ${data.id}")
  } else if (credentials['basicSSHUserPrivateKey']) {
    def data = credentials['basicSSHUserPrivateKey']
    PrivateKeySource keySource = new DirectEntryPrivateKeySource(data.privateKey)
    cred = new BasicSSHUserPrivateKey(CredentialsScope.GLOBAL, data.id, data.username, keySource, data.passphrase, data.description)
    println("Setting up credential ${data.id}")
  } else if (credentials['vaultUsernamePasswordCredentialImpl']) {
    def data = credentials['vaultUsernamePasswordCredentialImpl']
    cred = new VaultUsernamePasswordCredentialImpl(CredentialsScope.GLOBAL, data.id, data.description)
    cred.setUsernameKey(data.usernameKey)
    cred.setPasswordKey(data.passwordKey)
    cred.setEngineVersion(2)
    cred.setPath(data.path)
    println("Setting up credential ${data.id}")
  } else if (credentials['vaultKubernetesCredential']) {
    def data = credentials['vaultKubernetesCredential']
    cred = new VaultKubernetesCredential(CredentialsScope.GLOBAL, data.id, data.description, data.role)
    println("Setting up credential ${data.id}")
  } else if (credentials['vaultSSHUserPrivateKeyImpl']) {
    def data = credentials['vaultSSHUserPrivateKeyImpl']
    cred = new VaultSSHUserPrivateKeyImpl(CredentialsScope.GLOBAL, data.id, data.description)
    cred.setEngineVersion(2)
    cred.setPassphraseKey(data.passphraseKey)
    cred.setPath(data.path)
    cred.setPrivateKeyKey(data.privateKeyKey)
    cred.setUsernameKey(data.usernameKey)
    println("Setting up credential ${data.id}")
  }
  
  if (cred) {
    if (!store.addCredentials(domain, cred)) {
      store.removeCredentials(domain, cred)
      store.addCredentials(domain, cred)
    }
  }
}

def registry = Jenkins.getInstance().getExtensionList(ConfiguratorRegistry.class).get(0)
def context = new ConfigurationContext(registry)
def store = Jenkins.instance.getExtensionList('com.cloudbees.plugins.credentials.SystemCredentialsProvider')[0].getStore()
Yaml parser = new Yaml()

def configDir = new File(System.getenv('JENKINS_HOME') + '/jenkins_config/jaac')
configDir.eachFileRecurse (FileType.FILES) { file ->
  if (file.name.endsWith('.credentials')) {
    def configText = file.text
    configText = SecretSourceResolver.resolve(context, configText)

    def conf = parser.load(configText)
    conf.credentials.system.domainCredentials.each {
      def domain
      if (it.domain) {
        println("Setting up domain ${it.domain.name}")
        domain = new Domain(it.domain.name, it.domain.description, null)
        store.addDomain(domain, new ArrayList<Credentials>())
      } else
        domain = Domain.global()
      it.credentials.each { cred ->
        addCredential(store, domain, cred)
      }
    }
  }
}

println("Successfully configured credentials")
