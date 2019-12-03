[![Build status](https://build.appcenter.ms/v0.1/apps/4754bc99-c7e9-423f-b8cf-5386c52a5aac/branches/master/badge)](https://appcenter.ms)
![INSTRUCTION](https://img.shields.io/badge/instruction--coverage-82%25-brightgreen.svg)

# Introduction
Welcome to Rates! Drew Dennis's rise to the Revolut interview challenge to create a real-time,
production-ready currency converter.

# Setup overview
## Building and running
Pretty standard--clone and use Gradle or Android Studio. Shouldn't be anything fancy to know.

## Testing
Also standard--just run `testDebugUnitTest`

## CI
This app is integrated with Microsoft AppCenter for CI. On every push to master,
a new build is created. A build badge is also used here in the repo, which MS provides.

On every build, all unit tests are run prior to passing the build.

# Architecture & Design overview
This app uses a clean architecture style for business logic:

## Overall architecture
- `core` package for core business logic that is non-Android specific ("pure Kotlin"). You will find
business entities (e.g. data classes), and interactors (use cases for interacting w/ entities), and
repository interface definitions (basic definition of the data source a use case must rely upon).

- `data` package for implementation of business logic at a data level (e.g. network calls,
persistence, etc.). The primary architectural pattern here is the **repository** pattern.
Repositories are typically backed by one or many abstract data sources (e.g. API, local storage,
memory). They provide a concrete implementation to the `interactors` so that a data source is
available for the use cases.

- `ui` package for presentation of the core/data dynamic

## Software design decisions
We choose to not propagate any caught errors, network issues, or any issues fetching data otherwise
to the user. We always rely on usable local defaults so that the user may always use the application.
This is the most usable design, as the user may always do currency conversions even without internet,
and even if it is the very first use of the application and no internet is available.

The downside is that we may not always have the most current rates. In the future, we could add
a timestamp to the UI, or some message otherwise indicating that the rates are not the most recent.

To abide by this decision, we exploit the repository pattern to its full potential, by always
attempting to provide the most immediate access to data (e.g. keep in-memory references).

We also use this pattern to have usable defaults that are bundled into the app's assets. For example,
though it won't be the most current, we bundle in a default API response for the rates.

The beauty of this approach is that the user simply may always use the application (except in some
extreme condition where assets are failing to load).

Moreover, the "every second" streaming of currency rates also works tremendously in our favor,
as we have no need to worry about any retry policy with the network call. We are simply always
"retrying" becuse we are never in an unrecoverable error state as it pertains to fetching rates.

## UI architecture
Within the `ui` package, we are using an MVVM pattern. This has been chosen because it is the most
suitable for the real-time nature of the currency updates, particularly with rates updating every
second and that affecting an entire list of conversions.

There is one main ViewModel, `MainVM`, and a corresponding view, `MainActivity`.

Only the ViewModel is unit tested here. There is room for more tests via Espresso within the view
boundary of the MVVM construct.

### LiveData
As part of the MVVM construct, we use the `LiveData` component. This facilitates tracking the
view's lifecycle with subscribing/observing to costly data sources. Therefore, we do not need to
keep track of Rx subscriptions within the view but rather just let `LiveData` manage.

Since the back-end uses Rx, we use the `LiveDataReactiveStreams` construct to map from `Observable`
to `LiveData`.

### RecyclerView notes
As part of the UI code for accomplishing the requirement for a list row, when clicked, to move
to the top of the list, we are primarily using RecyclerView and its `notifyItemMoved` for
a large savings in UI code implementation, because RecyclerView takes care of animating the
transition to the top.

Also, RecyclerView is the most efficient way to display a list, especially in intensive situations
where we have all rows updating every single second.

We may also note that we prefer using `notifyItemChanged` over `notifyDataSetChanged`, as the former
does not re-render the entire view but just each individual viewholder is re-bound. This results
in a much more stable experience.

Unfortunately, we are currently only using `notifyItemChanged` in a global sense: just iterating
over each list item and notifying the item has changed. So, it is still as efficient as it could be,
particularly when it comes to scrolling performance.

In the future, this could be improved possibly by using RecyclerView's `DiffUtil` to do more
incremental / partial viewholder bindings.

## Testing architecture
Not much to explain, other than you will find preference for concrete implementations over
mocks. We use `TestScheduler` where needed to control the timing of the Rx streams.

We do use Robolectric on occasion, but do not use it as a first resort since it takes longer to run

Therefore, for timeliness, for testability, for code cleanliness, and everything else,
you should expect a general rule of, ABSTRACT THE ANDROID SDK AS FAR AWAY AS POSSIBLE).

### Jacoco
We are using the Android Jacoco Gradle Plugin to generate coverage reports.
We are using the Jacoco Badge Plugin to generate a README badge from the Jacoco reports.

Currently this is not part of the CI pipeline, as it affects a change in the README that must be
committed (so CI builds would be resulting in more CI builds by committing/pushing the README change).

This could be accounted for in the future. Currently the Jacoco report and subsequent badge must
be generated locally and then pushed to `master`.

## Build arch/design
Some small yet important design decisions are made in the organization of build logic:

- Abstract custom build constants (e.g. dependency versions) into `buildSrc`. This allows for
much better consistency within builds, expandable build arch for other library or app modules using
the same dependencies, and also increased dev productivity by the tight integration between the IDE
and Kotlin (we can autocomplete / treat the source in `buildSrc` as part of our build code, and as
an 'integrated' way of designing and coding our build scripts)

    - This includes dependency coordinates and dependency versions, but this also includes what we
    call "project constants" (`buildSrc/main/java/ProjectConstants.kt`), which would be typical
    Android-oriented versions like `compileSdkVersion`, and also the version code and name.

Signing is currently the typical local-keystore-based signing.

## Libraries
You can see a functional list of all libraries used by opening `buildSrc/src/main/java/Dependencies.kt`

# Acknowledgments
There are plenty of libraries, for which I am not yet taking the time to copy/paste their licenses
here.

For the resource-bundled country flag icons, I have used an icon pack downloaded from
[flaticon.com](http://flaticon.com), via the author [Freepik](https://www.freepik.com/home)

The prescripted attribution is thus:
```
Icon(s) made by [Freepik](https://www.freepik.com/home) from [www.flaticon.com](http://www.flaticon.com)
```

# Contact info
If you have any questions, or would like to be invited to the AppCenter CI console, or anything
else, you may contact me directly:

**Email**: [dennisdrew@gmail.com](mailto:dennisdrew@gmail.com)
**Phone**: [+15134033455](tel:+15134033455)