# m1ke

m1ke - a primitive version control system git analogue which is able to work only locally

m1le can be run only from command line.

In order to initialize m1ke to work in particulary filder you need to run the command:

`m1ke init`

In order to scan a folder for the files and the made changes you need to perform:

`m1ke integrate`

After this command m1ke opens a specific user branch which is preserved there (if there is several branches, it opens that branch, which took place last change). If m1ke has never started on this folder then created primary branch `master`. The user should be warned about this by message.

If We want to commit changes - use a special command

```
...

> C: \ project m1ke save -m "Saving hello message"
```

When we commit, we remain in a special hidden folder change itself.

Now imagine that in this location apart your branch has the same file but with other changes.

Create a branch (that was not a master for example):

`m1ke create-branch someBranchName`

Choose branch:

`m1ke get-branch someBranchName`

When you select a branch - you drag the changes that you are there and have kept using m1ke save


If you move to another branch, but you've already made some changes, you will lose those changes.

Delete thread:

`m1ke remove-branch someBranchName`



