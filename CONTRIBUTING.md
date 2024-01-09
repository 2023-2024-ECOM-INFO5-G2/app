# Welcome to the contributing guide of the project

Thank you for your interest in the project and for your desire to contribute to it.

Read our [Code of Conduct](CODE_OF_CONDUCT.md) to keep our community friendly and respectful.

In this guide you will get an overview of the contribution workflow from opening an issue, to creating a merge request.

Use the table of contents to navigate through the guide.

- [Welcome to the contributing guide of the project](#welcome-to-the-contributing-guide-of-the-project)
  - [Pre-requisites](#pre-requisites)
  - [Issues](#issues)
    - [Create a new issue](#create-a-new-issue)
    - [Solve an issue](#solve-an-issue)
  - [Make changes](#make-changes)
    - [Make changes locally](#make-changes-locally)
      - [Create a branch for the new feature](#create-a-branch-for-the-new-feature)
      - [Create the new data](#create-the-new-data)
      - [Create the new routes and controllers](#create-the-new-routes-and-controllers)
      - [Create the new tests](#create-the-new-tests)
      - [Create the new documentation](#create-the-new-documentation)
      - [Create a component](#create-a-component)
      - [Create composable and store (if needed)](#create-composable-and-store-if-needed)
      - [Create a view](#create-a-view)
      - [Add a new route](#add-a-new-route)
      - [Add new translations](#add-new-translations)
      - [Add new front-end tests](#add-new-front-end-tests)
      - [Commit your changes](#commit-your-changes)
      - [Pull Request](#pull-request)
      - [Your PR is merged!](#your-pr-is-merged)

## Pre-requisites

On your local machine :

- Ensure git is installed (From the command line, type `git --version`. If you get a result, you have Git installed)
- Install a source code editor, or decide which tool you’re going to use to edit files.
- Install git flow :
  - You can find the installation instructions for git flow on the web
  - This tool is used to manage the branches and the releases of the project
  - You can find more information about git flow
    here: [git flow](https://www.atlassian.com/fr/git/tutorials/comparing-workflows/gitflow-workflow#:~:text=Qu'est%2Dce%20que%20Gitflow,Vincent%20Driessen%20de%20chez%20nvie.)
  - The default configuration is used
  - The default branch is `develop`
  - The default prefix for the branches is `feature/`, `bugfix/`, `release/` and `hotfix/`
  - The default prefix for the tags is `v`
  - The first time you use git flow, you need to initialize it with the command `git flow init`

## Issues

### Create a new issue

If you spot a problem with the project, search if an issue already
exists [here](https://gitlab.ig.umons.ac.be/stage4a/umons-sensor-backend/-/issues). If it does, add a comment to the
existing. If it does not, create a new issue.

### Solve an issue

Scan through our existing issues to find one that interests you. You can narrow down the search using labels as filters.
See Labels for more information. As a general rule, we don’t assign issues to anyone. If you find an issue to work on,
you are welcome to open a MR with a fix.

## Make changes

### Make changes locally

#### Create a branch for the new feature

- You need to create a new branch for the new feature you want to add to the project
- You can use the command `git flow feature start <feature_name>`

#### Create the new data

TODO : Add the instructions to create the new data

#### Create the new routes and controllers

TODO : Add the instructions to create the new routes and controllers

#### Create the new tests

TODO : Add the instructions to create the new tests

#### Create the new documentation

TODO : Add the instructions to create the new documentation

## Create a component

TODO : Add the instructions to create the new component

## Create composable and store (if needed)

TODO : Add the instructions to create the new composable and store

- Store are used to store the data of the application
- Composables are used to store the logic of the application
- You can find more information about the difference between store and composables
  here: [Store vs Composables](https://v3.vuejs.org/guide/composition-api-introduction.html#why-composition-api)

## Create a view

TODO : Add the instructions to create the new view

## Add a new route

TODO : Add the instructions to add a new route

- You can find more information about the router
  here: [Vue Router](https://router.vuejs.org/guide/#html)

## Add new translations

TODO : Add the instructions to add new translations

- You can find more information about the i18n
  here: [Vue I18n](https://kazupon.github.io/vue-i18n/)

## Add new front-end tests

TODO : Add the instructions to add new front-end tests

#### Commit your changes

Commit the changes once you are happy with them. Don't forget to self-review to speed up the review process.

#### Pull Request

- When you're finished with the changes, create a pull request, also known as a PR.
- You need to create a pull request to merge your branch into the branch `develop`
- A pull request is created on the web interface of github
- A reviewer will be assigned to your pull request and will review your code
- You will be notified when the reviewer has finished the review
- You will correct the issues raised by the reviewer and push the changes to your branch
- The reviewer will be notified of the changes and will review them again
- This process will continue until the reviewer approves your pull request
- You can find more information about pull request here:
  - [Github](https://docs.github.com/en/github/collaborating-with-issues-and-pull-requests/about-pull-requests)

#### Your PR is merged!

Congratulations! You are now a contributor to the project. Thank you for your contribution!
