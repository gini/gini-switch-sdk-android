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
    compile 'net.gini.switchsdk:switchsdk-release@aar'
    …
  }

During the time of development of version 1.0 this is the only way we provide the SDK. Later Gini will push it to an maven repository and adjust this document.
This is also the reason that the dependencies that are needed by the SDK have to be provided by the client. Right now now they are the following ones:

#. com.android.support:appcompat-v7
#. com.android.support:exifinterface
#. com.android.support:design

Where the version has to be bigger or equal to 25.2.0. so the following lines should also be added to your gradle build file:

.. code-block:: groovy

  dependencies {
    …
    compile 'com.android.support:appcompat-v7:25.2.0'
    compile 'com.android.support:exifinterface:25.2.0'
    compile 'com.android.support:design:25.2.0'
    …
  }

.. note:: This will be not necessery when the SDK is available on a maven repository.


Integrating the SDK
===================

To integrate the SDK into your code, simple use the provided ``SwitchSdk.init`` method to generate a SwitchSdk instance.
Hereby a Context, your client id, your client secret and your domain is needed(For more information about those parameters and how to configure the SDK see the chapter: :ref:`Configuration`)

.. code-block:: java

  final SwitchSdk switchSdk = SwitchSdk.init(this, "clientId", "clientPw", "domain");

The generated SwitchSdk instance can now be used to generate the needed Activity and to get the found extractions.
