.. _integrate:

===========
Integration
===========


Add the SDK to your project
=======================

The Gini Switch SDK is provided in a maven repository as an aar archive on jcenter. To integrate it into your project add the newest version to your gradle dependencies:

.. code-block:: groovy

  dependencies {
    compile 'net.gini:switch:${version}'
  }


.. note:: You can visit `bintray <https://bintray.com/gini/maven/switch/_latestVersion>`_ to get information about the latest version.


Integrating the SDK
===================

To integrate the SDK into your code, simple use the provided ``SwitchSdk.init`` method to generate a SwitchSdk instance.
Hereby a Context, your client id, your client secret and your domain is needed(For more information about those parameters and how to configure the SDK see the chapter: :ref:`Configuration`)

.. code-block:: java

  final SwitchSdk switchSdk = SwitchSdk.init(this, "clientId", "clientPw", "domain");

The generated SwitchSdk instance can now be used to generate the needed Activity and to get the found extractions.

.. note:: The SDK only works correct if the ``init`` method has been called.

Dependencies
============

The Switch SDK is using the external Http library `okHttp <https://github.com/square/okhttp>`_ version 3.6.0.
As well as the `Android Support Library <https://developer.android.com/topic/libraries/support-library/index.html>`_ version 26.0.1.

If your project is using a different kind of the support library problems can appear during run- or compile time.
You have different options to address this problems. The easiest solution is probably to use the same version as the SDK. If we update the SDK we tend to update the used support library as well to the latest version.
However if you want to use another support library version you can do so by forcing the same support library version on all your used libraries. This can be done with the following gradle method:

.. code-block:: groovy

  configurations.all {
    resolutionStrategy.eachDependency { DependencyResolveDetails details ->
        def requested = details.requested
        if (requested.group == 'com.android.support') {
            if (!requested.name.startsWith("multidex")) {
                details.useVersion 'dessired-support-lib-version'
            }
        }
    }
  }

.. note:: We only test the Gini Switch SDK with the support library version which is used in the library. Problems occuring related to forcing another library version might not be solvable by us.
