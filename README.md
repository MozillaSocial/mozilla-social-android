# MozillaSocialAndroid

## Contributing Guidelines

- Our module structure is based on [nowinandroid](https://github.com/android/nowinandroid)
- Repositories should contain data-repository logic of reconciling different data sources (local, remote, etc.). They should mirror the underlying API, so as to avoid complicated data merging
- More complex data transformation should happen in a use case in the domain module

## Dependency updates

We're using renovate for automatic dependency updates.
More information in a dedicated readme: [./renovate-readme.md](./renovate-readme.md)
