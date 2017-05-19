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
#. Your client id *(A String containing the client id for the SDK.)*
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
Therefore the SDK provides the possibility to add a okHttpClient which is being reused in the SDK:

.. code-block:: java

  public TariffSdk withOkHttpClient(@NonNull OkHttpClient okHttpClient)

Accent Color
------------

To set an accent color for the SDK you can overwrite the ``GiniTheme`` style. This color is applied to the overflow menu icon, the dialog's text color of their buttons and the color of the loading indicator. Overwritting other colors is being ignored and will not be processed by the SDK for now.
This style can be applied to the SDK with the ``setTheme()`` method.

.. code-block:: xml

  <style name="CustomTheme" parent="GiniTheme">
      <item name="colorAccent">#FFEB3B</item>
  </style>


.. code-block:: java

  tariffSdk.setTheme(R.style.CustomTheme);


Button Style
------------

To set a custom button style a stateList drawable has to be created. This drawable should support all needed states of a button. e.g. pressed, focused. This drawable can be applied to the SDK with the ``setButtonStyleSelector()`` method and is used to style the buttons of the review screen, preview screen, take picture screen and show extractions screen.(TODO: add references to the screen pages)

.. code-block:: java

  tariffSdk.setButtonStyleSelector(R.drawable.custom_button);

.. note:: See `official Android documentation <https://developer.android.com/guide/topics/resources/drawable-resource.html#StateList>`_ to StateList for more information.


Button Text Color
-----------------

To set the button text color use the ``setButtonTextColor()`` method. This color is applied to all buttons of the review screen, preview screen, take picture screen and show extractions screen.(TODO: add references to the screen pages)
The color has to be a resource id.

.. code-block:: java

  tariffSdk.setButtonTextColor(R.color.your_color);


Positive Color
--------------

To set the positive color use the ``setPositiveColor()`` method. This color is used to indicate that that a process succeeded. It is shown in status indicator of the images.
The color has to be a resource id.

.. code-block:: java

  tariffSdk.setPositiveColor(R.color.your_positiveColor)

.. note:: The color should indicate success and can therefore be something like green.


Negative Color
--------------

To set the negative color use the ``setNegativeColor()`` method. This color is used to indicate that that a process failed or something went wrong. It is shown in the status indicator of the images.
The color has to be a resource id.

.. code-block:: java

  tariffSdk.setNegativeColor(R.color.your_negativeColor)

.. note:: The color should indicate failure and can therefore be something like red.



Exit Dialog Text
----------------

To set a title text for the exit dialog the ``setExitDialogText()`` method can be used. This dialog is shown when the user presses back or cancel.
The text has to be a string resource id.

.. code-block:: java

  tariffSdk.setExitDialogText(R.string.exit_text);



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
Accent Color         int            Yes
Button Style         int            Yes
Positive Color       int            Yes
Negative Color       int            Yes
Exit Dialog Text     int            Yes
==================   ============   ============
