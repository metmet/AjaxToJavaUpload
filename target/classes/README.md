AjaxToJavaUpload
========
Ajax upload file to java in Oracle DB and file system

Facts
-----
- version: 0.0.0

Requirements
------------
- Maven :
			download jar ojdbc14_g.jar
 			http://www.oracle.com/technetwork/database/enterprise-edition/jdbc-10201-088211.html
 			copy jar in C:\path\lib
 			cd 'C:\path\lib'
			mvn install:install-file -DgroupId=com.oracle -DartifactId=ojdbc14 -Dversion=10.2.0.1.0 -Dpackaging=jar -Dfile=ojdbc14_g.jar
			
			mvn eclipse:clean
			
			mvn -Dwtpversion=2.0 compile eclipse:eclipse

- jquery-1.8.3.js
- jquery-ui-1.9.2.custom.js


Compatibility
-------------
- IE7+ , Chrome ...


Installation Instructions
-------------------------
1. Use Maven

Contribution
------------
Any contribution is highly appreciated. The best way to contribute code is to open a [pull request on GitHub](https://help.github.com/articles/using-pull-requests).

Developers
----------
Matteo Borrelli
https://github.com/metmet

License
-------
[The MIT License](http://opensource.org/licenses/MIT)

Copyright
---------
Copyright (c) 2013 MB
