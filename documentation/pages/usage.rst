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
