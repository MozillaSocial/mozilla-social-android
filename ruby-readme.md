# Ruby

## FAQs

### Can I use ruby that comes with my system?

Maybe? Multiple guides, including the Fastlane guide I started from say this is a bad idea
(on macOS anyway). Here's a post listing some reasons:
[Why You Shouldn't Use the System Ruby to Install Gems on a Mac](https://www.moncefbelyamani.com/why-you-shouldn-t-use-the-system-ruby-to-install-gems-on-a-mac/).
(Warning: the author also sells a script for installing and configuring Ruby automatically for [$50](https://www.rubyonmac.dev/pricing/)!)

So I haven't even tried it really, so maaaaybe. But probably not?
If you think this guide is an overkill and want to just try using system Ruby I guess go for it.
But if something's weird or not working, remember people tried to warn you.

### Can I use `sudo` when following instructions to install Ruby?

**NO.** If at some point you hit some errors and the solution that you think
of or find online is to re-run the last command with `sudo` then **don't**. Go back to the last
guide you were following, go back to this guide and make sure you didn't miss a step (like I did).
Trust me, you don't need `sudo` to configure Ruby environment.

## Ruby installation TL;DR

Here's what worked for me. Hope it works for you too:
* Homebrew (if you know you already have Homebrew on your machine, skip to the next section)
  * Check if you have Homebrew installed: 
    ```shell
    brew --version
    ```
    For me it prints `Homebrew 4.1.9`. If it shows something similar for you, you have Homebrew
    installed nad can skip to the next section.
  * Follow instructions on https://brew.sh/ to install Homebrew.
* `rbenv`:
  * Install `rbenv` from Homebrew:
    ```shell
    brew install rbenv
    ```
  * Run `rbenv init` and follow printed instructions.
  * Close your Terminal window and open a new one so your changes take effect.
* Ruby:
  * `rbenv` doesn't just install Ruby. It builds it. So you need a build environment.
    Sounds scary I know. [`rbenv` wiki](https://github.com/rbenv/ruby-build/wiki#suggested-build-environment)
    lists a bunch of things you need, but from my experience all you need is:
    ```shell
    brew install gmp
    ```
    * Other packages it tells you to install already come as dependencies when you `brew install rbenv`.
      And the environment variable doesn't seem to be required (at least as long as you're not installing
      and old version of Ruby).
    * `brew install rust` is optional, we don't really need it. Some machines (like GitHub Actions runners)
      already come with it preinstalled so `rbenv` is going to use existing Rust if it's there.
  * Install the version of Ruby we defined in `.ruby-version` by running:
    ```shell
    rbenv install "$(cat .ruby-version)"
    ```
  * Install Bundler
    ```shell
    gem install bundler
    ```
  * Install needed gems
    ```shell
    bundle install
    ```

## References
In case the TL;DR doesn't work for you for some reason here are the guides I followed:
* https://docs.fastlane.tools/
* https://github.com/rbenv/rbenv#installation
* https://github.com/rbenv/ruby-build/wiki#suggested-build-environment

`rbenv` is not the only Ruby manager. I just ran with it because that's what Fastlane docs
recommended. Later @bassrock recommended it too, which is all I needed to know and I didn't ask
any questions. But just in case here are some resources on all available Ruby managers
(which I didn't read):
* https://www.ruby-lang.org/en/documentation/installation/#managers
* https://github.com/rbenv/rbenv/wiki/Comparison-of-version-managers
