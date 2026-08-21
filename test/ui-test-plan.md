# UI Test Plan

Use this file with the `$test-ui` skill. Each command starts a fresh instance
of the program.

## Test case: reject an empty todo description

**Aim:** Verify that an empty `todo` command displays a helpful error and the
program continues to accept the next command.

**Command:**

```text
$uiTestBuildDirectory = Join-Path $env:TEMP 'peter-ui-test'
New-Item -ItemType Directory -Force -Path $uiTestBuildDirectory | Out-Null
javac -d $uiTestBuildDirectory src\main\java\*.java
java -cp $uiTestBuildDirectory Peter
```

**Inputs:**

```text
todo
bye
```

**Expected output:**

```text
____________________________________________________________
 ____      _
|  _ \ ___| |_ ___ _ __
| |_) / _ \ __/ _ \ '__|
|  __/  __/ ||  __/ |
|_|   \___|\__\___|_|
Yo! I'm Peter.
What crazy adventures are we making today?
____________________________________________________________
____________________________________________________________
Please include a description after 'todo'.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test case: reject malformed deadline commands

**Aim:** Verify that a deadline command requires the `/by` marker, a
description, and a due date, while allowing the program to continue afterward.

**Command:**

```text
$uiTestBuildDirectory = Join-Path $env:TEMP 'peter-ui-test'
New-Item -ItemType Directory -Force -Path $uiTestBuildDirectory | Out-Null
javac -d $uiTestBuildDirectory src\main\java\*.java
java -cp $uiTestBuildDirectory Peter
```

**Inputs:**

```text
deadline task
deadline /by tomorrow
deadline task /by
bye
```

**Expected output:**

```text
____________________________________________________________
 ____      _
|  _ \ ___| |_ ___ _ __
| |_) / _ \ __/ _ \ '__|
|  __/  __/ ||  __/ |
|_|   \___|\__\___|_|
Yo! I'm Peter.
What crazy adventures are we making today?
____________________________________________________________
____________________________________________________________
Use 'deadline <description> /by <date>'.
____________________________________________________________
____________________________________________________________
Please include a description before '/by'.
____________________________________________________________
____________________________________________________________
Please include a due date after '/by'.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test case: reject an unknown command

**Aim:** Verify that an unknown command displays a helpful error and is not
added to the task list.

**Command:**

```text
$uiTestBuildDirectory = Join-Path $env:TEMP 'peter-ui-test'
New-Item -ItemType Directory -Force -Path $uiTestBuildDirectory | Out-Null
javac -d $uiTestBuildDirectory src\main\java\*.java
java -cp $uiTestBuildDirectory Peter
```

**Inputs:**

```text
blah
bye
```

**Expected output:**

```text
____________________________________________________________
 ____      _
|  _ \ ___| |_ ___ _ __
| |_) / _ \ __/ _ \ '__|
|  __/  __/ ||  __/ |
|_|   \___|\__\___|_|
Yo! I'm Peter.
What crazy adventures are we making today?
____________________________________________________________
____________________________________________________________
I'm sorry, but I don't understand that command. Please try again.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```
