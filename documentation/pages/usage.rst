.. _usage:

=====
Usage
=====


Usage of the SDK
================

The only class that should be used to interact with the SDK is the ``SwitchSdk`` and its created instance from the chapter before(:ref:`Configuration`).
To start the SDK a method to get a configured Activity is provided by in the ``SwitchSdk`` class.

.. code-block:: java

  final Intent switchSdkIntent = switchSdk.getSwitchSdkIntent();
  startActivityForResult(switchSdkIntent, SwitchSdk.REQUEST_CODE);

At this point the SDK takes care of taking pictures, camera access, image upload and retrieving the extractions.
After the SDK has been finished(successfully as well as with a failure) a result code is set and can be received via the usual Activity's ``onActivityResult`` method.

.. note:: Using the SDK other than via the provided ``SwitchSdk`` instance can lead to faulty behaviour and is not supported by us.

Receiving Extractions
=====================

The found information inside a bill are called extractions, to receive those extractions the SDK offers a method to get them.
The extractions are provided inside the class ``Extractions`` and they can be received when the result code of the previous started activity was ``SwitchSdk.EXTRACTIONS_AVAILABLE``. Every extraction offers a corresponding getter, in the form of ``getExtractionName()``.

.. code-block:: java

  if(resultCode == SwitchSdk.EXTRACTIONS_AVAILABLE)
    Extractions extractions = mSwitchSdk.getExtractions();

Supported extractions are:

======================      ======================   ============   ==============
Extraction                  Getter                   Type           Default value
======================      ======================   ============   ==============
Wholesale supplier          getCompanyName           String         ""
Consumption value           getConsumptionValue      double         Double.NaN
Consumptions unit           getConsumptionUnit       String         ""
Energy meter number         getEnergyMeterNumber     String         ""
======================      ======================   ============   ==============


Providing Feedback
==================

Depending on your use case your app probably presents the extractions to the user and gives them the opportunity to correct them. Yes, there could be errors. We do our best to prevent them - but it’s more unlikely to happen if your app sends us feedback for the extractions we have delivered. Your app should send feedback only for the extractions the user has seen and accepted. Feedback should be sent for corrected extractions and for correct extractions.
The Switch SDK's Extraction class offers a Builder that can be used to alter the given extractions.
You can get an instance of this Builder by calling the method ``Extractions.newBuilder(retrievedExtractions)`` and then use it.
See the following example code how this is done:

.. code-block:: java

  final SwitchSdk switchSdk = SwitchSdk.getSdk();
  final Extractions retrievedExtractions = switchSdk.getExtractions();

  //Let the user review and alter the extractions…

  final Extractions feedbackExtractions = Extractions.newBuilder(retrievedExtractions)
                                      .companyName(feedbackCompanyName)
                                      .energyMeterNumber(feedbackCounterNumber)
                                      .build();
  switchSdk.provideFeedback(feedbackExtractions);
