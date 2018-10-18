import groovy.json.JsonSlurper

def nexusApi=''
def gitLaburl=''
def bundleList = ["ttg-bundles-mdo-package-my-care"]


def run(value) {
    def cmd = ['/bin/sh', '-c', value]
    cmd.execute().with {
        def output = new StringWriter()
        def error = new StringWriter()
        it.waitForProcessOutput(output, error)
        println "Error=$error"
        println "Output=$output"
    }
}

def projects=new ArrayList<>()
    def projectList="/service/rest/v1/search?repository=npm-local"
    def projectListUrl =new URL(projectList)
    def projectsPage = new JsonSlurper().parse(projectListUrl.newReader())
    projects.addAll(projectsPage)

def projectNames
projects.each{
    projectNames="${it.items.name}"
    projectNames= projectNames.tokenize(",")*.trim()
   }
projectNames=projectNames.unique()
projectNames.pop()
projectNames.removeLast()

println "${projectNames}"


def projectId
projectNames.each { name ->
    projectId = "${name}"

    def tagList = "http://${nexusApi}/service/rest/v1/search?repository=npm-local&name=${projectId}"
    def tagListUrl
    tagListUrl = new URL(tagList)
    def tags = new JsonSlurper().parse(tagListUrl.newReader())
    def tagVersions="${tags.items.version}"
    tagVersions=tagVersions.substring(1)
    tagVersions=tagVersions.substring(0, tagVersions.length() - 1)
    tagVersions=tagVersions.tokenize(",")*.trim()


    def tagArray=new ArrayList<>()
    tagVersions.each {
        def tagValue="${it}".tokenize('.').join("")
        tagArray.addAll("${tagValue}")
    }

    tagArrayint="${tagArray}".tokenize(',[]')*.toInteger()
    tagArrayint=tagArrayint.sort()


    def latestTag="0"+tagArrayint.max()
    def tagLength=latestTag.length()
    if (tagLength<3) {
        latestTag="0."+latestTag.toString().replaceAll(".(?!\$)", "\$0.")
    } else {
        latestTag="${latestTag[0]}.${latestTag[1]}.${latestTag[2..tagLength-1]}"
    }
    println "$projectId@${latestTag}"


    bundleList.each { repoName ->
        [
                "if [ -f ${repoName}/Bundlefile ]; then continue; else git clone --recursive git@${gitLaburl}:ttg-bundles/${repoName}.git; fi",
                "cd ${repoName} && git stash && make git-checkout git-pull",
                "cd ${repoName} && sed -i 's/${projectId}@.*/${projectId}@${latestTag}/g' Bundlefile",
                "cd ${repoName} && sed -i 's/${projectId}@.*/${projectId}@${latestTag}/g' Bundlefile.dev"
        ].each {
            run("${it}")
        }
    }
}
