rootProject.name = "JVM-LowLevel"
include(":accessor")
include(":accessor:jdk8")
findProject(":accessor:jdk8")?.name = "accessor-jdk8"
include(":core")
