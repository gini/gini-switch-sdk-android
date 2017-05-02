.. _configuration:

=============
Configuration
=============


Configuration of the SDK
========================

The Gini Tariff SDK offers possibilities to configure it to your needs.

Mandatory Configurations
========================

There are four parameters that have to be provided during the init call of the SDK

#. A Context *(Best the application context but not explicitly necessary since the application context is being received from the context instance anyway.)*
#. Your client ID *(A String containing the client id for the SDK.)*
#. Your client secret *(A String containing the client secret for the SDK.)*
#. Your domain *(A String containing your domain, e.g.* ``gini.net`` *)*

.. note:: All of the above parameters have to be provided and are needed by the SDK to work correctly. An **IllegalArgumentException** is thrown if one of them is null.

All other configurations that can be set are optional and are explained in detail in the next section.

Optional Configurations
=======================

OkHttpClient
------------

Since the SDK uses the library okHttp for its network operations an okHttp client has to be used.
OkHttp clients are encouraged to be singletons due to caching, joining and canceling of calls.
Therefore the SDK provides the posibility to add a okHttpClient which is being reused in the SDK:

.. code-block:: java

  public TariffSdk withOkHttpClient(@NonNull OkHttpClient okHttpClient)

Theme
-----

To style the screens of the SDK an Android theme can be provided to the SDK.
The provided theme should extend the TariffSDKTheme and can overrite the following attributes:
``TODO list attributes after design was specified``
The theme can be set via:

.. code-block:: java

  public TariffSdk withTheme(@StyleRes final int theme);


Overview Of Configurations
==========================

==================   ============   ============
Name                 Type           Optional
==================   ============   ============
Context              Context        No
Client ID            String         No
Client Secret        String         No
Domain               String         No
OkHttpClient         OkHttpClient   Yes
Theme                int            Yes
==================   ============   ============
