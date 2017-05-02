.. _integrate:

===========
Integration
===========


Add the SDK to your project
=======================

The gini sdk is provided as an aar archive. To integrate it into your project copy the aar archive file into your projects ``libs`` folder.
Then add this folder, if not already done, to your projects dependencies with:

.. code-block:: groovy

  repositories {
      …
      flatDir{
          dirs 'libs'
      }
  }

Next step is to add the SDK to your applications ``build.gradle`` file with:

.. code-block:: groovy

  dependencies {
    …
    compile 'net.gini.tariffsdk:tariffsdk-release@aar'
    …
  }

During the time of development of version 1.0 this is the only way we provide the SDK, later we will push it to an maven repository and adjust this document.


Integrating the SDK
===================

To integrate the SDK into your code, simple use the provided ``TariffSdk.init`` method to generate a TariffSdk instance.
Hereby a Context, your client id, your client secret and your domain is needed(For more information about those parameters and how to configure the SDK see the chapter: :ref:`Configuration`)

.. code-block:: java

  final TariffSdk tariffSdk = TariffSdk.init(this, "clientId", "clientPw");

The generated TariffSdk instance can now be used to generate the needed Activity and to get the found extractions.
