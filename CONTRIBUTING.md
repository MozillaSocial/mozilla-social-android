# Contributor guidelines

Before opening a PR please open an issue first, describe the bug you want to fix
or a feature you want to implement, mention you'd like to work on it and discuss it with us.
Thanks!

# Coding Rules

* Our module structure is based on [nowinandroid](https://github.com/android/nowinandroid)
* Repositories should contain data-repository logic of reconciling different data sources (local, remote, etc.).
  They should mirror the underlying API, so as to avoid complicated data merging
* More complex data transformation should happen in a use case in the domain module
* For logging we're using timber. Use it directly. Don't introduce a custom `Log` interface, don't inject it as a dependency.
