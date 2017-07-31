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
