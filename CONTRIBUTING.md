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

## Dependencies

We're using [Gradle Version Catalogs](https://developer.android.com/build/migrate-to-catalogs)
to declare all dependencies in one place: [gradle/libs.versions.toml](gradle/libs.versions.toml).
We're following these rules to make it easier to look at the version catalog and make it easier
to find things in it:
* Keep each section in alphabetical order. This applies to the standard sections
  (`[versions]`, `[libraries]`, `[bundles]` and `[plugins]`) as well as one special case:
  at the bottom of `[libraries]` there is a comment that separates out a "section" for dependencies
  used only in `build-logic`.
* Add a definition to `[versions]` and use `version.ref` only if the version is used more than once
  and if the dependencies are meant to always use the same version.
* Use the simpler `{ module = "androidx.appcompat:appcompat" }` syntax
  instead of the more verbose `{ group = "androidx.appcompat", name = "appcompat" }` one.
* We don't have clear rules for choosing names for the definitions. Use your best judgement.
  Here are a few recommendations:
  * Use `words-separated-by-dashes` case.
  * Use descriptive names. Err on the side of being verbose. Don't come up with abbreviations
    to save a few characters. (It's OK to use existing well known abbreviations like KSP.)
  * Looking at the dependency's own group and name is usually a good start. It's fine to
    remove repetitions if there are any (like in `androidx.appcompat:appcompat`).
  * It's a good idea to prefix all androidx dependencies with `androidx-`. This way they are all
    grouped together when we sort alphabetically and some of their names are pretty generic without
    the prefix.
  * It's not required to prefix all dependencies with their group name/owning company/author.
    Especially if they have well known, unique names. So we don't have to prefix all Square
    libraries with `squareup-` or something.
