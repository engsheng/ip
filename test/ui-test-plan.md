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

## Test case: reject invalid mark and unmark task numbers

**Aim:** Verify that `mark` and `unmark` reject missing, non-numeric, and
out-of-range task numbers without ending the program.

**Command:**

```text
$uiTestBuildDirectory = Join-Path $env:TEMP 'peter-ui-test'
New-Item -ItemType Directory -Force -Path $uiTestBuildDirectory | Out-Null
javac -d $uiTestBuildDirectory src\main\java\*.java
java -cp $uiTestBuildDirectory Peter
```

**Inputs:**

```text
mark
unmark 1
todo read
mark first
unmark nope
mark 2
unmark 0
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
Please provide a task number to mark.
____________________________________________________________
____________________________________________________________
There are no tasks to unmark.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] read
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Please enter an integer task number to mark.
____________________________________________________________
____________________________________________________________
Please enter an integer task number to unmark.
____________________________________________________________
____________________________________________________________
Task number must be between 1 and 1.
____________________________________________________________
____________________________________________________________
Task number must be between 1 and 1.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test case: reject malformed event commands

**Aim:** Verify that an event command requires `/from` and `/to` in order,
along with a description, start time, and end time.

**Command:**

```text
$uiTestBuildDirectory = Join-Path $env:TEMP 'peter-ui-test'
New-Item -ItemType Directory -Force -Path $uiTestBuildDirectory | Out-Null
javac -d $uiTestBuildDirectory src\main\java\*.java
java -cp $uiTestBuildDirectory Peter
```

**Inputs:**

```text
event meeting
event /from noon /to 1pm
event meeting /from /to 1pm
event meeting /from noon /to
event meeting /to 1pm /from noon
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
Use 'event <description> /from <start> /to <end>'.
____________________________________________________________
____________________________________________________________
Please include a description before '/from'.
____________________________________________________________
____________________________________________________________
Please include a start time after '/from'.
____________________________________________________________
____________________________________________________________
Please include an end time after '/to'.
____________________________________________________________
____________________________________________________________
Use 'event <description> /from <start> /to <end>'.
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

## Test case: delete a task from the list

**Aim:** Verify that `delete` removes the selected task, displays its full
details, updates the task count, and renumbers the remaining tasks.

**Command:**

```text
$uiTestBuildDirectory = Join-Path $env:TEMP 'peter-ui-test'
New-Item -ItemType Directory -Force -Path $uiTestBuildDirectory | Out-Null
javac -d $uiTestBuildDirectory src\main\java\*.java
java -cp $uiTestBuildDirectory Peter
```

**Inputs:**

```text
todo read book
event project meeting /from Aug 6th 2pm /to 4pm
deadline return book /by June 6th
delete 2
list
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
Got it. I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [D][ ] return book (by: June 6th)
Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
Noted. I've removed this task:
  [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
2.[D][ ] return book (by: June 6th)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test case: reject invalid delete task numbers

**Aim:** Verify that `delete` rejects missing, non-integer, and out-of-range
task numbers, handles an empty list, and continues accepting commands.

**Command:**

```text
$uiTestBuildDirectory = Join-Path $env:TEMP 'peter-ui-test'
New-Item -ItemType Directory -Force -Path $uiTestBuildDirectory | Out-Null
javac -d $uiTestBuildDirectory src\main\java\*.java
java -cp $uiTestBuildDirectory Peter
```

**Inputs:**

```text
delete
delete 1
todo read book
delete first
delete 0
delete 2
delete 1
list
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
Please provide a task number to delete.
____________________________________________________________
____________________________________________________________
There are no tasks to delete.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Please enter an integer task number to delete.
____________________________________________________________
____________________________________________________________
Task number must be between 1 and 1.
____________________________________________________________
____________________________________________________________
Task number must be between 1 and 1.
____________________________________________________________
____________________________________________________________
Noted. I've removed this task:
  [T][ ] read book
Now you have 0 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```
