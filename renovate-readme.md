# Renovate

We're using the Renovate bot, based on a common Pocket configuration: https://github.com/Pocket/renovate-config

## Configuration customizations

Because JSON doesn't support comments, this readme walks through any customization we apply
on top of Pocket configuration.

### Disable Kotlin updates
```json
{
  "packageRules": [
    {
      "matchSourceUrls": [
        "https://github.com/JetBrains/kotlin"
      ],
      "enabled": false
    }
  ]
}
```

This rule disables updating Kotlin.

The reason is we're using Jetpack Compose and each Compose compiler version requires a specific
Kotlin version. See: https://developer.android.com/jetpack/androidx/releases/compose-kotlin

This means we can only update Kotlin by updating Compose compiler at the same time.
This usually means we can't use a new Kotlin version as soon as it drops. We have to wait for
a Compose compiler update.

To update Kotlin/Compose compiler:
* Wait for renovate to detect a new Compose compiler version.
* Let renovate open a PR with Compose compiler update.
* Manually push a commit to renovate's branch to also update Kotlin to
  [a matching version](https://developer.android.com/jetpack/androidx/releases/compose-kotlin).
* Then the branch should compile. Review, test and verify as normal and then merge.

Here's an example PR upgrading Compose compiler to 1.5.0 and Kotlin to 1.9.0:
https://github.com/Pocket/MozillaSocialAndroid/pull/59
