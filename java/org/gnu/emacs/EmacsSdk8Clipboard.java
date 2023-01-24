/* Communication module for Android terminals.  -*- c-file-style: "GNU" -*-

Copyright (C) 2023 Free Software Foundation, Inc.

This file is part of GNU Emacs.

GNU Emacs is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or (at
your option) any later version.

GNU Emacs is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with GNU Emacs.  If not, see <https://www.gnu.org/licenses/>.  */

package org.gnu.emacs;

/* Importing the entire package avoids the deprecation warning.  */
import android.text.*;

import android.content.Context;
import android.util.Log;

import java.io.UnsupportedEncodingException;

/* This class implements EmacsClipboard for Android 2.2 and other
   similarly old systems.  */

@SuppressWarnings ("deprecation")
public class EmacsSdk8Clipboard extends EmacsClipboard
{
  private static final String TAG = "EmacsSdk8Clipboard";
  private ClipboardManager manager;

  public
  EmacsSdk8Clipboard ()
  {
    String what;
    Context context;

    what = Context.CLIPBOARD_SERVICE;
    context = EmacsService.SERVICE;
    manager
      = (ClipboardManager) context.getSystemService (what);
  }

  /* Set the clipboard text to CLIPBOARD, a string in UTF-8
     encoding.  */

  @Override
  public void
  setClipboard (byte[] bytes)
  {
    try
      {
	manager.setText (new String (bytes, "UTF-8"));
      }
    catch (UnsupportedEncodingException exception)
      {
	Log.w (TAG, "setClipboard: " + exception);
      }
  }

  /* Return whether or not Emacs owns the clipboard.  Value is 1 if
     Emacs does, 0 if Emacs does not, and -1 if that information is
     unavailable.  */

  @Override
  public int
  ownsClipboard ()
  {
    return -1;
  }

  /* Return whether or not clipboard content currently exists.  */

  @Override
  public boolean
  clipboardExists ()
  {
    return manager.hasText ();
  }

  /* Return the current content of the clipboard, as plain text, or
     NULL if no content is available.  */

  @Override
  public byte[]
  getClipboard ()
  {
    String string;
    CharSequence text;

    text = manager.getText ();

    if (text == null)
      return null;

    string = text.toString ();

    try
      {
	return string.getBytes ("UTF-8");
      }
    catch (UnsupportedEncodingException exception)
      {
	Log.w (TAG, "getClipboard: " + exception);
      }

    return null;
  }
};