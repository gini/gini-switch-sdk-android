.. _configuration:

=============
Configuration
=============


Configuration of the SDK
========================

The Gini Switch SDK offers possibilities to configure it to your needs.

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
Therefore the SDK provides the possibility to add a okHttpClient which is being reused in the SDK via an overloaded init method:

.. code-block:: java

  public SwitchSdk init(…, OkHttpClient okhttpClient);


Enable Logging
------------

You can enable logging inside the SDK to get some information what is going on there. The logging is disabled by default and should only be turned on for testing and debugging purposes. The log output is printed into the usual Android Logcat output and there are also the same log levels, which can also be defined. See ``Logging Level`` for more information about it.

.. code-block:: java

  public SwitchSdk showLogging(boolean show);


Set Logging Level
------------

You can define the level of what should be printed in the logs. They are similar to the ones in the Android Log class. There are: VERBOSE, INFO, DEBUG, WARN and ERROR. By default all of them are shown.

.. code-block:: java

  public SwitchSdk setLoggingLevel(Logging.LogLevel logLevel)


Accent Color
------------

To set an accent color for the SDK you can overwrite the ``GiniTheme`` style. This color is applied to the overflow menu icon, the dialog's text color of their buttons and the color of the loading indicator. Overwritting other colors is being ignored and will not be processed by the SDK for now.
This style can be applied to the SDK with the ``setTheme()`` method.

.. code-block:: xml

  <style name="CustomTheme" parent="GiniTheme">
      <item name="colorAccent">#FFEB3B</item>
  </style>


.. code-block:: java

  switchSdk.setTheme(R.style.CustomTheme);


Button Style
------------

To set a custom button style a stateList drawable has to be created. This drawable should support all needed states of a button. e.g. pressed, focused. This drawable can be applied to the SDK with the ``setButtonStyleSelector()`` method and is used to style the buttons of the review screen, preview screen, take picture screen and show extractions screen.(TODO: add references to the screen pages)

.. code-block:: java

  switchSdk.setButtonStyleSelector(R.drawable.custom_button);

.. note:: See `official Android documentation <https://developer.android.com/guide/topics/resources/drawable-resource.html#StateList>`_ to StateList for more information.


Button Text Color
-----------------

To set the button text color use the ``setButtonTextColor()`` method. This color is applied to all buttons of the review screen, preview screen, take picture screen and show extractions screen.(TODO: add references to the screen pages)
The color has to be a resource id.

.. code-block:: java

  switchSdk.setButtonTextColor(R.color.your_color);


Positive Color
--------------

To set the positive color use the ``setPositiveColor()`` method. This color is used to indicate that that a process succeeded. It is shown in status indicator of the images.
The color has to be a resource id.

.. code-block:: java

  switchSdk.setPositiveColor(R.color.your_positiveColor)

.. note:: The color should indicate success and can therefore be something like green.


Negative Color
--------------

To set the negative color use the ``setNegativeColor()`` method. This color is used to indicate that that a process failed or something went wrong. It is shown in the status indicator of the images.
The color has to be a resource id.

.. code-block:: java

  switchSdk.setNegativeColor(R.color.your_negativeColor)

.. note:: The color should indicate failure and can therefore be something like red.



Exit Dialog Text
----------------

To set a title text for the exit dialog the ``setExitDialogText()`` method can be used. This dialog is shown when the user presses back or cancel.
The text has to be a string resource id.

.. code-block:: java

  switchSdk.setExitDialogText(R.string.exit_text);


Title Text (Review Screen)
--------------------------------

To set the title in the review screen use the ``setReviewTitleText()`` method. This text is shown in the review screen as a title and should hint your user to check if the image has the correct orientation and is readable.
The text should be a string resource.

.. code-block:: java

  switchSdk.setReviewTitleText(R.string.your_title_text);


Discard Button Text (Review Screen)
--------------------------------

To set the text for the discard button in the review screen use the ``setReviewDiscardText()`` method.
The text should be a string resource.

.. code-block:: java

  switchSdk.setReviewDiscardText(R.string.your_discard_button_text);


Keep Button Text (Review Screen)
--------------------------------

To set the text for the keep button in the review screen use the ``setReviewKeepText()`` method.
The text should be a string resource.

.. code-block:: java

  switchSdk.setReviewKeepText(R.string.your_keep_button_text);


Title Success Text (Preview Screen)
--------------------------------

To set the success title in the preview screen use the ``setPreviewSuccessText()`` method. This text is shown in the preview screen as a title and should say that the analyzing of the image was successful.
The text should be a string resource.

.. code-block:: java

  switchSdk.setPreviewSuccessText(R.string.your_success_text);


Title Failed Text (Preview Screen)
--------------------------------

To set the failed title in the preview screen use the ``setPreviewFailedText()`` method. This text is shown in the preview screen as a title and should say that the analyzing of the image failed.
The text should be a string resource.

.. code-block:: java

  switchSdk.setPreviewFailedText(R.string.your_failed_text);


Text (Analyze Completed Screen)
-------------------------------

To set the text in the analyze completed screen use the ``setAnalyzedText()`` method. This string is applied to the text seen in the analyze completed screen.
The string has to be a resource id.

.. code-block:: java

  switchSdk.setAnalyzedText(R.string.your_string);


Text Color (Analyze Completed Screen)
------------------------------------

To set the text color use the ``setAnalyzedTextColor()`` method. This color is applied to the text seen in the analyze completed screen.
The color has to be a resource id.

.. code-block:: java

  switchSdk.setAnalyzedTextColor(R.color.your_color);


Image (Analyze Completed Screen)
--------------------------------

To set the image color use the ``setAnalyzedImage()`` method. This iamge is shown in the analyze completed screen.
The image has to be a resource id of a drawable. The image will be displayed in a 96x96 dp size, therefore your drawable should be in this size, or you should use vector drawables.

.. code-block:: java

  switchSdk.setAnalyzedImage(R.drawable.your_analyzed_image);


Text Size (Analyze Completed Screen)
------------------------------------

To set the text size use the ``setAnalyzedTextSize()`` method. This size is applied to the text seen in the analyze completed screen.
The size has to be in ``sp``.

.. code-block:: java

  switchSdk.setAnalyzedTextSize(16);

.. note:: See `official Android documentation <https://developer.android.com/guide/topics/resources/more-resources.html#Dimension>`_ about dimensions for more information.



Overview Of Configurations
==========================

==================================   ============   ============
Name                                 Type           Optional
==================================   ============   ============
Context                              Context        No
Client ID                            String         No
Client Secret                        String         No
Domain                               String         No
OkHttpClient                         OkHttpClient   Yes
Enable Logging                       boolean        Yes
Logging Level                        LoggingLevel   Yes
Accent Color                         int            Yes
Button Style                         int            Yes
Positive Color                       int            Yes
Negative Color                       int            Yes
Exit Dialog Text                     int            Yes
Title Success Text (PS)              int            Yes
Title Failed Text (PS)               int            Yes
Text (ACS)                           int            Yes
Text Color (ACS)                     int            Yes
Text Size (ACS)                      int            Yes
Image (ACS)                          int            Yes
==================================   ============   ============
