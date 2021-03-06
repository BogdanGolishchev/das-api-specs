<!---
  © Copyright IBM Corp. 2017
  
  Licensed under the Apache License, Version 2.0 (the "License"); 
  you may not use this file except in compliance with the License. 
  You may obtain a copy of the License at:
  
  http://www.apache.org/licenses/LICENSE-2.0 
  
  Unless required by applicable law or agreed to in writing, software 
  distributed under the License is distributed on an "AS IS" BASIS, 
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
  implied. See the License for the specific language governing 
--->

# Node.js samples using the DAS Data API
Sample code showing use of Domino Access Services (DAS) with Swagger
generated client code.  You can use this code to test data API client
code generated by
[Swagger Codegen](https://github.com/swagger-api/swagger-codegen).

### Prerequisites
- A Domino server with the data API installed and enabled.
- At least one server database with the data API enabled.
  For example, you may want to use the demo database from
  the XPages Extension Library (XLibExt.nsf).  
- A Node.js runtime environment.
- A Node.js package generated from **data.yaml**.  For example,
  you can use the [online Swagger editor](http://editor2.swagger.io)
  to import **data.yaml** and then generate a "Javascript" client.

# Instructions
These instructions show how to install and run [searchtest.js](searchtest.js).
To run a different data API client sample, follow these same instructions
and substitute file names where appropriate.

First create a root folder and two sub-folders as shown below:

```
/tests
/tests/ibm_domino_data_api
/tests/searchtest
```

The root folder name is not important.  Choose a name other than
**/tests** if necessary.

### Unzip the generated client code
Next unzip the generated code to **/tests/ibm_domino_data_api**.
When you are finished, the contents of the folder should look
something like this:

```
docs
src
test
.swagger-codegen-ignore
.travis.yml
git_push.sh
mocha.opts
package.json
README.md
```

### Create your Node.js application
Now use **npm** to initialize your Node.js application. Use a Node.js 
command shell to execute the following commands:

```
cd /tests/searchtest
npm init
```

The **npm** command will prompt you for information about your
application.  Be sure to specify **searchtest.js** as the
entry point.  Otherwise, you can accept the default values
as shown below:

```
name: (searchtest)
version: (1.0.0)
description: Data API test
entry point: (index.js) searchtest.js
test command:
git repository:
keywords:
author:
license: (ISC)
```

Use **npm** to make your application dependent on **ibm_domino_data_api**
(the generated client code):

```
npm install --save ../ibm_domino_data_api
```

Download [searchtest.js](searchtest.js) to the **/tests/searchtest** folder.
Then open your local copy of **searchtest.js** in your favorite text editor
and customize the following values for your environment:

```javascript
// IMPORTANT: Change these values to match your test environment:
//
// - basePath is the base URL for your Domino server.
//   The data service MUST be installed and enabled
//   on the server.  Be sure to change the protocol to
//   http if your server doesn't support https.
//
// - username is the name of a user who has access to your
//   Domino server
//
// - password is the user's password
//
// - folder is the database folder name relative to the Domino
//   data directory.  Use '.' if the database is in the data
//   directory itself.
//
// - database is the database file name.  Use 'XPagesExt.nsf'
//   if your server has a copy of the XPages Extension Library
//   demo database.
//

var basePath = 'https://yourserver.yourorg.com';
var username = 'First Last';
var password = 'password';
var folder = '.';
var database = 'XPagesExt.nsf';
```

### Run your Node.js application
After saving your local changes, return to the Node.js command shell to run 
your application:

```
node searchtest
```

The following is some sample output from a successful run:

```
/tests/searchtest> node searchtest
Searching documents in database XPagesExt.nsf matching Lorem ...
Document search API called successfully. Response follows ...

Document 0
   unid: 91AD4A64E882BCF085257AB5005F3708
   modified: 2013-07-16T21:44:22Z
   href: /XPagesExt.nsf/api/data/documents/unid/91AD4A64E882BCF085257AB5005F3708

Document 1
   unid: B653AD5CC9C9667885257AB5005F10B6
   modified: 2013-07-16T21:44:03Z
   href: /XPagesExt.nsf/api/data/documents/unid/B653AD5CC9C9667885257AB5005F10B6

Document 2
   unid: E9B5987BA06C319585257AB5005EE592
   modified: 2013-07-16T21:43:40Z
   href: /XPagesExt.nsf/api/data/documents/unid/E9B5987BA06C319585257AB5005EE592
```

In other words, the **searchtest.js** application searches XPagesExt.nsf
for documents containing "Lorem".  You can specify a different search
string on the command line.  The following command searches for documents
containing "Ipsum":

```
node searchtest Ipsum
```
