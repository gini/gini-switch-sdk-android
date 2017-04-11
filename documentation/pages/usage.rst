.. _usage:

=====
Usage
=====


Usage of the SDK
================

The only class that should be used to interact with the SDK is the ``TariffSdk`` and it's created instance from the chapter before(:ref:`Configuration`).
To start the SDK a method to get a configured Activity is provided by in the ``TariffSdk`` class.

.. code-block:: java

  final Intent tariffSdkIntent = tariffSdk.getTariffSdkIntent();
  startActivityForResult(tariffSdkIntent, TariffSdk.REQUEST_CODE);

At this point the SDK takes care of taking pictures, camera access, image upload and retrieving the extractions.
After the SDK has been finished(successfully as well as with a failure) a result code is set and can be received via the usual Activity's ``onActivityResult`` method.

.. note:: Using the SDK other than via the provided ``TariffSdk`` instance can lead to faulty behaviour and is not supported by us.

Receiving Extractions
=====================

The extractions are provided inside the ``Extractions`` class and can be received when the result code of the previous started activity was ``TariffSdk.RESULT_EXTRACTIONS_RECEIVED``.

.. code-block:: java

  if(resultCode == TariffSdk.RESULT_EXTRACTIONS_RECEIVED)
    final Extractions extractions = TariffSdk.getExtractions();
