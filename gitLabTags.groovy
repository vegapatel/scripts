import groovy.json.JsonSlurper

def gitlabApihost='' //need to be updated 
def gitlabApitoken='' //need to be updated
def pageNumber='1'

def customModules = [] as String[]
def bundleList = []

def projects=new ArrayList<>()
while (true) {
    def projectList="http://${gitlabApihost}/api/v4/projects?page=${pageNumber}&per_page=100&private_token=${gitlabApitoken}"
    def projectListUrl =new URL(projectList)
    def projectsPage = new JsonSlurper().parse(projectListUrl.newReader())

    if (projectsPage.isEmpty()){
        break
    }
    pageNumber++
    projects.addAll(projectsPage)
}

def projectNames=[:]
projects.each {
    def projectName = ["${it.name}": "${it.id}"]
    projectNames.putAll(projectName)
}

def run(value) {
    def cmd = ['/bin/sh', '-c', value]
    cmd.execute().with {
        def output = new StringWriter()
        def error = new StringWriter()
        it.waitForProcessOutput(output, error)
        println "error=$error"
        println "ouput=$output"
    }
}

def projectId
def file=new File('tags.sh')
file.text=''
customModules.each { module ->
    if (projectNames.containsKey("${module}")) {
        projectId = projectNames.get("${module}")
    }

    def tagList = "http://${gitlabApihost}/api/v4/projects/${projectId}/repository/tags?&private_token=${gitlabApitoken}"
    def tagListUrl
    tagListUrl = new URL(tagList)
    def tags = new JsonSlurper().parse(tagListUrl.newReader())
    tags.sort { it.commit.created_at }
    //def moduleName="${module}".replaceAll('-','_')
    def latestTag = tags.toArray().getAt(-1).name

    def exportVariable = "${module}=${latestTag}"
    println "${exportVariable}"

    file.withWriterAppend { out ->
        out.println "export ${exportVariable}"
    }


    bundleList.each { repoName ->
        [
                "if [ -f ${repoName}/Bundlefile ]; then continue; else git clone --recursive git@<gitlab>:ttg-bundles/${repoName}.git; fi",
                "cd ${repoName} && make git-checkout git-pull",
                "cd ${repoName} && sed -i 's/${module}@.*/${module}@${latestTag}/g' Bundlefile",
                "cd ${repoName} && sed -i 's/${module}@.*/${module}@${latestTag}/g' Bundlefile.dev",
                "echo SUCCESSFULLY UPDATED TAGS"
        ].each {
            run("${it}")
        }
    }

}
