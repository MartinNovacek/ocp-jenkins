// Approve all unowned scripts as these are scripts generated during startup by seed jobs
import jenkins.model.Jenkins

def scriptApproval = Jenkins.instance.getExtensionList('org.jenkinsci.plugins.scriptsecurity.scripts.ScriptApproval')[0]

println('Approving automatically generated scripts')

def hashesToDisplay = scriptApproval.pendingScripts.findAll{ true }.collect{ it }
hashesToDisplay.each { 
  println it.getProperties()
  println it.script
  println it.context.getProperties()
}

def hashesToApprove = scriptApproval.pendingScripts.findAll{ it.context.user == null }.collect{ it.getHash() }
hashesToApprove.each {
  println("Approving hash ${it}")
  scriptApproval.approveScript(it)
}
