# Android Repository of the Switch SDK
The Gini Switch SDK provides components for capturing, reviewing and analyzing photos of electricity bills in Germany.

By integrating this library into your application you can allow your users to easily take a picture of a document, review it and extract the necessary information to then proceed and change their electricity provider.

The SDK takes care of all interactions with our backend and provides you with customizable UI you can easily integrate within your app to take care of the whole process for you.

Even though a lot of the UI's appearance is customizable, the overall user flow and UX is not. We have worked hard to find the best way to navigate our users through the process and would like partners to follow the same path.

The Gini Switch SDK has been designed for portrait orientation. The UI is automatically forced to portrait when being displayed.

### Integration
[ ![Download](https://api.bintray.com/packages/gini/maven/switch/images/download.svg) ](https://bintray.com/gini/maven/switch/_latestVersion)

Add the latest version of the SDK to your gradle dependencies:
```Groovy
compile 'net.gini:switch:${version}'
```

### Documentation
See [documentation](http://developer.gini.net/gini-switch-sdk-android/index.html) for how to integrate the SDK with your app and customize its UI.

### Contribution and Individual Changes
If you discover ways of improving our SDK we would be more than happy if you let us know e.g. by creating an issue here on Github or creating a pull request.

Please note that if you are using our SDK together with our API (under a licensed contract), only our official SDK versions are supported. Unfortunately we cannot support your individual changes.


### Third party libraries
- [okHttp v3.6.0](http://square.github.io/okhttp/)
- [Android Support Library v26.0.1](https://developer.android.com/topic/libraries/support-library/revisions.html#26-0-1)
(If you have troubles regarding your support library version and Gini Switch's version you can find different solutions suggested in the [documentation](http://developer.gini.net/gini-switch-sdk-android/pages/integration.html#dependencies).)

### License

The Gini Switch SDK for Android is licensed under the MIT License. See the LICENSE file for more info.

**Important:** Always make sure to ship all license notices and permissions with your application.
