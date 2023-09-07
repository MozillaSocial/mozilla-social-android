# MozillaSocialAndroid

## Contributing Guidelines

- Our module structure is based on [nowinandroid](https://github.com/android/nowinandroid)
- Repositories should contain data-repository logic of reconciling different data sources (local, remote, etc.). They should mirror the underlying API, so as to avoid complicated data merging
- More complex data transformation should happen in a use case in the domain module

## Ruby

Some tools we're using—for example Fastlane—depend on Ruby.
They are primarily meant to run on CI, but you can run them locally if you want or need to.
We're using `rbenv` to setup Ruby environment. Here are installation and setup instructions
in case you need them: [./ruby-readme.md](./ruby-readme.md).

## Daily builds

To manually create a daily build run `ci/daily-build.sh` from this directory.
To manually publish a daily release run `ci/daily-release.sh` from this directory.

These scripts depends on secrets, so before running either make sure you followed instructions in
[./secrets/README.md](./secrets/README.md).

## Dependency updates

We're using renovate for automatic dependency updates.
More information in a dedicated readme: [./renovate-readme.md](./renovate-readme.md)
