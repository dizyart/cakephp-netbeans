# About

This plugin provides support for CakePHP.

*** KEEP THIS FILE UPDATED OR REMOVE IT COMPLETELY ***

- 2010/07/16
- 2013/01/01 last update

## ENVIRONMENT

- [NetBeans IDE 7.2](http://netbeans.org/downloads/index.html)
- CakePHP 1.3.x
- CakePHP 2.x

## WHAT WORKS:

- badge icon ![badge icon](https://raw.github.com/junichi11/cakephp-netbeans/cakephp2/src/org/cakephp/netbeans/ui/resources/cakephp_badge_8.png) (displayed on project folder)
- configuration files (add app/config files to important files)
- display file path in Toolbar
(If you don't want to display, uncheck Display > Toolbar > View.)
- ignored files (hide app/tmp directory)
- cake commands support [*1]
- go to view, go to action
- clear cache action [*1]
- install plugins action (from zip file URL)[*1]
- template files (ctp, helper, component, behavior, shell, task)
- create new CakePHP projects from new project option
- code completion support [v0.6]
- format for CakePHP action [v0.6.8]
- go to element file from view file [v0.6.9]
- display and change debug level [v0.6.10]
- multiple app directories support [v0.6.14]
- check default action [v0.6.15]
- image and element code completion [v0.6.16]
- support for PHPUnit settings and create test case [v0.6.17]

[*1] right-click in project node > CakePHP > (Run Command | Clear Cache | Install Plugins)

### Existing Source

Your NetBeans Project needs to have the following trees.

CakePHP 1.3.x

    myproject
    ├─nbproject
    ├─app
    ├─cake
    ├─plugins
    ├─vendors
    ├─...

CakePHP 2.x

    myproject
    ├─nbproject
    ├─app
    ├─lib
    │  └─Cake
    ├─plugins
    ├─vendors
    ├─...

NewProject (Ctrl + Shift + N) > PHP > PHP Application with Existing Source
Please select your cakephp dir(e.g. /home/NetBeansProjects/myproject)

### App Directory Name

Multiple app directory names support.

#### Change app directory name

Please set the following if you would like to different app directory name.

1. Project properties > Framework > CakePHP
2. `app Folder name` : please set your new app folder name

#### Use multiple app directories

If you use multiple app directories:

```
// CakePHP 1
cakephp1.3
├─app
├─app2
├─app3
├─myapp
├─...
├─cake
├─plugins
├─vendors
├─...

// CakePHP 2
cakephp2.x
├─app
├─app2
├─app3
├─mycustom
├─...
├─lib
│  └─Cake
├─plugins
├─vendors
├─...

```

Use **app** directory as NetBeans project e.g. app1, app2, myapp, e.t.c.:

```
myproject(e.g. myapp)
├── nbproject
├── Config
├── Console
├── Controller
├── Lib
├── Locale
├── Model
├── Plugin
├── Test
├── Vendor
├── View
├── index.php
├── tmp
└── webroot
```

1. Project properties > Framework > CakePHP
2. Check `Use the relative path to the CakePHP directory from the project directory.`
3. `CakePHP Directory` : "../"

Please notice that Code Completion is not available. You have to add the cakephp core path to include path.

After settings, please close your project and reopen it.

### Clear Cache Action

Delete each files of app/tmp/cache/* directorys.

### Install Plugins Action

Settings: `Tool > Option > PHP > CakePHP`

- Name : Set Plugin name. This name is Folder name.
- Url : Set github zipball url

**Url is only zipball url.**

Run Action: see [*1]

### Create New CakePHP Project

**You need to connect the network.**

1. File > New Project (`Ctrl + Shift + N`)
2. PHP > PHP Application
3. Set the project name
4. Run Configuration
5. PHP Framework > check CakePHP PHP Web Framework

Select `Unzip` or `git command`.
If you select `Unzip`, also select CakePHP version.
If you want to create a database.php file, please, check the Create database.php.

Also set the following automatically.

- change permission of app/tmp directory (777)
- change Security.salt and Security.cipherSeed values

create a database, you can immediately start development in a local environment.

### Code Completion

Support for core components and helpers (default).
Also support for classes in $uses, $components, $helpers.(also contain the alias)

```php
// e.g.
public $uses('Comment', 'Member', 'User');
// $this->Comm [Ctrl + Space] => $this->Comment [Ctrl + Space]

public $components('Search.Prg', 'Foo');
// $this->P [Ctrl + Space] => $this->Prg-> [Ctrl + Space] => display methods and fields

public $helpers('Session', 'Html' => array('className' => 'MyHtml'));
// $this->Html-> [Ctrl + Space] => display MyHtmlHelper class methods and fields
```

image file completion

```php
$this->Html->image('[Ctrl + Space]');
// popup file and directory names in the webroot/img
$this->Html->image('subdir/[Ctrl + Space]');
// popup file and directory names in the webroot/img/subdir
// ...
$this->Html->image('/mydir/[Ctrl + Space]');
// if you want to use files or directories in the webroot directory,
// please, start with "/"
```

element file completion

```php
$this->element('[Ctrl + Space]');
// popup file names in the appdir/View/Elements/
```

### Go To View Action
You can move from controller action to view file.

1. Right-click at the controller action
2. Navigate > Go to view

e.g.

```php
class MainController extends AppController{
	// ... something
	public function index() {
		// Right-click or shortcut here
	}
}
```

If you use shortcut, register with Keymap.

When the view file doesn't exist, create the empty view file automatically if set as follows.

Check `Auto create a view file when go to view action is run`
at Right-click on project node > property > Framework > CakePHP

If you use the theme, set $theme to controller field.

### Go To Action Action

Similar to Go to view action.

1. Right-click on the view file (in editor)
2. Navigate > Go to action

### Format+ Action (Format for CakePHP)

This action run the following.

1. Reformat (format settings : Tool > Option > Editor > Format)
2. Remove indents of Document Block
3. Change line separator to LF(Unix)

Q. Why do you add this action?

A. I add in order to follow the [CakePHP Coding Standars](http://book.cakephp.org/2.0/en/contributing/cakephp-coding-conventions.html)

### Hyperlink for view element files (v0.6.9)

You can go to the element file from view file.

Search the following element file directories:

- app(app/View/Elements, app/views/elememts)
- core(lib/cake/View/Elements, cake/views/elements)

e.g.

```php
$this->element('sample');
```
Hold down Ctrl key and click on 'sample'.

If there is sample.ctp in the above directories, open the sample.ctp.
Otherwise do nothing.


### Display and Change debug level (v0.6.10)

You can change debug level on popup list.

When you choose the CakePHP file node, you would find the cake icon and debug level number at the lower right of the window.

If you change debug level, click the icon. Then the popup is displayed. Please, select the debug level number.

### Check Default Action (v0.6.15)

Check default asset names. (e.g. css/cake.generic.css, img/cake.icon.png, ...)

Check whether favicon.ico is changed.

### PHPUnit Test settings (PHPUnit Test Init Action)
**Support for only CakePHP2.x**

Project-right-click > CakePHP > PHPUnit Test Init

Do settings for doing PHPUnit Test with NetBeans.

Create following files:

- nbproject/NetBeansSuite.php
- nbproject/phpunit.bat or phpunit.sh
- app/webroot/bootstrap_phpunit.php

And set PHPUnit settings of Project properties:

- bootstrap
- custome script

If you run this action, you can test using fixture with NetBeans.

#### bootstrap and NetBeansSuite Sources

https://gist.github.com/2055307 ([nojimage](https://github.com/nojimage))

## HOW TO RUN:

- download NBM file or manually create NBM file
- manually install NBM file

## HOW TO DEVELOP:

- read this page [1] for the general overview but notice that current sources need to be used, then
- checkout NB sources via Mercurial (preferred, easy updates)[2] or download archive with latest NB sources from NB download site [3]
- Tools > NetBeans Platforms - add new platform "NetBeans IDE Dev" that points to the cloned/downloaded sources
- if not done yet, open this module in NetBeans
- Run

### References

- [1] [Php Framework Development](http://wiki.netbeans.org/PhpFrameworkDevelopment)
- [2] [Working With NetBeans Sources](http://wiki.netbeans.org/WorkingWithNetBeansSources)
- [3] [Nightly latest zip](http://bits.netbeans.org/download/trunk/nightly/latest/zip/)

## License
[Common Development and Distribution License (CDDL) v1.0 and GNU General Public License (GPL) v2](http://netbeans.org/cddl-gplv2.html)
