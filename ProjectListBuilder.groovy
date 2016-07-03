// work with current build
def build = Thread.currentThread()?.executable
def changes = []
def affectedFiles = []

// get ChangesSets with all changed items
def changeItems = build.changeSet.items
changes += changeItems as List
changes.each {
  affectedFiles += it.getAffectedFiles()
}


// second subset in regex is project name
def regex = /(^.*\/\/depot\/source_code_dir\/)(.*?)(\/.*)/

//create unique project list from affected file list
def Set<String> projects = []
def matcher
affectedFiles.each {
	matcher = ( it =~ regex )
	if (matcher.matches()) {
		projects.add(matcher[0][2])
	} 
}

//define project sequence in which we have to build modules
def Set<String> projectSequence = []
projectSequence.add("module1")
projectSequence.add("module2")
projectSequence.add("module3")

//rearrage unique project list according to defined sequences.
def Set<String> rearrengedProjects = []
projectSequence.each() {
     if(projects.contains(it)) {
           rearrengedProjects.add(it)
     }
}

// you can add extra logic here to check if project list contains x project then add y project to list.

// flattening project list to make it work on shell script
def projectsString = ""
rearrengedProjects.each() {
         projectsString += it + " "
}

//PROJECT_LIST environment variable will be now available through out the build.
def map = [PROJECT_LIST: projectsString]
return map 
