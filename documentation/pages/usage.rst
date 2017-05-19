.. _usage:

=====
Usage
=====


Usage of the SDK
================

The only class that should be used to interact with the SDK is the ``TariffSdk`` and its created instance from the chapter before(:ref:`Configuration`).
To start the SDK a method to get a configured Activity is provided by in the ``TariffSdk`` class.

.. code-block:: java

  final Intent tariffSdkIntent = tariffSdk.getTariffSdkIntent();
  startActivityForResult(tariffSdkIntent, TariffSdk.REQUEST_CODE);

At this point the SDK takes care of taking pictures, camera access, image upload and retrieving the extractions.
After the SDK has been finished(successfully as well as with a failure) a result code is set and can be received via the usual Activity's ``onActivityResult`` method.

.. note:: Using the SDK other than via the provided ``TariffSdk`` instance can lead to faulty behaviour and is not supported by us.

Receiving Extractions
=====================

The found information inside a bill are called extractions, to receive those extractions the SDK offers a method to get them.
The extractions are provided inside a set of ``Extraction`` classes and can be received when the result code of the previous started activity was ``TariffSdk.EXTRACTIONS_AVAILABLE``.

.. code-block:: java

  if(resultCode == TariffSdk.EXTRACTIONS_AVAILABLE)
    Set<Extraction> extractions = mTariffSdk.getExtractions();

The ``Extraction`` class includes the name of the extraction and its value. These are the extractions that have been found by the SDK. Inside the SDK the user checks the extractions for their correctness, therefore it is possible that the extractions have been altered by the user.