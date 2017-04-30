## Teresa Jenkins plugin

The main objective of this project is to turn teresa-cli available to Jenkins.

### 1 - Installation

* Install the teresa-cli on Jenkins server https://github.com/luizalabs/teresa-cli

### 2 - Clone repository

Clone the repository:

```bash
git clone https://github.com/eHattori/teresa-build-plugin

```
### 3 - Running 

the plugin can to be executed in a dev environment with : 
 
```bash
mvn hdi:run
```
And Generate package with
 
```bash
mvn hdi:hdi
```
### 4 - Install

For install the plugin follow this steps

1 - In the Jenkins interface, navigate to Manage Jenkins → Manage Plugins.
2 - Click the Advanced tab.
3 - In the Upload Plugin section, click Choose File, and select teresa-build.hpi.
4 - Click Upload. Jenkins displays a status screen that shows the plugin installing.