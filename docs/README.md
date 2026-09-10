# Duke User Guide

// Update the title above to match the actual product name

// Product screenshot goes here

// Product intro goes here

## Adding deadlines

// Describe the action and its outcome.

// Give examples of usage

Example: `keyword (optional arguments)`

// A description of the expected outcome goes here

```
expected output
```

## Finding tasks

Searches your task list for tasks whose descriptions match every keyword you
give, and lists them with their original task numbers so you can `mark`,
`unmark` or `delete` them straight from the results.

Format: `find <keywords>`

* Keywords are separated by spaces, and **all** of them must match, so adding
  a keyword narrows the results.
* Keyword **order does not matter**, and the keywords need not be next to each
  other in the description, so `find read book` finds `read a book`.
* Matching **ignores case** and matches **partial words**, so `find book`
  finds `bookshop`.
* Only the description is searched. Dates and task types are not.

Example: `find read book`

Given a list containing `read book`, `return book`, `buy milk` and
`read a book`, only the two tasks containing both `read` and `book` are shown:

```
Here are the matching tasks in your list:
1.[T][X] read book
5.[T][ ] read a book
```

If no task matches every keyword, Peter says so instead:

```
There are no matching tasks in your list.
```

## Feature XYZ

// Feature details