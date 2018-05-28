This is Gradle alternative of [Android multi repository GIT repo tool](https://source.android.com/setup/develop/repo).
> This alfa version uses your native `git` command. Next version will be use [JGIT](https://www.eclipse.org/jgit/)

Typicaly forkflow of project with multi sources (GIT repos)
- prepare sources
- setup / init build
- build
  - snapshot
  - release
    - absolutize manifest.xml
    - reproducible build from absolutize manifest.xml
  


