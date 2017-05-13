# Smart CampUZ

Go to the [Wiki](https://github.com/UNIZAR-30249-2016-SmartCampUZ/smartCampUZ/wiki) in order to view our documentation.

[![Build Status](https://travis-ci.org/UNIZAR-30249-2016-SmartCampUZ/smartCampUZ.svg?branch=master)](https://travis-ci.org/UNIZAR-30249-2016-SmartCampUZ/smartCampUZ)
[![codecov](https://codecov.io/gh/UNIZAR-30249-2016-SmartCampUZ/smartCampUZ/branch/master/graph/badge.svg)](https://codecov.io/gh/UNIZAR-30249-2016-SmartCampUZ/smartCampUZ)

## Start using this App

### Prerequisites

- [JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Gradle](https://docs.gradle.org/current/userguide/installation.html)
- [PostgreSQL 9.5](https://www.postgresql.org/download/linux/ubuntu) (with[Postgis](http://postgis.net/install/))

### Steps

Fork this repo. After that, you have to clone your forked repo on your local machine on the directory you desire: `git clone https://github.com/your-github-username/smartCampUZ`.

Assuming you have already installed PostgreSQL, access to template1, wich is a default PostgreSQL database:
```
user@smachine:/smartCampuz$ sudo su postgres
postgres@smachine:/smartCampuz$ psql template1
```

Once you are loged in, you should create a user.
```
template1=# CREATE USER <myUser> WITH PASSWORD '<myPassword>';
\q
exit
```

Without logging out from `posrtgres` account, execute the resetDB.sh script. This will create a database named 
**smartcampuz** owned by a user named **smartuser**. If you want to change those values you can manually replace-them-all with
your chosen one.
```
postgres@smachine:/smartCampuz$ sh resetDB.sh
```

Now locate and set the "<myUser>" and "<myPassword>" in /GitHub/smartCampUZ/src/main/resources/application.properties (the following are the default ones in the project):

```
#Username and password
spring.datasource.username=smartuser
spring.datasource.password=smartpass
```

In order to check that everything is working correctly, you can execute the commands `gradle build` or `gradle check`.

## Build & Run

You can run the application use the following commands:

- **Deploying** Open a terminal in project source directory and type the command `gradle bootRun`. 
That deploys an Spring server in http://localhost:8080, using the files located on the project's source directory.

- **Rebuilding** Thanks to DevTools package the server will auto-reload each time a *binary* changes. 
In order to update the *binaries* from your IDE, compile/make the project. (Ctrl+F9 in IntelliJ)

- **Stopping** A Spring Server is not designed to be shut down so the unique way is by killing the procces with `Ctrl + C`.

## Tests

Gradle allows you to run the tests defined on src/test with `gradle test`.

Note that `gradle check`and `gradle build` will also execute the tests.

You can indicate gradle no to run them with the pervios mentioned commands using the `-x test` option, i.e `gralde build -x test`.

Another way to prevent the test from being executed is to add the "@Ignore" annotation before the test class declaration.

# EditorConfig
[EditorConfig](http://editorconfig.org/) helps developers maintain consistent coding styles between different editors and IDEs. It is a file format for defining coding styles and a collection of text editor plugins that enable editors to read the file format and adhere to defined styles.
You need to create a .editorconfig file in which you define the coding style rules. It is similar to the format accepted by gitignore.

## IDEs supported by EditorConfig
These editors come bundled with native support for EditorConfig. Everything should just work: [BBEdit](http://www.barebones.com/support/technotes/editorconfig.html), [Builder](https://wiki.gnome.org/Apps/Builder/Features#EditorConfig), [CLion](https://github.com/JetBrains/intellij-community/tree/master/plugins/editorconfig), [GitHub](https://github.com/RReverser/github-editorconfig#readme), [Gogs](https://gogs.io/), [IntelliJIDEA](https://github.com/JetBrains/intellij-community/tree/master/plugins/editorconfig), [RubyMine](https://github.com/JetBrains/intellij-community/tree/master/plugins/editorconfig), [SourceLair](https://www.sourcelair.com/features/editorconfig), [TortoiseGit](https://tortoisegit.org/), [WebStorm](https://github.com/JetBrains/intellij-community/tree/master/plugins/editorconfig).

## IDEs not supported by EditorConfig file

To use EditorConfig with one of these editors, you will need to install a plugin: [AppCode](https://plugins.jetbrains.com/plugin/7294), [Atom](https://github.com/sindresorhus/atom-editorconfig#readme), [Brackets](https://github.com/kidwm/brackets-editorconfig/), [Coda](https://panic.com/coda/plugins.php#Plugins), [Code::Blocks](https://github.com/editorconfig/editorconfig-codeblocks#readme), [Eclipse](https://github.com/ncjones/editorconfig-eclipse#readme), [Emacs](https://github.com/editorconfig/editorconfig-emacs#readme), [Geany](https://github.com/editorconfig/editorconfig-geany#readme), [Gedit](https://github.com/editorconfig/editorconfig-gedit#readme), [Jedit](https://github.com/editorconfig/editorconfig-jedit#readme), [Komodo](http://komodoide.com/packages/addons/editorconfig/), [NetBeans](https://github.com/welovecoding/editorconfig-netbeans#readme), [NotePadd++](https://github.com/editorconfig/editorconfig-notepad-plus-plus#readme), [PhpStorm](https://plugins.jetbrains.com/plugin/7294), [PyCharm](https://plugins.jetbrains.com/plugin/7294), [Sublime Text](https://github.com/sindresorhus/editorconfig-sublime#readme), [Textadept](https://github.com/editorconfig/editorconfig-textadept#readme), [textmate](https://github.com/Mr0grog/editorconfig-textmate#readme), [Vim](https://github.com/editorconfig/editorconfig-vim#readme), [Visual Studio](https://github.com/editorconfig/editorconfig-visualstudio#readme), [Visual Studio Code](https://marketplace.visualstudio.com/items?itemName=EditorConfig.EditorConfig), [Xcode](https://github.com/MarcoSero/EditorConfig-Xcode)
